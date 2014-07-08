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
    private final long time;

    public PastTrackPoint(AisVesselPosition vesselPosition) {
        Position pos = vesselPosition.getPos();
        this.lat = pos.getLatitude();
        this.lon = pos.getLongitude();
        this.cog = vesselPosition.getCog() != null ? vesselPosition.getCog() : 0;
        this.sog = vesselPosition.getSog() != null ? vesselPosition.getSog() : 0;
        Date ts = vesselPosition.getSourceTimestamp();
        if (ts == null) {
            this.time = vesselPosition.received.getTime();
        } else {
            this.time = ts.getTime();
        }
    }

    public boolean isDead(int ttl) {
        int elapsed = (int) ((System.currentTimeMillis() - time) / 1000);
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

    public long getTime() {
        return time;
    }
    
    public Date getDate() {
        return new Date(time);
    }

    @Override
    public int compareTo(PastTrackPoint p2) {
        if (this.time < p2.time) {
            return -1;
        } else if (this.time > p2.time) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object p2) {
        return time == ((PastTrackPoint)p2).getTime();
    }

    @Override
    public String toString() {
        return "PastTrackPoint [time=" + time + ", lat=" + lat + ", lon=" + lon + ", cog=" + cog + ", sog=" + sog + "]";
    }

    @Override
    public int hashCode() {
        return (int) time;
    }

}
