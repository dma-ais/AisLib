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

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketStreams {

    static final Logger LOG = LoggerFactory.getLogger(AisPacketStreams.class);

    public static AisPacketStream immutableStream(AisPacketStream s) {
        return s instanceof ImmutableAisPacketStream ? s : new ImmutableAisPacketStream(s);
    }

    public static AisPacketStream newStream() {
        return new DefaultAisPacketStream();
    }

    abstract static class AbstractAisPacketStream implements AisPacketStream {

        /** {@inheritDoc} */
        @Override
        public void add(AisPacket p) {
            throw new UnsupportedOperationException("Stream is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public final AisPacketStream filter(String expression) {
            return filter(AisPacketFilters.parseSourceFilter(expression));
        }

        /** {@inheritDoc} */
        @Override
        public AisPacketStream filterOnMessageType(int... messageTypes) {
            return filter(AisPacketFilters.filterOnMessageType(messageTypes));
        }

        /** {@inheritDoc} */
        @Override
        public Subscription subscribeMessages(final Consumer<AisMessage> c) {
            requireNonNull(c);
            if (c instanceof AisPacketStream.StreamConsumer) {
                final AisPacketStream.StreamConsumer<AisMessage> sc = (StreamConsumer<AisMessage>) c;
                return subscribePackets(new AisPacketStream.StreamConsumer<AisPacket>() {
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
                return subscribePackets(new Consumer<AisPacket>() {
                    public void accept(AisPacket t) {
                        AisMessage m = t.tryGetAisMessage();
                        if (m != null) {
                            c.accept(m);
                        }
                    }
                });
            }
        }

        /** {@inheritDoc} */
        @Override
        public Subscription subscribePacketSink(final OutputStreamSink<AisPacket> sink, final OutputStream os) {
            requireNonNull(sink);
            requireNonNull(os);
            return subscribePackets(new StreamConsumer<AisPacket>() {
                @Override
                public void accept(AisPacket t) {
                    try {
                        sink.process(os, t);
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
                        sink.footer(os);
                    } catch (IOException e) {
                        if (cause == null) {// dont throw if already on exception path
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    static class DelegatingAisPacketStream implements AisPacketStream {
        final AisPacketStream stream;

        /**
         * @param stream
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

        /**
         * @param messageTypes
         * @return
         * @see dk.dma.ais.packet.AisPacketStream#filterOnMessageType(int[])
         */
        public AisPacketStream filterOnMessageType(int... messageTypes) {
            return stream.filterOnMessageType(messageTypes);
        }

        public Subscription subscribeMessages(Consumer<AisMessage> c) {
            return stream.subscribeMessages(c);
        }

        public Subscription subscribePackets(Consumer<AisPacket> c) {
            return stream.subscribePackets(c);
        }

        /** {@inheritDoc} */
        @Override
        public Subscription subscribePacketSink(OutputStreamSink<AisPacket> sink, OutputStream os) {
            return null;
        }
    }

    static class ImmutableAisPacketStream extends DelegatingAisPacketStream {

        /**
         * @param stream
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
