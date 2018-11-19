package dk.dma.ais.json_decoder_helpers.decoded_objects;

import dk.dma.ais.message.AisPosition;

import java.math.BigDecimal;
import java.math.MathContext;

@SuppressWarnings("unused")
public class DecodedAisPosition {

    private Double latitude;
    private Double longitude;

    public DecodedAisPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Put the correct precision in, it should be 7 digits after the decimal.
     * NOTE: the 1 in the addition is for the period
     * @param aisPosition the AisPosition object to decode
     */
    public DecodedAisPosition(AisPosition aisPosition) {

        int latNeg = aisPosition.getLatitudeDouble() < 0 ? 1 : 0;
        int longNeg = aisPosition.getLongitudeDouble() < 0 ? 1 : 0;

        BigDecimal latBd = new BigDecimal(aisPosition.getLatitudeDouble());
        String strLat = Double.toString(aisPosition.getLatitudeDouble());
        int bigDigitsLat = strLat.substring(0, strLat.indexOf(".", 0) - 1).length();
        latBd = latBd.round(new MathContext(7 + bigDigitsLat + 1 + latNeg));

        BigDecimal lonBd = new BigDecimal(aisPosition.getLongitudeDouble());
        String strLon = Double.toString(aisPosition.getLatitudeDouble());
        int bigDigitsLon = strLon.substring(0, strLon.indexOf(".", 0) - 1).length();
        lonBd = lonBd.round(new MathContext(7 + bigDigitsLon + 1 + longNeg));
        this.latitude = latBd.doubleValue();
        this.longitude = lonBd.doubleValue();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
