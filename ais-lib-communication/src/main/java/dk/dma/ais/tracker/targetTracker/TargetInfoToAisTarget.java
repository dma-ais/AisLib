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
package dk.dma.ais.tracker.targetTracker;

import dk.dma.ais.data.AisTarget;
import dk.dma.ais.data.AisVesselStatic;
import dk.dma.ais.data.AisVesselTarget;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.tracker.targetTracker.TargetTracker.MmsiTarget;

import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.function.Consumer;

/**
 * Necessary Evil for legacy resources and convenience
 * 
 * @author Jens Tuxen
 *
 */
public class TargetInfoToAisTarget {

    static PriorityQueue<AisPacket> getPacketsInOrder(TargetInfo ti) {

        // Min-Heap = Oldest first when creating AisTarget (natural)
        PriorityQueue<AisPacket> messages = new PriorityQueue<>();

        if (ti == null) {
            return messages;
        }

        for (AisPacket p : ti.getStaticPackets()) {
            if (p != null) {
                messages.add(p);
            }
        }

        if (ti.hasPositionInfo()) {
            AisPacket p = ti.getPositionPacket();
            if (p != null) {
                messages.add(p);
            }
        }

        return messages;
    }

    public static AisTarget generateAisTarget(TargetInfo ti) {
        PriorityQueue<AisPacket> normal = getPacketsInOrder(ti);
        // why reversed also? A workaround for AisTargets without static information for some reason
        // TODO: this needs to be replaced with a proper fix
        PriorityQueue<AisPacket> reversed = new PriorityQueue<>(normal.size(), Collections.reverseOrder());
        reversed.addAll(normal);
        AisTarget a = generateAisTarget(normal);
        updateAisTarget(a, reversed);
        return a;
    }

    /**
     * Update aisTarget with messages (note that if the packets are of different class type,
     *
     * @param aisTarget
     * @param messages
     * @return
     */
    static AisTarget updateAisTarget(AisTarget aisTarget, PriorityQueue<AisPacket> messages) {
        while (!messages.isEmpty()) {
            aisTarget = updateWithNewestTakingPrecedent(aisTarget, messages.poll());
        }
        return aisTarget;
    }

    /**
     * Newest AisTarget takes precedent when sources have conflicting Class A/B packets.
     *
     * @param t
     * @param m
     * @return
     */
    private static AisTarget updateWithNewestTakingPrecedent(AisTarget t, AisPacket m) {
        try {
            t.update(m.tryGetAisMessage());
        } catch (NullPointerException e) {
            // pass
        } catch (IllegalArgumentException exc) {
            // happens when we try to update ClassA with ClassB and visa
            // versa
            // the youngest (newest report) takes president

            AisTarget tmp = AisTarget.createTarget(m.tryGetAisMessage());

            if (tmp != null && tmp.getLastReport() != null) {
                if (t != null && t.getLastReport() != null) {
                    return (tmp.getLastReport().after(t.getLastReport())) ? tmp : t;
                }
                return tmp;
            }

        }
        return t;

    }

    static AisTarget generateAisTarget(PriorityQueue<AisPacket> messages) {
        AisTarget aisTarget = null;

        while (aisTarget == null && !messages.isEmpty()) {
            aisTarget = AisTarget.createTarget(messages.poll().tryGetAisMessage());
        }

        return updateAisTarget(aisTarget, messages);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AisReader reader = AisReaders.createDirectoryReader("src/test", "s*.txt", true);

        dk.dma.ais.tracker.targetTracker.TargetTracker tt = new TargetTracker();
        tt.subscribeToPacketStream(reader.stream());

        reader.start();
        reader.join();

        tt.targets.values().forEach(new Consumer<MmsiTarget>() {

            @Override
            public void accept(MmsiTarget t) {
                System.out.println("TARGET " + t.mmsi);
                TargetInfo ti = t.getLatest(ttt -> ttt != null);
                AisTarget k = generateAisTarget(ti);
                if (k != null && k instanceof AisVesselTarget) {
                    AisVesselStatic avs = ((AisVesselTarget) k).getVesselStatic();
                    if (avs != null) {
                        System.out.println(avs.getName());
                    }

                }

                for (AisPacket l : getPacketsInOrder(ti)) {

                    System.out.println(l.getBestTimestamp());
                    AisMessage m = l.tryGetAisMessage();

                    if (m instanceof AisMessage5) {
                        System.out.println("MY NAME IS " + ((AisMessage5) m).getName());
                    }

                }
                System.out.println("TARGET END " + t.mmsi);

            }
        });

    }

}
