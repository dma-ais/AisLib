package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.enums.SpecialManoeuvreIndicator;
import dk.dma.ais.json_decoder_helpers.enums.SyncState;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisPositionMessageDecoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisPositionMessageDecoderTest {

    @Test
    public void shouldWork() throws AisMessageException, SixbitException, SentenceException, JsonProcessingException, JSONException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30"
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisPositionMessage;

        AisPositionMessage aisPositionMessage = (AisPositionMessage) aisMessage;

        AisPositionMessageDecoder aisPositionMessageDecoder = new AisPositionMessageDecoder(aisPositionMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisPositionMessageDecoder);

        JSONObject jsonObject = new JSONObject(json);

//        assert (Integer)jsonObject.get("msgId") == 1;

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, NavigationalStatus.get(0).prettyStatus())).toString(),
                jsonObject.get("navStatusDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Turning right at 0.0 degrees/ min")).toString(),
                jsonObject.get("rotDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getSogDFO(171)).toString(),
                jsonObject.get("sogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, PositionAccuracy.get(0).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(36.8121133, 21.3901667)).toString(),
                jsonObject.get("position").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getCogDFO(3136)).toString(),
                jsonObject.get("cogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getTrueHeadingDFO(315)).toString(),
                jsonObject.get("trueHeadingDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getUtcSecDFO(8)).toString(),
                jsonObject.get("utcSecDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, SpecialManoeuvreIndicator.get(0).prettyPrint())).toString(),
                jsonObject.get("specialManIndicatorDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, SyncState.get(0).prettyPrint())).toString(),
                jsonObject.get("syncStateDFO").toString(),
                true);
    }


    @Test
    public void shouldNotWork() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,402R3WiuH@jaaPtgjhOgcA7000S:,0*7D" //this is a AisMessage 4
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisPositionMessage);
    }

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,181:Jqh02c1Qra`E46I<@9n@059l,0*30" //changed the A to B
        );

        AisMessage.getInstance(vdm);
    }

}