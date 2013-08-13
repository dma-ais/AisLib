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
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.enav.model.geometry.Position;

/**
 * 
 * @author Kasper Nielsen
 */
public class TargetInfo implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The MMSI number of the target. */
    final int mmsi;

    final AisTargetType targetType;

    /** The latest positionPacket that was received. */
    final byte[] positionPacket;
    final long positionTimestamp;
    final Position position;

    final int heading;
    final float cog;
    final float sog;
    final byte navStatus;

    // The latest static info
    final long staticTimestamp;
    final byte[] staticData1;
    final byte[] staticData2;
    final int staticShipType;

    transient volatile boolean isBackedUp; /* = false */

    /** Used by Basestation and ATON targets. */
    TargetInfo(int mmsi, AisTargetType targetType, long positionTimestamp, Position position, byte[] positionPacket) {
        this(mmsi, targetType, positionTimestamp, position, -1, -1, -1, (byte) -1, positionPacket, -1, null, null, -1);
    }

    TargetInfo(int mmsi, AisTargetType targetType, long positionTimestamp, Position position, int heading, float cog,
            float sog, byte navStatus, byte[] positionPacket) {
        this(mmsi, targetType, positionTimestamp, position, heading, cog, sog, navStatus, positionPacket, -1, null,
                null, -1);
    }

    TargetInfo(int mmsi, long positionTimestamp, Position position, int heading, float cog, float sog, byte navStatus,
            byte[] positionPacket, TargetInfo statics) {
        this(mmsi, statics.targetType, positionTimestamp, position, heading, cog, sog, navStatus, positionPacket,
                statics.staticTimestamp, statics.staticData1, statics.staticData2, statics.staticShipType);
    }

    TargetInfo(int mmsi, TargetInfo positional, long staticTimestamp, byte[] staticData1, byte[] staticData2,
            int staticShipType) {
        this(mmsi, positional.targetType, positional.positionTimestamp, positional.position, positional.heading,
                positional.cog, positional.sog, positional.navStatus, positional.positionPacket, staticTimestamp,
                staticData1, staticData2, staticShipType);
    }

    TargetInfo(int mmsi, AisTargetType targetType, long staticTimestamp, byte[] staticData1, byte[] staticData2,
            int staticShipType) {
        this(mmsi, targetType, -1, null, -1, -1, -1, (byte) -1, null, staticTimestamp, staticData1, staticData2,
                staticShipType);
    }

    /**
     * @param timestamp
     * @param latitude
     * @param longitude
     * @param heading
     */
    TargetInfo(int mmsi, AisTargetType targetType, long positionTimestamp, Position p, int heading, float cog, float sog,
            byte navStatus, byte[] positionPacket, long staticTimestamp, byte[] staticData1, byte[] staticData2,
            int staticShipType) {
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

    public AisPositionMessage getPositionMessage() {
        return positionPacket == null ? null : (AisPositionMessage) getPositionPacket().tryGetAisMessage();
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

    public TargetInfo merge(TargetInfo other) {
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
        return new TargetInfo(mmsi, positionTimestamp, position, heading, cog, sog, navStatus, positionPacket, other);
    }

    static TargetInfo updateStatic(int mmsi, AisTargetType targetType, TargetInfo existing, long timestamp,
            byte[] staticData1, byte[] staticData2, int staticShipType) {
        if (existing == null) {
            return new TargetInfo(mmsi, targetType, timestamp, staticData1, staticData2, staticShipType);
        }
        return new TargetInfo(mmsi, existing, timestamp, staticData1, staticData2, staticShipType);
    }

    static TargetInfo updatePosition(int mmsi, AisTargetType targetType, TargetInfo existing, long timestamp,
            final AisPacket packet, final IVesselPositionMessage message) {
        Position p = message.getValidPosition();
        if (p != null) {
            int heading = 0;
            float cog = message.getCog();
            float sog = message.getSog();
            byte navStatus = -1;
            if (existing == null) {
                return new TargetInfo(mmsi, targetType, timestamp, p, heading, cog, sog, navStatus,
                        packet.toByteArray());
            } else {
                return new TargetInfo(mmsi, timestamp, p, heading, cog, sog, navStatus, packet.toByteArray(), existing);
            }
        }
        return existing;
    }

    static TargetInfo updateTarget(int mmsi, AisPacket packet, AisTargetType targetType, AisMessage message,
            long timestamp, AisPacketSource source, final TargetInfo existing, Map<AisPacketSource, byte[]> msg24Part0) {
        // Handle ATON and BS targets are easy to handle
        if (targetType == AisTargetType.ATON || targetType == AisTargetType.BS) {
            // make sure it has a never timestamp
            if (existing != null && timestamp <= existing.positionTimestamp) {
                return existing;
            }
            return new TargetInfo(mmsi, targetType, timestamp, message.getValidPosition(), packet.toByteArray());
        }

        // Some messages might contain both a static and position message
        TargetInfo result = existing;

        // check if target type is different from existing, in which case we will never keep some of the old
        // value. For example, keep the static part, but update the position

        // Handles Position messages
        if (message instanceof IVesselPositionMessage) {
            // only update if never timestamp
            if (existing == null || timestamp >= existing.positionTimestamp) {
                // check if another targetType in which case we drop it
                // we need to have this check, after having checked the timestamp
                if (existing != null && existing.targetType != targetType) {
                    result = null;
                }
                result = TargetInfo.updatePosition(mmsi, targetType, result, timestamp, packet,
                        (IVesselPositionMessage) message);
            }
        }

        if (message instanceof AisStaticCommon) {
            // only update if never timestamp
            if (existing == null || timestamp >= existing.staticTimestamp) {
                // check if another targetType in which case we drop it
                // we need to have this check, after having checked the timestamp

                // we check on result, to make sure we do not replace positional updates
                // with previous results.
                if (result != null && result.targetType != targetType) {
                    result = null;
                }

                AisStaticCommon c = (AisStaticCommon) message;
                // AisMessage24 is split into 2 parts, if we get a part 0.
                // Save in a hashtable, where it is kept, until we receive part 1
                if (c instanceof AisMessage24) {
                    if (((AisMessage24) c).getPartNumber() == 0) {
                        msg24Part0.put(source, packet.toByteArray());
                        return existing; // the target is updated when we receive part 1
                    } else {
                        byte[] part0 = msg24Part0.remove(source);
                        if (part0 != null) {
                            result = TargetInfo.updateStatic(mmsi, targetType, result, timestamp, part0,
                                    packet.toByteArray(), c.getShipType());
                        }
                    }
                } else {
                    result = TargetInfo.updateStatic(mmsi, targetType, result, timestamp, packet.toByteArray(), null,
                            c.getShipType());
                }
            }
        }
        return result;
    }
}
