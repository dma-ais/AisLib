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
package dk.dma.ais.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.configuration.reader.AisFileReaderConfiguration;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.util.function.Consumer;

/**
 * Use AisReader to assert that both zip and gz works
 * 
 * @author Jens Tuxen
 *
 */
public class CompressedAisReaderTest {    
    
    @Test
    public void compressedTest() throws InterruptedException, IOException, URISyntaxException {
        
        final String[] files = {"replay_dump.txt", "replay_dump.txt.tar.gz", "replay_dump.txt.gz", "replay_dump.txt.linux.zip"};
    
        final ArrayList<AtomicInteger> counters = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            counters.add(new AtomicInteger());
        }
        
        
        
        for (int i=0; i<files.length; i++) {
            final int k = i;
            
            Path p = Paths.get(this.getClass().getClassLoader().getResource(files[k]).toURI());
            AisReader aisReader = AisReaders.createReaderFromFile(p.toAbsolutePath().toString());
            
            aisReader.registerPacketHandler(new Consumer<AisPacket>() {

                @Override
                public void accept(AisPacket t) {
                    counters.get(k).incrementAndGet();
                    
                }

            });
            
            aisReader.start();
            aisReader.join();
        }
        //System.out.println(counters.get(0).get());
        Assert.assertTrue(counters.get(0).get() > 10);
        
        for (int i=1; i<files.length; i++) {
            Assert.assertEquals(counters.get(0).get(), counters.get(i).get());
        }
        
        
        
    }

}
