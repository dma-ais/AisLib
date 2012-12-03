package dk.dma.ais.decode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage6;
import dk.dma.ais.message.binary.RouteSuggestionReply;
import dk.dma.ais.proprietary.GatehouseFactory;
import dk.dma.ais.proprietary.IProprietaryFactory;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.Abm;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

public class DecodeTest {

    private static final Logger LOG = LoggerFactory.getLogger(DecodeTest.class);

    /**
     * Test the AisStreamReader class
     * 
     * Notice that inputStream could be any InputStream. TCP, file, pipe etc. For fail tolerant TCP reading use
     * AisTcpReader
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
        // Register proprietary handler
        aisReader.addProprietaryFactory(new GatehouseFactory());

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
        List<IProprietaryFactory> proprietaryFactories = new ArrayList<>();
        proprietaryFactories.add(new GatehouseFactory());

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
                if (line.indexOf("$P") >= 0) {
                    // Go through factories to find one that fits
                    for (IProprietaryFactory factory : proprietaryFactories) {
                        if (factory.match(line)) {
                            tags.add(factory.getTag(line));
                        }
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

}
