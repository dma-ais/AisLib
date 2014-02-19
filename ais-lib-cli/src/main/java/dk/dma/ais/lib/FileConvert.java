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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

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

    /**
     * getOutputSinks
     * 
     * @return
     */
    static final List<String> getOutputSinks() {
        LinkedList<String> methods = new LinkedList<String>();
        Field[] declaredMethods = AisPacketOutputSinks.class
                .getDeclaredFields();
        for (Field m : declaredMethods) {
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

        final long start = System.currentTimeMillis();
        final AtomicInteger count = new AtomicInteger();
        
        final EConsumer<String> consumer = new EConsumer<String>() {
            
            public void process(BufferedOutputStream bos, AisPacket p) throws IOException {
                sink.process(bos, p, count.getAndIncrement());
            }

            @Override
            public void accept(String s) throws IOException {
                Path path = Paths.get(s);
                LOG.debug("Started processing file " + path);

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
                                      
                    while (true) {            
                        AisPacket p = null;
                        p = apis.readPacket();
                        if (p == null) {
                            break;
                        }
                        process(bos, p);
                    }

                } catch (IOException e) {
                    //continue, but log error
                    LOG.error(e.toString());
                    //throw e;
                    
                } finally {
                    bos.close();
                }
                long ms = System.currentTimeMillis()
                        - start;
                
                
                LOG.debug("Finished processing file, " + count
                        + " packets was imported in total. Current Path: " + path);

            }

        };
        
        /* Creates a pool of executors, 4 threads.
         * Each thread will open a file using an aispacket reader, 10000 files can be submitted to the queue (memory limit)
         * Afterwards, the calling thread will execute the job instead.
         */
        ThreadPoolExecutor threadpoolexecutor = new ThreadPoolExecutor(4, 4, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),new ThreadPoolExecutor.CallerRunsPolicy());
        for (final String s: sources) {
            threadpoolexecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        consumer.accept(s);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
            });
        }
        
        threadpoolexecutor.awaitTermination(999, TimeUnit.DAYS);
    }

    public static void main(String[] args) throws Exception {
        FileConvert fc = new FileConvert();
        fc.execute(args);
    }
}
