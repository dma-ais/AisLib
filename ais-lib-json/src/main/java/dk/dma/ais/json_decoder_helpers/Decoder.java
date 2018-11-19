package dk.dma.ais.json_decoder_helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessageDecoder;
import dk.dma.ais.json_decoder_helpers.util.DecoderGrabbingException;
import dk.dma.ais.json_decoder_helpers.util.MessageDecoderMapSingleton;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * This decoder class allows a user to pass in an Ais Message or string to be decoded and returns the message in a
 * human readable JSON format that decodes all enums and flags.
 */
public class Decoder {
    private Logger logger = LoggerFactory.getLogger(Decoder.class);

    private Vdm vdm;
    private AisMessage aisMessage;

    private static final String INPUT_QUEUE_NAME = "noms.data.streamer";
    static String delimiter = "\r\n";

    /**
     * You can pass in an AIS sentenceLine and it will be decoded
     * @param aisSentenceString a single message to decode, if it is a multiple line message you can separate them
     *                        with '\r\n' or use the other constructor     *
     */
    public Decoder(String aisSentenceString) throws SixbitException, SentenceException, AisMessageException {
        aisMessage = parseMessageFromSentence(aisSentenceString);
    }

    /**
     * Here you may pass in multiple line messages individually to return a single decoded string
     * @param aisSentenceStrings many strings that represent one AisMessage
     */
    public Decoder(String... aisSentenceStrings) throws SixbitException, SentenceException, AisMessageException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, aisSentenceStringsLength = aisSentenceStrings.length; i < aisSentenceStringsLength; i++) {
            String sentence = aisSentenceStrings[i];
            if (i != 0) {
                sb.append("\r\n");
            }
            sb.append(sentence);
        }
        aisMessage = parseMessageFromSentence(sb.toString());
    }

    /**
     * You may also pass in an AisMessage into this constructor in order to decode it into a human readable JSON
     * @param aisMessage any of the {@link dk.dma.ais.message.AisMessage} Types that are supported. At this time
     *                   the Types supported are AisMessages {@link dk.dma.ais.message.AisMessage1},
     *                   {@link dk.dma.ais.message.AisMessage2}, {@link dk.dma.ais.message.AisMessage3},
     *                   {@link dk.dma.ais.message.AisMessage4}, {@link dk.dma.ais.message.AisMessage5},
     *                   {@link dk.dma.ais.message.AisMessage9}, {@link dk.dma.ais.message.AisMessage11},
     *                   {@link dk.dma.ais.message.AisMessage12}, {@link dk.dma.ais.message.AisMessage17},
     *                   {@link dk.dma.ais.message.AisMessage18}, {@link dk.dma.ais.message.AisMessage19},
     *                   {@link dk.dma.ais.message.AisMessage21}, {@link dk.dma.ais.message.AisMessage27},
     *                   {@link dk.dma.ais.message.AisPositionMessage}, {@link dk.dma.ais.message.AisStaticCommon}
     *                   and {@link dk.dma.ais.message.UTCDateResponseMessage}
     */
    public Decoder(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

    public AisMessage getAisMessage() {
        return aisMessage;
    }

    /**
     * This does the decoding and returns the JSON string
     * @param pretty true - pretty print the json, false leave it compact
     * @return Either the JSON string of the object or null if the decoding failed
     */
    public String decode(boolean pretty) {
        try {
            AisMessageDecoder decoder = MessageDecoderMapSingleton.getInstance().getDecoderForMessage(aisMessage);

            if (decoder != null) { //This happens if there is an unsupported message type
                if (pretty) {

                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(decoder);

                } else {

                    ObjectWriter objectWriter = new ObjectMapper().writer();
                    return objectWriter.writeValueAsString(decoder);

                }
            }
        } catch (DecoderGrabbingException e) {
            logger.warn("Got the AIS Message but could not get the corresponding decoder");
        } catch (JsonProcessingException e) {
            logger.warn("There was an error in parsing the AisMessage as a JSON");
        }
        return null;
    }

    /**
     *
     * @param sentence the sentence in the AIS format (for example !AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30)
     * @return The {@link dk.dma.ais.message.AisMessage} object that can be of any message type
     * @throws AisMessageException thrown if there is a parsing error in the AisMessage
     * @throws SixbitException thrown if there is a parsing error in the AisMessage
     */
    private AisMessage parseMessageFromSentence(String sentence) throws AisMessageException, SixbitException {
        vdm = new Vdm();

        List<String> sentences = Arrays.asList(sentence.split(delimiter));
        sentences.forEach(line -> {
            try {
                vdm.parse(line);
            } catch (SentenceException e) {
                logger.warn("Could not parse out the multiple line message");
            }
        });

        return AisMessage.getInstance(vdm);
    }

}
