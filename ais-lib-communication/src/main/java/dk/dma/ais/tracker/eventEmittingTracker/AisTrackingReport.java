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

package dk.dma.ais.tracker.eventEmittingTracker;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.commons.util.DateTimeUtil;
import dk.dma.enav.model.geometry.Position;
import net.jcip.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * A tracking report based on AisPacket (AIS data)
 */
@Immutable
public final class AisTrackingReport extends TrackingReport {

    private final AisPacket packet;

    public AisTrackingReport(AisPacket aisPacket) {
        AisMessage aisMessage = aisPacket.tryGetAisMessage();
        if (! (aisMessage instanceof IVesselPositionMessage)) {
            throw new IllegalArgumentException("aisPacket must be a position report.");
        }

        this.packet = aisPacket;
    }

    public AisPacket getPacket() {
        return packet;
    }

    @Override
    public long getTimestamp() {
        return packet.getBestTimestamp();
    }

    @Override
    public LocalDateTime getTimestampTyped() {
        return DateTimeUtil.MILLIS_TO_LOCALDATETIME_UTC.apply(packet.getBestTimestamp());
    }

    @Override
    public Position getPosition() {
        return ((IVesselPositionMessage) packet.tryGetAisMessage()).getPos().getGeoLocation();
    }

    @Override
    public float getCourseOverGround() {
        return (float) (((IVesselPositionMessage) packet.tryGetAisMessage()).getCog() / 10.0);
    }

    @Override
    public float getSpeedOverGround() {
        return (float) (((IVesselPositionMessage) packet.tryGetAisMessage()).getSog() / 10.0);
    }

    @Override
    public float getTrueHeading() {
        int trueHeading = ((IVesselPositionMessage) packet.tryGetAisMessage()).getTrueHeading();
        return trueHeading > 500 /* 511 */ ? Float.NaN : trueHeading;
    }
}
