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
package dk.dma.ais.bus;

import dk.dma.ais.packet.AisPacket;

/**
 * An element on the AIS bus with possibilities for further metadata
 */
// TODO concurrency handling
// Immutable
public class AisBusElement {

    private final AisPacket packet;
    private final long timestamp;

    public AisBusElement(AisPacket packet) {
        this.packet = packet;
        this.timestamp = System.currentTimeMillis();
    }

    public AisPacket getPacket() {
        return packet;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

}
