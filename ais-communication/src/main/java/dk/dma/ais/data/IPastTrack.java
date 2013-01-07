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

import java.util.List;

/**
 * Interface for past track implementations 
 */
public interface IPastTrack {

	/**
	 * Add position to past track if it is more than minimum distance from last position
	 * @param vesselPosition
	 * @param minDist
	 */
	void addPosition(AisVesselPosition vesselPosition, int minDist);
	
	/**
	 * Remove points in past track older than ttl
	 * @param ttl
	 */
	void cleanup(int ttl);
	
	/**
	 * Get past track points
	 * @return
	 */
	List<PastTrackPoint> getPoints();
	
}
