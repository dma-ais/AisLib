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
package dk.dma.ais.filter;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.packet.AisPacket;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A down sampling filter.
 * <p/>
 * For position reports 1,2,3,4 and 18, only one message will be forwarded within the give sampling rate. E.g. one message per
 * minute.
 * <p/>
 * For static reports 5 and 24 the same apply. But it is handled separately from the position reports.
 * <p/>
 * All remaining message types are passed through without down sampling.
 * <p/>
 * As opposed to the DownSampleFilter (which operates on current system time), this filter uses the timestamp on
 * AisPackets as current time when filtering.
 */
@ThreadSafe
public class ReplayDownSampleFilter implements IPacketFilter {

    /**
     * Sample rate in seconds
     */
    private volatile long samplingRate = 60;

    /**
     * Map from MMSI to last time a pos report was received
     */
    private final Map<Integer, Long> posReceived = new ConcurrentHashMap<>();

    /**
     * Map from MMSI to last time a static report was received
     */
    private final Map<Integer, Long> statReceived = new ConcurrentHashMap<>();

    /**
     * Empty contructor
     */
    public ReplayDownSampleFilter() {
    }

    /**
     * Constructor given sampling rate in seconds
     *
     * @param samplingRate
     */
    public ReplayDownSampleFilter(long samplingRate) {
        this.samplingRate = samplingRate;
    }

    protected static int getMessageId(AisPacket packet) {
        int msgId = -1;

        AisMessage message = packet.tryGetAisMessage();
        if (message != null) {
            msgId = message.getMsgId();
        }
        return msgId;
    }

    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        // If not sampling always accept
        if (samplingRate == 0) {
            return false;
        }

        // If unknown packet always accept
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return false;
        }

        boolean posReport = message instanceof IPositionMessage;
        boolean statReport = message instanceof AisStaticCommon;
        if (!posReport && !statReport) { // Only these messages are filtered
            return false;
        }

        long now = packet.getBestTimestamp();
        Long lastReceived = null;

        // Get last received
        Map<Integer, Long> receiveSet = posReport ? posReceived : statReceived;
        lastReceived = receiveSet.get(message.getUserId());
        if (lastReceived == null) {
            lastReceived = 0L;
        }

        // Elapsed in seconds
        double elapsed = (now - lastReceived) / 1000.0;

        // Sample message
        if (elapsed < samplingRate) {
            return true;
        }

        // Mark new received time
        receiveSet.put(message.getUserId(), now);

        // Do not filter
        return false;
    }

    /**
     * Get sampling rate in seconds
     * 
     * @return
     */
    public long getSamplingRate() {
        return samplingRate;
    }

    /**
     * Set sampling rate in seconds
     * 
     * @param samplingRate
     */

    public void setSamplingRate(long samplingRate) {
        this.samplingRate = samplingRate;
    }

}
