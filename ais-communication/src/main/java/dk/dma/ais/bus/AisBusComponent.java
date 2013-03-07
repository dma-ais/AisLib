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

import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.filter.PacketFilterCollection;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.transform.IAisPacketTransformer;

/**
 * Base class for all AisBus components receiving and handing of AIS packets. All components are able to filter, transform and tag
 * all received data.
 */
@ThreadSafe
public abstract class AisBusComponent {

    public static final int STATUS_CREATED = 0;
    public static final int STATUS_INITIALIZED = 1;
    public static final int STATUS_STARTED = 2;

    /**
     * The status of the component
     */
    @GuardedBy("this")
    private int status = STATUS_CREATED;

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
    }

    /**
     * Initialize the component after configuration. Must be called be starting the component.
     */
    public synchronized void init() {
        if (status != STATUS_CREATED) {
            throw new IllegalStateException("Component must be in state CREATED to be initialized");
        }
        status = STATUS_INITIALIZED;
    }

    /**
     * Start the component. Must be called after init().
     */
    public synchronized void start() {
        if (status != STATUS_INITIALIZED) {
            throw new IllegalStateException("Component must be in state CREATED to be initialized");
        }
        status = STATUS_STARTED;
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
     * Method to handle incoming packet for all AisBus components. Will do filtering, transformation and tagging.
     * 
     * @param element
     * @return
     */
    protected AisPacket handleReceived(AisPacket packet) {
        // Filter message
        if (filters.rejectedByFilter(packet)) {
            return null;
        }

        // Transform packet
        for (IAisPacketTransformer transformer : packetTransformers) {
            packet = transformer.transform(packet);
        }

        // Do tagging
        // TODO

        // TODO update statistics

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

}
