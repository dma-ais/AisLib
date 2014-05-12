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
package dk.dma.ais.tracker;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import jsr166e.ConcurrentHashMapV8;
import jsr166e.ConcurrentHashMapV8.Action;
import jsr166e.ConcurrentHashMapV8.BiFun;
import jsr166e.ConcurrentHashMapV8.Fun;
import jsr166e.LongAdder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.packet.AisPacketStream;
import dk.dma.ais.packet.AisPacketStream.Subscription;
import dk.dma.ais.reader.AisReaderGroup;
import dk.dma.enav.util.function.BiPredicate;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class TargetTracker implements Tracker {

    /** All targets that we are currently monitoring. */
    final ConcurrentHashMapV8<Integer, MmsiTarget> targets = new ConcurrentHashMapV8<>();

    public int countNumberOfReports(
            final BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);
        final LongAdder la = new LongAdder();
        targets.forEachValue(10, new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                for (Entry<AisPacketSource, TargetInfo> e : t.entrySet()) {
                    if (predicate.test(e.getKey(), e.getValue())) {
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
     * @param sourcePredicate
     *            a predicate that can be used on the source
     * @return the number of tracked targets
     */
    public int countNumberOfTargets(
            final Predicate<? super AisPacketSource> sourcePredicate,
            final Predicate<? super TargetInfo> targetPredicate) {
        requireNonNull(sourcePredicate);
        requireNonNull(targetPredicate);
        final LongAdder la = new LongAdder();
        targets.forEachValue(10, new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                TargetInfo best = t.getNewest(sourcePredicate);
                if (best != null && targetPredicate.test(best)) {
                    la.increment();
                    return;
                }
            }
        });
        return la.intValue();
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
    public Map<Integer, TargetInfo> findTargets(
            final Predicate<? super AisPacketSource> sourcePredicate,
            final Predicate<? super TargetInfo> targetPredicate) {
        requireNonNull(sourcePredicate);
        requireNonNull(targetPredicate);
        final ConcurrentHashMapV8<Integer, TargetInfo> result = new ConcurrentHashMapV8<>();
        targets.forEachValue(10, new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                TargetInfo best = t.getNewest(sourcePredicate);
                if (best != null && targetPredicate.test(best)) {
                    result.put(best.mmsi, best);
                }
            }
        });
        return result;
    }

    /**
     * Subscribes to all packets via {@link AisReaderGroup#stream()} from the
     * specified group.
     * 
     * @param g
     *            the group
     * @return the subscription
     */
    @Override
    public Subscription readFromStream(AisPacketStream stream) {
        return stream.subscribe(new Consumer<AisPacket>() {
            public void accept(AisPacket p) {
                update(p);
            }
        });
    }

    public TargetInfo getNewest(int mmsi) {
        return getNewest(mmsi, Predicate.TRUE);
    }

    public Entry<AisPacketSource, TargetInfo> getNewestEntry(int mmsi) {
        return getNewestEntry(mmsi, Predicate.TRUE);
    }

    public Enumeration<AisPacketSource> getAisPacketSources(int mmsi) {
        return targets.get(mmsi).keys();
    }

    public TargetInfo getNewest(int mmsi,
            Predicate<? super AisPacketSource> sourcePredicate) {
        MmsiTarget target = targets.get(mmsi);
        return target == null ? null : target.getNewest(sourcePredicate);
    }

    public Entry<AisPacketSource, TargetInfo> getNewestEntry(int mmsi,
            Predicate<? super AisPacketSource> sourcePredicate) {
        MmsiTarget target = targets.get(mmsi);
        return target == null ? null : target.getNewestEntry(sourcePredicate);
    }

    /**
     * Removes all targets that are accepted by the specified predicate. Is
     * typically used to remove targets based on time stamps.
     * 
     * @param predicate
     *            the predicate that selects which items to remove
     */
    public void removeAll(
            final BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);
        targets.forEachValue(10, new Action<MmsiTarget>() {
            public void apply(MmsiTarget t) {
                for (Map.Entry<AisPacketSource, TargetInfo> e : t.entrySet()) {
                    if (predicate.test(e.getKey(), e.getValue())) {
                        t.remove(e.getKey(), e.getValue());
                    }
                }
                // if there are no more targets just remove it
                // tryUpdate contains functionality to make sure we do not have
                // any consistency issues.
                if (t.isEmpty()) {
                    targets.remove(t.mmsi, t);
                }
            }
        });
    }

    /**
     * A little helper method that makes sure we do not get lost updates when
     * updating a target. While the MMSI target is being cleaned.
     * 
     * @param mmsi
     *            the MMSI number
     * @param c
     *            a consumer that can use the MMSI target
     */
    private void tryUpdate(final int mmsi, Consumer<MmsiTarget> c) {
        for (;;) {
            // Lets first get the target. Or create a new target if it does not
            // currently exist
            MmsiTarget t = targets.computeIfAbsent(mmsi,
                    new Fun<Integer, MmsiTarget>() {
                        public MmsiTarget apply(Integer ignore) {
                            return new MmsiTarget(mmsi);
                        }
                    });
            c.accept(t);

            // We can get some very rare races with the cleanup method. So we
            // just need to check that the mmsi target we
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
    void update(final AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        final Date date = packet.getTags().getTimestamp();
        // We only want to handle messages containing targets data
        // #1-#3, #4, #5, #18, #21, #24 and a valid timestamp
        if (message != null && date != null) {
            // find the target type
            final AisTargetType targetType = message.getTargetType();
            // only update if there is a target type
            if (targetType != null) {
                tryUpdate(message.getUserId(), new Consumer<MmsiTarget>() {
                    public void accept(final MmsiTarget t) {
                        // lazy compute the new target info
                        t.compute(
                                AisPacketSource.create(packet),
                                new BiFun<AisPacketSource, TargetInfo, TargetInfo>() {
                                    public TargetInfo apply(
                                            AisPacketSource source,
                                            TargetInfo existing) {
                                        return TargetInfo.updateTarget(
                                                existing, packet, targetType,
                                                date.getTime(), source,
                                                t.msg24Part0);
                                    }
                                });
                    }
                });
            }
        }
    }

    /**
     * Used by the backup routine to restore data.
     * 
     * @param sb
     *            the source
     * @param ti
     *            the target info
     */
    void update(final AisPacketSource sb, final TargetInfo ti) {
        tryUpdate(ti.mmsi, new Consumer<MmsiTarget>() {
            public void accept(MmsiTarget t) {
                t.merge(sb, ti,
                        new BiFun<TargetInfo, TargetInfo, TargetInfo>() {
                            public TargetInfo apply(TargetInfo existing,
                                    TargetInfo newOne) {
                                return existing == null ? newOne : existing
                                        .merge(newOne);
                            }
                        });
            }
        });
    }

    /**
     * A single ship containing multiple reports for different combinations of
     * sources.
     */
    @SuppressWarnings("serial")
    static class MmsiTarget extends
            ConcurrentHashMapV8<AisPacketSource, TargetInfo> {

        /** The MMSI number */
        final int mmsi;

        /** A cache of AIS messages 24 part 0. */
        final ConcurrentHashMapV8<AisPacketSource, byte[]> msg24Part0 = new ConcurrentHashMapV8<>();

        MmsiTarget(int mmsi) {
            this.mmsi = mmsi;
        }

        /**
         * Returns the newest position and static data.
         * 
         * @param predicate
         *            a predicate for filtering on the sources
         * @return the newest position and static data
         */
        TargetInfo getNewest(Predicate<? super AisPacketSource> predicate) {
            TargetInfo best = null;
            for (Entry<AisPacketSource, TargetInfo> i : entrySet()) {
                if (predicate.test(i.getKey())) {
                    // if more than one target matches the predicate
                    // we merge two at a. Taking the newest static information
                    // from one or the other.
                    // and merges it with the newest position information from
                    // one or the other (if needed).
                    best = best == null ? i.getValue() : best.merge(i
                            .getValue());
                }
            }
            return best;
        }

        Entry<AisPacketSource, TargetInfo> getNewestEntry(
                Predicate<? super AisPacketSource> predicate) {
            TargetInfo best = null;
            Entry<AisPacketSource, TargetInfo> bestEntry = null;
            for (Entry<AisPacketSource, TargetInfo> i : entrySet()) {
                if (predicate.test(i.getKey())) {
                    best = best == null ? i.getValue() : best.merge(i
                            .getValue());
                }

                if (i.getValue().equals(best)) {
                    bestEntry = i;
                }

            }

            return bestEntry;
        }
    }
}
