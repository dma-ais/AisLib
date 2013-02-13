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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;

/**
 * Abstract base class for message filters that allows both ais messages and ais packets as argument.
 */
public abstract class MessageFilterBase implements IMessageFilter, IPacketFilter {

    /**
     * Helper method to extract message from packet and do test
     */
    @Override
    public synchronized boolean rejectedByFilter(AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        if (message != null) {
            return this.rejectedByFilter(message);
        }
        return false;
    }

}
