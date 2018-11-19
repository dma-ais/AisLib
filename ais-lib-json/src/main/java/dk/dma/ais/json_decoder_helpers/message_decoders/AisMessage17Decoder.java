package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage17;

@SuppressWarnings("unused")
@Decodes(className = AisMessage17.class)
public class AisMessage17Decoder  extends AisMessageDecoder{

    private transient AisMessage17 aisMessage17;

    private DecodedAisFieldObject lon;
    private DecodedAisFieldObject lat;
//    private DecodedAisFieldObject payload; //Obsolete RTCM2 protocol
//    private DecodedAisFieldObject spare1; //Not used
//    private DecodedAisFieldObject spare2; //Not used

    public AisMessage17Decoder(AisMessage17 aisMessage17) {
        super(aisMessage17);
        this.aisMessage17 = aisMessage17;
    }

    //region Getters

    public DecodedAisFieldObject getLon() {
        int lon = aisMessage17.getLon();
        String text;
        if (lon == 181) {
            text = "No position information";
        } else {
            int val = lon * 181 * 60 * 10;
            text = "Longitude is " + val;
        }
        return new DecodedAisFieldObject(lon, text);
    }

    public DecodedAisFieldObject getLat() {
        int lat = aisMessage17.getLat();
        String text;
        if (lat == 91) {
            text = "No position information";
        } else {
            int val = lat * 91 * 60 * 10;
            text = "Latitude is " + val;
        }
        return new DecodedAisFieldObject(lat, text);
    }

    //endregion

    //region Setters

    public void setLon(DecodedAisFieldObject lon) {
        this.lon = lon;
    }

    public void setLat(DecodedAisFieldObject lat) {
        this.lat = lat;
    }

    //endregion
}
