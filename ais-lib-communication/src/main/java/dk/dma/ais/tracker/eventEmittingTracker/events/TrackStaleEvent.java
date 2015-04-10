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

package dk.dma.ais.tracker.eventEmittingTracker.events;

import dk.dma.ais.tracker.eventEmittingTracker.Track;

/**
 * The TrackStaleEvent is fired by the tracker when a track has not been updated for a while. It
 * is then considered "stale" all will never receive any updates again. If the same vessel later
 * on starts reporting, then a new track will be created.
 */
public class TrackStaleEvent extends TrackEvent {

    public TrackStaleEvent(Track track) {
        super(track);
    }

}
