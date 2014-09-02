/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implements of {@link AisPacketStream}.
 * 
 * @author Kasper Nielsen
 */
class AisPacketStreamImpl extends AisPacketStream {

    /** The logger */
    static final Logger LOG = LoggerFactory.getLogger(AisPacketStreamImpl.class);

    /** A lock we use to make sure packets are delivered in order. */
    private final Object deliveryLock = new Object();

    final ConcurrentHashMap<SubscriptionImpl, SubscriptionImpl> subscriptions;

    final Predicate<? super AisPacket> predicate;

    final AisPacketStreamImpl root;

    AisPacketStreamImpl() {
        predicate = null;
        subscriptions = new ConcurrentHashMap<>();
        root = null;
    }

    AisPacketStreamImpl(AisPacketStreamImpl parent, Predicate<? super AisPacket> predicate) {
        this.root = requireNonNull(parent);
        this.predicate = requireNonNull(predicate);
        this.subscriptions = parent.subscriptions;
    }

    public void add(AisPacket p) {
        requireNonNull(p);
        if (root != null) {
            throw new UnsupportedOperationException("Can only add elements to the root stream");
        }
        synchronized (deliveryLock) {
            for (SubscriptionImpl s : subscriptions.keySet()) {
                s.packets.add(p);
                s.deliver();
            }
        }
        // This approach does not work, as we cannot guarantee in-order delivery
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
        return new AisPacketStreamImpl(root == null ? this : root,
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
        subscriptions.put(s, s);
        return s;
    }

    class SubscriptionImpl implements Subscription {
        final CountDownLatch cancelled = new CountDownLatch(1);
        final Consumer<? super AisPacket> consumer;
        final AtomicLong count = new AtomicLong();
        final ReentrantLock lock = new ReentrantLock();
        final ConcurrentLinkedQueue<AisPacket> packets = new ConcurrentLinkedQueue<>();
        final Predicate<? super AisPacket> predicate;

        SubscriptionImpl(Predicate<? super AisPacket> predicate, Consumer<? super AisPacket> consumer) {
            this.predicate = predicate;
            this.consumer = requireNonNull(consumer);
        }

        /** {@inheritDoc} */
        @Override
        public void awaitCancelled() throws InterruptedException {
            cancelled.await();
        }

        /** {@inheritDoc} */
        @Override
        public boolean awaitCancelled(long timeout, TimeUnit unit) throws InterruptedException {
            return cancelled.await(timeout, unit);
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
                if (e != null) {
                    LOG.error("Cancelling subscription, because of error", e);
                }
                if (cancelled.getCount() > 0) {
                    subscriptions.remove(this);
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

        /** This method delivers the actual event. */
        void deliver() {
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
            return !subscriptions.containsKey(this);
        }
    }

}
