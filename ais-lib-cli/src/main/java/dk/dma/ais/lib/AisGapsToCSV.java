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

package dk.dma.ais.lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketFilters;
import java.util.function.Predicate;

/**
 * @author Jens Tuxen
 * 
 */
public final class AisGapsToCSV extends AbstractCommandLineDirectoryReader {
    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(AisGapsToCSV.class);

    @Parameter(names = "-start", description = "Start date (inclusive), format == yyyy-MM-ddZ")
    private volatile Date start;

    @Parameter(names = "-stop", description = "Stop date (exclusive), format == yyyy-MM-ddZ")
    private volatile Date stop;

    @Parameter(names = "-output")
    private String output = ".";

    @Parameter(names = "-filename", required = false, description = "output file path")
    private String filename = new Date().toLocaleString() + "_GAP.csv";

    @Parameter(names = "-sourceFilters", required = false, description = "List of sources to inspect")
    private List<String> sourceFilters;

    private PrintWriter fos;
    private ConcurrentSkipListMap<Integer, Boolean> seconds = new ConcurrentSkipListMap<Integer, Boolean>();

    private Predicate<AisPacket> sourceFiltersPredicate;

    /**
     * start time of first processed packet
     */
    Long startTime;
    public Long getStartTime() {
        if (startTime == null) {
            startTime = System.currentTimeMillis();
        }
        return startTime;
    }

    private final AtomicInteger count = new AtomicInteger();
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * dk.dma.ais.lib.AbstractCommandLineDirectoryReader#run(com.google.inject
     * .Injector)
     */
    @Override
    protected void run(Injector injector) throws Exception {
        Integer startTime = (int) (start.getTime() / 1000);
        Integer stopTime = (int) (stop.getTime() / 1000);

        fos = new PrintWriter(new BufferedWriter(new FileWriter(Paths.get(
                output, filename).toFile())));

        // initialize a very big collection of seconds
        LOG.debug("Initializing HashMap");
        for (int i = startTime; i < stopTime; i++) {
            seconds.put(i, false);
        }
        LOG.debug("Initialized HashMap");

        if (sourceFilters != null) {
            sourceFiltersPredicate = AisPacketFilters
                    .filterOnSourceId(sourceFilters.toArray(new String[0]));
        }

        // start the reading (blocks until done)
        LOG.debug("Starting Directory Reader");
        super.run(injector);

        LOG.debug("Writing CSV file");
        for (Entry<Integer, Boolean> entry : seconds.entrySet()) {
            fos.write(entry.getKey().toString() + ","
                    + entry.getValue().toString() + "\n");
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new AisGapsToCSV().execute(args);
    }

    @Override
    public void accept(final AisPacket t) {
        Integer totalPackets = count.incrementAndGet();
        if (totalPackets % 100000 == 0) {
            Long start = getStartTime();
            Long ms = System.currentTimeMillis() - start;
            LOG.debug("Total Packets: "+totalPackets+" "+(totalPackets / ((double)ms / 1000))+ " packets/s");
        }
        
        final long timestamp = t.getBestTimestamp() / 1000;
        if (t != null && timestamp > 0) {
            if (sourceFiltersPredicate != null && !sourceFiltersPredicate.test(t)) {
                return;
            }
            seconds.remove((int) timestamp);
        }

    }


}
