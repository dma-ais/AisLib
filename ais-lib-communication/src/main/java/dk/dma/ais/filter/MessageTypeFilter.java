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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.Vdm;

/**
 * Filtering based on message types
 */
@ThreadSafe
public class MessageTypeFilter implements IPacketFilter {
    
    /**
     * Set of message types either allowed or disallowed
     */
    private final Set<Integer> messageTypes = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
    private volatile boolean disallowed;
    
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
