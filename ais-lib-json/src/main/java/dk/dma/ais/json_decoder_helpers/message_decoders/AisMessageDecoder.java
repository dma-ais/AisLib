package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage;

/**
 * This decoder and all of its children decode their corresponding AisMessages into a human readable Json
 */
@SuppressWarnings("unused")
@Decodes(className = AisMessage.class)
public abstract class AisMessageDecoder {

    private AisMessage aisMessage;

    private int msgId;
    private DecodedAisFieldObject repeatDFO;
    private Integer userId;


    public AisMessageDecoder(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

    //region Getters

    public Integer getMsgId() {
        if(aisMessage.getMsgId() == 0){
            return null;
        }
        return aisMessage.getMsgId();
    }

    public DecodedAisFieldObject getRepeatDFO() {
        int repeat = aisMessage.getRepeat();
        String text = "Message has been repeated " + repeat + " times";
        return new DecodedAisFieldObject(repeat, text);
    }

    public Integer getUserId() {
        if (aisMessage.getUserId() == 0) {
            return null;
        }
        return aisMessage.getUserId();
    }

    //endregion

    //region Setters

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public void setRepeatDFO(DecodedAisFieldObject repeatDFO) {
        this.repeatDFO = repeatDFO;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    //endregion
}
