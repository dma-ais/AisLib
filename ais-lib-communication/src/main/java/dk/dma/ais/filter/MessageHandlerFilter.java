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

import java.util.concurrent.CopyOnWriteArraySet;

import net.jcip.annotations.ThreadSafe;

import dk.dma.ais.message.AisMessage;
import dk.dma.enav.util.function.Consumer;

/**
 * Message handler filter
 * 
 * A filter receives AIS messages, do some manipulation, and delivers messages to a list of receivers. Hence filters can be put
 * between an AIS source and handlers.
 * 
 * Thread safe by delegation
 * 
 */
@ThreadSafe
public class MessageHandlerFilter implements Consumer<AisMessage> {

    /**
     * The actual message filter
     */
    private final IMessageFilter messageFilter;

    /**
     * Set of receivers from the filter
     */
    private CopyOnWriteArraySet<Consumer<? super AisMessage>> receivers = new CopyOnWriteArraySet<>();

    /**
     * Constructor given message filter
     * 
     * @param messageFilter
     */
    public MessageHandlerFilter(IMessageFilter messageFilter) {
        this.messageFilter = messageFilter;
    }

    /**
     * Register a receiver of filtered messages
     * 
     * @param receiver
     */
    public void registerReceiver(Consumer<? super AisMessage> receiver) {
        receivers.add(receiver);
    }

    /**
     * Remove receiver
     * 
     * @param receiver
     */
    public void removeReceiver(Consumer<? super AisMessage> receiver) {
        receivers.remove(receiver);
    }

    /**
     * Send message to receivers
     * 
     * @param aisMessage
     */
    protected void sendMessage(AisMessage aisMessage) {
        for (Consumer<? super AisMessage> receiver : receivers) {
            receiver.accept(aisMessage);
        }
    }

    /**
     * Receive message
     */
    @Override
    public void accept(AisMessage message) {
        if (!messageFilter.rejectedByFilter(message)) {
            sendMessage(message);
        }
    }

}
