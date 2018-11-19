package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.Decoder;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.ShipType;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage5Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

public class AisMessage5DecoderTest {

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();

        vdm.parse("!AIVDM,2,1,9,B,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09");
        vdm.parse("!AIVDM,2,2,9,B,888888888888880,2*2E");

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage5;

        AisMessage5 aisMessage5 = (AisMessage5) aisMessage;

        AisMessage5Decoder aisMessage5Decoder = new AisMessage5Decoder(aisMessage5);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage5Decoder);

        JSONObject jsonObject = new JSONObject(json);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Station compliant with Recommendation ITU-R M.1371-1")).toString(),
                jsonObject.get("versionDFO").toString(),
                true);

        assertEquals(6514895, jsonObject.getLong("imo"));

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, EPFDFixType.get(1).prettyPrint())).toString(),
                jsonObject.get("posTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(38, "Draught is 3.8 m")).toString(),
                jsonObject.get("draughtDFO").toString(),
                true);

        assertEquals("FORUS", jsonObject.getString("dest").trim());

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getDteDFO(0)).toString(),
                jsonObject.get("dteDFO").toString(),
                true);

        //these are the static common things that are inherited

        assertEquals(jsonObject.getString("callsign").trim(), "LFNA");

        assertEquals(jsonObject.getString("name").trim(), "FALKVIK");

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(79, ShipType.get(79).prettyPrint())).toString(),
                jsonObject.get("shipTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(40, "Distance from GPS antenna to bow 40 m")).toString(),
                jsonObject.get("dimBowDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(10, "Distance from GPS antenna to stern 10 m")).toString(),
                jsonObject.get("dimSternDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(4, "Distance from GPS antenna to port 4 m")).toString(),
                jsonObject.get("dimPortDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(5, "Distance from GPS antenna to starboard 5 m")).toString(),
                jsonObject.get("dimStarboardDFO").toString(),
                true);
    }


    @Test
    public void shouldNotWork() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,B39H9GP0;@H136UI3ijEowU5oP06,0*14" //this is a AisMessage 18
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

    @Test
    public void gimme() throws Exception{
        Vdm vdm = new Vdm();

        vdm.parse("!AIVDM,2,1,9,B,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09");
        vdm.parse("!AIVDM,2,2,9,B,888888888888880,2*2E");

        AisMessage message = AisMessage.getInstance(vdm);
        Decoder decoder = new Decoder(message);
        String json = decoder.decode(true);
        System.out.println(json);
    }

}