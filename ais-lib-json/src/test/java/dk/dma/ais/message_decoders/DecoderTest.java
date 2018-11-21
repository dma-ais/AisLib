package dk.dma.ais.message_decoders;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.Decoder;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessageDecoder;
import dk.dma.ais.json_decoder_helpers.util.DecoderGrabbingException;
import dk.dma.ais.json_decoder_helpers.util.MessageDecoderMapSingleton;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.sentence.SentenceException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DecoderTest {
    private Logger logger = LoggerFactory.getLogger(DecoderTest.class);

    private Decoder decoder;
    List<String> aisSentences;

    @Before
    public void setup() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Objects.requireNonNull(
                getClass().getClassLoader().getResource("ais250.txt")).getFile()));

        readInData(br);
    }

    private void readInData(BufferedReader br) throws IOException {
        String line;
        aisSentences = new ArrayList<>();
        while((line = br.readLine()) != null ) {
            aisSentences.add(line);
        }
        br.close();
    }

    @Test
    public void testGetDecoder() {
        List<AisMessage> aisMessages = new ArrayList<>();
        List<AisMessageDecoder> aisMessageDecoders = new ArrayList<>();

        AtomicInteger parsingErrors = new AtomicInteger();
        List<String> msgIdsFailed = new ArrayList<>();

        aisSentences.forEach(sentence -> {
            try {
                decoder = new Decoder(sentence);
                aisMessages.add(decoder.getAisMessage());
            } catch (SixbitException | AisMessageException | SentenceException e) {
                logger.warn("AisMessage could not be parsed from sentence");
                parsingErrors.getAndIncrement();
            }
        });

        aisMessages.forEach(msg -> {
            try {
                aisMessageDecoders.add(MessageDecoderMapSingleton.getInstance().getDecoderForMessage(msg));
            } catch (DecoderGrabbingException e) {
                msgIdsFailed.add(Integer.toString(msg.getMsgId()));
            }
        });

        assert !msgIdsFailed.contains(Integer.toString(1));  //covered by AisPositionMessageDecoder
        assert !msgIdsFailed.contains(Integer.toString(2));  //covered by AisPositionMessageDecoder
        assert !msgIdsFailed.contains(Integer.toString(3));  //covered by AisPositionMessageDecoder
        assert !msgIdsFailed.contains(Integer.toString(4));  //covered by UTCDateResponseMessageDecoder
        assert !msgIdsFailed.contains(Integer.toString(5));
        assert !msgIdsFailed.contains(Integer.toString(9));
        assert !msgIdsFailed.contains(Integer.toString(11)); //covered by UTCDateResponseMessageDecoder
        assert !msgIdsFailed.contains(Integer.toString(12));
        assert !msgIdsFailed.contains(Integer.toString(17));
        assert !msgIdsFailed.contains(Integer.toString(18));
        assert !msgIdsFailed.contains(Integer.toString(19));
        assert !msgIdsFailed.contains(Integer.toString(21));
        assert !msgIdsFailed.contains(Integer.toString(27));
        System.out.println("Ais Message Decoders has: " + aisMessageDecoders.size() + " elements");
        System.out.println("Failed to Id: " + msgIdsFailed.size() + " elements");

        assertEquals(aisMessages.size(), aisMessageDecoders.size() + msgIdsFailed.size());
    }
}
