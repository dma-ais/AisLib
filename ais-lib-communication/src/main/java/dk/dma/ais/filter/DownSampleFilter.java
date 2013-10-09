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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;

/**
 * A down sampling filter.
 * 
 * For position reports 1,2,3,4 and 18, only one message will be forwarded within the give sampling rate. E.g. one message per
 * minute.
 * 
 * For static reports 5 and 24 the same apply. But it is handled separately from the position reports.
 * 
 * All remaining message types are passed through without down sampling.
 * 
 */
@ThreadSafe
public class DownSampleFilter extends MessageFilterBase {

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
    public DownSampleFilter() {
    }

    /**
     * Constructor given sampling rate in seconds
     * 
     * @param samplingRate
     */
    public DownSampleFilter(long samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public boolean rejectedByFilter(AisMessage message) {
        // If not sampling always accept
        if (samplingRate == 0) {
            return false;
        }

        boolean posReport = false;

        switch (message.getMsgId()) {
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
            // All other are not filterted
            return false;
        }

        Long now = System.currentTimeMillis();
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
