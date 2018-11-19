package dk.dma.ais.json_decoder_helpers.enums;

public enum PositionAccuracy {
    LOW(0),
    HIGH(1);

    private final int code;

    PositionAccuracy(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PositionAccuracy get(int intPositionAccuracyCode) {
        for (PositionAccuracy positionAccuracy : PositionAccuracy.values()) {
            if (intPositionAccuracyCode == positionAccuracy.getCode()) {
                return positionAccuracy;
            }
        }
        return LOW;
    }

    public String prettyPrint() {
        switch (this) {
            case LOW:
                return "Low (> 10 m) (default)";
            case HIGH:
                return "High (< 10 m)";
            default:
                return "Low (> 10 m) (default)";
        }
    }


    @Override
    public String toString() {
        return prettyPrint();
    }
}
