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

import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;

/**
 * Filter that holds a collection of packet filers and checks against all filters
 * 
 * Thread safe by delegation
 */
@ThreadSafe
public class PacketFilterCollection implements IPacketFilter {

    private final CopyOnWriteArrayList<IPacketFilter> packetFilters = new CopyOnWriteArrayList<>();
    public static final int TYPE_AND = 0;
    public static final int TYPE_OR = 1;
    
    private int filterType = TYPE_AND;
    

    public int getFilterType() {
        return filterType;
    }


    public PacketFilterCollection() {

    }

    /**
     * Check against all filters
     */
    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        switch (filterType) {
        case TYPE_OR:
            for (IPacketFilter filter : packetFilters) {
                if (!filter.rejectedByFilter(packet)) {
                    return false;
                }
            }
            return true;
        case TYPE_AND:
            //fall through to default        
        default:
            for (IPacketFilter filter : packetFilters) {
                if (filter.rejectedByFilter(packet)) {
                    return true;
                }
            }
            return false;
        }
        
        
    }

    /**
     * Add a filter
     * 
     * @param filter
     */
    public void addFilter(IPacketFilter filter) {
           packetFilters.add(filter);
    }
    
    /**
     * 
     */
    public boolean isType(int t) {
        return filterType == t;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
        
    }
}
