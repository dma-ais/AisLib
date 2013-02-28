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
import java.util.concurrent.CopyOnWriteArraySet;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.MessageQueueOverflowException;

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
    private IMessageQueue<AisBusElement> busQueue;

    /**
     * Collection of consumer threads
     */
    private final CopyOnWriteArraySet<AisBusConsumer> consumers = new CopyOnWriteArraySet<>();

    /**
     * Collection of providers
     */
    private final CopyOnWriteArraySet<AisBusProvider> providers = new CopyOnWriteArraySet<>();

    // Some configuration that should come from somewhere else (conf class)
    @GuardedBy("this")
    private int busPullMaxElements = 1000;
    @GuardedBy("this")
    private int busQueueSize = 10000;

    public AisBus() {

    }
    
    /**
     * Initialize AisBus
     */
    @Override
    public synchronized void init() {
        // Create the bus
        busQueue = new BlockingMessageQueue<>(busQueueSize);
        super.init();
    }

    /**
     * Start AisBus thread
     * 
     * @return thread
     */
    public synchronized void start() {       
        // Start thread
        Thread thread = new Thread(this);
        setThread(thread);
        thread.start();
        super.start();
    }

    /**
     * Start all consumers
     */
    public void startConsumers() {
        for (AisBusConsumer consumer : consumers) {
            consumer.start();
        }
    }
    
    /**
     * Start all providers
     */
    public void startProviders() {
        for (AisBusProvider provider : providers) {
            provider.start();
        }
    }
    
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

    /**
     * Register a consumer
     * 
     * @param consumer
     */
    public void registerConsumer(AisBusConsumer consumer) {
        // Tie aisbus to consumer
        consumer.setAisBus(this);
        // Make consumer queue
        consumers.add(consumer);
    }

    /**
     * Register a provider
     * 
     * @param provider
     */
    public void registerProvider(AisBusProvider provider) {
        // Tie aisbus to provider
        provider.setAisBus(this);
        // Add to set of providers
        providers.add(provider);
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
            for (AisBusConsumer consumer : consumers) {
                // Distribute elements
                for (AisBusElement element : elements) {
                    consumer.push(element);
                }
            }
        }
    }

    public synchronized void setBusPullMaxElements(int busPullMaxElements) {
        this.busPullMaxElements = busPullMaxElements;
    }

    public synchronized void setBusQueueSize(int busQueueSize) {
        this.busQueueSize = busQueueSize;
    }

}
