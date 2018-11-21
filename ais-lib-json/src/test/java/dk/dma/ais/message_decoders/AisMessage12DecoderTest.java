package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage12Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage12DecoderTest {

    @Test(expected = SentenceException.class)
    public void shouldThrowException()  throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,<02;oP0kKcv0@<address@hidden:?2?EB7PDB16693P381>>5<PikP,0*37" //changes : to ;
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
        assert !(aisMessage instanceof AisMessage12);
    }

    @Test
    public void shouldWork() throws Exception{
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,<5?SIj1;GbD07??4,0*38"
        );
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage12;

        AisMessage12Decoder aisMessage12Decoder = new AisMessage12Decoder((AisMessage12) aisMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage12Decoder);

        JSONObject jsonObject = new JSONObject(json);

        assert jsonObject.getInt("seqNum") == 0;

        assert jsonObject.getLong("destination") == 316123456;

        assert jsonObject.getString("message").equals("GOOD");

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "No retransmission")).toString(),
                jsonObject.get("retransmitDFO").toString(),
                true);
    }

}