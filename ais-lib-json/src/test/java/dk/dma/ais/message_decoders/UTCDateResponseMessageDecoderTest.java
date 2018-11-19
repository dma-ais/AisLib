package dk.dma.ais.message_decoders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisDate;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.*;
import dk.dma.ais.json_decoder_helpers.message_decoders.UTCDateResponseMessageDecoder;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.UTCDateResponseMessage;
import dk.dma.ais.message_decoders.util.DecoderTestHelper;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class UTCDateResponseMessageDecoderTest {

    @Test
    public void shouldWork() throws AisMessageException, SixbitException, SentenceException, JsonProcessingException, JSONException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,402R3WiuH@jaaPtgjhOgcA7000S:,0*7D"
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert aisMessage instanceof UTCDateResponseMessage;

        UTCDateResponseMessage utcDateResponseMessage = (UTCDateResponseMessage) aisMessage;

        UTCDateResponseMessageDecoder utcDateResponseMessageDecoder = new UTCDateResponseMessageDecoder(utcDateResponseMessage);

        ObjectWriter objectWriter = new ObjectMapper().writer();

        String json = objectWriter.writeValueAsString(utcDateResponseMessageDecoder);

        JSONObject jsonObject = new JSONObject(json);

//        assert (Integer)jsonObject.get("msgId") == 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(2006, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 41);
        calendar.set(Calendar.SECOND, 41);

        Date date = calendar.getTime();

        JSONObject actualJsonObject = new JSONObject(jsonObject.get("calendarDateDFO").toString());
        String actual = actualJsonObject.get("textDate").toString();

        JSONObject expectedJsonObject = new JSONObject(DecoderTestHelper.getJson(objectWriter,
                new DecodedAisDate(date)).toString());
        String expected = expectedJsonObject.get("textDate").toString();

        assertEquals(expected, actual);


        // Could not do this test because I was parsing a different date every time form the data
//        JSONAssert.assertEquals(
//                DecoderTestHelper.getJson(objectWriter,
//                        new DecodedAisDate(date)).toString(),
//                jsonObject.get("calendarDateDFO").toString(),
//                false);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(1, PositionAccuracy.get(1).prettyPrint())).toString(),
                jsonObject.get("posAccDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisPosition(55.478300, 13.2703333)).toString(),
                jsonObject.get("positionDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(7, EPFDFixType.get(7).prettyPrint())).toString(),
                jsonObject.get("posTypeDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, TransmissionControl.get(0).prettyPrint())).toString(),
                jsonObject.get("transmissionControlDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, SyncState.get(0).prettyPrint())).toString(),
                jsonObject.get("syncStateDFO").toString(),
                true);

        JSONAssert.assertEquals(
                DecoderTestHelper.getJson(objectWriter,
                        new DecodedAisFieldObject(0, Raim.get(0).prettyPrint())).toString(),
                jsonObject.get("raimDFO").toString(),
                true);

    }

    @Test
    public void shouldNotWork() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30" //this is a AisMessage 1
        );

        AisMessage aisMessage = AisMessage.getInstance(vdm);
        assert !(aisMessage instanceof UTCDateResponseMessage);
    }

    @Test(expected = SentenceException.class)
    public void shouldThrowException() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,402R3WiuH@jaaPtgjhOgcA7000S:,0*7C" //changed the D to C
        );

        AisMessage.getInstance(vdm);
    }
}