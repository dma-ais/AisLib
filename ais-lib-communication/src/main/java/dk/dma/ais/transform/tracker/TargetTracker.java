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
package dk.dma.ais.transform.tracker;

import java.util.Date;
import java.util.Map;

import jsr166e.ConcurrentHashMapV8;
import jsr166e.ConcurrentHashMapV8.Action;
import jsr166e.ConcurrentHashMapV8.BiFun;
import jsr166e.ConcurrentHashMapV8.Fun;
import jsr166e.LongAdder;
import dk.dma.ais.data.AisTarget;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.packet.AisPacketStream.Subscription;
import dk.dma.ais.reader.AisReaderGroup;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class TargetTracker {

    /** All targets we are currently monitoring. */
    final ConcurrentHashMapV8<Integer, MmsiTarget> targets = new ConcurrentHashMapV8<>();

    /** Cleans up old targets according to the timeout policy. Must be called regular by users of this class. */
    public void clean() {
        targets.forEachValueInParallel(new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                if (t.isEmpty()) {
                    targets.remove(t.mmsi, t);
                }
                t.purgeOldEntries(1);
            }
        });
    }

    /**
     * Find all targets that matches the specified predicates.
     * 
     * @param sourcePredicate
     *            the predicate on sources
     * @param targetPredicate
     *            the predicate on targets
     * @return a map of matching targets
     */
    public Map<Integer, TargetInfo> findTargets(final Predicate<AisPacketSource> sourcePredicate,
            final Predicate<TargetInfo> targetPredicate) {
        final ConcurrentHashMapV8<Integer, TargetInfo> result = new ConcurrentHashMapV8<>();
        targets.forEachValueInParallel(new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                TargetInfo best = t.getBest(sourcePredicate);
                if (best != null && targetPredicate.test(best)) {
                    result.put(best.mmsi, best);
                }
            }
        });
        return result;
    }

    public int countNumberOfReports(final Predicate<? super AisPacketSource> sourcePredicate) {
        final LongAdder la = new LongAdder();
        targets.forEachValueInParallel(new Action<MmsiTarget>() {
            public void apply(MmsiTarget e) {
                for (AisPacketSource s : e.keySet()) {
                    if (sourcePredicate.test(s)) {
                        la.increment();
                    }
                }
            }
        });
        return la.intValue();
    }

    /**
     * Returns the number of tracked targets.
     * 
     * @return the number of tracked targets
     */
    public int countNumberOfTargets(final Predicate<? super AisPacketSource> sourcePredicate) {
        final LongAdder la = new LongAdder();
        targets.forEachValueInParallel(new Action<MmsiTarget>() {
            public void apply(MmsiTarget e) {
                for (AisPacketSource s : e.keySet()) {
                    if (sourcePredicate.test(s)) {
                        la.increment();
                        return;
                    }
                }
            }
        });
        return la.intValue();
    }

    void tryUpdate(final int mmsi, Consumer<MmsiTarget> c) {
        for (;;) {
            // Lets first get the target. Or create a new target if it does not currently exist
            MmsiTarget t = targets.computeIfAbsent(mmsi, new Fun<Integer, MmsiTarget>() {
                public MmsiTarget apply(Integer ignore) {
                    return new MmsiTarget(mmsi);
                }
            });
            c.accept(t);

            // We can get some very rare races with the cleanup method. So we just need to check that the mmsi target we
            // just updated/created is the same now
            if (t == targets.get(mmsi)) {
                return;
            }
        }
    }

    /**
     * Updates the tracker with the specified packet
     * 
     * @param packet
     *            the packet to update the trigger with
     */
    public void update(final AisPacket packet) {
        final AisMessage message = packet.tryGetAisMessage();
        final Date date = packet.getTags().getTimestamp();
        // We only want to handle messages containing targets data
        // #1-#3, #4, #5, #18, #21, #24 and a valid timestamp
        if (message != null && date != null && AisTarget.isTargetDataMessage(message)) {
            tryUpdate(message.getUserId(), new Consumer<MmsiTarget>() {
                public void accept(MmsiTarget t) {
                    t.update(packet, message, date.getTime());
                }
            });
        }
    }

    void update(final AisPacketSource sb, final TargetInfo ti) {
        tryUpdate(ti.mmsi, new Consumer<MmsiTarget>() {
            public void accept(MmsiTarget t) {
                t.merge(sb, ti, new BiFun<TargetInfo, TargetInfo, TargetInfo>() {
                    public TargetInfo apply(TargetInfo existing, TargetInfo newOne) {
                        return existing == null ? newOne : existing.merge(newOne);
                    }
                });
            }
        });
    }

    /**
     * Subscribes to all packets via {@link AisReaderGroup#stream()} from the specified group.
     * 
     * @param g
     *            the group
     * @return the subscription
     */
    public Subscription updateFrom(AisReaderGroup g) {
        return g.stream().subscribe(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket p) {
                update(p);
            }
        });
    }

    @SuppressWarnings("serial")
    static class MmsiTarget extends ConcurrentHashMapV8<AisPacketSource, TargetInfo> {
        /** The mmsi number */
        final int mmsi;

        MmsiTarget(int mmsi) {
            this.mmsi = mmsi;
        }

        TargetInfo getBest(Predicate<? super AisPacketSource> predicate) {
            TargetInfo best = null;
            for (Entry<AisPacketSource, TargetInfo> i : entrySet()) {
                if (predicate.test(i.getKey())) {
                    best = best == null ? i.getValue() : best.merge(i.getValue());
                }
            }
            return best;
        }

        boolean hasAny(Predicate<? super AisPacketSource> predicate) {
            for (AisPacketSource s : keySet()) {
                if (predicate.test(s)) {
                    return true;
                }
            }
            return false;
        }

        void purgeOldEntries(long before) {
            // TODO implement
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "MmsiTarget [mmsi=" + mmsi + ", targetSize = " + size() + ", targets=" + super.toString() + "]";
        }

        void update(final AisPacket packet, final AisMessage message, final long timestamp) {
            AisPacketSource sb = AisPacketSource.create(packet);
            compute(sb, new BiFun<AisPacketSource, TargetInfo, TargetInfo>() {
                public TargetInfo apply(AisPacketSource source, TargetInfo info) {
                    if (message instanceof AisPositionMessage) {
                        return TargetInfo.updatePosition(mmsi, info, timestamp, packet, (AisPositionMessage) message);
                    }
                    return info;
                }
            });
        }
    }
}
