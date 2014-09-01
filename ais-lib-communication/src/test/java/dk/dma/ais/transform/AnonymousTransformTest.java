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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import java.util.function.Consumer;

public class AnonymousTransformTest implements Consumer<AisPacket> {

    private final AnonymousTransformer anonymizer = new AnonymousTransformer();

    @Override
    public void accept(AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return;
        }
        System.out.println("---");
        System.out.println("original packet  :\n" + packet.getStringMessage());
        System.out.println("original message :\n" + message);
        AisPacket newPacket = anonymizer.transform(packet);
        AisMessage newMessage = null;
        if (newPacket != null) {
            newMessage = newPacket.tryGetAisMessage();
            newMessage = newPacket.tryGetAisMessage();
            Assert.assertNotNull(newMessage);
            Assert.assertEquals(message.getMsgId(), newMessage.getMsgId());
        }
        System.out.println("new packet       :\n" + (newPacket != null ? newPacket.getStringMessage() : null));
        System.out.println("new message      :\n" + newMessage);
    }

    @Test
    public void anonymizeTest() throws IOException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("replay_dump.txt.gz");
        Assert.assertNotNull(url);
        InputStream inputStream = new GZIPInputStream(url.openStream());
        Assert.assertNotNull(inputStream);

        try (AisPacketReader r = new AisPacketReader(inputStream)) {
            r.forEachRemaining(this);
        }
    }

}
