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

import dk.dma.ais.message.AisMessage;
import dk.dma.commons.util.io.OutputStreamSink;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A stream of packets.
 *
 * @author Kasper Nielsen
 */
public abstract class AisPacketStream {

    /**
     * Thrown by a subscriber to indicate that the subscription should be cancelled.
     */
    public static final RuntimeException CANCEL = new RuntimeException();

    /**
     * Adds the specified packet to the stream
     *
     * @param p the p
     * @throws UnsupportedOperationException if the stream is immutable
     */
    public void add(AisPacket p) {
        throw new UnsupportedOperationException("Stream is immutable");// default stream is immutable.
    }

    /**
     * Returns a new stream that only streams packets accepted by the specified predicate.
     *
     * @param predicate the predicate to test against each element
     * @return the new stream
     */
    public abstract AisPacketStream filter(Predicate<? super AisPacket> predicate);

    /**
     * Filter ais packet stream.
     *
     * @param expression the expression
     * @return the ais packet stream
     */
    public AisPacketStream filter(String expression) {
        return filter(AisPacketFilters.parseExpressionFilter(expression));
    }

    /**
     * Filter on message type ais packet stream.
     *
     * @param messageTypes the message types
     * @return the ais packet stream
     */
    public AisPacketStream filterOnMessageType(int... messageTypes) {
        return filter(AisPacketFilters.filterOnMessageType(messageTypes));
    }

    /**
     * Immutable stream ais packet stream.
     *
     * @return the ais packet stream
     */
    public final AisPacketStream immutableStream() {
        return this instanceof ImmutableAisPacketStream ? this : new ImmutableAisPacketStream(this);
    }

    /**
     * Limit ais packet stream.
     *
     * @param limit the limit
     * @return the ais packet stream
     */
    public AisPacketStream limit(long limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be at least 1, was: " + limit);
        }
        final AtomicLong l = new AtomicLong(limit);
        return filter(new Predicate<AisPacket>() {
            public boolean test(AisPacket element) {
                if (l.getAndDecrement() <= 0) {
                    throw AisPacketStream.CANCEL;
                }
                return true;
            }
        });
    }

    /**
     * Subscribe subscription.
     *
     * @param c the c
     * @return the subscription
     */
    public abstract Subscription subscribe(Consumer<AisPacket> c);

    /**
     * Subscribe messages subscription.
     *
     * @param c the c
     * @return the subscription
     */
    public Subscription subscribeMessages(final Consumer<AisMessage> c) {
        requireNonNull(c);
        if (c instanceof AisPacketStream.StreamConsumer) {
            final AisPacketStream.StreamConsumer<AisMessage> sc = (StreamConsumer<AisMessage>) c;
            return subscribe(new AisPacketStream.StreamConsumer<AisPacket>() {
                public void accept(AisPacket t) {
                    AisMessage m = t.tryGetAisMessage();
                    if (m != null) {
                        c.accept(m);
                    }
                }

                public void begin() {
                    sc.begin();
                }

                public void end(Throwable cause) {
                    sc.end(cause);
                }
            });
        } else {
            return subscribe(new Consumer<AisPacket>() {
                public void accept(AisPacket t) {
                    AisMessage m = t.tryGetAisMessage();
                    if (m != null) {
                        c.accept(m);
                    }
                }
            });
        }
    }

    /**
     * Subscribe sink subscription.
     *
     * @param sink the sink
     * @param os   the os
     * @return the subscription
     */
    public Subscription subscribeSink(final OutputStreamSink<AisPacket> sink, final OutputStream os) {
        requireNonNull(sink);
        requireNonNull(os);
        final AtomicLong l = new AtomicLong();
        return subscribe(new StreamConsumer<AisPacket>() {
            @Override
            public void accept(AisPacket t) {
                try {
                    sink.process(os, t, l.incrementAndGet());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public void begin() {
                try {
                    sink.header(os);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public void end(Throwable cause) {
                try {
                    sink.footer(os, l.get());
                } catch (IOException e) {
                    if (cause == null) {// dont throw if already on exception path
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * Returns a new stream.
     *
     * @return the new stream
     */
    public static AisPacketStream newStream() {
        return new AisPacketStreamImpl();
    }

    /**
     * The type Stream consumer.
     *
     * @param <T> the type parameter
     */
    public abstract static class StreamConsumer<T> implements Consumer<T> {
        /**
         * Invoked immediately before the first message is delivered.
         */
        public void begin() {}

        /**
         * Invoked immediately after the last message has been delivered.
         *
         * @param cause if an exception caused the consumer to be unsubscribed
         */
        public void end(Throwable cause) {}
    }

    /**
     * A subcription is created each time a new consumer is added to the stream.
     */
    public interface Subscription {
        /**
         * Await cancelled.
         *
         * @throws InterruptedException the interrupted exception
         */
        void awaitCancelled() throws InterruptedException;

        /**
         * Await cancelled boolean.
         *
         * @param timeout the timeout
         * @param unit    the unit
         * @return the boolean
         * @throws InterruptedException the interrupted exception
         */
        boolean awaitCancelled(long timeout, TimeUnit unit) throws InterruptedException;

        /**
         * Cancels this subscription. After this subscription has been cancelled. No more packets will be delivered to
         * the consumer.
         */
        void cancel();

        /**
         * Returns whether or not the subscription has been cancelled.  @return the boolean
         *
         * @return the boolean
         */
        boolean isCancelled();
    }

    /**
     * The type Delegating ais packet stream.
     */
    static class DelegatingAisPacketStream extends AisPacketStream {
        /**
         * The Stream.
         */
        final AisPacketStream stream;

        /**
         * Instantiates a new Delegating ais packet stream.
         *
         * @param stream the stream
         */
        public DelegatingAisPacketStream(AisPacketStream stream) {
            this.stream = requireNonNull(stream);
        }

        public void add(AisPacket p) {
            stream.add(p);
        }

        public AisPacketStream filter(Predicate<? super AisPacket> predicate) {
            return stream.filter(predicate);
        }

        public AisPacketStream filter(String expression) {
            return stream.filter(expression);
        }

        public AisPacketStream filterOnMessageType(int... messageTypes) {
            return stream.filterOnMessageType(messageTypes);
        }

        public AisPacketStream limit(long limit) {
            return stream.limit(limit);
        }

        public Subscription subscribe(Consumer<AisPacket> c) {
            return stream.subscribe(c);
        }

        public Subscription subscribeMessages(Consumer<AisMessage> c) {
            return stream.subscribeMessages(c);
        }

        /** {@inheritDoc} */
        @Override
        public Subscription subscribeSink(OutputStreamSink<AisPacket> sink, OutputStream os) {
            return stream.subscribeSink(sink, os);
        }
    }

    /**
     * The type Immutable ais packet stream.
     */
    static class ImmutableAisPacketStream extends DelegatingAisPacketStream {

        /**
         * Instantiates a new Immutable ais packet stream.
         *
         * @param stream the stream
         */
        public ImmutableAisPacketStream(AisPacketStream stream) {
            super(stream);
        }

        /** {@inheritDoc} */
        @Override
        public void add(AisPacket p) {
            throw new UnsupportedOperationException("Stream is immutable");
        }
    }
}
