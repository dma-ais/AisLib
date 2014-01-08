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

package dk.dma.ais.lib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketOutputSinks;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.commons.app.AbstractCommandLineTool;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.util.function.EConsumer;

/**
 * Converts a set of files into another sink format 
 * 
 * @author Jens Tuxen
 * 
 */
public class FileConvert extends AbstractCommandLineTool {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(FileConvert.class);

    @Parameter(required = true, description = "files to import...")
    List<String> sources;

    /** Where files should be moved to after having been processed. */
    @Parameter(names = "-path", required = false, description = "base-path to put converted files")
    Path convertTo = Paths.get("converted/");

    /** Where files should be moved to after having been processed. */
    @Parameter(names = "-keepStructure", required = false, description = "Whether to keep path structure")
    boolean keepFileStructure = true;

    @Parameter(names = "-fileEnding", required = false, description = "File ending")
    String fileEnding = ".txt";
    
    @Parameter(names = "-outputFormat", required = false, description = "Output formats: [OUTPUT_TO_TEXT, OUTPUT_PREFIXED_SENTENCES, OUTPUT_TO_HTML]")
    String outputSinkFormat = "OUTPUT_PREFIXED_SENTENCES";
    
    
    static final List<String> getOutputSinks() {
        LinkedList<String> methods = new LinkedList<String>();
        Field[] declaredMethods = AisPacketOutputSinks.class.getDeclaredFields();
        for (Field m: declaredMethods) {
            if (m.getName().startsWith("OUTPUT")) {
                methods.add(m.getName());   
            }
        }
        return methods;
    }

    /** {@inheritDoc} */
    @Override
    protected void run(Injector injector) throws Exception {        
        @SuppressWarnings("unchecked")
        final OutputStreamSink<AisPacket> sink = (OutputStreamSink<AisPacket>) AisPacketOutputSinks.class.getField(outputSinkFormat).get(null);
        final LinkedList<IPacketFilter> filters = new LinkedList<>();
        filters.add(new IPacketFilter() {
            @Override
            public boolean rejectedByFilter(AisPacket packet) {
                return false;
            }
        });

        final long start = System.currentTimeMillis();
        final AtomicInteger count = new AtomicInteger();
        
        final EConsumer<String> consumer = new EConsumer<String>() {
            
            public void process(BufferedOutputStream bos, AisPacket p) throws IOException {
                if (p != null && p.getBestTimestamp() > 0) {
                    for (IPacketFilter filter : filters) {
                        if (!filter.rejectedByFilter(p)) {
                            sink.process(bos, p, count.get());
                        }
                    }
                }
            }

            @Override
            public void accept(String s) throws IOException {
                Path path = Paths.get(s);
                System.out.println("Started processing file " + path);

                Path endPath;
                if (keepFileStructure) {
                    Path relative;
                    if (path.isAbsolute()) {
                        relative = path.subpath(1, path.getNameCount()-1);
                    } else {
                        relative = path;
                    }
                    endPath = Paths.get(convertTo.toString(),
                            relative.toString());
                    
                    new File(endPath.toString()).mkdirs();
                } else {
                    endPath = convertTo;
                }

                final BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(Paths.get(
                                endPath.toString(),
                                path.getFileName().toString()+fileEnding).toFile()), 209715); // 200kb
                                                                 // buffer

                
                try (AisPacketReader apis = AisPacketReader
                        .createFromFile(path, true)) {
                    /*
                     * tested reading packets into a buffer before processing (no effect) 
                     */
                    ArrayList<AisPacket> packets = new ArrayList<AisPacket>(128000);                  
                    while (true) {
                        try {
                            AisPacket p = null;
                            for (int i = 0; i< 128000; i++) {
                                count.incrementAndGet();
                                p = apis.readPacket();
                                if (p == null) {
                                    break;
                                } else {
                                    packets.add(p);
                                }
                            }
                            
                            for (AisPacket aisPacket : packets) {
                                process(bos, aisPacket);
                            }
                            packets.clear();
                            
                            if (p == null) {
                                bos.close();
                                break;
                                
                            }

                        } catch (Exception e) {
                            System.out.println("failed to read packet: "
                                            + e.getMessage());
                        }
                    }

                } finally {
                    bos.close();
                }
                long ms = System.currentTimeMillis()
                        - start;
                
                System.out.println(count.get()
                        + " packets,  " + count.get()
                        / ((double) ms / 1000)
                        + " packets/s");
                
                System.out.println("Finished processing file, " + count
                        + " packets was imported in total. Current Path: " + path);

            }

        };
        
        for (String s: sources) {
            consumer.accept(s);
        }   
    }

    public static void main(String[] args) throws Exception {
        FileConvert fc = new FileConvert();
        fc.execute(args);
    }
}
