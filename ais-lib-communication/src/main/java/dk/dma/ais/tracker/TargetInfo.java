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

import java.io.Serializable;
import java.util.Map;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.enav.model.geometry.Position;

/**
 * Information about a target
 * 
 * @author Kasper Nielsen
 */
public class TargetInfo implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The MMSI number of the target. */
    final int mmsi;

    /** The target type of the info, is never null. */
    final AisTargetType targetType;

    /** The latest positionPacket that was received. */
    final long positionTimestamp;
    final byte[] positionPacket;
    final Position position;

    final float cog;
    final int heading;
    final byte navStatus;
    final float sog;

    // The latest static info
    final long staticTimestamp;
    final byte[] staticData1;
    final byte[] staticData2;
    final int staticShipType;

    private TargetInfo(int mmsi, AisTargetType targetType, long positionTimestamp, Position p, int heading, float cog,
            float sog, byte navStatus, byte[] positionPacket, long staticTimestamp, byte[] staticData1,
            byte[] staticData2, int staticShipType) {
        this.mmsi = mmsi;
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
    }

    public AisPacket getPositionPacket() {
        return positionPacket == null ? null : AisPacket.fromByteArray(positionPacket);
    }

    public int getStaticCount() {
        return staticData1 == null ? 0 : staticData2 == null ? 1 : 2;
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
    private TargetInfo mergeWithStaticFrom(TargetInfo other) {
        return new TargetInfo(mmsi, targetType, positionTimestamp, position, heading, cog, sog, navStatus,
                positionPacket, other.staticTimestamp, other.staticData1, other.staticData2, other.staticShipType);
    }

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

            return new TargetInfo(mmsi, targetType, timestamp, message.getValidPosition(), -1, -1, -1, (byte) -1,
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
                int heading = 0;
                float cog = ivm.getCog();
                float sog = ivm.getSog();
                byte navStatus = message instanceof AisPositionMessage ? (byte) ((AisPositionMessage) message)
                        .getNavStatus() : (byte) -1;

                if (existing == null) {
                    return new TargetInfo(mmsi, targetType, timestamp, p, heading, cog, sog, navStatus,
                            packet.toByteArray(), -1, null, null, -1);
                } else {
                    return new TargetInfo(mmsi, targetType, timestamp, p, heading, cog, sog, navStatus,
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
                    // Save in a hashtable, where it is kept, until we receive part 1
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
                    return new TargetInfo(mmsi, targetType, -1, null, -1, -1, -1, (byte) -1, null, timestamp, static0,
                            static1, c.getShipType());
                } else {
                    return new TargetInfo(mmsi, existing.targetType, existing.positionTimestamp, existing.position,
                            existing.heading, existing.cog, existing.sog, existing.navStatus, existing.positionPacket,
                            timestamp, static0, static1, c.getShipType());
                }
            }
        }
        return existing;
    }
}
