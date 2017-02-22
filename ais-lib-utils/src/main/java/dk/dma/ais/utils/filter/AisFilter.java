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
package dk.dma.ais.utils.filter;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.filter.ExpressionFilter;
import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.LocationFilter;
import dk.dma.ais.filter.ReplayDownSampleFilter;
import dk.dma.ais.filter.ReplayDuplicateFilter;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.ais.sentence.Vdm;
import dk.dma.commons.app.AbstractCommandLineTool;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Circle;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.zip.GZIPOutputStream;

/**
 * Command line tool to do various AIS reading, filtering and writing
 */
public class AisFilter extends AbstractCommandLineTool implements Consumer<AisPacket> {

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

    @Parameter(names = "-start", description = "Start time in format yyyy-MM-dd-HH:mm (UTC time)")
    String starttimeStr;

    @Parameter(names = "-end", description = "End time in format yyyy-MM-dd-HH:mm (UTC time)")
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

    @Parameter(names = "-split", description = "Split into multiple output files with this maximum size in bytes. Files will be postfixed with a running number.")
    Long splitSize;

    @Parameter(names = "-z", description = "Compress output files")
    boolean compress;

    private final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private PrintStream out;

    private long start;

    private long end;

    private long msgCount;

    private long bytes;

    private volatile boolean stop;

    private final List<IPacketFilter> filters = new CopyOnWriteArrayList<>();

    private final FilterSettings filter = new FilterSettings();

    private int fileCounter;

    private int currentFileBytes;

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

        // Add times
        filter.parseStartAndEnd(starttimeStr, endtimeStr);

        // Add base stations
        filter.parseBaseStations(baseStations);

        // Add countries
        filter.parseCountries(countries);

        // Add regions
        filter.parseRegions(regions);

        // Output stream
        if (outFile == null) {
            out = System.out;
        }

        // Message handler
        // Add filters
        // Maybe insert downsampling filter
        if (downsampleRate > 0) {
            filters.add(new ReplayDownSampleFilter(downsampleRate));
        }
        if (doubletFiltering) {
            filters.add(new ReplayDuplicateFilter());
        }
        if (expression != null) {
            filters.add(new ExpressionFilter(expression));
        }
        if (geometry != null) {
            LocationFilter locationFilter = new LocationFilter();
            Area a = getGeometry(geometry);
            locationFilter.addFilterGeometry(e -> a.contains(e));
            filters.add(locationFilter);
        }

        // Register handler
        aisReader.registerPacketHandler(this);

        // Start reader thread
        long start = System.currentTimeMillis();
        aisReader.start();

        while (true) {
            Thread.sleep(500);
            if (aisReader.getStatus() == Status.DISCONNECTED) {
                break;
            }
            if (runtime > 0 && System.currentTimeMillis() - start > runtime) {
                stop = true;
                aisReader.stopReader();
                break;
            }
        }
    }

    private PrintStream getNextOutputStram() {
        if (outFile == null) {
            throw new Error("No output stream and no out file argument given");
        }
        String filename;
        if (splitSize == null) {
            filename = outFile;
        } else {
            Path path = Paths.get(outFile);
            filename = String.format("%06d", fileCounter++) + "." + path.getFileName();
            Path dir = path.getParent();
            if (dir != null) {
                filename = dir + "/" + filename;
            }
        }
        if (compress) {
            filename += ".gz";
        }
        try {
            if (!compress) {
                return new PrintStream(filename);
            } else {
                return new PrintStream(new GZIPOutputStream(new FileOutputStream(filename)));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open output file: " + filename, e);
        }
    }

    @Override
    public void accept(AisPacket packet) {
        if (out == null) {
            // Open new output stream
            out = getNextOutputStram();
        }

        end = System.currentTimeMillis();
        if (start == 0) {
            start = end;
        }

        for (IPacketFilter filter : filters) {
            if (filter.rejectedByFilter(packet)) {
                return;
            }
        }

        Integer baseMMSI = -1;
        String country = "";
        String region = "";

        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return;
        }

        // Get source tag properties
        IProprietarySourceTag sourceTag = vdm.getSourceTag();
        if (sourceTag != null) {
            baseMMSI = sourceTag.getBaseMmsi();
            if (sourceTag.getCountry() != null) {
                country = sourceTag.getCountry().getTwoLetter();
            }
            if (sourceTag.getRegion() != null) {
                region = sourceTag.getRegion();
            }
        }
        if (region.equals("")) {
            region = "0";
        }

        // Maybe check for start date
        Date timestamp = vdm.getTimestamp();
        if (filter.getStartDate() != null && timestamp != null) {
            if (timestamp.before(filter.getStartDate())) {
                return;
            }
        }

        // Maybe check for end date
        if (filter.getEndDate() != null && timestamp != null) {
            if (timestamp.after(filter.getEndDate())) {
                System.exit(0);
            }
        }

        // Maybe check for base station MMSI
        if (filter.getBaseStations().size() > 0) {
            if (!filter.getBaseStations().contains(baseMMSI)) {
                return;
            }
        }

        // Maybe check for country
        if (filter.getCountries().size() > 0) {
            if (!filter.getCountries().contains(country)) {
                return;
            }
        }

        // Maybe check for region
        if (filter.getRegions().size() > 0) {
            if (!filter.getRegions().contains(region)) {
                return;
            }
        }

        if (stop) {
            return;
        }

        // Count message
        msgCount++;

        // Print tag line packet
        out.print(packet.getStringMessage() + "\r\n");

        // Count bytes
        bytes += packet.getStringMessage().length() + 2;
        currentFileBytes += packet.getStringMessage().length() + 2;

        // Maybe print parsed
        if (dumpParsed) {
            if (timestamp != null) {
                out.println("+ timetamp " + timestampFormat.format(timestamp));
            }
            if (vdm.getTags() != null) {
                for (IProprietaryTag tag : vdm.getTags()) {
                    out.println("+ " + tag.toString());
                }
            }
            AisMessage aisMessage = packet.tryGetAisMessage();
            if (aisMessage != null) {
                out.println("+ " + aisMessage.toString());
            } else {
                out.println("+ AIS message could not be parsed");
            }

            // Check for binary message
            if (aisMessage instanceof AisBinaryMessage) {
                AisBinaryMessage binaryMessage = (AisBinaryMessage) aisMessage;
                try {
                    AisApplicationMessage appMessage = binaryMessage.getApplicationMessage();
                    out.println(appMessage);
                } catch (SixbitException e) {}
            }
            out.println("---------------------------");
        }

        // Maybe time for new outfile
        if (splitSize != null && currentFileBytes >= splitSize) {
            out.close();
            out = null;
            currentFileBytes = 0;
        }

    }

    public List<IPacketFilter> getFilters() {
        return filters;
    }

    public void setDumpParsed(boolean dumpParsed) {
        this.dumpParsed = dumpParsed;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void printStats() {
        long elapsed = end - start;
        double elapsedSecs = elapsed / 1000.0;
        double msgPerMin = msgCount / (elapsedSecs / 60.0);
        long kbytes = bytes / 1000;

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        System.err.println("\n");
        System.err.println("Elapsed  : " + df.format(new Date(elapsed)));
        System.err.println("Messages : " + msgCount);
        System.err.println("Msg/min  : " + String.format(Locale.US, "%.2f", msgPerMin));
        System.err.println("Msg/sec  : " + String.format(Locale.US, "%.2f", msgPerMin / 60.0));
        System.err.println("KBytes   : " + kbytes);
        System.err.println("KB/s     : " + String.format(Locale.US, "%.2f", kbytes / elapsedSecs));
        System.err.println("Kbps     : " + String.format(Locale.US, "%.2f", kbytes * 8.0 / elapsedSecs));
    }

    @Override
    public void shutdown() {
        stop = true;
        printStats();
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
        System.out
                .println("\t-name     Glob pattern for files to read. '.zip' and '.gz' files are decompressed automatically");
        System.out.println("\t-o        Write output to file");
        System.out
                .println("\t-split    Split into multiple output files with this maximum size in bytes, files will be postfixed with a running number");
        System.out.println("\t-z        Compress output files with gzip");
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
        System.out.println("\t-exp      Filter by expression. See ExpressionFilter.g4");
        System.out
        .println("\t-geo      Filter by geometry. Circle: 'circle,lat,lon,radius' Bounding box: 'bb,lat1,lon1,lat2,lon2' ");
        System.out.println("\t-help     Show this help");
    }

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Uncaught exception in thread " + t.getClass().getCanonicalName() + ": "
                        + e.getMessage());
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
