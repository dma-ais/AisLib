package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.enums.SyncState;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage9;

@SuppressWarnings("unused")
@Decodes(className = AisMessage9.class)
public class AisMessage9Decoder extends AisMessageDecoder{

    private transient AisMessage9 aisMessage9;

    private DecodedAisFieldObject altitudeDFO;
    private DecodedAisFieldObject sogDFO;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition posDFO;
    private DecodedAisFieldObject cogDFO;
    private DecodedAisFieldObject utcSecDFO;
//    private DecodedAisFieldObject regionalReservedDFO; //Not used
    private DecodedAisFieldObject dteDFO;
//    private DecodedAisFieldObject spareDFO; //Not used
    private DecodedAisFieldObject assignedDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject commStateSelectorFlagDFO;
    private DecodedAisFieldObject syncStateDFO;
    private int slotTimeoutDFO;
//    private DecodedAisFieldObject subMessageDFO; //Not used

    public AisMessage9Decoder(AisMessage9 aisMessage9) {
        super(aisMessage9);
        this.aisMessage9 = aisMessage9;
    }

    //region Getters

    public DecodedAisFieldObject getAltitudeDFO() {
        int alt = aisMessage9.getAltitude();
        String text;
        if (alt == 4095) {
            text = "Not available";
        } else if (alt == 4094) {
            text = "Altitude is 4094 m or higher";
        } else {
            text = "Altitude is " + alt + " m";
        }
        return new DecodedAisFieldObject(alt, text);
    }

    public DecodedAisFieldObject getSogDFO() {
        int sog = aisMessage9.getSog();
        return CommonFieldDecoderHelper.getSogDFO(sog);
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisMessage9.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPosDFO() {        
        return new DecodedAisPosition(aisMessage9.getPos());
    }

    public DecodedAisFieldObject getCogDFO() {
        int cog = aisMessage9.getCog();
        return CommonFieldDecoderHelper.getCogDFO(cog);
    }

    public DecodedAisFieldObject getUtcSecDFO() {
        int utcSec = aisMessage9.getUtcSec();
        return CommonFieldDecoderHelper.getUtcSecDFO(utcSec);
    }

    public DecodedAisFieldObject getDteDFO() {
        int dte = aisMessage9.getDte();
        return CommonFieldDecoderHelper.getDteDFO(dte);
    }

    public DecodedAisFieldObject getAssignedDFO() {
        int assigned = aisMessage9.getAssigned();
        String text;
        if (assigned == 1) {
            text = "Station operating in assigned mode";
        } else {
            text = "Station operating in autonomous and continuous mode"; //Default
        }
        return new DecodedAisFieldObject(assigned, text);
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = aisMessage9.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }

    public DecodedAisFieldObject getCommStateSelectorFlagDFO() {
        int commStateSelecotrFlag = aisMessage9.getCommStateSelectorFlag();
        String text;
        if (commStateSelecotrFlag == 0) {
            text = "SOTDMA communication state follows";
        } else {
            text = "ITDMA communication state follows";
        }
        return new DecodedAisFieldObject(commStateSelecotrFlag, text);
    }

    public DecodedAisFieldObject getSyncStateDFO() {
        int syncState = aisMessage9.getSyncState();
        return new DecodedAisFieldObject(syncState, SyncState.get(syncState).prettyPrint());
    }

    public int getSlotTimeoutDFO() {
        return aisMessage9.getSlotTimeout();
    }

    //endregion
    
    //region Setters

    public void setAltitudeDFO(DecodedAisFieldObject altitudeDFO) {
        this.altitudeDFO = altitudeDFO;
    }

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

    public void setUtcSecDFO(DecodedAisFieldObject utcSecDFO) {
        this.utcSecDFO = utcSecDFO;
    }

    public void setDteDFO(DecodedAisFieldObject dteDFO) {
        this.dteDFO = dteDFO;
    }
    
    public void setAssignedDFO(DecodedAisFieldObject assignedDFO) {
        this.assignedDFO = assignedDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setCommStateSelectorFlagDFO(DecodedAisFieldObject commStateSelectorFlagDFO) {
        this.commStateSelectorFlagDFO = commStateSelectorFlagDFO;
    }

    public void setSyncStateDFO(DecodedAisFieldObject syncStateDFO) {
        this.syncStateDFO = syncStateDFO;
    }

    public void setSlotTimeoutDFO(int slotTimeoutDFO) {
        this.slotTimeoutDFO = slotTimeoutDFO;
    }


    //endregion
}
