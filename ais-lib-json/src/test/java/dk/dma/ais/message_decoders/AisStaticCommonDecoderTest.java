package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.ShipType;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisStaticCommonDecoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisStaticCommonDecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,E5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //changed C to E
        );
        AisMessage.getInstance(vdm);
    }

    @Test
    public void shouldNotWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,<5?SIj1;GbD07??4,0*38" //this is ais message 12
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisStaticCommon);
    }

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //This is message 19
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisStaticCommon;

        AisStaticCommonDecoder aisStaticCommonDecoder = new AisStaticCommonDecoder((AisStaticCommon) aisMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisStaticCommonDecoder);

        JSONObject jsonObject = new JSONObject(json);

        shouldWork(jsonObject, objectWriter);
    }

    public static void shouldWork(JSONObject jsonObject, ObjectWriter objectWriter) throws Exception{

        assert jsonObject.get("callsign") == JSONObject.NULL;

        assert jsonObject.getString("name").equals("CAPT.J.RIMES@@@@@@@@");

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(70, ShipType.get(70).prettyPrint())).toString(),
                jsonObject.get("shipTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(5, "Distance from GPS antenna to bow 5 m")).toString(),
                jsonObject.get("dimBowDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(21, "Distance from GPS antenna to stern 21 m")).toString(),
                jsonObject.get("dimSternDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(4, "Distance from GPS antenna to port 4 m")).toString(),
                jsonObject.get("dimPortDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(4, "Distance from GPS antenna to starboard 4 m")).toString(),
                jsonObject.get("dimStarboardDFO").toString(),
                true);
    }

}