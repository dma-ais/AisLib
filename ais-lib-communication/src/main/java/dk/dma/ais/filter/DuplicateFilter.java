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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;

/**
 * A doublet filter.
 * 
 * The doublet filter works by only allowing the same message through once in a time window. The six bit string of the message is
 * used as unique identifier.
 * 
 */
@ThreadSafe
public class DuplicateFilter extends MessageFilterBase {

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

    public DuplicateFilter() {
    }

    /**
     * Constructor given window size
     * 
     * @param window
     *            size in milliseconds
     */
    public DuplicateFilter(long windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public synchronized boolean rejectedByFilter(AisMessage message) {
        // Get time now
        Long now = System.currentTimeMillis();

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
