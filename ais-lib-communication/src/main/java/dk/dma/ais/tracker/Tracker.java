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

package dk.dma.ais.tracker;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.eventEmittingTracker.Track;

import java.util.Collection;

/**
 * A tracking service receives AisPackets and based on these it maintains a collection of all known tracks,
 * including their position, speed, course, etc.
 *
 * If a certain track has not received any updates for a while
 * it enters status 'stale' and will receive no further updates. Instead a new track is created if more
 * AisPackets are received from the same vessel later on.
 *
 * The AisPackets are assumed to arrive in timely order; i.e. by ever-increasing values of timestamp.
 *
 * TODO: All tracker implementations in this package should implement a refactored version of this interface.
 *
 */
public interface Tracker {

    /**
     * Update the tracker with a new AisPacket.
     * @param aisPacket the AIS packet.
     */
    void update(AisPacket aisPacket);

    /**
     * Count and return the number of tracks current ACTIVE or STALE in the tracker.
     * @return the no. of tracks.
     */
    int getNumberOfTracks();

    /**
     *  Register a subscriber to receive Events from the tracker.
     *  The mechanism is based on Google's EventBus.
     *
     *  @see <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/eventbus/EventBus.html">Google EventBus</a>
     */
    void registerSubscriber(Object subscriber);

    /**
     * Get a collection of tracks from the tracker. The tracks in the set are still "live" and may be change state asynchronously by tracker threads.
     *
     * @return The set of of Tracks contained in the track at the time of the call.
     */
    Collection<Track> getTracks();
}
