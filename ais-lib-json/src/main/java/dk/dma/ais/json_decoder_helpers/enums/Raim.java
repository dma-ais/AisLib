package dk.dma.ais.json_decoder_helpers.enums;

public enum Raim {

    RAIM_NOT_IN_USE(0),
    RAIM_IN_USE(1);

    private final int code;

    Raim(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Raim get(int intRaimCode) {
        for (Raim raim : Raim.values()) {
            if (intRaimCode == raim.getCode()) {
                return raim;
            }
        }
        return RAIM_NOT_IN_USE;
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