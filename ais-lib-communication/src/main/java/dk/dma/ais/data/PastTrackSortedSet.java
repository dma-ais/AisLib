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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import dk.dma.enav.model.geometry.Position;

/**
 * Class to hold track of a vessel target
 */
public class PastTrackSortedSet implements IPastTrack, Serializable {

    private static final long serialVersionUID = 1L;

    private TreeSet<PastTrackPoint> points = new TreeSet<>();

    public void addPosition(AisVesselPosition vesselPosition, int minDist) {
        if (vesselPosition == null || vesselPosition.getPos() == null) {
            return;
        }

        // Get the timestamp of this message
        if (vesselPosition.getSourceTimestamp() == null) {
            // Will not allow generating past track for reports without
            // timestamp
            return;
        }

        // Create new point
        PastTrackPoint newPoint = new PastTrackPoint(vesselPosition);
        if (!points.add(newPoint)) {
            return;
        }

        if (minDist <= 0) {
            return;
        }

        // Determine if the new point is the most recent point
        boolean newest = newPoint == points.last();

        // Remove neighbors until minimum distance to next neighbor is satisfied
        if (newest) {
            while (true) {
                PastTrackPoint neighbor = points.lower(newPoint);
                if (neighbor == null) {
                    break;
                }
                PastTrackPoint nextNeighbor = points.lower(neighbor);
                if (nextNeighbor == null) {
                    break;
                }
                if (isTooClose(newPoint, nextNeighbor, minDist)) {
                    points.remove(neighbor);
                } else {
                    break;
                }
            }
        } else {
            // Remove new point if it is too close to either neighbor
            PastTrackPoint beforeNeighbor = points.lower(newPoint);
            if (beforeNeighbor != null && isTooClose(newPoint, beforeNeighbor, minDist)) {
                points.remove(newPoint);
            } else {
                PastTrackPoint afterNeighbor = points.ceiling(newPoint);
                if (afterNeighbor != null && isTooClose(newPoint, afterNeighbor, minDist)) {
                    points.remove(newPoint);
                }
            }
        }

    }

    private static boolean isTooClose(PastTrackPoint p1, PastTrackPoint p2, int minDist) {
        Position pos1 = Position.create(p1.getLat(), p1.getLon());
        Position pos2 = Position.create(p2.getLat(), p2.getLon());
        return pos1.rhumbLineDistanceTo(pos2) < minDist;
    }

    public void cleanup(int ttl) {
        while (points.size() > 0 && points.first().isDead(ttl)) {
            points.pollFirst();
        }
    }

    public List<PastTrackPoint> getPoints() {
        return new ArrayList<>(points);
    }

}
