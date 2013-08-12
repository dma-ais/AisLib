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

import java.io.Serializable;

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.ShipTypeColor;
import dk.dma.ais.packet.AisPacket;
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

    /** The latest positionPacket that was received. */
    final byte[] positionPacket;
    final long positionTimestamp;

    final float latitude;
    final float longitude;

    final int heading;
    final float cog;
    final float sog;
    final byte navStatus;

    final long staticTimestamp;
    final byte[] staticData1;
    final byte[] staticData2;
    ShipTypeColor color = ShipTypeColor.GREY;

    transient volatile boolean isBackedUp; /* = false */

    /**
     * @param timestamp
     * @param latitude
     * @param longitude
     * @param heading
     */
    TargetInfo(int mmsi, long positionTimestamp, float latitude, float longitude, int heading, float cog, float sog,
            byte navStatus, byte[] positionPacket, long staticTimestamp, byte[] staticData1, byte[] staticData2) {
        this.mmsi = mmsi;
        // Position data
        this.positionTimestamp = positionTimestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.cog = cog;
        this.sog = sog;
        this.navStatus = navStatus;
        this.positionPacket = positionPacket;

        // Static Data
        this.staticTimestamp = staticTimestamp;
        this.staticData1 = staticData1;
        this.staticData2 = staticData2;
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
        return positionTimestamp >= other.positionTimestamp ? updateStatic(other) : other.updateStatic(this);
    }

    /**
     * Copies the static information from the specified target into a new target. Maintaining the position information
     * of the current target.
     * 
     * @param other
     * @return the new target
     */
    private TargetInfo updateStatic(TargetInfo other) {
        return new TargetInfo(mmsi, positionTimestamp, latitude, longitude, heading, cog, sog, navStatus,
                positionPacket, other.staticTimestamp, other.staticData1, other.staticData2);
    }

    static TargetInfo updatePosition(int mmsi, TargetInfo existing, long timestamp, final AisPacket packet,
            final AisPositionMessage message) {
        if (existing != null) {
            if (timestamp <= existing.positionTimestamp) {
                return existing;
            }
        }

        Position p = message.getValidPosition();
        if (p != null) {
            float lat = (float) p.getLatitude();
            float lon = (float) p.getLongitude();
            int heading = 0;
            float cog = message.getCog();
            float sog = message.getSog();
            byte navStatus = -1;
            return new TargetInfo(mmsi, timestamp, lat, lon, heading, cog, sog, navStatus, packet.toByteArray(), 0,
                    null, null);
        }
        return existing;
    }
}
