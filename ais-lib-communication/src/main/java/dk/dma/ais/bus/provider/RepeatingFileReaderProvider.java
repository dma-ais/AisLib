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
package dk.dma.ais.bus.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.ReplayTransformer;
import dk.dma.enav.util.function.Consumer;

/**
 * Provider that reads repeatedly from the same file 
 */
@ThreadSafe
public class RepeatingFileReaderProvider extends AisBusProvider implements Consumer<AisPacket>, Runnable {
    
    private static final Logger LOG = LoggerFactory.getLogger(RepeatingFileReaderProvider.class);
    
    /**
     * The AIS reader to provide packets
     */
    private AtomicReference<AisReader> aisReader = new AtomicReference<>();
    private final String filename;
    private final boolean gzip;
    
    public RepeatingFileReaderProvider(String filename, boolean gzip) {
        this.filename = filename;
        this.gzip = gzip;
    }
    
    @Override
    public void start() {
        super.start();
        Thread t = new Thread(this);
        setThread(t);
        t.start();
    };

    @Override
    public void cancel() {
        getThread().interrupt();
        AisReader reader = aisReader.get();
        if (reader != null) {
            reader.stopReader();
            try {
                reader.join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            getThread().join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
    }
    
    @Override
    public void run() {
        InputStream stream;
        while (true) {  
            try {
                stream = new FileInputStream(filename);
                if (gzip) {
                    stream = new GZIPInputStream(stream);
                }                
            } catch (IOException e) {
                if (!getThread().isInterrupted()) {
                    LOG.error("Failed to open stream: " + e.getMessage());
                }
                return;
            }
            aisReader.set(new AisStreamReader(stream));            
            aisReader.get().registerPacketHandler(this);
            aisReader.get().start();
            try {
                aisReader.get().join();
            } catch (InterruptedException e) {
                return;
            }
            
            try {
                stream.close();
            } catch (IOException e) {
            }
            
            // Special handling for possible replay transformers that needs to be reset
            for (IAisPacketTransformer transformer : getPacketTransformers()) {
                if (transformer instanceof ReplayTransformer) {
                    ((ReplayTransformer) transformer).reset();
                }                
            }
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }
            
        }
        
    }

    /**
     * Receive packet from reader
     */
    @Override
    public void accept(AisPacket packet) {
        // Push to bus
        push(packet);        
    }

}
