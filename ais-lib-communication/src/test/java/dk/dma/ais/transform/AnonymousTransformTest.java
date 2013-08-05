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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.util.function.Consumer;

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
    public void anonymizeTest() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("replay_dump.txt.gz");
        Assert.assertNotNull(url);
        InputStream inputStream = new GZIPInputStream(url.openStream());
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisReader aisReader = AisReaders.createReaderFromInputStream(inputStream);
        // AisReader aisReader = new AisTcpReader("ais163.sealan.dk:65262");

        aisReader.registerPacketHandler(this);
        aisReader.start();
        aisReader.join();
    }

}
