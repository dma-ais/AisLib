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
package dk.dma.enav.ais.filter;

import java.util.HashSet;
import java.util.Set;

import dk.dma.enav.ais.message.AisMessage;
import dk.dma.enav.messaging.MessageHandler;
import dk.dma.enav.messaging.MessageMetadata;

/**
 * An abstract base class for filters to extend
 * 
 * A filter receives AIS messages, do some manipulation, and delivers messages to a list of receivers. Hence filters can
 * be put between an AIS source and handlers.
 * 
 */
public abstract class GenericFilter implements MessageHandler<AisMessage> {

    /**
     * Set of receivers from the filter
     */
    private Set<MessageHandler<AisMessage>> receivers = new HashSet<>();

    /**
     * Register a receiver of filtered messages
     * 
     * @param receiver
     */
    public void registerReceiver(MessageHandler<AisMessage> receiver) {
        receivers.add(receiver);
    }

    /**
     * Remove receiver
     * 
     * @param receiver
     */
    public void removeReceiver(MessageHandler<AisMessage> receiver) {
        receivers.remove(receiver);
    }

    /**
     * Send message to receivers
     * 
     * @param aisMessage
     */
    protected void sendMessage(AisMessage aisMessage) {
        for (MessageHandler<AisMessage> receiver : receivers) {
            receiver.process(aisMessage, new MessageMetadata());
        }
    }
}
