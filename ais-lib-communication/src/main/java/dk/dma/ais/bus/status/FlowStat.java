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

import java.util.LinkedList;

import net.jcip.annotations.NotThreadSafe;

/**
 * Class that holds flow statistics
 */
@NotThreadSafe
public class FlowStat {

    private final long interval;
    private final long created;
    private Long lastReceived;
    // Timestamps for receives
    private LinkedList<Long> receives = new LinkedList<>();

    /**
     * Default interval of one minute to calculate rate
     */
    public FlowStat() {
        this(60000);
    }

    /**
     * Constructor given the interval to calculate rate for
     * @param interval in milliseconds
     */
    public FlowStat(long interval) {
        this.created = System.currentTimeMillis();
        this.interval = interval;
    }

    /**
     * Get the last received time
     * @return
     */
    public Long getLastReceived() {
        return lastReceived;
    }

    /**
     * Get time of creation
     * @return
     */
    public long getCreated() {
        return created;
    }

    /**
     * Get rate as receives/seconds
     * @return
     */
    public double getRate() {
        long now = System.currentTimeMillis();
        truncateReceives(now);
        if (receives.size() == 0) {
            return 0;
        }
        long last = receives.getLast();
        if (last == now) {
            return 0;
        }
        return receives.size() / ((double) (now - last) / 1000);
    }

    /**
     * Indicate a reception
     */
    public void received() {
        lastReceived = System.currentTimeMillis();
        receives.addFirst(lastReceived);
        truncateReceives(lastReceived);
    }

    private void truncateReceives(long now) {
        while (receives.size() > 0) {
            long last = receives.getLast();
            if (now - last > interval) {
                receives.removeLast();
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FlowStat [interval=");
        builder.append(interval);
        builder.append(", created=");
        builder.append(created);
        builder.append(", lastReceived=");
        builder.append(lastReceived);
        builder.append(", receives=");
        builder.append(receives.size());
        builder.append(", rate=");
        builder.append(getRate());
        builder.append("]");
        return builder.toString();
    }


}
