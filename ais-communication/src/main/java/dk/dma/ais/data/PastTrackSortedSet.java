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
        points.add(newPoint);

        // Get the previous neighbor
        PastTrackPoint lastPoint = points.lower(newPoint);
        if (lastPoint != null) {
            // Downsample on distance
            Position lastPos = Position.create(lastPoint.getLat(), lastPoint.getLon());
            Position newPos = Position.create(newPoint.getLat(), newPoint.getLon());
            if (newPos.rhumbLineDistanceTo(lastPos) < minDist) {
                points.remove(lastPoint);
            }
        }
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
