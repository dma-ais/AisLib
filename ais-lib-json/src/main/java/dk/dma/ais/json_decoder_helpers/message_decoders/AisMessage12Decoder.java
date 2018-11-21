package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage12;


@SuppressWarnings("unused")
@Decodes(className = AisMessage12.class)
public class AisMessage12Decoder extends AisMessageDecoder{

    private transient AisMessage12 aisMessage12;

    private int seqNum;
    private long destination;
    private DecodedAisFieldObject retransmitDFO;
    private String message;

    public AisMessage12Decoder(AisMessage12 aisMessage12) {
        super(aisMessage12);
        this.aisMessage12 = aisMessage12;
    }

    //region Getters

    public int getSeqNum() {
        return aisMessage12.getSeqNum();
    }

    public long getDestination() {
        return aisMessage12.getDestination();
    }

    public DecodedAisFieldObject getRetransmitDFO() {
        int retransmit = aisMessage12.getRetransmit();
        String text;
        if (retransmit == 1) {
            text = "Retransmitted";
        } else {
            text = "No retransmission"; //default
        }
        return new DecodedAisFieldObject(retransmit, text);
    }

    public String getMessage() {
        return aisMessage12.getMessage();
    }

    //endregion
    
    //region Setters

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }

    public void setRetransmitDFO(DecodedAisFieldObject retransmitDFO) {
        this.retransmitDFO = retransmitDFO;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //endregion
}
