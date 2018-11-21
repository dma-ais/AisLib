package dk.dma.ais.json_decoder_helpers.util;

public class DecoderGrabbingException extends IllegalAccessException {

    String msg;

    public DecoderGrabbingException(String s) {
        super(s);
        msg = s;
    }

    @Override
    public String getMessage() {
        return "DecoderGrabbingException: " + msg;
    }
}
