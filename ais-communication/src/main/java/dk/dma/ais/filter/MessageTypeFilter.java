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

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.NotThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.Vdm;

/**
 * Filtering based on message types
 */
@NotThreadSafe
public class MessageTypeFilter implements IPacketFilter {
    
    /**
     * Set of message types either allowed or disallowed
     */
    private final Set<Integer> messageTypes = new HashSet<>();
    private boolean disallowed;
    
    public MessageTypeFilter() {
        
    }

    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return true;
        }
        if (messageTypes.contains(vdm.getMsgId())) {
            return disallowed;            
        }
        return !disallowed;
    }
    
    public Set<Integer> getMessageTypes() {
        return messageTypes;
    }
    
    public void setDisallowed(boolean disallowed) {
        this.disallowed = disallowed;
    }

}
