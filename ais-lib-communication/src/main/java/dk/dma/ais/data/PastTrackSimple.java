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
