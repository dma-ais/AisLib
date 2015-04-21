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
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.tracker.Target;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Immutable information about a target. Whenever we receive a new message from the target. We create a new TargetInfo
 * instance via {@link #updateTarget(TargetInfo, AisPacket, AisTargetType, long, AisPacketSource, Map)}.
 * 
 * @author Kasper Nielsen
 */
public class TargetInfo extends Target {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = 2L;

    /** The target type of the info, is never null. */
    final AisTargetType targetType;
    
    /** The latest position and time stamp that was received Packet that was received. */
    final long positionTimestamp;
    final byte[] positionPacket;
    final Position position;

    final float cog;
    final int heading;
    final byte navStatus;
    final float sog;
    
    //Do not want serialization of mutable complex object
    transient volatile AisTarget aisTarget;
    //further caching
    transient volatile AisPacket positionAisPacket;
    transient volatile AisPacket staticAisPacket1;
    transient volatile AisPacket staticAisPacket2;

    // The latest static info
    final long staticTimestamp;
    final byte[] staticData1;
    final byte[] staticData2;
    final int staticShipType;

    final AisPacketSource packetSource;
    /**
     * @return the packetSource
     */
    public AisPacketSource getPacketSource() {
        return packetSource;
    }

    private TargetInfo(AisPacketSource packetSource, int mmsi, AisTargetType targetType, long positionTimestamp, Position p, int heading, float cog,
            float sog, byte navStatus, byte[] positionPacket, long staticTimestamp, byte[] staticData1,
            byte[] staticData2, int staticShipType) {
        super(mmsi);

        this.packetSource=packetSource;
                
        this.targetType = requireNonNull(targetType);

        // Position data
        this.positionTimestamp = positionTimestamp;
        this.position = p;
        this.heading = heading;
        this.cog = cog;
        this.sog = sog;
        this.navStatus = navStatus;
        this.positionPacket = positionPacket;

        // Static Data
        this.staticTimestamp = staticTimestamp;
        this.staticData1 = staticData1;
        this.staticData2 = staticData2;
        this.staticShipType = staticShipType;
        
        // Caching for AisPackets
        //this.staticAisPacket1 = getStaticAisPacket1();
        //this.staticAisPacket2 = getStaticAisPacket2();
        //this.positionAisPacket = getPositionPacket();
        
        // Caching for getAisTarget()
        //this.aisTarget = getAisTarget();
    }

    /**
     * Returns the target type of the target.
     * @return
     */
    public AisTargetType getTargetType() {
        return targetType;
    }

    public long getPositionTimestamp() {
        return positionTimestamp;
    }

    public Position getPosition() {
        return position;
    }

    public float getCog() {
        return cog;
    }

    public int getHeading() {
        return heading;
    }

    public byte getNavStatus() {
        return navStatus;
    }

    public float getSog() {
        return sog;
    }

    public long getStaticTimestamp() {
        return staticTimestamp;
    }

    public int getStaticShipType() {
        return staticShipType;
    }

    public AisTarget getAisTarget() {
        AisTarget t = aisTarget;
        return t == null ?aisTarget = TargetInfoToAisTarget.generateAisTarget(this) : t;
    }

    /**
     * Returns the country of the vessel based on its MMSI number.
     * 
     * @return the country of the vessel based on its MMSI number
     */
    public Country getCountry() {
        return Country.getCountryForMmsi(getMmsi());
    }

    /**
     * Returns the latest received position packet. Or <code>null</code> if no position has been received from the
     * vessel.
     * 
     * @return the latest received position packet
     */
    public AisPacket getPositionPacket() {
        byte[] positionPacket = this.positionPacket;
        if (positionPacket != null) {
            AisPacket positionAisPacket = this.positionAisPacket;
            return positionAisPacket == null ? positionAisPacket = AisPacket.fromByteArray(positionPacket): positionAisPacket;
        }
        return null;
    }

    /**
     * Returns the number of static packets we have received. Either return 0 if we have received 0 packets. 1 if we
     * have received a single packet. Or 2 we if have received two packets (AisMessage type 24).
     * 
     * @return the number of static packets we have received
     */
    public int getStaticCount() {
        return staticData1 == null ? 0 : staticData2 == null ? 1 : 2;
    }

    /**
     * Returns any static packets we have received from the target.
     */
    public AisPacket[] getStaticPackets() {
        if (staticData1 == null) {
            return new AisPacket[0];
        } else if (staticData2 == null) {
            return new AisPacket[] { getStaticAisPacket1() };
        } else {
            return new AisPacket[] { getStaticAisPacket1(), getStaticAisPacket2() };
        }
    }
    
    public AisPacket[] getPackets() {
        if (!hasPositionInfo()) {
            return getStaticPackets();
        } 
        
        Collection<AisPacket> packets = new ArrayList<>();
        
        for (AisPacket p: getStaticPackets()) {
            packets.add(p);
        }
        
        packets.add(getPositionPacket());
        
        return packets.toArray(new AisPacket[packets.size()]);
    }

    public AisPacket getStaticAisPacket1() {
        byte[] staticData1 = this.staticData1;
        if (staticData1 != null) {
            AisPacket staticAisPacket1 = this.staticAisPacket1;
            return staticAisPacket1 == null ? staticAisPacket1 = AisPacket.fromByteArray(staticData1): staticAisPacket1;
        }
        return null;
    }
    
    public AisPacket getStaticAisPacket2() {
        byte[] staticData2 = this.staticData2;
        if (staticData2 != null) {
            AisPacket staticAisPacket2 = this.staticAisPacket2;
            return staticAisPacket2 == null ? staticAisPacket2 = AisPacket.fromByteArray(staticData2): staticAisPacket2;
        }
        return null;
    }    

    /**
     * Returns true if we have positional information.
     * 
     * @return true if we have positional information
     */
    public boolean hasPositionInfo() {
        return positionPacket != null;
    }

    /**
     * Returns true if we have static information.
     * 
     * @return true if we have static information
     */
    public boolean hasStaticInfo() {
        return staticData1 != null;
    }

    TargetInfo merge(TargetInfo other) {
        if (positionTimestamp >= other.positionTimestamp && staticTimestamp >= other.staticTimestamp) {
            return this;
        } else if (other.positionTimestamp >= positionTimestamp && other.staticTimestamp >= staticTimestamp) {
            return other;
        }
        return positionTimestamp >= other.positionTimestamp ? mergeWithStaticFrom(other) : other
                .mergeWithStaticFrom(this);
    }

    /**
     * Copies the static information from the specified target into a new target. Maintaining the position information
     * of the current target.
     * 
     * @param other
     * @return the new target
     */
    TargetInfo mergeWithStaticFrom(TargetInfo other) {
        //assert packetSource == other.packetSource
        return new TargetInfo(packetSource, getMmsi(), targetType, positionTimestamp, position, heading, cog, sog, navStatus,
                positionPacket, other.staticTimestamp, other.staticData1, other.staticData2, other.staticShipType);
    }

    /**
     * We have received a new AIS message from a target.
     * 
     * @param existing
     *            non-null if an existing target with the same MMSI number already exists
     * @param packet
     *            the packet that was received
     * @param targetType
     *            the type of target
     * @param timestamp
     *            the timestamp of the packet
     * @param source
     *            the source of the packet
     * @param msg24Part0
     *            a map of cached message type 24 static part 0 messages
     * @return a new target info
     */
    static TargetInfo updateTarget(TargetInfo existing, AisPacket packet, AisTargetType targetType, long timestamp,
            AisPacketSource source, Map<AisPacketSource, byte[]> msg24Part0) {
        AisMessage message = packet.tryGetAisMessage();// is non-null
        int mmsi = message.getUserId();
        // ATON and BS targets are easy to handle because they do not contain much other than a position
        if (targetType == AisTargetType.ATON || targetType == AisTargetType.BS) {

            // make sure it has a never timestamp
            if (existing != null && timestamp <= existing.positionTimestamp) {
                return existing;
            }

            return new TargetInfo(source, mmsi, targetType, timestamp, message.getValidPosition(), -1, -1, -1, (byte) -1,
                    packet.toByteArray(), -1, null, null, -1);
        }
        TargetInfo result = updateTargetWithPosition(existing, packet, message, mmsi, targetType, timestamp, source);
        return updateTargetWithStatic(packet, message, mmsi, targetType, timestamp, source, result, msg24Part0);
    }

    static TargetInfo updateTargetWithPosition(TargetInfo existing, AisPacket packet, AisMessage message, int mmsi,
            AisTargetType targetType, long timestamp, AisPacketSource source) {
        if (message instanceof IVesselPositionMessage) {
            // only update if never timestamp
            if (existing == null || timestamp >= existing.positionTimestamp) {
                // check if another targetType in which case we drop the existing one
                // we need to have this check, after having checked the timestamp
                if (existing != null && existing.targetType != targetType) {
                    existing = null;
                }
                IVesselPositionMessage ivm = (IVesselPositionMessage) message;
                Position p = message.getValidPosition();
                int heading = ivm.getTrueHeading();
                float cog = ivm.getCog();
                float sog = ivm.getSog();
                byte navStatus = message instanceof AisPositionMessage ? (byte) ((AisPositionMessage) message)
                        .getNavStatus() : (byte) -1;

                if (existing == null) {
                    return new TargetInfo(source, mmsi, targetType, timestamp, p, heading, cog, sog, navStatus,
                            packet.toByteArray(), -1, null, null, -1);
                } else {
                    return new TargetInfo(source, mmsi, targetType, timestamp, p, heading, cog, sog, navStatus,
                            packet.toByteArray(), existing.staticTimestamp, existing.staticData1, existing.staticData2,
                            existing.staticShipType);
                }
            }
        }
        return existing;
    }

    static TargetInfo updateTargetWithStatic(AisPacket packet, AisMessage message, int mmsi, AisTargetType targetType,
            long timestamp, AisPacketSource source, TargetInfo existing, Map<AisPacketSource, byte[]> msg24Part0) {
        if (message instanceof AisStaticCommon) {
            // only update if never timestamp
            if (existing == null || timestamp >= existing.staticTimestamp) {
                // check if another targetType in which case we drop the existing one
                // we need to have this check, after having checked the timestamp
                if (existing != null && existing.targetType != targetType) {
                    existing = null;
                }

                AisStaticCommon c = (AisStaticCommon) message;
                byte[] static0;
                byte[] static1 = null;
                if (c instanceof AisMessage24) {
                    // AisMessage24 is split into 2 parts, if we get a part 0.
                    // Save in a hash table, where we keep it until we receive part 1
                    if (((AisMessage24) c).getPartNumber() == 0) {
                        msg24Part0.put(source, packet.toByteArray());
                        // we know that existing have not been updated by updateTargetWithPosition because
                        // AisMessage24 only contains static information, so existing=original
                        return existing; // the target is updated when we receive part 1
                    } else {
                        static0 = msg24Part0.remove(source);
                        if (static0 == null) {
                            return existing;// We do not have the first part:(
                        }
                        static1 = packet.toByteArray();
                    }
                } else {
                    static0 = packet.toByteArray();
                }

                if (existing == null) {
                    return new TargetInfo(source, mmsi, targetType, -1, null, -1, -1, -1, (byte) -1, null, timestamp, static0,
                            static1, c.getShipType());
                } else {
                    return new TargetInfo(source, mmsi, existing.targetType, existing.positionTimestamp, existing.position,
                            existing.heading, existing.cog, existing.sog, existing.navStatus, existing.positionPacket,
                            timestamp, static0, static1, c.getShipType());
                }
            }
        }
        return existing;
    }

}
