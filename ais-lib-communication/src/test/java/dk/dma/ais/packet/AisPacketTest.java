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
package dk.dma.ais.packet;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AisPacketTest {

    @Test
    public void readPacketTest() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("small_cb_example.txt");
        Assert.assertNotNull(url);
        InputStream inputStream = url.openStream();
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisReader aisReader = AisReaders.createReaderFromInputStream(inputStream);
        // Set the source name
        aisReader.setSourceId("some_file_dump");

        aisReader.registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket aisPacket) {
                System.out.println("--\npacket received:\n" + aisPacket.getStringMessage());

                // Try to get timestamp
                Date timestamp = aisPacket.getTimestamp();
                System.out.println("timestamp: " + timestamp);
                Assert.assertNotNull(timestamp);

                // Get tagging
                AisPacketTags tagging = aisPacket.getTags();
                Assert.assertEquals(tagging.getSourceId(), "some_file_dump");

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
    public void retryTest() throws IOException {

        try (AisPacketReader r = AisPacketReader.createFromSystemResource("retry_example.txt", false)) {
            r.forEachRemaining(new Consumer<AisPacket>() {
                final int[] senders = { 563510000, 211235220, 2655619, 246250000, 205634000, 211462260 };
                int count;

                @Override
                public void accept(AisPacket aisPacket) {
                    AisMessage message = null;
                    try {
                        message = aisPacket.getAisMessage();
                    } catch (AisMessageException | SixbitException e) {
                        Assert.fail(e.getMessage());
                    }
                    Assert.assertEquals(senders[count++], message.getUserId());
                }
            });
        }
    }

    @Test
    public void packetFromStringTest() throws SentenceException, AisMessageException, SixbitException {
        String msg;
        msg = "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n";
        msg += "\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacket.readFromString(msg);
        Assert.assertNotNull(packet);
        Assert.assertNotNull(packet.getVdm());
        Assert.assertNotNull(packet.getVdm().getTags());
        AisMessage aisMessage = AisMessage.getInstance(packet.getVdm());
        Assert.assertEquals(aisMessage.getMsgId(), 5);
    }

    @Test
    public void packetTaggingTest() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n";
        msg += "\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacket.readFromString(msg);
        AisPacketTags tags = packet.getTags();
        Assert.assertEquals(tags.getSourceId(), null);
        Assert.assertEquals(tags.getSourceCountry().getThreeLetter(), "NLD");
        Assert.assertEquals(tags.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(tags.getSourceBs(), null);
        Assert.assertEquals(tags.getSourceType(), null);

        msg = "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n";
        msg += "\\si:AISD*3F\\\r\n";
        msg += "\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        packet = AisPacket.readFromString(msg);
        tags = packet.getTags();
        Assert.assertEquals(tags.getSourceId(), "AISD");
        Assert.assertEquals(tags.getSourceCountry().getThreeLetter(), "NLD");
        Assert.assertEquals(tags.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(tags.getSourceBs(), null);
        Assert.assertEquals(tags.getSourceType(), null);

        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\si:AISD,sb:2190048,sc:SWE,st:SAT*1E\\\r\n";
        msg += "\\g:1-2-0136,c:1354725824*22\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        packet = AisPacket.readFromString(msg);
        tags = packet.getTags();
        Assert.assertEquals(tags.getSourceId(), "AISD");
        Assert.assertEquals(tags.getSourceCountry().getThreeLetter(), "SWE");
        Assert.assertEquals(tags.getTimestamp().getTime(), 1354725824000L);
        Assert.assertEquals(tags.getSourceBs().intValue(), 2190048);
        Assert.assertEquals(tags.getSourceType(), SourceType.SATELLITE);
    }

    @Test
    public void packetVdmCanBeUsedToCreateMessage() throws SentenceException, AisMessageException, SixbitException {
        String sentence = "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E";

        Vdm vdm = new Vdm();
        vdm.parse(sentence);

        AisMessage messageFromDirectVdm = AisMessage.getInstance(vdm);

        assertThat(messageFromDirectVdm, is(not(nullValue())));

        AisPacket packet = AisPacket.readFromString(sentence);
        AisMessage msg = packet.tryGetAisMessage();

        Vdm packetVdm = msg.getVdm();
        AisMessage messageFromPacketVdm = AisMessage.getInstance(packetVdm);

        assertThat(messageFromPacketVdm, is(not(nullValue())));
    }
}
