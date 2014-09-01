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

import java.util.concurrent.CopyOnWriteArraySet;

import net.jcip.annotations.ThreadSafe;

import dk.dma.ais.message.AisMessage;
import java.util.function.Consumer;

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
