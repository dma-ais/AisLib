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
package dk.dma.ais.data;

import java.io.Serializable;
import java.util.Date;

import dk.dma.enav.model.geometry.Position;

/**
 * Class to represent a point on a past track
 */
public class PastTrackPoint implements Serializable, Comparable<PastTrackPoint> {

    private static final long serialVersionUID = 1L;

    private final double lat;
    private final double lon;
    private final double cog;
    private final double sog;
    private final Date time;

    public PastTrackPoint(AisVesselPosition vesselPosition) {
        Position pos = vesselPosition.getPos();
        this.lat = pos.getLatitude();
        this.lon = pos.getLongitude();
        this.cog = vesselPosition.getCog() != null ? vesselPosition.getCog() : 0;
        this.sog = vesselPosition.getSog() != null ? vesselPosition.getSog() : 0;
        Date ts = vesselPosition.getSourceTimestamp();
        if (ts == null) {
            this.time = vesselPosition.received;
        } else {
            this.time = ts;
        }
    }

    public boolean isDead(int ttl) {
        int elapsed = (int) ((System.currentTimeMillis() - this.time.getTime()) / 1000);
        return elapsed > ttl;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getCog() {
        return cog;
    }

    public double getSog() {
        return sog;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public int compareTo(PastTrackPoint p2) {
        if (this.time.getTime() < p2.time.getTime()) {
            return -1;
        } else if (this.time.getTime() > p2.time.getTime()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "PastTrackPoint [time=" + time + ", lat=" + lat + ", lon=" + lon + ", cog=" + cog + ", sog=" + sog + "]";
    }

    @Override
    public int hashCode() {
        return (int) getTime().getTime();
    }

}
