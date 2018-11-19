package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage18Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage18DecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException()  throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,B6CdCm0t3`tba35f;V9faHi7kP06,0*58" //changed @ to ;
        );
        AisMessage.getInstance(vdm);
    }

    @Test
    public void shouldNotWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //this is ais message 19
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisMessage18);
    }

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58"
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage18;

        AisMessage18Decoder aisMessage18Decoder = new AisMessage18Decoder((AisMessage18) aisMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage18Decoder);

        JSONObject jsonObject = new JSONObject(json);


        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getSogDFO(14)).toString(),
                jsonObject.get("sogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, PositionAccuracy.get(1).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(40.0052833, 53.0109967)).toString(),
                jsonObject.get("posDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getCogDFO(1770)).toString(),
                jsonObject.get("cogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getTrueHeadingDFO(177)).toString(),
                jsonObject.get("trueHeadingDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getUtcSecDFO(34)).toString(),
                jsonObject.get("utcSecDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Class B CS unit")).toString(),
                jsonObject.get("classBUnitFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1,
                                "Equipped with integrated display displaying Message 12 and 14")).toString(),
                jsonObject.get("classBDisplayFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Equipped with DSC function")).toString(),
                jsonObject.get("classBDscFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1,
                                "Capable of operating over the whole marine band")).toString(),
                jsonObject.get("classBBandFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Frequency management via Message 22")).toString(),
                jsonObject.get("classBMsg22FlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Station operating in autonomous and continuous mode")).toString(),
                jsonObject.get("modeFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "ITDMA communication state follows")).toString(),
                jsonObject.get("commStateSelectorFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(393222, "ITDMA communication state")).toString(),
                jsonObject.get("commStateDFO").toString(),
                true);

    }
}