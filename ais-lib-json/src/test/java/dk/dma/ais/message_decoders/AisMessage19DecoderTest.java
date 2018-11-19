package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.message_decoders.AisMessage19Decoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AisMessage19DecoderTest {

    @Test
    public void shouldWork() throws Exception {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B" //AisMessage19
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof AisMessage19;

        AisMessage19 aisMessage19 = (AisMessage19) aisMessage;

        AisMessage19Decoder aisMessage19Decoder = new AisMessage19Decoder(aisMessage19);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(aisMessage19Decoder);

        JSONObject jsonObject = new JSONObject(json);

        AisStaticCommonDecoderTest.shouldWork(jsonObject, objectWriter);
//        assert (Integer)jsonObject.get("msgId") == 1;

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getSogDFO(87)).toString(),
                jsonObject.get("sogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, PositionAccuracy.get(0).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(29.543695, -88.81039167)).toString(),
                jsonObject.get("posDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getCogDFO(3359)).toString(),
                jsonObject.get("cogDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getTrueHeadingDFO(511)).toString(),
                jsonObject.get("trueHeadingDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getUtcSecDFO(46)).toString(),
                jsonObject.get("utcSecDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, EPFDFixType.get(1).prettyPrint())).toString(),
                jsonObject.get("posTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        CommonFieldDecoderHelper.getDteDFO(0)).toString(),
                jsonObject.get("dteDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, "Station operating in autonomous and continuous mode")).toString(),
                jsonObject.get("modeFlagDFO").toString(),
                true);

    }


    @Test
    public void shouldNotWork() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,402R3WiuH@jaaPtgjhOgcA7000S:,0*7D" //this is a AisMessage 4
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof AisMessage19);
    }

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,2,1,0,C,C8u:8C@t7@TnGCKfm6Po`e6N`:Va0L2J;06HV50JV?SjBPL3,0*28" //changed the B to C
        );

        AisMessage.getInstance(vdm);
    }

}