package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.NavaidTypes;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage21Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage21;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;


public class AisMessage21DecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException()  throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDO,2,1,5,B,D1c2;q@b44ah4ah0h:2ab@70VRpU<Bgpm4:gP50HH`Th`QF5,0*7B");
        vdm.parse("!AIVDO,2,2,5,B,1CQ1A83PCAH0,0*60");
        AisMessage.getInstance(vdm);
    }

    @Test
    public void shouldNotWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //this is ais message 19
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisMessage21);
    }

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDO,2,1,5,B,E1c2;q@b44ah4ah0h:2ab@70VRpU<Bgpm4:gP50HH`Th`QF5,0*7B");
        vdm.parse("!AIVDO,2,2,5,B,1CQ1A83PCAH0,0*60");
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage21;

        AisMessage21Decoder aisMessage21Decoder = new AisMessage21Decoder((AisMessage21) aisMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage21Decoder);

        JSONObject jsonObject = new JSONObject(json);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, NavaidTypes.get(1).prettyPrint())).toString(),
                jsonObject.get("atonTypeDFO").toString(),
                true);

        assertEquals("THIS IS A TEST NAME1", jsonObject.getString("name").trim());

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, PositionAccuracy.get(0).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(-38.220166667, 	145.181)).toString(),
                jsonObject.get("posDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(5, "Position A (bow) dim. 5 m")).toString(),
                jsonObject.get("dimBowDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(3, "Position B (stern) dim. 3 m")).toString(),
                jsonObject.get("dimSternDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(3, "Position C (port) dim. 3 m")).toString(),
                jsonObject.get("dimPortDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(5, "Position D (starboard) dim. 5 m")).toString(),
                jsonObject.get("dimStarboardDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, EPFDFixType.get(1).prettyPrint())).toString(),
                jsonObject.get("posTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getUtcSecDFO(9)).toString(),
                jsonObject.get("utcSecDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Off position")).toString(),
                jsonObject.get("offPositionDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Real AtoN at indicated position")).toString(),
                jsonObject.get("virtualDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Station operating in assigned mode")).toString(),
                jsonObject.get("assignedDFO").toString(),
                true);

        assertEquals("EXTENDED NAME", jsonObject.getString("nameExt").trim());
    }
}