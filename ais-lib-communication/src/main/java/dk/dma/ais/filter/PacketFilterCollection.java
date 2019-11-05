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

import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;

/**
 * Filter that holds a collection of packet filers and checks against all filters
 * <p>
 * Thread safe by delegation
 */
@ThreadSafe
public class PacketFilterCollection implements IPacketFilter {

    private final CopyOnWriteArrayList<IPacketFilter> packetFilters = new CopyOnWriteArrayList<>();
    /**
     * The constant TYPE_AND.
     */
    public static final int TYPE_AND = 0;
    /**
     * The constant TYPE_OR.
     */
    public static final int TYPE_OR = 1;
    
    private int filterType = TYPE_AND;


    /**
     * Gets filter type.
     *
     * @return the filter type
     */
    public int getFilterType() {
        return filterType;
    }


    /**
     * Instantiates a new Packet filter collection.
     */
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
     * @param filter the filter
     */
    public void addFilter(IPacketFilter filter) {
           packetFilters.add(filter);
    }

    /**
     * Is type boolean.
     *
     * @param t the t
     * @return the boolean
     */
    public boolean isType(int t) {
        return filterType == t;
    }

    /**
     * Sets filter type.
     *
     * @param filterType the filter type
     */
    public void setFilterType(int filterType) {
        this.filterType = filterType;
        
    }
}
