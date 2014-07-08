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

import java.util.List;

/**
 * Interface for past track implementations
 */
public interface IPastTrack {

    /**
     * Add position to past track if it is more than minimum distance from last position
     * 
     * @param vesselPosition
     * @param minDist
     */
    void addPosition(AisVesselPosition vesselPosition, int minDist);

    /**
     * Remove points in past track older than ttl
     * 
     * @param ttl
     */
    void cleanup(int ttl);

    /**
     * Get past track points
     * 
     * @return
     */
    List<PastTrackPoint> getPoints();

}
