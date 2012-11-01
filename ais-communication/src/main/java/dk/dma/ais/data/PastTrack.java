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
import java.util.LinkedList;
import java.util.List;

import dk.dma.enav.model.geometry.Position;

/**
 * Class to hold track of a vessel target
 */
public class PastTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<PastTrackPoint> points = new LinkedList<>();

    public PastTrack() {

    }

    public void addPosition(AisVesselPosition vesselPosition, int minDist) {
        if (vesselPosition == null || vesselPosition.getPos() == null)
            return;
        Position pos = vesselPosition.getPos();

        // Determine distance from last track point and maybe discard
        if (points.size() > 0) {
            PastTrackPoint lastPoint = points.get(points.size() - 1);
            Position lastPos = Position.create(lastPoint.getLat(), lastPoint.getLon());
            if (pos.rhumbLineDistanceTo(lastPos) < minDist) {
                return;
            }
        }

        // Add point
        points.add(new PastTrackPoint(vesselPosition));
    }

    public void cleanup(int ttl) {
        while (points.size() > 0 && points.peekFirst().isDead(ttl)) {
            points.removeFirst();
        }
    }

    public List<PastTrackPoint> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<PastTrackPoint> points) {
        this.points = points;
    }

}
