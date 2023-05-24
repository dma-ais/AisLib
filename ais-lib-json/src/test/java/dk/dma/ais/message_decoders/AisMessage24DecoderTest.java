package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.Decoder;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.ShipType;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage24Decoder;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage5Decoder;
import dk.dma.ais.message.*;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.*;

public class AisMessage24DecoderTest {


    @Test
    public void shouldTestWhenPartNumberIsZero() throws Exception {
        Vdm vdm = new Vdm();

        vdm.parse("!AIVDM,1,1,0,B,H<PJ3GA0504h4m18Dp400000000,2*75");
        System.out.println(vdm.getRawSentences());

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage24;

        AisMessage24 aisMessage24 = (AisMessage24) aisMessage;

        AisMessage24Decoder aisMessage24Decoder = new AisMessage24Decoder(aisMessage24);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage24Decoder);

        JSONObject jsonObject = new JSONObject(json);
        assertEquals(0, jsonObject.getLong("partNumber"));

        assertEquals("PAPALAMPRENA", jsonObject.getString("name"));

        assertEquals(839287645, jsonObject.getLong("userId"));
    }

    @Test
    public void shouldTestWhenPartNumberIsZeroAndRemoveDefaultSymbol() throws Exception {
        Vdm vdm = new Vdm();

        vdm.parse("!AIVDM,1,1,0,B,H<PJ3G@00000000000000000000,2*4D");
        System.out.println(vdm.getRawSentences());

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage24;

        AisMessage24 aisMessage24 = (AisMessage24) aisMessage;

        AisMessage24Decoder aisMessage24Decoder = new AisMessage24Decoder(aisMessage24);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage24Decoder);

        JSONObject jsonObject = new JSONObject(json);
        assertEquals(0, jsonObject.getLong("partNumber"));

        if (jsonObject.has("name") && jsonObject.isNull("name")) {
            assertTrue(jsonObject.has("name"));
            assertTrue(jsonObject.isNull("name"));
        }

        if (jsonObject.has("userId") && jsonObject.isNull("userId")) {
            assertTrue(jsonObject.has("userId"));
            assertTrue(jsonObject.isNull("userId"));
        }
    }


    @Test
    public void shouldTestWhenPartNumberIsOne() throws Exception {
        Vdm vdm = new Vdm();

        vdm.parse("!AIVDM,1,1,0,B,H<PJ3GDtWPPPPPP<7:F000083426,0*24");
        System.out.println(vdm.getRawSentences());

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage24;

        AisMessage24 aisMessage24 = (AisMessage24) aisMessage;

        AisMessage24Decoder aisMessage24Decoder = new AisMessage24Decoder(aisMessage24);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage24Decoder);

        JSONObject jsonObject = new JSONObject(json);

        assertEquals(1, jsonObject.getLong("partNumber"));

        assertEquals("LGJV", jsonObject.getString("callsign"));

        assertEquals(839287645, jsonObject.getLong("userId"));

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(60, "Passenger-all-ships-of-this-type")).toString(),
                jsonObject.get("shipTypeDFO").toString(),
                true);


        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, "Distance from GPS antenna to bow 1 m")).toString(),
                jsonObject.get("dimBowDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(3, "Distance from GPS antenna to stern 3 m")).toString(),
                jsonObject.get("dimSternDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(4, "Distance from GPS antenna to port 4 m")).toString(),
                jsonObject.get("dimPortDFO").toString(),
                true);


        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(2, "Distance from GPS antenna to starboard 2 m")).toString(),
                jsonObject.get("dimStarboardDFO").toString(),
                true);
    }
}
