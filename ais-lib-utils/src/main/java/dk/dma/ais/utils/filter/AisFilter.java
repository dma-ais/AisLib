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

import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.lang.StringUtils;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.filter.ExpressionFilter;
import dk.dma.ais.filter.LocationFilter;
import dk.dma.ais.filter.ReplayDownSampleFilter;
import dk.dma.ais.filter.ReplayDuplicateFilter;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.commons.app.AbstractCommandLineTool;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Circle;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;

/**
 * Command line tool to do various AIS reading, filtering and writing
 */
public class AisFilter extends AbstractCommandLineTool {

    @Parameter(names = "-f", description = "Read from file filename (gzip or zip uncompression applied if filename ends with .gz or .zip respectively)")
    String filename;

    @Parameter(names = "-t", description = "TCP round robin connection to host1:port1 ... hostN:portN")
    String hostPort;

    @Parameter(names = "-d", description = "Directory to scan for files to read")
    String dir;

    @Parameter(names = "-name", description = "Glob pattern for files to read. '.zip' and '.gz' files are decompressed automatically")
    String name;

    @Parameter(names = "-r", description = "Recursive directory scan for files")
    boolean recursive;

    @Parameter(names = "-bs", description = "b1,...,bN comma separated list of base station MMSI's")
    String baseStations;

    @Parameter(names = "-country", description = "c1,...,cN comma separated list of country codes in two letter ISO 3166")
    String countries;

    @Parameter(names = "-region", description = "r1,...,rN comma separated list of regions")
    String regions;

    @Parameter(names = "-time", description = "time to run in seconds")
    long runtime;

    @Parameter(names = "-dump", description = "Dump message content (default false)")
    boolean dumpParsed;

    @Parameter(names = "-start", description = "Start time in format yyyy-MM-dd-HH:mm (Local time)")
    String starttimeStr;

    @Parameter(names = "-end", description = "End time in format yyyy-MM-dd-HH:mm (Local time)")
    String endtimeStr;

    @Parameter(names = "-timeout", description = "TCP read timeout in seconds, default none")
    int timeout;

    @Parameter(names = "-ds", description = "Down sample rate in seconds (default none)")
    long downsampleRate;

    @Parameter(names = "-df", description = "Do doublet filtering (default off)")
    boolean doubletFiltering;

    @Parameter(names = "-exp", description = "Filter by expression. See SourceFilter.g4")
    String expression;

    @Parameter(names = "-geo", description = "Filter by geometry. Circle: 'circle,lat,lon,radius' Bounding box: 'bb,lat1,lon1,lat2,lon2'")
    String geometry;

    @Parameter(names = "-o", description = "Output file. Default stdout.")
    String outFile;
    
    volatile MessageHandler messageHandler;

    public AisFilter() {
        super("AisFilter");
    }

    @Override
    protected void run(Injector injector) throws Exception {
        if (filename == null && hostPort == null && (dir == null || name == null)) {
            usage();
            System.exit(-1);
        }

        // Create reader
        AisReader aisReader;
        if (filename != null) {
            aisReader = AisReaders.createReaderFromFile(filename);
        } else if (hostPort != null) {
            AisTcpReader rrAisReader = AisReaders.createReader(hostPort);
            rrAisReader.setTimeout(timeout);
            aisReader = rrAisReader;
        } else {
            aisReader = AisReaders.createDirectoryReader(dir, name, recursive);
        }

        runtime *= 1000;

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

        // Output stream
        PrintStream out = System.out;
        if (outFile != null) {
            out = new PrintStream(outFile);
        }

        // Message handler
        messageHandler = new MessageHandler(filterSettings, out);
        messageHandler.setDumpParsed(dumpParsed);

        // Add filters
        // Maybe insert downsampling filter
        if (downsampleRate > 0) {
            messageHandler.getFilters().add(new ReplayDownSampleFilter(downsampleRate));
        }
        if (doubletFiltering) {
            messageHandler.getFilters().add(new ReplayDuplicateFilter());
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
        long start = System.currentTimeMillis();
        aisReader.start();

        while (true) {
            Thread.sleep(500);
            if (aisReader.getStatus() == Status.DISCONNECTED) {
                break;
            }
            if (runtime > 0 && (System.currentTimeMillis() - start > runtime)) {
                messageHandler.setStop(true);
                aisReader.stopReader();
                break;
            }
        }
    }
    
    @Override
    public void shutdown() {
        MessageHandler mh = messageHandler;
        if (mh != null) {
            mh.setStop(true);
            mh.printStats();
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

    @Override
    public void usage() {
        System.out.println("Usage: AisFilter [options]");
        System.out.println("\t-t        TCP round robin connection to host1:port1 ... hostN:portN");
        System.out
                .println("\t-f        Read from file filename (gzip or zip uncompression applied if filename ends with .gz or .zip)");
        System.out.println("\t-d        Directory to scan for files to read");
        System.out.println("\t-r        Recursive directory scan for files");
        System.out.println("\t-name     Glob pattern for files to read. '.zip' and '.gz' files are decompressed automatically");
        System.out.println("\t-o        Write output to file");
        System.out.println("\t-timeout  TCP read timeout in seconds, default none");
        System.out.println("\t-bs       b1,...,bN comma separated list of base station MMSI's");
        System.out.println("\t-region   r1,...,rN comma separated list of regions");
        System.out.println("\t-country  c1,...,cN comma separated list of country codes in two letter ISO 3166");
        System.out.println("\t-time     Time to run in seconds (default indefinitely)");
        System.out.println("\t-dump     Dump message content (default false)");
        System.out.println("\t-start    Start time in format yyyy-MM-dd-HH:mm (Local time)");
        System.out.println("\t-end      End time in format yyyy-MM-dd-HH:mm (Local time)");
        System.out.println("\t-ds       Down sample rate in seconds (default none)");
        System.out.println("\t-df       Do doublet filtering (default off)");
        System.out.println("\t-exp      Filter by expression. See SourceFilter.g4");
        System.out
                .println("\t-geo      Filter by geometry. Circle: 'circle,lat,lon,radius' Bounding box: 'bb,lat1,lon1,lat2,lon2' ");
        System.out.println("\t-help     Show this help");
    }

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Uncaught exception in thread " + t.getClass().getCanonicalName() + ": " + e.getMessage());
                System.exit(-1);
            }
        });
        final AisFilter aisFilter = new AisFilter();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                aisFilter.shutdown();
            }
        });
        aisFilter.execute(args);
    }

}
