package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage17Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage17;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage17DecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDM,2,1,5,A,B02VqLPA4I6C07h5Ed1h<OrsuBTTwS?r:C?w`?la<gno1RTRwSP9:BcurA8a,0*3A");
        vdm.parse("!AIVDM,2,2,5,A,:Oko02TSwu8<:Jbb,0*11");
        AisMessage.getInstance(vdm);
    }

    @Test
    public void shouldNotWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //this is ais message 19
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisMessage17);
    }

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDM,2,1,5,A,A02VqLPA4I6C07h5Ed1h<OrsuBTTwS?r:C?w`?la<gno1RTRwSP9:BcurA8a,0*3A");
        vdm.parse("!AIVDM,2,2,5,A,:Oko02TSwu8<:Jbb,0*11");
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage17;

        AisMessage17 aisMessage17 = (AisMessage17) aisMessage;

        AisMessage17Decoder aisMessage17Decoder = new AisMessage17Decoder(aisMessage17);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage17Decoder);

        JSONObject jsonObject = new JSONObject(json);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(17478, "Longitude is 1898110800")).toString(),
                jsonObject.get("lon").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(35992, "Latitude is 1965163200")).toString(),
                jsonObject.get("lat").toString(),
                true);
    }

}