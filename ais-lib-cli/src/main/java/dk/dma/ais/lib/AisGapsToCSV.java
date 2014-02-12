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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketFilters;
import dk.dma.enav.util.function.Predicate;

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
    private String filename = new Date().toLocaleString()+"_GAP.csv";
    
    
    @Parameter(names = "-sourceFilters", required = true, description = "List of sources to inspect")
    private List<String> sourceFilters;
    
    private PrintWriter fos;
    private ConcurrentSkipListMap<Integer, Boolean> seconds = new ConcurrentSkipListMap<Integer, Boolean>();
    
    private Predicate<AisPacket> sourceFiltersPredicate;



    private ExecutorCompletionService<Void> completion;
    
    /* (non-Javadoc)
     * @see dk.dma.ais.lib.AbstractCommandLineDirectoryReader#run(com.google.inject.Injector)
     */
    @Override
    protected void run(Injector injector) throws Exception {        
        Integer startTime = (int) (start.getTime()/1000);
        Integer stopTime = (int) (stop.getTime()/1000);
        
        fos = new PrintWriter(new BufferedWriter(new FileWriter(Paths.get(output,filename).toFile())));
        
        //initialize a very big collection of seconds
        LOGGER.debug("Initializing HashMap");
        for (int i=startTime; i< stopTime; i++) {
            seconds.put(i,false);
        }
        
        sourceFiltersPredicate = AisPacketFilters.filterOnSourceId(sourceFilters.toArray(new String[0]));
        
        //create a service to consume the aispackets: use minimum 1 cpu and at most ALL_CPUS-1
        ExecutorService service = Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()-1));
        //ExecutorService service = Executors.newSingleThreadExecutor();
        completion = new ExecutorCompletionService<>(service);
        
        Thread results = new Thread(new Runnable() {
            
            @Override
            public void run() {
                Logger log = LoggerFactory.getLogger(Thread.class);
                long start = System.currentTimeMillis();
                final AtomicInteger count = new AtomicInteger();
                while(true) {
                    try {
                        Future<Void> result = completion.poll(10, TimeUnit.SECONDS);
                        if (result == null) break;
                        
                        long ms = System.currentTimeMillis() - start;
                        if (count.incrementAndGet() % 1000000 == 0) {
                            log.info(count.get()
                                    + " packets,  " + count.get()
                                    / ((double) ms / 1000)
                                    + " packets/s");
                        }
                        
                        
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
            }
        });
        results.start();
        
        //start the reading (blocks until done)
        super.run(injector);
        
        
        for (Entry<Integer, Boolean> entry : seconds.entrySet()) {
            //System.out.println(entry.getKey().toString()+","+entry.getValue().toString());
            fos.write(entry.getKey().toString()+","+entry.getValue().toString()+"\n");
        }
        
        fos.close();
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
        //submit tasks for completion (result is a Void object since we're manipulating a common ConcurrentSkipListMap)
        completion.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                final long timestamp = t.getBestTimestamp()/1000;
                if (t != null && sourceFiltersPredicate.test(t) && timestamp > 0) {
                    //seconds.put((int) (t.getBestTimestamp()/1000),true);
                    seconds.remove((int) timestamp);
                }
                

                return null;
            }
        });

    }
    
    

    
    

}
