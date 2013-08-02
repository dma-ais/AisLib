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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import jsr166e.ConcurrentHashMapV8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
class DefaultAisPacketStream extends AisPacketStreams.AbstractAisPacketStream {
    /** The logger */
    static final Logger LOG = LoggerFactory.getLogger(DefaultAisPacketStream.class);

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
    public Subscription subscribe(Consumer<AisPacket> c) {
        SubscriptionImpl s = new SubscriptionImpl(predicate, c);
        map.put(s, s);
        return s;
    }

    class SubscriptionImpl implements Subscription {
        final AtomicLong count = new AtomicLong();
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
            cancel(null);
        }

        /** {@inheritDoc} */
        synchronized void cancel(Throwable e) {
            lock.lock();
            try {
                if (cancelled.getCount() > 0) {
                    map.remove(this);
                    cancelled.countDown();
                    if (consumer instanceof AisPacketStream.StreamConsumer) {
                        try {
                            ((AisPacketStream.StreamConsumer<?>) consumer).end(e);
                        } catch (RuntimeException ex) {
                            if (e == null) {
                                LOG.error("Failed to write footer", ex);
                            }
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        public void deliver() {
            while (lock.tryLock()) {
                try {
                    if (packets.isEmpty()) {
                        return;
                    }
                    for (AisPacket p = packets.poll(); p != null; p = packets.poll()) {
                        try {
                            if (predicate == null || predicate.test(p)) {
                                if (count.getAndIncrement() == 0 && consumer instanceof AisPacketStream.StreamConsumer) {
                                    ((AisPacketStream.StreamConsumer<?>) consumer).begin();
                                }
                                consumer.accept(p);
                            }
                        } catch (RuntimeException e) {
                            try {
                                cancel(e == AisPacketStream.CANCEL ? null : e);
                            } catch (RuntimeException ignore) {}
                            return;
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

        /**
         * {@inheritDoc}
         * 
         * @return
         */
        @Override
        public boolean awaitCancelled(long timeout, TimeUnit unit) throws InterruptedException {
            return cancelled.await(timeout, unit);
        }
    }

}
