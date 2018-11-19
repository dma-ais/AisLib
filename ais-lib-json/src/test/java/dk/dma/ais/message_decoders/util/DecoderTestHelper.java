package dk.dma.ais.message_decoders.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisDate;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import org.json.JSONException;
import org.json.JSONObject;

public class DecoderTestHelper {

    public static JSONObject getJson(ObjectWriter writer, DecodedAisFieldObject dafo) throws JsonProcessingException, JSONException {
        return new JSONObject(writer.writeValueAsString(dafo));
    }

    public static JSONObject getJson(ObjectWriter writer, DecodedAisPosition dap) throws JsonProcessingException, JSONException {
        return new JSONObject(writer.writeValueAsString(dap));
    }

    public static JSONObject getJson(ObjectWriter writer, DecodedAisDate dad) throws JsonProcessingException, JSONException {
        return new JSONObject(writer.writeValueAsString(dad));
    }

}
