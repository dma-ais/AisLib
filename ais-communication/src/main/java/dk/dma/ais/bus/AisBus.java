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

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.filter.PacketFilterCollection;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;

public class AisBus extends Thread {

    /**
     * Filters to apply to received packets
     */
    private final PacketFilterCollection filters = new PacketFilterCollection();

    /**
     * Queue to represent the bus
     */
    private final IMessageQueue<AisBusElement> busQueue;

    /**
     * Collection of consumer threads
     */
    private final List<MessageQueueReader<AisBusElement>> consumerThreads = new ArrayList<>();

    // Some configuration that should come from somewhere else (conf class)
    private final int busPullMaxElements = 100;
    private final int consumerPullMaxElements = 100;
    private final int busQueueSize = 5000;
    private final int consumerQueueSize = 5000;

    public AisBus() {
        // TODO size from where
        // maybe configuration as argument
        
        // Filters from somewhere

        // Create the bus
        busQueue = new BlockingMessageQueue<>(busQueueSize);
    }

    /**
     * Push element onto the bus. Returns false if the bus is overflowing
     * 
     * @param element
     * @return if pushing was a success
     */
    public boolean push(AisBusElement element) {
        // Filter messages in this thread (the client thread)
        if (filters.rejectedByFilter(element.getPacket())) {
            return true;
        }
        // Push to the bus
        try {
            busQueue.push(element);
        } catch (MessageQueueOverflowException e) {
            System.err.println("Overflow when pushing to bus");
            // TODO handle overflow
            return false;
        }
        return true;
    }

    public void registerConsumer(AisBusConsumer consumer) {
        // Make consumer queue
        IMessageQueue<AisBusElement> consumerQueue = new BlockingMessageQueue<>(consumerQueueSize);
        // Make consumer thread
        MessageQueueReader<AisBusElement> consumerThread = new MessageQueueReader<>(consumer, consumerQueue, consumerPullMaxElements);
        // Add consumer thread to list
        consumerThreads.add(consumerThread);

        // TODO where to handle queueing
        // How to handle overflow when queing is happening in ais bus
        // otherwise consumer could be called and handle queing itself
        //
    }
    
    
    public void shutdown() {
        // TODO
        // Investigate what this would require
    }

    /**
     * Thread method that distributes elements
     */
    @Override
    public void run() {
        // Start all consumer threads
        for (MessageQueueReader<AisBusElement> consumerThread : consumerThreads) {            
            consumerThread.start();
        }

        while (true) {
            // Consume from bus queue
            List<AisBusElement> elements = busQueue.pull(busPullMaxElements);
            // Iterate through consumers
            for (MessageQueueReader<AisBusElement> consumerThread : consumerThreads) {            
                // Distribute elements
                for (AisBusElement element : elements) {
                    try {
                        consumerThread.getQueue().push(element);
                    } catch (MessageQueueOverflowException e) {
                        // TODO handle overflow
                        // Maybe call method on consumer
                        // Do some kind of central overflow event down sampling somewhere
                        System.err.println("Overflow when pushing to consumer queue");
                    }
                }
            }            
        }

    }

}
