/* Copyright (c) 2012 Danish Maritime Authority
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
public class PastTrackPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    private double lat;
    private double lon;
    private double cog;
    private double sog;
    private Date time;

    public PastTrackPoint(AisVesselPosition vesselPosition) {
        Position pos = vesselPosition.getPos();
        this.lat = pos.getLatitude();
        this.lon = pos.getLongitude();
        this.cog = (vesselPosition.getCog() != null) ? vesselPosition.getCog() : 0;
        this.sog = (vesselPosition.getSog() != null) ? vesselPosition.getSog() : 0;
        this.time = vesselPosition.getSourceTimestamp();
        if (this.time == null) {
            this.time = vesselPosition.received;
        }
    }

    public boolean isDead(int ttl) {
        int elapsed = (int) ((System.currentTimeMillis() - this.time.getTime()) / 1000);
        return (elapsed > ttl);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getCog() {
        return cog;
    }

    public void setCog(double cog) {
        this.cog = cog;
    }

    public double getSog() {
        return sog;
    }

    public void setSog(double sog) {
        this.sog = sog;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
