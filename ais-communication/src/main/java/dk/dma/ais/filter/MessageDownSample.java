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
package dk.dma.ais.filter;

import java.util.HashMap;
import java.util.Map;

import dk.dma.ais.message.AisMessage;

/**
 * A down sampling filter.
 * 
 * For position reports 1,2,3,4 and 18, only one message will be forwarded within the give sampling rate. E.g. one
 * message per minute.
 * 
 * For static reports 5 and 24 the same apply. But it is handled separately from the position reports.
 * 
 * All remaining message types are passed through without down sampling.
 * 
 */
public class MessageDownSample extends GenericFilter {

    /**
     * Sample rate in seconds
     */
    private long samplingRate = 60;

    /**
     * Map from MMSI to last time a pos report was received
     */
    private Map<Long, Long> posReceived = new HashMap<>();

    /**
     * Map from MMSI to last time a static report was received
     */
    private Map<Long, Long> statReceived = new HashMap<>();

    /**
     * Empty contructor
     */
    public MessageDownSample() {}

    /**
     * Constructor given sampling rate
     * 
     * @param samplingRate
     */
    public MessageDownSample(long samplingRate) {
        this.samplingRate = samplingRate;
    }

    /**
     * Receive message from source or other filter
     */
    @Override
    public synchronized void receive(AisMessage aisMessage) {
        // If not sampling just forward
        if (samplingRate == 0) {
            sendMessage(aisMessage);
            return;
        }

        boolean posReport = false;

        switch (aisMessage.getMsgId()) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 18:
            // Pos reports
            posReport = true;
            break;
        case 5:
        case 24:
            // Stat report
            break;
        default:
            // All other are just forwared
            sendMessage(aisMessage);
            return;
        }

        Long now = System.currentTimeMillis();
        Long lastReceived = null;

        // Get last received
        Map<Long, Long> receiveSet = posReport ? posReceived : statReceived;
        lastReceived = receiveSet.get(aisMessage.getUserId());
        if (lastReceived == null) {
            lastReceived = 0L;
        }

        // Elapsed in seconds
        double elapsed = (now - lastReceived) / 1000.0;

        // Sample message
        if (elapsed < samplingRate) {
            return;
        }

        // Mark new received time
        receiveSet.put(aisMessage.getUserId(), now);

        // Send message
        sendMessage(aisMessage);

    }

    /**
     * Get sampling rate in seconds
     * 
     * @return
     */
    public synchronized long getSamplingRate() {
        return samplingRate;
    }

    /**
     * Set sampling rate in seconds
     * 
     * @param samplingRate
     */
    public synchronized void setSamplingRate(long samplingRate) {
        this.samplingRate = samplingRate;
    }

}
