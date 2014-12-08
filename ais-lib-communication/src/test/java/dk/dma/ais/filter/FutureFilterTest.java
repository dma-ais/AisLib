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
package dk.dma.ais.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import dk.dma.ais.packet.AisTestPackets;
import dk.dma.ais.reader.AisDirectoryReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;

public class FutureFilterTest {
    @Ignore
    @Test
    public void liveStreamTest() throws InterruptedException {
        AisTcpReader reader1 = AisReaders.createReader("10.33.128.173:25000");

        final FutureFilter f = new FutureFilter();
        
        
        reader1.registerPacketHandler(packet -> {
            if (packet != null && f.rejectedByFilter(packet)) {
                System.out.println("Message was rejected");
                System.out.println("Current Date: "+new Date());
                System.out.println("Message Date:"+new Date(packet.getBestTimestamp()));
                
                
                System.out.println(packet.getStringMessage());
                
                
               
                
            }
        });
        reader1.start();
        reader1.join();

    }
    
    @Test
    public void testDatesNotInFuture() throws IOException, InterruptedException {
        
        final FutureFilter f = new FutureFilter();
        
        assertFalse(f.rejectedByFilter(AisTestPackets.p1()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p2()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p3()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p4()));
        
        AisDirectoryReader directoryReader = AisReaders.createDirectoryReader("src/test", "stream_example.txt", true);
        
        directoryReader.registerPacketHandler(packet -> assertFalse(f.rejectedByFilter(packet)));
        
        directoryReader.start();
        directoryReader.join();
        
    }

}
