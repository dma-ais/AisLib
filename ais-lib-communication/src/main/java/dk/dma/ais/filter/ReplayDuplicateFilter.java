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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;

/**
 * A doublet filter for replayed streams
 * 
 * The doublet filter works by only allowing the same message through once in a time window. The six bit string of the message is
 * used as unique identifier.
 * 
 */
@ThreadSafe
public class ReplayDuplicateFilter implements IPacketFilter {

    /**
     * Number of message receptions between cleanups
     */
    private static final long MAX_CLEANUP_AGE = 10000;

    /**
     * A TreeMap is used with raw six bit string as key. compareTo is used which is rather effective for differing messages. Using
     * HashMap can result in wrong filtering as two strings can produce the same hash code.
     */
    @GuardedBy("this")
    private final Map<DoubletEntry, Long> sixbitReceived = new TreeMap<>();

    /**
     * A default window size of 10 seconds is used
     */
    private volatile long windowSize = 10000;

    /**
     * Number of message receptions since last cleanup
     */
    private volatile long cleanupAge;

    public ReplayDuplicateFilter() {
    }

    /**
     * Constructor given window size
     * 
     * @param window
     *            size in milliseconds
     */
    public ReplayDuplicateFilter(long windowSize) {
        this.windowSize = windowSize;
    }
    
    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        // Get time now
        long now = packet.getBestTimestamp();
        
        // Get AisMessage
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return true;
        }

        // Make new entry
        DoubletEntry newEntry = new DoubletEntry(message.getVdm().getSixbitString(), now);

        // Try to find matching received
        Long lastReceived = sixbitReceived.get(newEntry);
        if (lastReceived == null) {
            lastReceived = 0L;
        }

        // Elapsed in msecs
        long elapsed = now - lastReceived;

        if (elapsed < windowSize) {
            // Doublet within window
            // System.out.println("Doublet: " + newEntry.getSixbit() +
            // " elapsed: " + elapsed);
            return true;
        }

        // Save message in map
        sixbitReceived.put(newEntry, now);

        // Update message reception count
        cleanupAge++;

        // Do cleanup for every 1.000 inserts/updates
        if (cleanupAge >= MAX_CLEANUP_AGE) {
            List<DoubletEntry> oldEntries = new ArrayList<>(sixbitReceived.size());
            // Iterate through all elements
            for (DoubletEntry entry : sixbitReceived.keySet()) {
                if (now - entry.getReceived() > windowSize) {
                    oldEntries.add(entry);
                }
            }
            for (DoubletEntry oldEntry : oldEntries) {
                sixbitReceived.remove(oldEntry);
            }
            cleanupAge = 0;
        }

        // Send message
        return false;
    }

    public long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * An entry class with sixbit string and reception date
     */
    public class DoubletEntry implements Comparable<DoubletEntry> {

        private String sixbit;
        private long received;

        public DoubletEntry(String sixbit, long received) {
            this.sixbit = sixbit;
            this.received = received;
        }

        /**
         * Comparison is done only on six bit string
         */
        @Override
        public int compareTo(DoubletEntry doubletEntry) {
            return sixbit.compareTo(doubletEntry.sixbit);
        }

        public long getReceived() {
            return received;
        }

        public String getSixbit() {
            return sixbit;
        }

        @Override
        public boolean equals(Object obj) {
            DoubletEntry e = (DoubletEntry)obj;
            if (e == null || e.sixbit == null) {
                return false;
            }
            return sixbit.equals(((DoubletEntry) obj).sixbit);
        }

        @Override
        public int hashCode() {
            return sixbit.hashCode();
        }

    }

}
