package dk.dma.ais.json_decoder_helpers;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;

public class CommonFieldDecoderHelper {

    public CommonFieldDecoderHelper() {
    }

    public static DecodedAisFieldObject getSogDFO(int sog) {
        String text;
        if (sog == 1023) {
            text = "Speed over ground not available";
        } else if (sog == 1022) {
            text = "102.2 knots or higher";
        } else {
            double dbl = sog / 10.0;
            text = dbl + " knots";
        }
        return new DecodedAisFieldObject(sog, text);
    }

    public static DecodedAisFieldObject getCogDFO(int cog) {
        String text;
        if (cog >= 0 && cog < 3600) {
            double cogTrue = cog / 10.0;
            text = cogTrue + " degrees";
        } else if (cog == 3600) {
            text = "Course over ground not available";
        } else {
            text = "These values should not be used";
        }
        return new DecodedAisFieldObject(cog, text);
    }

    public static DecodedAisFieldObject getTrueHeadingDFO(int trueHeading) {
        String text;
        if (trueHeading >= 0 && trueHeading < 360) {
            text = trueHeading + " degrees";
        } else /*if (trueHeading == 511)*/ {
            text = "Not available";
        }
        return new DecodedAisFieldObject(trueHeading, text);
    }

    public static DecodedAisFieldObject getUtcSecDFO(int utcSec) {
        String text;
        if (utcSec >= 0 && utcSec < 60) {
            text = Integer.toString(utcSec);
        } else if (utcSec == 61) {
            text = "Positioning system is in manual input mode";
        } else if (utcSec == 62) {
            text = "Electronic position fixing system operates in estimated (dead reckoning) mode";
        } else if (utcSec == 63) {
            text = "Positioning system is inoperative";
        } else /*utcSec == 60*/ {
            text = "Time stamp unavailable";
        }
        return new DecodedAisFieldObject(utcSec, text);
    }

    public static DecodedAisFieldObject getDteDFO(int dte) {
        String text;
        if (dte == 0) {
            text = "Available";
        } else {
            text = "Not available";
        }
        return new DecodedAisFieldObject(dte, text);
    }
}
