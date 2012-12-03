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
package dk.dma.ais.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.enav.model.geometry.Position;

/**
 * Simple filtering based on the location of targets. Filtered on a list of geometries.
 */
public class TargetLocationFilter extends GenericFilter {

    /**
     * Map from MMSI to position
     */
    private Map<Long, Position> posMap = new ConcurrentHashMap<>();

    /**
     * List of geometries
     */
    private List<FilterGeometry> geomtries = new ArrayList<>();

    @Override
    public void receive(AisMessage aisMessage) {
        if (geomtries.size() == 0) {
            sendMessage(aisMessage);
            return;
        }

        if (aisMessage instanceof IPositionMessage) {
            posMap.put(aisMessage.getUserId(), ((IPositionMessage) aisMessage).getPos().getGeoLocation());
        }

        // // TODO match with polygons given. Or maybe circle with radius
        // GeoLocation center = new GeoLocation(70, -46);
        // //GeoLocation outer = new GeoLocation(57, -25);
        // //System.out.println("Distance: " + center.getGeodesicDistance(outer));
        //
        // Get location
        Position loc = posMap.get(aisMessage.getUserId());
        if (loc == null) {
            return;
        }

        for (FilterGeometry geometry : geomtries) {
            if (geometry.isWithin(loc)) {
                sendMessage(aisMessage);
                return;
            }
        }

        // // Must be in circle
        // if (loc.getGeodesicDistance(center) > 1800000) {
        // return;
        // }

        return;
    }

    public void addFilterGeometry(FilterGeometry geometry) {
        geomtries.add(geometry);
    }

}
