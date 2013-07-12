/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import jsr166e.ConcurrentHashMapV8;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
class DefaultAisPacketStream extends AisPacketStreams.AbstractAisPacketStream {

    final ConcurrentHashMapV8<SubscriptionImpl, SubscriptionImpl> map;

    final Predicate<? super AisPacket> predicate;
    final DefaultAisPacketStream root;

    final Object lock = new Object();

    DefaultAisPacketStream() {
        this.predicate = null;
        map = new ConcurrentHashMapV8<>();
        root = null;
    }

    DefaultAisPacketStream(DefaultAisPacketStream parent, Predicate<? super AisPacket> predicate) {
        this.root = requireNonNull(parent);
        this.predicate = requireNonNull(predicate);
        this.map = parent.map;
    }

    public void add(final AisPacket p) {
        requireNonNull(p);
        if (root != null) {
            throw new UnsupportedOperationException("Can only add elements to the root stream");
        }
        synchronized (lock) {
            for (SubscriptionImpl s : map.keySet()) {
                s.packets.add(p);
                s.deliver();
            }
        }
        //
        // map.forEachKeyInParallel(new Action<SubscriptionImpl>() {
        // @Override
        // public void apply(SubscriptionImpl s) {
        // s.deliver();
        // }
        // });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AisPacketStream filter(Predicate<? super AisPacket> predicate) {
        requireNonNull(predicate);
        return new DefaultAisPacketStream(root == null ? this : root,
                (Predicate<? super AisPacket>) (this.predicate == null ? predicate
                        : this.predicate.and((Predicate) predicate)));
    }

    void handlePacket(AisPacket p, SubscriptionImpl s) {
        if (s.predicate == null || s.predicate.test(p)) {
            s.consumer.accept(p);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Subscription subscribePackets(Consumer<AisPacket> c) {
        SubscriptionImpl s = new SubscriptionImpl(predicate, c);
        map.put(s, s);
        return s;
    }

    class SubscriptionImpl implements Subscription {
        final ReentrantLock lock = new ReentrantLock();
        final Consumer<? super AisPacket> consumer;
        final Predicate<? super AisPacket> predicate;
        final ConcurrentLinkedQueue<AisPacket> packets = new ConcurrentLinkedQueue<>();
        final CountDownLatch cancelled = new CountDownLatch(1);

        SubscriptionImpl(Predicate<? super AisPacket> predicate, Consumer<? super AisPacket> consumer) {
            this.predicate = predicate;
            this.consumer = requireNonNull(consumer);
        }

        /** {@inheritDoc} */
        @Override
        public void cancel() {
            map.remove(this);
            cancelled.countDown();
        }

        public void deliver() {
            while (lock.tryLock()) {
                try {
                    if (packets.isEmpty()) {
                        return;
                    }
                    for (AisPacket p = packets.poll(); p != null; p = packets.poll()) {
                        if (predicate == null || predicate.test(p)) {
                            consumer.accept(p);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isCancelled() {
            return !map.containsKey(this);
        }

        /** {@inheritDoc} */
        @Override
        public void awaitCancelled() throws InterruptedException {
            cancelled.await();
        }
    }
}
