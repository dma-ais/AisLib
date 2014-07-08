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

import java.util.Objects;
import java.util.TreeSet;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.data.AisTarget;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;

/**
 * Neccessary Evil for legacy resources and convenience
 * @author Jens Tuxen
 *
 */
public class TargetInfoToAisTarget {
    
    static TreeSet<AisPacket> getPacketsInOrder(TargetInfo ti) {
        TreeSet<AisPacket> messages = new TreeSet<>();

        for (AisPacket p : ti.getStaticPackets()) {
            try {
                messages.add(Objects.requireNonNull(p));
            } catch (NullPointerException exc) {
                // pass
            }
        }

        if (ti.hasPositionInfo()) {
            messages.add(ti.getPositionPacket());
        }

        return messages;
    }


    public static AisTarget generateAisTarget(TargetInfo ti) {
        return generateAisTarget(getPacketsInOrder(ti));
    }

    /**
     * Update aisTarget with messages (note that if the packets are of different
     * class type,
     * 
     * @param aisTarget
     * @param messages
     * @return
     */
    static AisTarget updateAisTarget(AisTarget aisTarget,
            TreeSet<AisPacket> messages) {
        for (AisPacket p : messages.descendingSet()) {
            try {
                aisTarget.update(p.getAisMessage());
            } catch (AisMessageException | SixbitException
                    | NullPointerException e) {
                // pass
            } catch (IllegalArgumentException exc) {
                // happens when we try to update ClassA with ClassB and visa
                // versa
                // the youngest (newest report) takes president
            }
        }
        return aisTarget;
    }

    @SuppressWarnings("unused")
    private AisTarget updateAisTarget(AisTarget aisTarget, TargetInfo ti) {
        return updateAisTarget(aisTarget, getPacketsInOrder(ti));
    }
    

    /**
     * Generate AisTarget with first packet being p
     * 
     * @param p
     *            ais packet
     * @param messages
     *            ordered ais packets
     * @return AisTarget
     */
    @SuppressWarnings("unused")
    private AisTarget generateAisTarget(final AisPacket p,
            final TreeSet<AisPacket> messages) {
        AisTarget aisTarget = AisTarget.createTarget(p.tryGetAisMessage());
        return updateAisTarget(aisTarget, messages);
    }

    static AisTarget generateAisTarget(TreeSet<AisPacket> messages) {
        AisTarget aisTarget = null;
        for (AisPacket packet : messages.descendingSet()) {
            aisTarget = AisTarget.createTarget(packet.tryGetAisMessage());

            if (aisTarget != null) {
                break;
            }
        }

        return updateAisTarget(aisTarget, messages);
    }
    
    
}
