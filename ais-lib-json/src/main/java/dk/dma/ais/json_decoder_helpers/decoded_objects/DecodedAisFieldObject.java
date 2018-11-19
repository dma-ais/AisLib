package dk.dma.ais.json_decoder_helpers.decoded_objects;

@SuppressWarnings("unused")
public class DecodedAisFieldObject {

    private Number ais_value;
    private String decoded_text;

    public DecodedAisFieldObject(Number ais_value, String decoded_text) {
        this.ais_value = ais_value;
        this.decoded_text = decoded_text;
    }

    public Number getAis_value() {
        return ais_value;
    }

    public void setAis_value(Number ais_value) {
        this.ais_value = ais_value;
    }

    public String getDecoded_text() {
        return decoded_text;
    }

    public void setDecoded_text(String decoded_text) {
        this.decoded_text = decoded_text;
    }
}
