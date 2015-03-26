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
package dk.dma.ais.tracker;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.packet.AisPacketStream;
import dk.dma.ais.packet.AisPacketStream.Subscription;
import dk.dma.ais.reader.AisReaderGroup;

/**
 * 
 * @author Kasper Nielsen
 * @author Jens Tuxen
 */
public class TargetTracker {

    /** All targets that we are currently monitoring. */
    final ConcurrentHashMap<Integer, MmsiTarget> targets = new ConcurrentHashMap<>();

    public int countNumberOfReports(BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);
        LongAdder la = new LongAdder();
        targets.values().stream().forEach(t -> {
            for (TargetInfo i : t.values()) {
                if (predicate.test(i.getPacketSource(), i)) {
                    la.increment();
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
    public int countNumberOfTargets(Predicate<? super AisPacketSource> sourcePredicate,
            Predicate<? super TargetInfo> targetPredicate) {
        return (int) findTargetStream(sourcePredicate, targetPredicate).count();
    }

    public List<TargetInfo> findTargetList(Predicate<? super AisPacketSource> sourcePredicate,
            Predicate<? super TargetInfo> targetPredicate) {
        return new ArrayList<>(findTargetStream(sourcePredicate, targetPredicate).collect(
                Collectors.toCollection(() -> new ConcurrentLinkedQueue<>())));
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
    public Map<Integer, TargetInfo> findTargets(Predicate<? super AisPacketSource> sourcePredicate,
            Predicate<? super TargetInfo> targetPredicate) {
        return findTargetStream(sourcePredicate, targetPredicate).collect(
                Collectors.toConcurrentMap(e -> e.getMmsi(), e -> e));
    }

    /**
     * Find all targets (including duplicates from other sources) which matches bipredicate
     * 
     * @param predicate
     * @return
     */
    public Collection<TargetInfo> findTargetsIncludingDuplicates(
            BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);

        final ConcurrentLinkedDeque<TargetInfo> tis = new ConcurrentLinkedDeque<>();

        targets.forEachValue(10, t -> {
            for (Entry<AisPacketSource, TargetInfo> e : t.entrySet()) {
                if (predicate.test(e.getKey(), e.getValue())) {
                    tis.add(e.getValue());
                }
            }
        });
        return tis;
    }

    public Stream<TargetInfo> findTargetStream(Predicate<? super AisPacketSource> sourcePredicate,
            Predicate<? super TargetInfo> targetPredicate) {
        return latest(sourcePredicate).filter(requireNonNull(targetPredicate));
    }

    public TargetInfo getLatestTarget(int mmsi) {
        return getLatestTarget(mmsi, e -> true);
    }

    public TargetInfo getLatestTarget(int mmsi, Predicate<? super AisPacketSource> sourcePredicate) {
        MmsiTarget target = targets.get(mmsi);
        return target == null ? null : target.getLatest(sourcePredicate);
    }

    public int getNumberOfTargets() {
        return targets.size();
    }

    public Set<AisPacketSource> getSourcesForMMSI(int mmsi) {
        MmsiTarget t = targets.get(mmsi);
        return t == null ? Collections.emptySet() : new HashSet<>(t.keySet());
    }

    private Stream<TargetInfo> latest(Predicate<? super AisPacketSource> sourcePredicate) {
        requireNonNull(sourcePredicate);
        return targets.values().parallelStream().map(t -> t.getLatest(sourcePredicate)).filter(e -> e != null);
    }

    /**
     * Subscribes to all packets via {@link AisPacketStream#subscribe(Consumer)} from the specified stream.
     * 
     * @param stream
     *            the group
     * @return the subscription
     */
    public Subscription subscribeToStream(AisPacketStream stream) {
        return stream.subscribe(c -> update(c));
    }

    /**
     * Removes all targets that are accepted by the specified predicate. Is typically used to remove targets based on
     * time stamps.
     * 
     * @param predicate
     *            the predicate that selects which items to remove
     */
    public void removeAll(BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);
        targets.values().stream().forEach(t -> {
            for (TargetInfo i : t.values()) {
                if (predicate.test(i.getPacketSource(), i)) {
                    t.remove(i.getPacketSource(), i);
                }
            }
            if (t.isEmpty()) {
                targets.remove(t.mmsi, t);
            }
        });
    }

    /**
     * A little helper method that makes sure we do not get lost updates when updating a target. While the MMSI target
     * is being cleaned.
     * 
     * @param mmsi
     *            the MMSI number
     * @param c
     *            a consumer that can use the MMSI target
     */
    private void tryUpdate(int mmsi, Consumer<MmsiTarget> c) {
        for (;;) {
            // Lets first get the target. Or create a new target if it does not
            // currently exist
            MmsiTarget t = targets.computeIfAbsent(mmsi, i -> new MmsiTarget(mmsi));
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
    void update(AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        Date date = packet.getTimestamp();

        // We only want to handle messages containing targets data
        // #1-#3, #4, #5, #18, #21, #24 and a valid timestamp
        if (message != null && date != null) {
            // find the target type
            AisTargetType targetType = message.getTargetType();
            // only update if there is a target type
            if (targetType != null) {
                tryUpdate(
                        message.getUserId(),
                        t -> t.compute(
                                AisPacketSource.create(packet),
                                (source, existing) -> TargetInfo.updateTarget(existing, packet, targetType,
                                        date.getTime(), source, t.msg24Part0)));
            }
        }
    }

    /**
     * Used by the backup routine to restore data.
     * 
     * @param packetSource
     *            the source
     * @param targetInfo
     *            the target info
     */
    void update(AisPacketSource packetSource, TargetInfo targetInfo) {
        tryUpdate(targetInfo.mmsi, t -> {
            t.merge(packetSource, targetInfo, (ex, newOne) -> ex == null ? newOne : ex.merge(newOne));
        });
    }

    /**
     * A single ship containing multiple reports for different combinations of sources.
     */
    @SuppressWarnings("serial")
    static class MmsiTarget extends ConcurrentHashMap<AisPacketSource, TargetInfo> {

        /** The MMSI number */
        final int mmsi;

        /** A cache of AIS messages 24 part 0. */
        final ConcurrentHashMap<AisPacketSource, byte[]> msg24Part0 = new ConcurrentHashMap<>();

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
        TargetInfo getLatest(Predicate<? super AisPacketSource> predicate) {
            TargetInfo best = null;
            for (Entry<AisPacketSource, TargetInfo> i : this.entrySet()) {
                if (predicate.test(i.getKey())) {
                    // if more than one target matches the predicate
                    // we merge two at a. Taking the newest static information
                    // from one or the other.
                    // and merges it with the newest position information from
                    // one or the other (if needed).
                    best = best == null ? i.getValue() : best.merge(i.getValue());
                }
            }
            return best;
        }
    }
}
