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
import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;

/**
 * Bus for exchanging AIS packets
 * 
 * Thread safety by delegation
 */
@ThreadSafe
public class AisBus extends AisBusComponent implements Runnable {

    /**
     * Queue to represent the bus
     */
    private final IMessageQueue<AisBusElement> busQueue;

    /**
     * Collection of consumer threads
     */
    private final CopyOnWriteArrayList<MessageQueueReader<AisBusElement>> consumerThreads = new CopyOnWriteArrayList<>();

    // Some configuration that should come from somewhere else (conf class)
    private final int busPullMaxElements = 1000;
    private final int consumerPullMaxElements = 1000;
    private final int busQueueSize = 10000;
    private final int consumerQueueSize = 10000;

    public AisBus() {
        // TODO size from where
        // maybe configuration as argument

        // Filters from somewhere

        // Create the bus
        busQueue = new BlockingMessageQueue<>(busQueueSize);
    }

    /**
     * Start AisBus thread
     * 
     * @return thread
     */
    public Thread start() {
        Thread t = new Thread(this);
        t.start();
        return t;
    }

    // TODO maybe private constructor and method to create

    /**
     * Push element onto the bus. Returns false if the bus is overflowing
     * 
     * @param packet
     * @return if pushing was a success
     */
    public boolean push(AisPacket packet) {
        // Do filtering, transformation and filtering (the client thread)
        packet = handleReceived(packet);
        if (packet == null) {
            return true;
        }

        // Push to the bus
        try {
            busQueue.push(new AisBusElement(packet));
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
        MessageQueueReader<AisBusElement> consumerThread = new MessageQueueReader<>(consumer, consumerQueue,
                consumerPullMaxElements);
        // Add consumer thread to list
        consumerThreads.add(consumerThread);
        // Start consumer thread
        consumerThread.start();
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
        List<AisBusElement> elements = new ArrayList<>();
        while (true) {
            elements.clear();
            // Consume from bus queue
            busQueue.pull(elements, busPullMaxElements);
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
