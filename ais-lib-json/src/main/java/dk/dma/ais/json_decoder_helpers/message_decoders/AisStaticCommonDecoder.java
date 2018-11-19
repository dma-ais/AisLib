package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.ShipType;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisStaticCommon;

@SuppressWarnings("unused")
@Decodes(className = AisStaticCommon.class)
public class AisStaticCommonDecoder extends AisMessageDecoder{

    private transient AisStaticCommon aisStaticCommon;

    private String callsign;
    private String name;
    private DecodedAisFieldObject shipTypeDFO;
    private DecodedAisFieldObject dimBowDFO;
    private DecodedAisFieldObject dimSternDFO;
    private DecodedAisFieldObject dimPortDFO;
    private DecodedAisFieldObject dimStarboardDFO;

    public AisStaticCommonDecoder(AisStaticCommon aisStaticCommon) {
        super(aisStaticCommon);
        this.aisStaticCommon = aisStaticCommon;
    }

    //region Getters

    public String getCallsign() {
        return aisStaticCommon.getCallsign();
    }

    public String getName() {
        return aisStaticCommon.getName();
    }

    public DecodedAisFieldObject getShipTypeDFO() {
        int shipType = aisStaticCommon.getShipType();
        return new DecodedAisFieldObject(shipType, ShipType.get(shipType).prettyPrint());
    }

    public DecodedAisFieldObject getDimBowDFO() {
        int dimBow = aisStaticCommon.getDimBow();
        String text;
        if (dimBow == 0) {
            text = "GPS position is not available";
        } else {
            text = "Distance from GPS antenna to bow " + dimBow + " m";
        }
        return new DecodedAisFieldObject(dimBow, text);
    }

    public DecodedAisFieldObject getDimSternDFO() {
        int dimStern = aisStaticCommon.getDimStern();
        String text;
        if (dimStern == 0) {
            text = "Length of ship is " + dimStern + " m";
        } else {
            text = "Distance from GPS antenna to stern " + dimStern + " m";
        }
        return new DecodedAisFieldObject(dimStern, text);
    }

    public DecodedAisFieldObject getDimPortDFO() {
        int dimPort = aisStaticCommon.getDimPort();
        String text;
        if (dimPort == 0) {
            text = "GPS position is not available";
        } else {
            text = "Distance from GPS antenna to port " + dimPort + " m";
        }
        return new DecodedAisFieldObject(dimPort, text);
    }

    public DecodedAisFieldObject getDimStarboardDFO() {
        int dimStarboard = aisStaticCommon.getDimStarboard();
        String text;
        if (dimStarboard == 0) {
            text = "Width of ship is " + dimStarboard + " m";
        } else {
            text = "Distance from GPS antenna to starboard " + dimStarboard + " m";
        }
        return new DecodedAisFieldObject(dimStarboard, text);
    }

    //endregion

    //region Setters

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShipTypeDFO(DecodedAisFieldObject shipTypeDFO) {
        this.shipTypeDFO = shipTypeDFO;
    }

    public void setDimBowDFO(DecodedAisFieldObject dimBowDFO) {
        this.dimBowDFO = dimBowDFO;
    }

    public void setDimSternDFO(DecodedAisFieldObject dimSternDFO) {
        this.dimSternDFO = dimSternDFO;
    }

    public void setDimPortDFO(DecodedAisFieldObject dimPortDFO) {
        this.dimPortDFO = dimPortDFO;
    }

    public void setDimStarboardDFO(DecodedAisFieldObject dimStarboardDFO) {
        this.dimStarboardDFO = dimStarboardDFO;
    }

    //endregion
}
