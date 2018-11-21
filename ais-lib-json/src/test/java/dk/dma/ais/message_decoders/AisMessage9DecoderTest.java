package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.enums.SyncState;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage9Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage9;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage9DecoderTest {

    @Test
    public void shouldNotWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,B6CdCm0t3`tba35f@V9faHi7kP06,0*58" //this is AisMessage 18
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisMessage9);
    }

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDO,1,1,,A,95M2;Q@41Tr4L4H@eRvQ;2h20000,0*0D"
        );

        AisMessage.getInstance(vdm);
    }

    @Test
    public void shouldWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDO,1,1,,A,95M2oQ@41Tr4L4H@eRvQ;2h20000,0*0D"
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage9;

        AisMessage9Decoder aisMessage9Decoder = new AisMessage9Decoder((AisMessage9) aisMessage);
        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage9Decoder);

        JSONObject jsonObject = new JSONObject(json);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(16, "Altitude is 16 m")).toString(),
                jsonObject.get("altitudeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getSogDFO(100)).toString(),
                jsonObject.get("sogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, PositionAccuracy.get(1).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(29.20575, -82.91646)).toString(),
                jsonObject.get("posDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getCogDFO(300)).toString(),
                jsonObject.get("cogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getUtcSecDFO(11)).toString(),
                jsonObject.get("utcSecDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getDteDFO(1)).toString(),
                jsonObject.get("dteDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Station operating in autonomous and continuous mode")).toString(),
                jsonObject.get("assignedDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "SOTDMA communication state follows")).toString(),
                jsonObject.get("commStateSelectorFlagDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, SyncState.get(0).prettyPrint())).toString(),
                jsonObject.get("syncStateDFO").toString(),
                true);

    }

}