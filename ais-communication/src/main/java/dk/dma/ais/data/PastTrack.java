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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import dk.dma.enav.model.geometry.Position;

/**
 * Class to hold track of a vessel target
 */
public class PastTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    private NavigableSet<PastTrackPoint> points = new TreeSet<>();

    public PastTrack() {

    }

    public synchronized void addPosition(AisVesselPosition vesselPosition, int minDist) {
        if (vesselPosition == null || vesselPosition.getPos() == null) {
            return;
        }

        // Get the timestamp of this message
        if (vesselPosition.getSourceTimestamp() == null) {
            // Will not allow generating past track for reports without
            // timestamp
            return;
        }

        PastTrackPoint newPoint = new PastTrackPoint(vesselPosition);
        points.add(newPoint);

        // Downsample. Find neighbour points.
        PastTrackPoint prevPoint = null;
        PastTrackPoint nextPoint = null;
        Iterator<PastTrackPoint> it = null;
        boolean found = false;
        for (it = points.descendingIterator(); it.hasNext();) {
            PastTrackPoint point = it.next();

            if (newPoint == point) {
                // Found point
                found = true;
                break;
            }

            nextPoint = point;
        }
        if (it.hasNext()) {
            prevPoint = it.next();
        }

        if (!found) {
            return;
        }
        Position pointPos = Position.create(newPoint.getLat(), newPoint.getLon());
        if (prevPoint != null) {
            Position prevPos = Position.create(prevPoint.getLat(), prevPoint.getLon());
            if (prevPos.rhumbLineDistanceTo(pointPos) < minDist) {
                points.remove(prevPoint);
            }
        }
        if (nextPoint != null) {
            Position nextPos = Position.create(nextPoint.getLat(), nextPoint.getLon());
            if (nextPos.rhumbLineDistanceTo(pointPos) < minDist) {
                points.remove(newPoint);
            }
        }

        // if (points.size() > 3) {
        // prevPoint = null;
        // System.out.println("----");
        // for (it = points.descendingIterator(); it.hasNext();) {
        // PastTrackPoint point = it.next();
        // if (prevPoint != null) {
        // pointPos = new GeoLocation(point.getLat(), point.getLon());
        // GeoLocation lastPos = new GeoLocation(prevPoint.getLat(), prevPoint.getLon());
        // System.out.println("   dist: " + pointPos.getRhumbLineDistance(lastPos));
        // if (pointPos.getRhumbLineDistance(lastPos) < 100) {
        // System.out.println("DIST ALARM");
        // System.exit(-1);
        // }
        // if (prevPoint.getTime().getTime() < point.getTime().getTime()) {
        // System.out.println("TIME ALARM");
        // System.exit(-1);
        // }
        // }
        // System.out.println("point: " + point);
        // prevPoint = point;
        // }
        // }

    }

    public synchronized void cleanup(int ttl) {
        while (points.size() > 0 && points.last().isDead(ttl)) {
            points.pollLast();
        }
    }

    public synchronized List<PastTrackPoint> getPoints() {
        List<PastTrackPoint> list = new ArrayList<>();
        for (PastTrackPoint point : points) {
            list.add(point);
        }
        return list;
    }

}
