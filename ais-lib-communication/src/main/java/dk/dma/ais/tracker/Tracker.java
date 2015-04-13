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
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.packet.AisPacketStream;

import java.io.IOException;

/**
 * A tracking service receives AisPackets and based on these it maintains a collection of all known tracks,
 * including their position, speed, course, etc.
 *
 * The AisPackets are assumed to arrive in timely order; i.e. by ever-increasing values of timestamp.
 *
 * TODO: Identify further methods which can be shared between tracker implementations.
 */
public interface Tracker {

    /**
     * Start to track targets based on AisPackets received from an AisPacketStream.
     *
     * @param stream the AisPacketStream to track.
     * @return the Subscription.
     */
    default AisPacketStream.Subscription subscribeToPacketStream(AisPacketStream stream) {
        return stream.subscribe(p -> update(p));
    }

    /**
     * Start to track targets based on AisPackets received from an AisPacketReader.
     *
     * @param packetReader the AisPacketReader to receive packets from.
     */
    default void subscribeToPacketReader(AisPacketReader packetReader) throws IOException {
        packetReader.forEachRemaining(p -> update(p));
    }

    /**
     * Update the tracker with a single AisPacket.
     * @param aisPacket the AIS packet.
     */
    void update(AisPacket aisPacket);

    /**
     * Returns the latest target info for the specified MMSI number.
     *
     * @param mmsi
     *            the MMSI number
     * @return the latest target info for the specified MMSI number
     */
    Target get(int mmsi);

    /**
     * Returns the number of targets that is being tracked.
     *
     * @return the number of targets that is being tracked
     */
    int size();

}
