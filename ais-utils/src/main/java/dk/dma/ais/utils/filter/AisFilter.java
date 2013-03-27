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
package dk.dma.ais.utils.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import dk.dma.ais.filter.DownSampleFilter;
import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.reader.RoundRobinAisTcpReader;

/**
 * Simple application to filter AIS messages based on various filters
 * 
 * See usage() for usage
 * 
 * @throws FileNotFoundException
 * @throws InterruptedException
 * 
 */
public class AisFilter {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        // Read command line arguments
        String filename = null;
        String hostPort = null;
        String baseStations = null;
        String countries = null;
        String regions = null;
        String runTimeStr = null;
        boolean dumpParsed = false;
        String starttimeStr = null;
        String endtimeStr = null;
        int timeout = 0;
        long downsampleRate = 0;
        PrintStream out = System.out;
        boolean doubletFiltering = false;

        if (args.length < 2) {
            usage();
            System.exit(1);
        }
        int i = 0;
        while (i < args.length) {
            if (args[i].indexOf("-t") >= 0) {
                hostPort = args[++i];
            } else if (args[i].indexOf("-f") >= 0) {
                filename = args[++i];
            } else if (args[i].indexOf("-C") >= 0) {
                timeout = Integer.parseInt(args[++i]);
            } else if (args[i].indexOf("-b") >= 0) {
                baseStations = args[++i];
            } else if (args[i].indexOf("-c") >= 0) {
                countries = args[++i];
            } else if (args[i].indexOf("-r") >= 0) {
                regions = args[++i];
            } else if (args[i].indexOf("-T") >= 0) {
                runTimeStr = args[++i];
            } else if (args[i].indexOf("-d") >= 0) {
                dumpParsed = true;
            } else if (args[i].indexOf("-S") >= 0) {
                starttimeStr = args[++i];
            } else if (args[i].indexOf("-E") >= 0) {
                endtimeStr = args[++i];
            } else if (args[i].indexOf("-D") >= 0) {
                downsampleRate = Long.parseLong(args[++i]);
            } else if (args[i].indexOf("-O") >= 0) {
                out = new PrintStream(args[++i]);
            } else if (args[i].indexOf("-F") >= 0) {
                doubletFiltering = true;
            }
            i++;
        }
        if (filename == null && hostPort == null) {
            usage();
            System.exit(1);
        }

        // Use TCP or file
        AisReader aisReader;
        if (filename != null) {
            aisReader = new AisStreamReader(new FileInputStream(filename));
        } else {
            RoundRobinAisTcpReader rrAisReader = new RoundRobinAisTcpReader();
            rrAisReader.setCommaseparatedHostPort(hostPort);
            rrAisReader.setTimeout(timeout);
            aisReader = rrAisReader;
        }

        // Run time
        long runtime = 60 * 60 * 1000; // In ms
        if (runTimeStr != null) {
            runtime = Long.parseLong(runTimeStr) * 1000;
        }

        // Create filter
        FilterSettings filterSettings = new FilterSettings();

        // Add times
        filterSettings.parseStartAndEnd(starttimeStr, endtimeStr);

        // Add base stations
        filterSettings.parseBaseStations(baseStations);

        // Add countries
        filterSettings.parseCountries(countries);

        // Add regions
        filterSettings.parseRegions(regions);

        // Message handler
        MessageHandler messageHandler = new MessageHandler(filterSettings, out);
        messageHandler.setDumpParsed(dumpParsed);

        // Add filters
        // Maybe insert downsampling filter
        if (downsampleRate > 0) {
            messageHandler.getFilters().add(new DownSampleFilter(downsampleRate));
        }
        if (doubletFiltering) {
            messageHandler.getFilters().add(new DuplicateFilter());
        }

        // Register handler
        aisReader.registerPacketHandler(messageHandler);

        // Start reader thread
        Date start = new Date();
        aisReader.start();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(messageHandler));

        while (true) {
            Thread.sleep(1000);

            if (aisReader instanceof AisStreamReader && aisReader.getStatus() == Status.DISCONNECTED) {
                System.exit(0);
            }

            Date now = new Date();
            if (now.getTime() - start.getTime() > runtime) {
                System.exit(0);
            }

        }

    }

    public static void usage() {
        System.out
                .println("Usage: AisFilter <-t|-f> <filename/host1:port1,...,hostN,portN> [-O filename] [-b b1,...,bN] [-c c1,...,cN] [-T seconds] [-d] [-C seconds] [-D seconds] [-F]");
        System.out.println("\t-t TCP round robin connection to host1:port1 ... hostN:portN");
        System.out.println("\t-f Read from file filename");
        System.out.println("\t-O Write output to file");
        System.out.println("\t-C TCP read timeout in seconds, default none");
        System.out.println("\t-b b1,...,bN comma separated list of base station MMSI's");
        System.out.println("\t-r r1,...,rN comma separated list of regions");
        System.out.println("\t-c c1,...,cN comma separated list of country codes in two letter ISO 3166");
        System.out.println("\t-T time to run in seconds (default 1 hour)");
        System.out.println("\t-d Dump message content (default false)");
        System.out.println("\t-S Start time in format yyyy-MM-dd-HH:mm (Local time)");
        System.out.println("\t-E End time in format yyyy-MM-dd-HH:mm (Local time)");
        System.out.println("\t-D Down sample rate in seconds (default none)");
        System.out.println("\t-F Do doublet filtering (default off)");
        System.out.println("\t-R Insert reply proprietary tag with time of reception");
    }

}
