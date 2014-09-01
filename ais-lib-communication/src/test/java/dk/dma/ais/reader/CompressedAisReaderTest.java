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
package dk.dma.ais.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import java.util.function.Consumer;

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
