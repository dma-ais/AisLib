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
package dk.dma.ais.decode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage6;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.binary.RouteSuggestionReply;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.proprietary.ProprietaryFactory;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.Abm;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Consumer;

public class DecodeTest {

    private static final Logger LOG = LoggerFactory.getLogger(DecodeTest.class);

    /**
     * Test the AisStreamReader class
     * 
     * Notice that inputStream could be any InputStream. TCP, file, pipe etc. For fail tolerant TCP reading use AisTcpReader
     * 
     * @throws IOException
     * @throws InterruptedException
     * 
     */
    // @Test
    public void aisStreamReaderTest() throws IOException, InterruptedException {
        // Make handler instances
        BaseReportHandler baseHandler = new BaseReportHandler();
        PositionHandler posHandler = new PositionHandler();

        // Open input stream
        URL url = ClassLoader.getSystemResource("stream_example.txt");
        Assert.assertNotNull(url);
        InputStream inputStream = url.openStream();
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        // Register handlers
        aisReader.registerHandler(baseHandler);
        aisReader.registerHandler(posHandler);

        // Start thread
        aisReader.start();

        // Wait for thread to finish
        aisReader.join();

        // There should be 70 base stations in file
        Assert.assertEquals("Expected 70 base stations", 70, baseHandler.getBaseStations().size());

        Assert.assertEquals("Expected 54 base station origins", 54, baseHandler.getBaseStationOrigins().size());

    }

    /**
     * Decode all messages in a file Tries to handle proprietary messages
     * 
     * Demonstrates and tests the process of decoding lines into Vdm messages, and the decoding into AIS messages
     * 
     * @throws IOException
     */
    @Test
    public void readLoopTest() throws IOException {
        // Make a list of proprietary handlers

        // Open file
        URL url = ClassLoader.getSystemResource("stream_example.txt");
        Assert.assertNotNull(url);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Assert.assertNotNull(in);
            String line;

            // Prepare message classes
            AisMessage message;
            Vdm vdm = new Vdm();
            LinkedList<IProprietaryTag> tags = new LinkedList<>();

            while ((line = in.readLine()) != null) {

                // Ignore everything else than sentences
                if (!line.startsWith("$") && !line.startsWith("!")) {
                    continue;
                }

                // Check if proprietary line
                if (ProprietaryFactory.isProprietaryTag(line)) {
                    // Try to parse with one the registered factories in
                    // META-INF/services/dk.dma.ais.proprietary.ProprietaryFactory
                    IProprietaryTag tag = ProprietaryFactory.parseTag(line);
                    if (tag != null) {
                        tags.add(tag);
                    }
                    continue;
                }

                // Handle VDM/VDO line
                try {
                    int result = vdm.parse(line);
                    // LOG.info("result = " + result);
                    if (result == 0) {
                        message = AisMessage.getInstance(vdm);
                        Assert.assertNotNull(message);
                        if (tags.size() > 0) {
                            message.setTags(tags);
                        }

                        // Message ready for handling

                    } else if (result == 1) {
                        // Wait for more data
                        continue;
                    } else {
                        LOG.error("Failed to parse line: " + line + " result = " + result);
                        Assert.assertTrue(false);
                    }

                } catch (Exception e) {
                    LOG.info("VDM failed: " + e.getMessage() + " line: " + line + " tag: "
                            + (tags.size() > 0 ? tags.peekLast() : "null"));
                    Assert.assertTrue(false);
                }

                // Create new VDM
                vdm = new Vdm();
                tags.clear();
            }
        }
    }

    @Test
    public void simpleDecodeTest() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("small_example.txt");
        Assert.assertNotNull(url);
        InputStream inputStream = url.openStream();
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        aisReader.start();
        aisReader.join();
    }

    @Test
    public void decodeWithCommentBlocks() throws IOException, InterruptedException {
        // Open input stream
        URL url = ClassLoader.getSystemResource("small_cb_example.txt");
        Assert.assertNotNull(url);
        InputStream inputStream = url.openStream();
        Assert.assertNotNull(inputStream);

        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        aisReader.registerHandler(new Consumer<AisMessage>() {
            @Override
            public void accept(AisMessage message) {
                System.out.println(message.getVdm().getCommentBlock());
            }
        });
        aisReader.start();
        aisReader.join();
    }

    @Test
    public void decodeAbmTest() throws SentenceException, SixbitException {
        String sentence = "!AIABM,1,1,0,219997000,0,12,<>j?1GhlLplPD5CDP6B?=P6BF,0*5F";
        Abm abm = new Abm();
        int result = abm.parse(sentence);
        Assert.assertEquals("ABM parse failed", 0, result);
        Assert.assertEquals("Message ID wrong", 12, abm.getMsgId());
    }

    @Test
    public void decodeRouteReplyAbm() throws SentenceException, SixbitException {
        String sentence = "!AIABM,1,1,1,990219000,0,6,0200<b1,0*16";
        Abm abm = new Abm();
        int result = abm.parse(sentence);
        Assert.assertEquals("ABM parse failed", 0, result);
        AisMessage6 msg6 = (AisMessage6) abm.getAisMessage(377085000, 0, 0);
        RouteSuggestionReply routeSuggestionReply = (RouteSuggestionReply) msg6.getApplicationMessage();
        Assert.assertEquals("ABM parse failed", 1, routeSuggestionReply.getResponse());
    }

    @Test
    public void decodeAbkTest() throws SentenceException, SixbitException {
        String line = "$AIABK,219012679,B,12,1,0*1D";
        Abk abk = new Abk();
        abk.parse(line);
        Assert.assertEquals(abk.getDestination(), 219012679);
        Assert.assertEquals(abk.getChannel().charValue(), 'B');
        Assert.assertEquals(abk.getMsgId(), 12);
        Assert.assertEquals(abk.getSequence(), 1);
        Assert.assertEquals(abk.getResult().getRes(), 0);

        line = "$AIABK,,,8,3,3*54";
        abk = new Abk();
        abk.parse(line);
        Assert.assertEquals(abk.getChannel().charValue(), '\0');
        Assert.assertEquals(abk.getMsgId(), 8);
        Assert.assertEquals(abk.getSequence(), 3);
        Assert.assertEquals(abk.getResult().getRes(), 3);
    }

    //@Test
    public void makeTrackTest() throws IOException, InterruptedException {
        // Open input stream
        // URL url = ClassLoader.getSystemResource("small_cb_example.txt");
        // Assert.assertNotNull(url);
        // InputStream inputStream = url.openStream();
        // Assert.assertNotNull(inputStream);
        String filename = "/Users/oleborup/pride_of_hull_311062900.txt";
        //String filename = "/Users/oleborup/pride_of_rotterdam_244980000.txt";
        FileInputStream inputStream = new FileInputStream(filename);

        final ArrayList<AisPositionMessage> posMessages = new ArrayList<>();

        // Make AIS reader instance
        AisStreamReader aisReader = new AisStreamReader(inputStream);
        aisReader.registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket packet) {
                Date t = packet.getTimestamp();
                if (t == null) {
                    return;
                }
                // Filter on country
                AisPacketTags tags = packet.getTags();
                if (tags == null) {
                    return;
                }
                Country country = tags.getSourceCountry();
                if (country == null) {
                    return;
                }
                if (!country.getTwoLetter().equals("NL") && !country.getTwoLetter().equals("GB")) {
                    return;
                }                
                AisMessage message = packet.tryGetAisMessage();
                if (message == null || !(message instanceof AisPositionMessage)) {
                    return;
                }
                posMessages.add((AisPositionMessage) message);
            }
        });
        aisReader.start();
        aisReader.join();
        inputStream.close();
        System.out.println("Position messages: " + posMessages.size());
        
        ArrayList<String> positions = new ArrayList<>();
        Integer mmsi = null;
        Position lastPos = null;
        Position refPos = Position.create(52.58, 2.3);
        for (AisPositionMessage posMessage : posMessages) {
            Position pos = posMessage.getPos().getGeoLocation();
            if (pos == null) {
                continue;
            }
            if (refPos.rhumbLineDistanceTo(pos) > 500000) {
                continue;
            }
            if (mmsi == null) {
                mmsi = posMessage.getUserId();
            }
            if (lastPos != null) {
                // Downsample on distance
                if (lastPos.rhumbLineDistanceTo(pos) < 1000) {
                    continue;
                }                
            }
            String strPos = String.format(Locale.US, "%f,%f,0", pos.getLongitude(), pos.getLatitude());
            positions.add(strPos);
            lastPos = pos;
        }
        
        System.out.println("Positions in KML: " + positions.size());
        StringBuilder buf = new StringBuilder();
        buf.append("<kml><Document><name>Tracks</name><open>1</open><Folder><name>" + mmsi + "</name>");
        buf.append("<Placemark><name>" + mmsi + "</name><LineString><coordinates>\n");
        buf.append(StringUtils.join(positions, " "));
        buf.append("\n</coordinates></LineString></Placemark></Folder></Document></kml>");
        PrintWriter out = new PrintWriter(filename + ".kml");
        out.print(buf);
        out.close();        
    }

}
