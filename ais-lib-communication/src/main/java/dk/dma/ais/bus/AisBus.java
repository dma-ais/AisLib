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
package dk.dma.ais.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.status.AisBusComponentStatus.State;
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

    private static final Logger LOG = LoggerFactory.getLogger(AisBus.class);
    private final OverflowLogger overflowLogger = new OverflowLogger(LOG);

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

    private volatile int busPullMaxElements = 1000;
    private volatile int busQueueSize = 10000;

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

    @Override
    public void cancel() {
        if (status.getState() != State.STARTED) {
            return;
        }
        getThread().interrupt();
        try {
            getThread().join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * Stop all consumers
     */
    public void stopConsumers() {
        for (AisBusConsumer consumer : consumers) {
            consumer.cancel();
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
     * Stop all providers
     */
    public void stopProviders() {
        for (AisBusProvider provider : providers) {
            provider.cancel();
        }
    }
        
    /**
     * Push packets non-blocking on to the bus
     * @param packet
     * @return if pushing was a success
     */
    public boolean push(AisPacket packet) {
        return push(packet, false);
    }

    /**
     * Push element onto the bus. Returns false if the bus is overflowing
     * 
     * @param packet
     * @param blocking
     * @return if pushing was a success
     */
    public boolean push(AisPacket packet, boolean blocking) {
        // Do filtering, transformation and filtering (the client thread)
        packet = handleReceived(packet);
        if (packet == null) {
            return true;
        }
        
        AisBusElement element = new AisBusElement(packet);

        // Push to the bus blocking
        if (blocking) {
            try {
                busQueue.put(element);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            return true;
        }

        // Push to the bus non-blocking
        try {
            busQueue.push(element);
        } catch (MessageQueueOverflowException e) {
            overflowLogger.log("AisBus overflow [rate=" + avgOverflowRate() + " packet/sec]");
            return false;
        }
        return true;
    }
    
    /**
     * Get the average overflow rate experienced by all providers
     * @return
     */
    public double avgOverflowRate() {
        double sum = 0;
        int count = 0;
        for (AisBusProvider provider : providers) {
            sum += provider.getStatus().getOverflowRate();
            count++;
        }
        return sum / count;        
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
     * Get consumer by name
     * @param name
     * @return
     */
    public AisBusConsumer getConsumer(String name) {
        for (AisBusConsumer consumer : consumers) {
            if (consumer.getName() != null && consumer.getName().equals(name)) {
                return consumer;
            }
        }
        return null;
    }
    
    /**
     * Get provider by name
     * @param name
     * @return
     */
    public AisBusProvider getProvider(String name) {
        for (AisBusProvider provider : providers) {
            if (provider.getName() != null && provider.getName().equals(name)) {
                return provider;
            }
        }
        return null; 
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
            try {
                busQueue.pull(elements, busPullMaxElements);
            } catch (InterruptedException e) {
                break;
            }
            // Iterate through consumers
            for (AisBusConsumer consumer : consumers) {
                // Distribute elements
                for (AisBusElement element : elements) {
                    consumer.push(element);
                }
            }
        }

        stopProviders();
        stopConsumers();

        setStopped();

        LOG.info("Stopped");
    }

    public void setBusPullMaxElements(int busPullMaxElements) {
        this.busPullMaxElements = busPullMaxElements;
    }

    public void setBusQueueSize(int busQueueSize) {
        this.busQueueSize = busQueueSize;
    }

    public Set<AisBusConsumer> getConsumers() {
        return Collections.unmodifiableSet(consumers);
    }

    public Set<AisBusProvider> getProviders() {
        return Collections.unmodifiableSet(providers);
    }

}
