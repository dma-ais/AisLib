package dk.dma.ais.json_decoder_helpers.enums;

public enum EPFDFixType {
    UNDEFINED(0), //default
    GPS(1),
    GLONASS(2),
    COMBINED_GPS_AND_GLONASS(3),
    LORAN_C(4),
    CHAYKA(5),
    INTEGRATED_NAVIGATION_SYSTEM(6),
    SURVEYED(7),
    GALILEO(8),
    INTERNAL_GNSS(15);

    private final int code;

    EPFDFixType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static EPFDFixType get(int positionTypeCode) {
        for (EPFDFixType epfdFixType : EPFDFixType.values()) {
            if (epfdFixType.getCode() == positionTypeCode) {
                return epfdFixType;
            }
        }
        return UNDEFINED; //UNDEFINED is default
    }

    public String prettyPrint() {
        String navStat = name().replace("_", " ");
        return navStat.substring(0, 1) + navStat.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return prettyPrint();
    }

}
