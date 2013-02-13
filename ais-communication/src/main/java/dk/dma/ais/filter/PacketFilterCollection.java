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

import dk.dma.ais.packet.AisPacket;

/**
 * Filter that holds a collection of packet filers and checks against all filters
 */
public class PacketFilterCollection implements IPacketFilter {

    private final List<IPacketFilter> packetFilters = new ArrayList<>();

    public PacketFilterCollection() {

    }

    /**
     * Check against all filters
     */
    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        synchronized (packetFilters) {
            for (IPacketFilter filter : packetFilters) {
                if (filter.rejectedByFilter(packet)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add a filter
     * 
     * @param filter
     */
    public void addFilter(IPacketFilter filter) {
        synchronized (packetFilters) {
            packetFilters.add(filter);
        }
    }

}
