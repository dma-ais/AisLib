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
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;

/**
 * @author Jens Tuxen
 *
 */
public final class AisGapsToCSV extends AbstractCommandLineDirectoryReader {
    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(AisGapsToCSV.class);

    @Parameter(names = "-output", required = false, description = "output file path")
    private String output = "gap.csv";
     
    @Parameter(names = "-start", description = "Start date (inclusive), format == yyyy-MM-dd")
    private volatile Date start;

    @Parameter(names = "-stop", description = "Stop date (exclusive), format == yyyy-MM-dd")
    private volatile Date stop;
    
    private PrintWriter fos;
    
    private ConcurrentSkipListMap<Integer, Boolean> seconds = new ConcurrentSkipListMap<Integer, Boolean>();
    
    
    /* (non-Javadoc)
     * @see dk.dma.ais.lib.AbstractCommandLineDirectoryReader#run(com.google.inject.Injector)
     */
    @Override
    protected void run(Injector injector) throws Exception {
        
        Integer startTime = (int) (start.getTime()/1000);
        Integer stopTime = (int) (stop.getTime()/1000);
        
        fos = new PrintWriter(new BufferedWriter(new FileWriter(new File(output))));
        
        //initialize a very big collection of seconds
        for (int i=startTime; i< stopTime; i++) {
            seconds.put(i,false);
        }
        
        LOGGER.debug("Initialized HashMap");
        
        super.run(injector);
        
        
        for (Entry<Integer, Boolean> entry : seconds.entrySet()) {
            System.out.println(entry.getKey().toString()+","+entry.getValue().toString());
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


    /* (non-Javadoc)
     * @see dk.dma.ais.lib.AbstractCommandLineDirectoryReader#process(dk.dma.ais.packet.AisPacket)
     */
    @Override
    public void process(AisPacket t) {
        if (t != null && t.getBestTimestamp() > 0) {
            //seconds.put((int) (t.getBestTimestamp()/1000),true);
            seconds.remove((int) (t.getBestTimestamp()/1000));
        }
        
    }

}
