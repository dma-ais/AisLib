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
 * <p>
 * TODO: Flow history
 */
@ThreadSafe
public class AisBusComponentStatus {

    /**
     * The enum State.
     */
    public enum State {
        /**
         * Created state.
         */
        CREATED,
        /**
         * Initialized state.
         */
        INITIALIZED,
        /**
         * Started state.
         */
        STARTED,
        /**
         * Connected state.
         */
        CONNECTED,
        /**
         * Not connected state.
         */
        NOT_CONNECTED,
        /**
         * Stopped state.
         */
        STOPPED;
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

    /**
     * Instantiates a new Ais bus component status.
     */
    public AisBusComponentStatus() {
        // Default one minute interval
        this(60000);
    }

    /**
     * Instantiates a new Ais bus component status.
     *
     * @param flowStatInterval the flow stat interval
     */
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

    /**
     * Gets state.
     *
     * @return the state
     */
    public synchronized State getState() {
        return state;
    }

    /**
     * Sets initialized.
     */
    public synchronized void setInitialized() {
        if (state != State.CREATED) {
            throw new IllegalStateException("Component must be in state CREATED to be initialized");
        }
        state = State.INITIALIZED;
    }

    /**
     * Sets started.
     */
    public synchronized void setStarted() {
        if (state == State.CREATED ) {
            throw new IllegalStateException("Component must be initialized to be started");
        }
        state = State.STARTED;
    }

    /**
     * Is started boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isStarted() {
        return state == State.STARTED || state == State.CONNECTED || state == State.NOT_CONNECTED;
    }

    /**
     * Sets connected.
     */
    public synchronized void setConnected() {
        state = State.CONNECTED;
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isConnected() {
        return state == State.CONNECTED;
    }

    /**
     * Sets not connected.
     */
    public synchronized void setNotConnected() {
        state = State.NOT_CONNECTED;
    }

    /**
     * Sets stopped.
     */
    public synchronized void setStopped() {
        state = State.STOPPED;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets flow stat interval.
     *
     * @return the flow stat interval
     */
    public long getFlowStatInterval() {
        return flowStatInterval;
    }

    /**
     * Gets in count.
     *
     * @return the in count
     */
    public synchronized long getInCount() {
        return inCount;
    }

    /**
     * Gets in rate.
     *
     * @return the in rate
     */
    public synchronized double getInRate() {
        return inCountStat.getRate();
    }

    /**
     * Gets filtered count.
     *
     * @return the filtered count
     */
    public synchronized long getFilteredCount() {
        return filteredCount;
    }

    /**
     * Gets filtered rate.
     *
     * @return the filtered rate
     */
    public synchronized double getFilteredRate() {
        return filteredCountStat.getRate();
    }

    /**
     * Gets overflow count.
     *
     * @return the overflow count
     */
    public synchronized long getOverflowCount() {
        return overflowCount;
    }

    /**
     * Gets overflow rate.
     *
     * @return the overflow rate
     */
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
