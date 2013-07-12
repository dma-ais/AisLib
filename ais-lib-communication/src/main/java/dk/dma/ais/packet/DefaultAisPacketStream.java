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
import jsr166e.ConcurrentHashMapV8;
import jsr166e.ConcurrentHashMapV8.Action;
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

        // TODO Must deliver in order
        map.forEachKeyInParallel(new Action<SubscriptionImpl>() {
            @Override
            public void apply(SubscriptionImpl s) {
                handlePacket(p, s);
            }
        });
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
        final Consumer<? super AisPacket> consumer;
        final Predicate<? super AisPacket> predicate;

        SubscriptionImpl(Predicate<? super AisPacket> predicate, Consumer<? super AisPacket> consumer) {
            this.predicate = predicate;
            this.consumer = requireNonNull(consumer);
        }

        /** {@inheritDoc} */
        @Override
        public void cancel() {
            map.remove(this);
        }
    }
}
