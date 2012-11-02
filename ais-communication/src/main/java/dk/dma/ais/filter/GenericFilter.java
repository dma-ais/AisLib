/* Copyright (c) 2011 Danish Maritime Safety Administration
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

import dk.dma.ais.message.AisMessage;
import dk.dma.enav.messaging.MaritimeMessageHandler;
import dk.dma.enav.messaging.MaritimeMessageMetadata;

/**
 * An abstract base class for filters to extend
 * 
 * A filter receives AIS messages, do some manipulation, and delivers messages to a list of receivers. Hence filters can
 * be put between an AIS source and handlers.
 * 
 */
public abstract class GenericFilter implements MaritimeMessageHandler<AisMessage> {

    /**
     * Set of receivers from the filter
     */
    private Set<MaritimeMessageHandler<AisMessage>> receivers = new HashSet<>();

    /**
     * Register a receiver of filtered messages
     * 
     * @param receiver
     */
    public void registerReceiver(MaritimeMessageHandler<AisMessage> receiver) {
        receivers.add(receiver);
    }

    /**
     * Remove receiver
     * 
     * @param receiver
     */
    public void removeReceiver(MaritimeMessageHandler<AisMessage> receiver) {
        receivers.remove(receiver);
    }

    /**
     * Send message to receivers
     * 
     * @param aisMessage
     */
    protected void sendMessage(AisMessage aisMessage) {
        for (MaritimeMessageHandler<AisMessage> receiver : receivers) {
            receiver.handle(aisMessage, new MaritimeMessageMetadata());
        }
    }
}
