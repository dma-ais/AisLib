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
package dk.dma.ais.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Predicate;

/**
 * Simple filtering based on the location of targets. Filtered on a list of geometries.
 */
@ThreadSafe
public class LocationFilter extends MessageFilterBase {

    /**
     * Map from MMSI to position
     */
    private Map<Integer, Position> posMap = new ConcurrentHashMap<>();

    /**
     * List of geometries
     */
    @GuardedBy("this")
    private List<Predicate<? super Position>> geomtries = new ArrayList<>();

    @Override
    public synchronized boolean rejectedByFilter(AisMessage message) {
        if (geomtries.size() == 0) {
            return false;
        }

        if (message instanceof IPositionMessage) {
            Position pos = ((IPositionMessage) message).getPos().getGeoLocation();
            if (pos != null) {
                posMap.put(message.getUserId(), pos);
            }
        }

        // Get location
        Position loc = posMap.get(message.getUserId());
        if (loc == null) {
            return true;
        }

        for (Predicate<? super Position> geometry : geomtries) {
            if (geometry.test(loc)) {
                return false;
            }
        }

        return true;
    }

    public synchronized void addFilterGeometry(Predicate<? super Position> geometry) {
        geomtries.add(geometry);
    }

}
