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

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;

/**
 * Abstract base class for message filters that allows both ais messages and ais packets as argument.
 */
@ThreadSafe
public abstract class MessageFilterBase implements IMessageFilter, IPacketFilter {

    /**
     * Helper method to extract message from packet and do test
     */
    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        if (message != null) {
            return this.rejectedByFilter(message);
        }
        return false;
    }

}
