package dk.dma.ais.json_decoder_helpers.enums;

public enum TransmissionControl {
    DEFAULT(0),
    REQUEST_RETRANSMISSION(1);

    private final int code;

    TransmissionControl(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TransmissionControl get(int transmissionsControlCode) {
        for (TransmissionControl transmissionControl : TransmissionControl.values()) {
            if (transmissionControl.getCode() == transmissionsControlCode) {
                return transmissionControl;
            }
        }
        return DEFAULT;
    }

    public String prettyPrint() {
        switch (this) {
            case DEFAULT:
                return "Class-A AIS station stops transmission of Message 27 within an AIS base station coverage area.";
            case REQUEST_RETRANSMISSION:
                return "Request Class-A station to transmit Message 27 within an AIS base station coverage area.";
            default:
                return "Class-A AIS station stops transmission of Message 27 within an AIS base station coverage area.";
        }
    }


    @Override
    public String toString() {
        return prettyPrint();
    }
}
