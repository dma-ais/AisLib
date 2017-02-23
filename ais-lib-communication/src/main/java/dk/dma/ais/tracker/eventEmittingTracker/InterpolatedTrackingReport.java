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

import dk.dma.commons.util.DateTimeUtil;
import dk.dma.enav.model.geometry.Position;
import net.jcip.annotations.NotThreadSafe;

import java.time.LocalDateTime;

@NotThreadSafe
public final class InterpolatedTrackingReport extends TrackingReport {
    private final long timestamp;
    private final Position position;
    private final float courseOverGround;
    private final float speedOverGround;
    private final float trueHeading;

    public InterpolatedTrackingReport(long timestamp, Position position, float courseOverGround, float speedOverGround, float trueHeading) {
        this.timestamp = timestamp;
        this.position = position;
        this.courseOverGround = courseOverGround;
        this.speedOverGround = speedOverGround;
        this.trueHeading = trueHeading;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public LocalDateTime getTimestampTyped() {
        return DateTimeUtil.MILLIS_TO_LOCALDATETIME_UTC.apply(timestamp);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public float getCourseOverGround() {
        return courseOverGround;
    }

    @Override
    public float getSpeedOverGround() {
        return speedOverGround;
    }

    @Override
    public float getTrueHeading() {
        return trueHeading;
    }

}
