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
package dk.dma.ais.bus;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.IQueueEntryHandler;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;

@ThreadSafe
public abstract class AisBusConsumer extends AisBusSocket implements IQueueEntryHandler<AisBusElement> {

    @GuardedBy("this")
    private MessageQueueReader<AisBusElement> consumerThread;
    @GuardedBy("this")
    private int consumerQueueSize = 10000;
    @GuardedBy("this")
    private int consumerPullMaxElements = 1000;

    public AisBusConsumer() {
        super();
    }

    /**
     * Receive elements from the queue
     */
    @Override
    public final void receive(AisBusElement queueElement) {
        // Do filtering, transformation and filtering
        AisPacket packet = handleReceived(queueElement.getPacket());
        if (packet == null) {
            return;
        }
        queueElement.setPacket(packet);
        receiveFiltered(queueElement);
    }
    
    /**
     * Push elements onto the queue
     * @param element
     */
    public final void push(AisBusElement element) {
        try {
            consumerThread.getQueue().push(element);
        } catch (MessageQueueOverflowException e) {
            // TODO handle overflow
            // Maybe call method on consumer
            // Do some kind of central overflow event down sampling somewhere
            System.err.println("Overflow when pushing to consumer queue");
        }
    }
    
    @Override
    public synchronized void init() {
        // Create consumer queue
        IMessageQueue<AisBusElement> consumerQueue = new BlockingMessageQueue<>(consumerQueueSize);
        // Make consumer thread
        consumerThread = new MessageQueueReader<>(this, consumerQueue, consumerPullMaxElements);
        super.init();
    }

    @Override
    public synchronized void start() {        
        // Start consumerThread
        consumerThread.start();
        super.start();
    }

    /**
     * All consumers must implement a method to get the filtered packet
     * @param queueElement
     */
    public abstract void receiveFiltered(AisBusElement queueElement);

    public synchronized void setConsumerQueueSize(int consumerQueueSize) {
        this.consumerQueueSize = consumerQueueSize;
    }

    public synchronized void setConsumerPullMaxElements(int consumerPullMaxElements) {
        this.consumerPullMaxElements = consumerPullMaxElements;
    }

}
