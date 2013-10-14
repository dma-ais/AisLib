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

import dk.dma.enav.model.geometry.Position;

/**
 * Class to hold track of a vessel target
 */
public class PastTrackSimple implements IPastTrack, Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<PastTrackPoint> points = new ArrayList<>();

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

        PastTrackPoint lastPoint = points.size() > 0 ? points.get(points.size() - 1) : null;
        PastTrackPoint newPoint = new PastTrackPoint(vesselPosition);

        // No previous points allowed
        if (lastPoint != null && lastPoint.getDate().after(newPoint.getDate())) {
            return;
        }

        // Downsample on distance
        if (lastPoint != null) {
            Position lastPos = Position.create(lastPoint.getLat(), lastPoint.getLon());
            Position thisPos = Position.create(newPoint.getLat(), newPoint.getLon());
            if (thisPos.rhumbLineDistanceTo(lastPos) < minDist) {
                return;
            }
        }

        points.add(newPoint);
    }

    public void cleanup(int ttl) {
        while (points.size() > 0 && points.get(0).isDead(ttl)) {
            points.remove(0);
        }
    }

    public List<PastTrackPoint> getPoints() {
        List<PastTrackPoint> list = new ArrayList<>(points);
        return list;
    }

}
