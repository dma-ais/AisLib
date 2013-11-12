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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.filter.DownSampleFilter;
import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.filter.ExpressionFilter;
import dk.dma.ais.filter.LocationFilter;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Circle;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;

/**
 * Simple application to filter AIS messages based on various filters
 * 
 * See usage() for usage
 * 
 * @throws InterruptedException
 * 
 */
public class AisFilter {

    public static void main(String[] args) throws InterruptedException, IOException {
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
        String expression = null;
        String geometry = null;
        
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
            } else if (args[i].indexOf("-e") >= 0) {
                expression = args[++i];
            } else if (args[i].indexOf("-g") >= 0) {
                geometry = args[++i];
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
            InputStream in = new FileInputStream(filename);
            if (filename.endsWith(".gz")) {
                in = new GZIPInputStream(in);
            }
            aisReader = AisReaders.createReaderFromInputStream(in);
        } else {
            AisTcpReader rrAisReader = AisReaders.createReader(hostPort);
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
        if (expression != null) {
            messageHandler.getFilters().add(new ExpressionFilter(expression));
        }
        if (geometry != null) {
            LocationFilter locationFilter = new LocationFilter();
            Area a = getGeometry(geometry);
            locationFilter.addFilterGeometry(a.contains());
            messageHandler.getFilters().add(locationFilter);
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

            if (!(aisReader instanceof AisTcpReader) && aisReader.getStatus() == Status.DISCONNECTED) {
                System.exit(0);
            }

            Date now = new Date();
            if (now.getTime() - start.getTime() > runtime) {
                System.exit(0);
            }

        }

    }

    private static Area getGeometry(String geometry) {
        String[] elems = StringUtils.split(geometry, ',');
        double[] numbers = new double[elems.length - 1];
        for (int i = 1; i < elems.length; i++) {
            numbers[i - 1] = Double.parseDouble(elems[i]);
        }
        if (elems[0].equalsIgnoreCase("circle")) {
            return new Circle(numbers[0], numbers[1], numbers[2], CoordinateSystem.GEODETIC);
        }
        if (elems[0].equalsIgnoreCase("bb")) {
            Position pos1 = Position.create(numbers[0], numbers[1]);
            Position pos2 = Position.create(numbers[2], numbers[3]);
            return BoundingBox.create(pos1, pos2, CoordinateSystem.GEODETIC);
        }
        return null;        
    }

    public static void usage() {
        System.out
                .println("Usage: AisFilter <-t|-f> <filename/host1:port1,...,hostN,portN> [-O filename] [-b b1,...,bN] [-c c1,...,cN] [-T seconds] [-d] [-C seconds] [-D seconds] [-F]");
        System.out.println("\t-t TCP round robin connection to host1:port1 ... hostN:portN");
        System.out.println("\t-f Read from file filename (gzip uncompression applied if filename ends with .gz)");
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
        System.out.println("\t-e Filter by expression. See SourceFilter.g4");
        System.out.println("\t-g Filter by geometry. Circle: 'circle,lat,lon,radius' Bounding box: 'bb,lat1,lon1,lat2,lon2' ");
    }

}
