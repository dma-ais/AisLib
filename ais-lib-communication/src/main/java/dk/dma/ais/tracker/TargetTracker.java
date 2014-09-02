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

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
public class TargetTracker implements Tracker {
    private static final Predicate<? super Object> PREDICATETRUE = new Predicate<Object>() {
        @Override
        public boolean test(Object o) {
            return true;
        }
    };

    /** All targets that we are currently monitoring. */
    final ConcurrentHashMap<Integer, MmsiTarget> targets = new ConcurrentHashMap<>(
            200000);

    public int countNumberOfReports(
            final BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);
        final LongAdder la = new LongAdder();
        targets.forEachValue(10, new java.util.function.Consumer<MmsiTarget>() {

            @Override
            public void accept(MmsiTarget t) {
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
     * Find all targets (including duplicates from other sources) which matches
     * bipredicate
     * 
     * @param predicate
     * @return
     */
    public Collection<TargetInfo> findTargetsIncludingDuplicates(
            final BiPredicate<? super AisPacketSource, ? super TargetInfo> predicate) {
        requireNonNull(predicate);

        final ConcurrentLinkedDeque<TargetInfo> tis = new ConcurrentLinkedDeque<TargetInfo>();

        targets.forEachValue(10, new java.util.function.Consumer<MmsiTarget>() {
            @Override
            public void accept(MmsiTarget t) {
                for (Entry<AisPacketSource, TargetInfo> e : t.entrySet()) {
                    if (predicate.test(e.getKey(), e.getValue())) {
                        tis.add(e.getValue());
                    }
                }
            }
        });
        return tis;
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
        targets.forEachValue(10, new java.util.function.Consumer<MmsiTarget>() {
            @Override
            public void accept(MmsiTarget t) {
                TargetInfo best = t.getNewest(sourcePredicate);
                if (best != null && targetPredicate.test(best)) {
                    la.increment();
                    return;
                }
            }
        });
        return la.intValue();
    }

    public int size() {
        return targets.size();
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
        final ConcurrentHashMap<Integer, TargetInfo> result = new ConcurrentHashMap<>();
        targets.forEachValue(10, new java.util.function.Consumer<MmsiTarget>() {
            @Override
            public void accept(MmsiTarget t) {
                TargetInfo best = t.getNewest(sourcePredicate);
                if (best != null && targetPredicate.test(best)) {
                    result.put(best.mmsi, best);
                }
            }
        });

        return result;
    }

    public Stream<TargetInfo> findTargets8(
            final Predicate<? super AisPacketSource> sourcePredicate,
            final Predicate<? super TargetInfo> targetPredicate) {
        requireNonNull(sourcePredicate);
        requireNonNull(targetPredicate);

        /*
         * lambda expressions not supported with source 1.7 return
         * targets.values().stream().sequential() .map(o ->
         * o.getNewest(sourcePredicate)) .filter(ti ->
         * targetPredicate.test((TargetInfo) ti));
         */

        return targets.values().stream().parallel()
                .map(new Function<MmsiTarget, TargetInfo>() {
                    @Override
                    public TargetInfo apply(MmsiTarget t) {
                        return t.getNewest(sourcePredicate);
                    }
                }).parallel()
                .filter(new java.util.function.Predicate<TargetInfo>() {
                    @Override
                    public boolean test(TargetInfo t) {
                        return t != null && targetPredicate.test(t);
                    }
                });
    }

    public Collection<TargetInfo> findTargets8List(
            final Predicate<? super AisPacketSource> sourcePredicate,
            final Predicate<? super TargetInfo> targetPredicate) {

        return findTargets8(sourcePredicate,targetPredicate).collect(Collectors.toCollection(new Supplier<ConcurrentLinkedQueue<TargetInfo>>() {

            @Override
            public ConcurrentLinkedQueue<TargetInfo> get() {
                // TODO Auto-generated method stub
                return new ConcurrentLinkedQueue<TargetInfo>();
            }
        }));

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
        return getNewest(mmsi, PREDICATETRUE);
    }

    public Entry<AisPacketSource, TargetInfo> getNewestEntry(int mmsi) {
        return getNewestEntry(mmsi, PREDICATETRUE);
    }

    public Set<AisPacketSource> getAisPacketSources(int mmsi) {
        return targets.get(mmsi).keySet();
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
        targets.forEachValue(10, new java.util.function.Consumer<MmsiTarget>() {
            @Override
            public void accept(MmsiTarget t) {
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
                    new Function<Integer, MmsiTarget>() {
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
        final Date date = packet.getTimestamp();
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
                                new BiFunction<AisPacketSource, TargetInfo, TargetInfo>() {
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
                        new BiFunction<TargetInfo, TargetInfo, TargetInfo>() {
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
    static class MmsiTarget extends ConcurrentHashMap<AisPacketSource, TargetInfo> {

        /** The MMSI number */
        final int mmsi;

        /** A cache of AIS messages 24 part 0. */
        final ConcurrentHashMap<AisPacketSource, byte[]> msg24Part0 = new ConcurrentHashMap<>();

        //switch to implements and then
        //final Cache<AisPacketSource, TargetInfo> cache = CacheBuilder
        //        .newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build();

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
            for (Entry<AisPacketSource, TargetInfo> i : this.entrySet()) {
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

        //Switch to implements and use a guava cache for automatic AisPacketSource eviction
        /*
        @Override
        public int size() {
            return cache.asMap().size();
        }

        @Override
        public boolean isEmpty() {
            return cache.asMap().isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return cache.asMap().containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return cache.asMap().containsValue(value);
        }

        @Override
        public TargetInfo get(Object key) {
            return cache.asMap().get(key);
        }

        @Override
        public TargetInfo put(AisPacketSource key, TargetInfo value) {
            return cache.asMap().put(key, value);
        }

        @Override
        public TargetInfo remove(Object key) {
            return cache.asMap().remove(key);
        }

        @Override
        public void putAll(
                Map<? extends AisPacketSource, ? extends TargetInfo> m) {
            cache.asMap().putAll(m);

        }

        @Override
        public void clear() {
            cache.asMap().clear();
        }

        @Override
        public Set<AisPacketSource> keySet() {
            return cache.asMap().keySet();
        }

        @Override
        public Collection<TargetInfo> values() {
            return cache.asMap().values();
        }

        @Override
        public Set<java.util.Map.Entry<AisPacketSource, TargetInfo>> entrySet() {
            return cache.asMap().entrySet();
        }

        @Override
        public TargetInfo putIfAbsent(AisPacketSource key, TargetInfo value) {
            // TODO Auto-generated method stub
            return cache.asMap().putIfAbsent(key, value);
        }

        @Override
        public boolean remove(Object key, Object value) {
            return cache.asMap().remove(key, value);
        }

        @Override
        public boolean replace(AisPacketSource key, TargetInfo oldValue,
                TargetInfo newValue) {
            return cache.asMap().replace(key, oldValue, newValue);
        }

        @Override
        public TargetInfo replace(AisPacketSource key, TargetInfo value) {
            return cache.asMap().replace(key, value);
        }
        */

    }

}
