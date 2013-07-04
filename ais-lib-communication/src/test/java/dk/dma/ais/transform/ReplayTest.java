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
package dk.dma.ais.transform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.enav.util.function.Consumer;

public class ReplayTest {
    
    @Test
    public void replayTest() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("replay_dump.txt.gz");
        Assert.assertNotNull(url);
        InputStream inputStream = new GZIPInputStream(url.openStream());
        Assert.assertNotNull(inputStream);
        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        
        final ReplayTransformer trans = new ReplayTransformer();
        trans.setSpeedup(100);
        
        aisReader.registerPacketHandler(new Consumer<AisPacket>() {
            Long origOffset;            
            @Override
            public void accept(AisPacket aisPacket) {
                trans.transform(aisPacket);
                long now = System.currentTimeMillis();
                long ts = aisPacket.getTimestamp().getTime();
                if (origOffset == null) {
                    origOffset = now - ts;
                }
                long offset = now - ts;                
                long diff = offset - origOffset;
                System.out.println("diff: " + diff);
                System.out.println(aisPacket.getStringMessage());
            }
        });
        
        aisReader.start();
        aisReader.join();        
    }

}
