package dk.dma.ais.json_decoder_helpers.enums;

public enum SpecialManoeuvreIndicator {

    NOT_AVAILABLE(0),
    NOT_ENGAGED_IN_SPECIAL_MANOEUVRE(1),
    ENGAGED_IN_SPECIAL_MANOEUVRE(2);

    private final int code;

    SpecialManoeuvreIndicator(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SpecialManoeuvreIndicator get(int intSpecialManeuverIndicator) {
        for (SpecialManoeuvreIndicator specialManoeuvreIndicator : SpecialManoeuvreIndicator.values()) {
            if (intSpecialManeuverIndicator == specialManoeuvreIndicator.getCode()) {
                return specialManoeuvreIndicator;
            }
        }
        return NOT_AVAILABLE;
    }

    public String prettyPrint() {
        String specialMan = name().replace("_", " ");
        return specialMan.substring(0, 1) + specialMan.substring(1).toLowerCase();
    }


    @Override
    public String toString() {
        return prettyPrint();
    }
}
