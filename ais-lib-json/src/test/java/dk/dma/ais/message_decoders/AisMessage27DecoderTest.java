package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage27Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage27;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage27DecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException()  throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,J5DfMB9FLsM?P00d,0*70" //changed K to J
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
        assert !(aisMessage instanceof AisMessage27);
    }

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,K5DfMB9FLsM?P00d,0*70"
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage27;

        AisMessage27Decoder aisMessage27Decoder = new AisMessage27Decoder((AisMessage27) aisMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage27Decoder);

        JSONObject jsonObject = new JSONObject(json);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, PositionAccuracy.get(1).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(5, NavigationalStatus.get(5).prettyStatus())).toString(),
                jsonObject.get("navStatusDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(-37.653333333, 176.1816667)).toString(),
                jsonObject.get("posDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getSogDFOMessage27(0)).toString(),
                jsonObject.get("sogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getCogDFOMessage27(11)).toString(),
                jsonObject.get("cogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Position is the current GNSS position")).toString(),
                jsonObject.get("gnssPosStatusDFO").toString(),
                true);
    }
}