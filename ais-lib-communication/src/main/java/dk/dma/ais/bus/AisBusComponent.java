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

import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.status.AisBusComponentStatus;
import dk.dma.ais.filter.PacketFilterCollection;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.transform.IAisPacketTransformer;

/**
 * Base class for all AisBus components receiving and handing of AIS packets. All components are able to filter, transform and tag
 * all received data.
 */
@ThreadSafe
public abstract class AisBusComponent {
    
    /**
     * Maximum time to wait for threads to stop after having been cancelled
     */
    public static final long THREAD_STOP_WAIT_MAX = 10000;

    /**
     * The status of the component
     */
    protected final AisBusComponentStatus status;

    /**
     * Thread for this component
     */
    @GuardedBy("this")
    private Thread thread;

    /**
     * Filters to apply to received packets
     */
    protected final PacketFilterCollection filters = new PacketFilterCollection();

    /**
     * Transformers to apply
     */
    protected final CopyOnWriteArrayList<IAisPacketTransformer> packetTransformers = new CopyOnWriteArrayList<>();

    public AisBusComponent() {
        this.status = new AisBusComponentStatus();
    }

    /**
     * Initialize the component after configuration. Must be called be starting the component.
     */
    public void init() {
        status.setInitialized();
    }

    /**
     * Start the component. Must be called after init().
     */
    public void start() {
        status.setStarted();
    }
        
    public void setConnected() {
        status.setConnected();
    }
    
    public void setNotConnected() {
        status.setNotConnected();
    }
    
    public void setStopped() {
        status.setStopped();
    }
    
    /**
     * Get component status
     * @return
     */
    public AisBusComponentStatus getStatus() {
        return status;
    }

    /**
     * Set component thread
     * 
     * @param thread
     */
    protected synchronized void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * Get component thread
     * 
     * @return
     */
    public synchronized Thread getThread() {
        return thread;
    }
    
    /**
     * All components must implement a way to stop    
     */
    public abstract void cancel();

    /**
     * Method to handle incoming packet for all AisBus components. Will do filtering, transformation and tagging.
     * 
     * @param element
     * @return
     */
    protected AisPacket handleReceived(AisPacket packet) {
        status.receive();
        
        // Filter message
        if (filters.rejectedByFilter(packet)) {
            // Update statistics
            status.filtered();
            return null;
        }

        // Transform packet
        for (IAisPacketTransformer transformer : packetTransformers) {
            packet = transformer.transform(packet);
        }

        // Update statistics
        if (packet == null) {
            status.filtered();
        }

        return packet;
    }

    /**
     * Get filters collection
     * 
     * @return
     */
    public PacketFilterCollection getFilters() {
        return filters;
    }

    /**
     * Get packet transformers list
     * 
     * @return
     */
    public CopyOnWriteArrayList<IAisPacketTransformer> getPacketTransformers() {
        return packetTransformers;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AisBusComponent [class=");
        builder.append(this.getClass());
        builder.append(", status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }
    
    public String rateReport() {
        return String.format("[received/filtered/overflow] %4.2f / %4.2f / %4.2f  (packets/sec)", status.getInRate(), status.getFilteredRate(), status.getOverflowRate());
    }
    

}
