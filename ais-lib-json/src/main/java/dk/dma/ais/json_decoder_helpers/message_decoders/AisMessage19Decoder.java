package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage19;


@SuppressWarnings("unused")
@Decodes(className = AisMessage19.class)
public class AisMessage19Decoder extends AisStaticCommonDecoder {

    private transient AisMessage19 aisMessage19;

    private DecodedAisFieldObject sogDFO;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition posDFO;
    private DecodedAisFieldObject cogDFO;
    private DecodedAisFieldObject trueHeadingDFO;
    private DecodedAisFieldObject utcSecDFO;
    private DecodedAisFieldObject posTypeDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject dteDFO;
    private DecodedAisFieldObject modeFlagDFO;

//    private DecodedAisFieldObject spare1; //Not used
//    private DecodedAisFieldObject spare2; //Not used
//    private DecodedAisFieldObject spare3; //Not used

    public AisMessage19Decoder(AisMessage19 aisMessage19) {
        super(aisMessage19);
        this.aisMessage19 = aisMessage19;
    }

    //region Getters

    public DecodedAisFieldObject getSogDFO() {
        int sog = aisMessage19.getSog();
        return CommonFieldDecoderHelper.getSogDFO(sog);
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisMessage19.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPosDFO() {
        return new DecodedAisPosition(aisMessage19.getPos());
    }

    public DecodedAisFieldObject getCogDFO() {
        int cog = aisMessage19.getCog();
        return CommonFieldDecoderHelper.getCogDFO(cog);
    }

    public DecodedAisFieldObject getTrueHeadingDFO() {
        int trueHeading = aisMessage19.getTrueHeading();
        return CommonFieldDecoderHelper.getTrueHeadingDFO(trueHeading);
    }

    public DecodedAisFieldObject getUtcSecDFO() {
        int utcSec = aisMessage19.getUtcSec();
        return CommonFieldDecoderHelper.getUtcSecDFO(utcSec);
    }

    public DecodedAisFieldObject getPosTypeDFO() {
        int posType = aisMessage19.getPosType();
        return new DecodedAisFieldObject(posType, EPFDFixType.get(posType).prettyPrint());
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raimCode = aisMessage19.getRaim();
        return new DecodedAisFieldObject(raimCode, Raim.get(raimCode).prettyPrint());
    }

    public DecodedAisFieldObject getDteDFO() {
        int dte = aisMessage19.getDte();
        return CommonFieldDecoderHelper.getDteDFO(dte);
    }

    public DecodedAisFieldObject getModeFlagDFO() {
        int modeFlag = aisMessage19.getModeFlag();
        String text;
        if (modeFlag == 0) {
            text = "Station operating in autonomous and continuous mode";
        } else {
            text = "Station operating in assigned mode";
        }
        return new DecodedAisFieldObject(modeFlag, text);
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

    public void setPosTypeDFO(DecodedAisFieldObject posTypeDFO) {
        this.posTypeDFO = posTypeDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setDteDFO(DecodedAisFieldObject dteDFO) {
        this.dteDFO = dteDFO;
    }

    public void setModeFlagDFO(DecodedAisFieldObject modeFlagDFO) {
        this.modeFlagDFO = modeFlagDFO;
    }

    //endregion
}
