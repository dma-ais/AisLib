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
    private int userId;

    public AisMessageDecoder(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

    //region Getters

    public int getMsgId() {
        return msgId;
    }

    public DecodedAisFieldObject getRepeatDFO() {
        int repeat = aisMessage.getRepeat();
        String text = "Message has been repeated " + repeat + " times";
        return new DecodedAisFieldObject(repeat, text);
    }

    public int getUserId() {
        return userId;
    }

    //endregion

    //region Setters

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public void setRepeatDFO(DecodedAisFieldObject repeatDFO) {
        this.repeatDFO = repeatDFO;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //endregion
}
