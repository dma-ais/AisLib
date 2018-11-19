package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage18;


@SuppressWarnings("unused")
@Decodes(className = AisMessage18.class)
public class AisMessage18Decoder extends AisMessageDecoder{

    private AisMessage18 aisMessage18;

    private DecodedAisFieldObject sogDFO;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition posDFO;
    private DecodedAisFieldObject cogDFO;
    private DecodedAisFieldObject trueHeadingDFO;
    private DecodedAisFieldObject utcSecDFO;
    private DecodedAisFieldObject classBUnitFlagDFO;
    private DecodedAisFieldObject classBDisplayFlagDFO;
    private DecodedAisFieldObject classBDscFlagDFO;
    private DecodedAisFieldObject classBBandFlagDFO;
    private DecodedAisFieldObject classBMsg22FlagDFO;
    private DecodedAisFieldObject modeFlagDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject commStateSelectorFlagDFO;
    private DecodedAisFieldObject commStateDFO;

    public AisMessage18Decoder(AisMessage18 aisMessage18) {
        super(aisMessage18);
        this.aisMessage18 = aisMessage18;
    }

    //region Getters
    public DecodedAisFieldObject getSogDFO() {
        int sog = aisMessage18.getSog();
        return CommonFieldDecoderHelper.getSogDFO(sog);
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisMessage18.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPosDFO() {
        return new DecodedAisPosition(aisMessage18.getPos());
    }

    public DecodedAisFieldObject getCogDFO() {
        int cog = aisMessage18.getCog();
        return CommonFieldDecoderHelper.getCogDFO(cog);
    }

    public DecodedAisFieldObject getTrueHeadingDFO() {
        int trueHeading = aisMessage18.getTrueHeading();
        return CommonFieldDecoderHelper.getTrueHeadingDFO(trueHeading);
    }

    public DecodedAisFieldObject getUtcSecDFO() {
        int utcSec = aisMessage18.getUtcSec();
        return CommonFieldDecoderHelper.getUtcSecDFO(utcSec);
    }

    public DecodedAisFieldObject getClassBUnitFlagDFO() {
        int classBUnitFlag = aisMessage18.getClassBUnitFlag();
        String text;
        if (classBUnitFlag == 0) {
            text =  "Class B SOTDMA unit";
        } else { //message is 1
            text = "Class B CS unit";
        }
        return new DecodedAisFieldObject(classBUnitFlag, text);
    }

    public DecodedAisFieldObject getClassBDisplayFlagDFO() {
        int classBDisplayFlag = aisMessage18.getClassBDisplayFlag();
        String text;
        if (classBDisplayFlag == 0) {
            text =  "No display available; not capable of displaying Message 12 and 14";
        } else { //message is 1
            text = "Equipped with integrated display displaying Message 12 and 14";
        }
        return new DecodedAisFieldObject(classBDisplayFlag, text);
    }

    public DecodedAisFieldObject getClassBDscFlagDFO() {
        int classBDescFlag = aisMessage18.getClassBDscFlag();
        String text;
        if (classBDescFlag == 0) {
            text = "Not equipped with DSC function";
        } else {
            text = "Equipped with DSC function";
        }
        return new DecodedAisFieldObject(classBDescFlag, text);
    }

    public DecodedAisFieldObject getClassBBandFlagDFO() {
        int classBBandFlag = aisMessage18.getClassBBandFlag();
        String text;
        if (classBBandFlag == 0) {
            text = "Capable of operating over the upper 525 kHz band of the marine band 1";
        } else {
            text = "Capable of operating over the whole marine band";
        }
        return new DecodedAisFieldObject(classBBandFlag, text);
    }

    public DecodedAisFieldObject getClassBMsg22FlagDFO() {
        int classBMsg22Flag = aisMessage18.getClassBMsg22Flag();
        String text;
        if (classBMsg22Flag == 0) {
            text = "No frequency management via Message 22 , operating on AIS1, AIS2 only";
        } else {
            text = "Frequency management via Message 22";
        }
        return new DecodedAisFieldObject(classBMsg22Flag, text);
    }

    public DecodedAisFieldObject getModeFlagDFO() {
        int modeFlag = aisMessage18.getModeFlag();
        String text;
        if (modeFlag == 0) {
            text = "Station operating in autonomous and continuous mode";
        } else {
            text = "Station operating in assigned mode";
        }
        return new DecodedAisFieldObject(modeFlag, text);
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = aisMessage18.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }

    public DecodedAisFieldObject getCommStateSelectorFlagDFO() {
        int commStateSelectorFlag = aisMessage18.getCommStateSelectorFlag();
        String text;
        if (commStateSelectorFlag == 0) {
            text = "SOTDMA communication state follows";
        } else {
            text = "ITDMA communication state follows";
        }
        return new DecodedAisFieldObject(commStateSelectorFlag, text);
    }

    public DecodedAisFieldObject getCommStateDFO() {
        int commState = aisMessage18.getCommState();
        String text;
        if (commState == 0) {
            text = "SOTDMA communication state";
        } else {
            text = "ITDMA communication state";
        }
        return new DecodedAisFieldObject(commState, text);
    }
    //endregion

    //region Setters

    public void setSogDFO(DecodedAisFieldObject sogDFO) {
        this.sogDFO = sogDFO;
    }

    public void setPosAccDFO(DecodedAisFieldObject posAccDFO) {
        this.posAccDFO = posAccDFO;
    }

    public void setPosDFO(DecodedAisPosition posDFO) {
        this.posDFO = posDFO;
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

    public void setClassBUnitFlagDFO(DecodedAisFieldObject classBUnitFlagDFO) {
        this.classBUnitFlagDFO = classBUnitFlagDFO;
    }

    public void setClassBDisplayFlagDFO(DecodedAisFieldObject classBDisplayFlagDFO) {
        this.classBDisplayFlagDFO = classBDisplayFlagDFO;
    }

    public void setClassBDscFlagDFO(DecodedAisFieldObject classBDscFlagDFO) {
        this.classBDscFlagDFO = classBDscFlagDFO;
    }

    public void setClassBBandFlagDFO(DecodedAisFieldObject classBBandFlagDFO) {
        this.classBBandFlagDFO = classBBandFlagDFO;
    }

    public void setClassBMsg22FlagDFO(DecodedAisFieldObject classBMsg22FlagDFO) {
        this.classBMsg22FlagDFO = classBMsg22FlagDFO;
    }

    public void setModeFlagDFO(DecodedAisFieldObject modeFlagDFO) {
        this.modeFlagDFO = modeFlagDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setCommStateSelectorFlagDFO(DecodedAisFieldObject commStateSelectorFlagDFO) {
        this.commStateSelectorFlagDFO = commStateSelectorFlagDFO;
    }

    public void setCommStateDFO(DecodedAisFieldObject commStateDFO) {
        this.commStateDFO = commStateDFO;
    }


    //endregion
}
