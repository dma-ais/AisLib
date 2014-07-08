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
     * Number of packets received by the component
     */
    private long inCount;
    /**
     * Number of packets filtered by the component
     */
    private long filteredCount;
    /**
     * Number of packets that cannot be delivered due to overflow at the receiver
     */
    private long overflowCount;

    /**
     * Statistics for in count the last flowStatInterval
     */
    private final FlowStat inCountStat;

    /**
     * Statistics for overflow the last flowStatInterval
     */
    private final FlowStat overflowCountStat;
    
    /**
     * Statistics for filtered packets last flowStatInterval
     */
    private final FlowStat filteredCountStat;

    public AisBusComponentStatus() {
        // Default one minute interval
        this(60000);
    }

    public AisBusComponentStatus(long flowStatInterval) {
        this.state = State.CREATED;
        this.startTime = System.currentTimeMillis();
        this.flowStatInterval = flowStatInterval;
        this.inCountStat = new FlowStat(flowStatInterval);
        this.overflowCountStat = new FlowStat(flowStatInterval);
        this.filteredCountStat = new FlowStat(flowStatInterval);
    }

    /**
     * Indicate a reception that is not filtered away
     */
    public synchronized void receive() {
        inCountStat.received();
        inCount++;
    }

    /**
     * Indicate a reception that is rejected by filter
     */
    public synchronized void filtered() {
        filteredCountStat.received();
        filteredCount++;
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
    
    public synchronized boolean isStarted() {
        return state == State.STARTED || state == State.CONNECTED || state == State.NOT_CONNECTED;
    }
    
    public synchronized void setConnected() {
        state = State.CONNECTED;
    }
    
    public synchronized boolean isConnected() {
        return state == State.CONNECTED;
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

    public synchronized long getInCount() {
        return inCount;
    }
    
    public synchronized double getInRate() {
        return inCountStat.getRate();
    }

    public synchronized long getFilteredCount() {
        return filteredCount;
    }
    
    public synchronized double getFilteredRate() {
        return filteredCountStat.getRate();
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
        builder.append(", inCount=");
        builder.append(inCount);
        builder.append(", filteredCount=");
        builder.append(filteredCount);
        builder.append(", overflowCount=");
        builder.append(overflowCount);
        builder.append(", inCountStat=");
        builder.append(inCountStat);
        builder.append(", overflowCountStat=");
        builder.append(overflowCountStat);
        builder.append("]");
        return builder.toString();
    }

}
