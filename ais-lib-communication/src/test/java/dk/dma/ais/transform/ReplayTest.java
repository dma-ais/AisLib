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
package dk.dma.ais.transform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import java.util.function.Consumer;

public class ReplayTest {

    @Test
    public void replayTest() throws IOException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("replay_dump.txt.gz");
        Assert.assertNotNull(url);
        InputStream inputStream = new GZIPInputStream(url.openStream());
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        final ReplayTransformer trans = new ReplayTransformer();
        trans.setSpeedup(100);

        try (AisPacketReader r = new AisPacketReader(inputStream)) {
            r.forEachRemaining(new Consumer<AisPacket>() {
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

        }
    }

}
