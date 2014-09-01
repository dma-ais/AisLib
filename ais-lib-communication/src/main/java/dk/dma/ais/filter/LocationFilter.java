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
package dk.dma.ais.filter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.enav.model.geometry.Position;
import java.util.function.Predicate;

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
    private List<Predicate<? super Position>> geomtries = new CopyOnWriteArrayList<>();

    @Override
    public boolean rejectedByFilter(AisMessage message) {
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

    public void addFilterGeometry(Predicate<? super Position> geometry) {
        geomtries.add(geometry);
    }

}
