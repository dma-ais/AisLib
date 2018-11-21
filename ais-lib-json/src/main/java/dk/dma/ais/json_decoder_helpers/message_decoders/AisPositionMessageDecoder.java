package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.enums.SpecialManoeuvreIndicator;
import dk.dma.ais.json_decoder_helpers.enums.SyncState;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.NavigationalStatus;

@SuppressWarnings("unused")
@Decodes(className = AisPositionMessage.class)
public class AisPositionMessageDecoder extends AisMessageDecoder{

    private transient AisPositionMessage aisPositionMessage;

    private DecodedAisFieldObject navStatusDFO;
    private DecodedAisFieldObject rotDFO;
    private DecodedAisFieldObject sogDFO;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition position;
    private DecodedAisFieldObject cogDFO;
    private DecodedAisFieldObject trueHeadingDFO;
    private DecodedAisFieldObject utcSecDFO;
    private DecodedAisFieldObject specialManIndicatorDFO;
    //    private DecodedAisFieldObject spareDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject syncStateDFO;

    public AisPositionMessageDecoder(AisPositionMessage aisPositionMessage) {
        super(aisPositionMessage);
        this.aisPositionMessage = aisPositionMessage;
    }

    //region Getters

    public DecodedAisFieldObject getNavStatusDFO() {
        int navStatus = aisPositionMessage.getNavStatus();
        return new DecodedAisFieldObject(navStatus, NavigationalStatus.get(navStatus).prettyStatus());
    }

    public DecodedAisFieldObject getRotDFO() {
        int rot = aisPositionMessage.getRot();
        String text;
        if (rot >= 0) { //then turning right
            if (rot == 127) {
                text = "Turning right at more than 5 degrees per 30s";
            } else {
                Double rotTrue = Math.pow((rot * 4.733), 2);
                text = "Turning right at " + rotTrue + " degrees/ min";
            }
        } else { //turning left
            if (rot == -128) {
                text = "No turn information available";
            } else if (rot == -127) {
                text = "Turning left at more than 5 degrees per 30s";
            } else {
                Double rotTrue = Math.pow((rot * 4.733), 2);
                text = "Turning left at " + rotTrue + " degrees/ min";
            }
        }
        return new DecodedAisFieldObject(rot, text);
    }

    public DecodedAisFieldObject getSogDFO() {
        int sog = aisPositionMessage.getSog();
        return CommonFieldDecoderHelper.getSogDFO(sog);
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisPositionMessage.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPosition() {
        return new DecodedAisPosition(aisPositionMessage.getPos());
    }

    public DecodedAisFieldObject getCogDFO() {
        int cog = aisPositionMessage.getCog();
        return CommonFieldDecoderHelper.getCogDFO(cog);
    }

    public DecodedAisFieldObject getTrueHeadingDFO() {
        int trueHeading = aisPositionMessage.getTrueHeading();
        return CommonFieldDecoderHelper.getTrueHeadingDFO(trueHeading);
    }

    public DecodedAisFieldObject getUtcSecDFO() {
        int utcSec = aisPositionMessage.getUtcSec();
        return CommonFieldDecoderHelper.getUtcSecDFO(utcSec);
    }

    public DecodedAisFieldObject getSpecialManIndicatorDFO() {
        int specialManIndicator = aisPositionMessage.getSpecialManIndicator();
        return new DecodedAisFieldObject(specialManIndicator, SpecialManoeuvreIndicator.get(specialManIndicator).prettyPrint());
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = aisPositionMessage.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }
    public DecodedAisFieldObject getSyncStateDFO() {
        int syncState = aisPositionMessage.getSyncState();
        return new DecodedAisFieldObject(syncState, SyncState.get(syncState).prettyPrint());
    }

    //endregion
    //region Setters

    public void setNavStatusDFO(DecodedAisFieldObject navStatusDFO) {
        this.navStatusDFO = navStatusDFO;
    }

    public void setRotDFO(DecodedAisFieldObject rotDFO) {
        this.rotDFO = rotDFO;
    }

    public void setSogDFO(DecodedAisFieldObject sogDFO) {
        this.sogDFO = sogDFO;
    }

    public void setPosAccDFO(DecodedAisFieldObject posAccDFO) {
        this.posAccDFO = posAccDFO;
    }

    public void setAisPositionMessage(DecodedAisPosition aisPosition) {
        this.position = aisPosition;
    }

    public void setCogDFO(DecodedAisFieldObject cogDFO) {
        this.cogDFO = cogDFO;
    }

    public void setTrueHeadingDFO(DecodedAisFieldObject trueHeadingDFO) {
        this.trueHeadingDFO = trueHeadingDFO;
    }

    public void setUtcSecDFO(DecodedAisFieldObject utcSecDFO) {
        this.utcSecDFO = utcSecDFO;
    }

    public void setSpecialManIndicatorDFO(DecodedAisFieldObject specialManIndicatorDFO) {
        this.specialManIndicatorDFO = specialManIndicatorDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setSyncStateDFO(DecodedAisFieldObject syncStateDFO) {
        this.syncStateDFO = syncStateDFO;
    }
    //endregion

}
