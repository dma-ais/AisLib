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
package dk.dma.ais.bus.status;

import net.jcip.annotations.ThreadSafe;

/**
 * Status of a bus component
 * 
 * TODO: Flow history
 */
@ThreadSafe
public class AisBusComponentStatus {

    public enum State {
        CREATED, INITIALIZED, STARTED, CONNECTED, NOT_CONNECTED, STOPPED;
    }

    /**
     * State of the component
     */
    private State state;

    /**
     * The interval in milliseconds to make flow statistics
     */
    private final long flowStatInterval;

    /**
     * Time of creation
     */
    private final long startTime;

    /**
     * Number of packets received by the component and not filtered
     */
    private long packetCount;
    /**
     * Number of packets received but filtered away by the component
     */
    private long filteredPacketCount;
    /**
     * Number of packets that cannot be delivered due to overflow at the receiver
     */
    private long overflowCount;

    /**
     * Statistics for packet count the last flowStatInterval
     */
    private final FlowStat packetCountStat;

    /**
     * Statistics for overflow the last flowStatInterval
     */
    private final FlowStat overflowCountStat;

    public AisBusComponentStatus() {
        // Default one minute interval
        this(60000);
    }

    public AisBusComponentStatus(long flowStatInterval) {
        this.state = State.CREATED;
        this.startTime = System.currentTimeMillis();
        this.flowStatInterval = flowStatInterval;
        this.packetCountStat = new FlowStat(flowStatInterval);
        this.overflowCountStat = new FlowStat(flowStatInterval);
    }

    /**
     * Indicate a reception that is not filtered away
     */
    public synchronized void receive() {
        packetCountStat.received();
        packetCount++;
    }

    /**
     * Indicate a reception that is rejected by filter
     */
    public synchronized void filtered() {
        filteredPacketCount++;
    }

    /**
     * Indicate overflow when delivering packet
     */
    public synchronized void overflow() {
        overflowCountStat.received();
        overflowCount++;
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized void setInitialized() {
        if (state != State.CREATED) {
            throw new IllegalStateException("Component must be in state CREATED to be initialized");
        }
        state = State.INITIALIZED;
    }

    public synchronized void setStarted() {
        if (state == State.CREATED ) {
            throw new IllegalStateException("Component must be initialized to be started");
        }
        state = State.STARTED;
    }
    
    public synchronized void setConnected() {
        state = State.CONNECTED;
    }
    
    public synchronized boolean isConnected() {
        return (state == State.CONNECTED);
    }
    
    public synchronized void setNotConnected() {
        state = State.NOT_CONNECTED;
    }
    
    public synchronized void setStopped() {
        state = State.STOPPED;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFlowStatInterval() {
        return flowStatInterval;
    }

    public synchronized long getPacketCount() {
        return packetCount;
    }

    public synchronized long getFilteredPacketCount() {
        return filteredPacketCount;
    }

    public synchronized long getOverflowCount() {
        return overflowCount;
    }
    
    public synchronized double getOverflowRate() {
        return overflowCountStat.getRate();
    }

    @Override
    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AisBusComponentStatus [state=");
        builder.append(state);
        builder.append(", flowStatInterval=");
        builder.append(flowStatInterval);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", packetCount=");
        builder.append(packetCount);
        builder.append(", filteredPacketCount=");
        builder.append(filteredPacketCount);
        builder.append(", overflowCount=");
        builder.append(overflowCount);
        builder.append(", packetCountStat=");
        builder.append(packetCountStat);
        builder.append(", overflowCountStat=");
        builder.append(overflowCountStat);
        builder.append("]");
        return builder.toString();
    }

}
