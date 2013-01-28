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
package dk.dma.ais.packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.reader.AisPacketReader;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.enav.util.function.Consumer;

public class AisPacketTest {

    @Test
    public void readPacketTest() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("small_cb_example.txt");
        Assert.assertNotNull(url);
        InputStream inputStream = url.openStream();
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        // Set the source name
        aisReader.setSourceName("some_file_dump");

        aisReader.registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket aisPacket) {
                System.out.println("--\npacket received:\n" + aisPacket.getStringMessage());

                // Try to get timestamp
                Date timestamp = aisPacket.getTimestamp();
                System.out.println("timestamp: " + timestamp);
                Assert.assertNotNull(timestamp);

                // Try to get AIS message

                try {
                    AisMessage message = aisPacket.getAisMessage();
                    if (message instanceof IPositionMessage) {
                        // Position message
                        ((IPositionMessage) message).getPos();
                    }
                } catch (AisMessageException | SixbitException e) {
                    // Failed to parse AIS message in VDM
                    e.printStackTrace();
                }

            }
        });

        aisReader.start();
        aisReader.join();

    }

    @Test
    public void packetFromStringTest() throws SentenceException, AisMessageException, SixbitException {
        String msg;
        msg = "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n";
        msg += "\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacketReader.from(msg);
        Assert.assertNotNull(packet);
        Assert.assertNotNull(packet.getVdm());
        Assert.assertNotNull(packet.getVdm().getTags());
        AisMessage aisMessage = AisMessage.getInstance(packet.getVdm());
        Assert.assertEquals(aisMessage.getMsgId(), 5);
    }

}
