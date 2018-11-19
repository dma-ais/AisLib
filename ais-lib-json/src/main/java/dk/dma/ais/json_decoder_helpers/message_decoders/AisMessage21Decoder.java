package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.enums.NavaidTypes;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage21;

@SuppressWarnings("unused")
@Decodes(className = AisMessage21.class)
public class AisMessage21Decoder extends AisMessageDecoder {

    private transient AisMessage21 aisMessage21;

    private DecodedAisFieldObject atonTypeDFO; //aid_type
    private String name;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition posDFO;
    private DecodedAisFieldObject dimBowDFO;
    private DecodedAisFieldObject dimSternDFO;
    private DecodedAisFieldObject dimPortDFO;
    private DecodedAisFieldObject dimStarboardDFO;
    private DecodedAisFieldObject posTypeDFO; //EPFD
    private DecodedAisFieldObject utcSecDFO;
    private DecodedAisFieldObject offPositionDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject virtualDFO;
    private DecodedAisFieldObject assignedDFO;
    private String nameExt;

//    private DecodedAisFieldObject regionalDFO; //Not used
//    private DecodedAisFieldObject spare1; //Not used
//    private DecodedAisFieldObject spare2; //Not used

    public AisMessage21Decoder(AisMessage21 aisMessage21) {
        super(aisMessage21);
        this.aisMessage21 = aisMessage21;
    }

    //region Getters

    /**
     * NavAidType
     */
    public DecodedAisFieldObject getAtonTypeDFO() {
        int navaid = aisMessage21.getAtonType();
        return new DecodedAisFieldObject(navaid, NavaidTypes.get(navaid).prettyPrint());
    }

    public String getName() {
        return aisMessage21.getName();
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisMessage21.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPosDFO() {
        return new DecodedAisPosition(aisMessage21.getPos());
    }

    public DecodedAisFieldObject getDimBowDFO() {
        int dimBow = aisMessage21.getDimBow();
        String text;
        if (dimBow == 511) {
            text = "511 m or greater";
        } else {
            text = "Position A (bow) dim. " + dimBow + " m";
        }
        return new DecodedAisFieldObject(dimBow, text);
    }

    public DecodedAisFieldObject getDimSternDFO() {
        int dimStern = aisMessage21.getDimStern();
        String text;
        if (dimStern == 511) {
            text = "511 m or greater";
        } else {
            text = "Position B (stern) dim. " + dimStern + " m";
        }
        return new DecodedAisFieldObject(dimStern, text);
    }

    public DecodedAisFieldObject getDimPortDFO() {
        int dimPort = aisMessage21.getDimPort();
        String text;
        if (dimPort == 63) {
            text = "63 m or greater";
        } else {
            text = "Position C (port) dim. " + dimPort + " m";
        }
        return new DecodedAisFieldObject(dimPort, text);
    }

    public DecodedAisFieldObject getDimStarboardDFO() {
        int dimStarboard = aisMessage21.getDimStarboard();
        String text;
        if (dimStarboard == 63) {
            text = "63 m or greater";
        } else {
            text = "Position D (starboard) dim. " + dimStarboard + " m";
        }
        return new DecodedAisFieldObject(dimStarboard, text);
    }

    /**
     *EPFDFixType
     */
    public DecodedAisFieldObject getPosTypeDFO() {
        int posType = aisMessage21.getPosType();
        return new DecodedAisFieldObject(posType, EPFDFixType.get(posType).prettyPrint());
    }

    public DecodedAisFieldObject getUtcSecDFO() {
        int utcSec = aisMessage21.getUtcSec();
        return CommonFieldDecoderHelper.getUtcSecDFO(utcSec);
    }

    public DecodedAisFieldObject getOffPositionDFO() {
        int offPosition = aisMessage21.getOffPosition();
        String text;
        if (offPosition == 0) {
            text = "On Position";
        } else {
            text = "Off position";
        }
        return new DecodedAisFieldObject(offPosition, text);
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = aisMessage21.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }

    public DecodedAisFieldObject getVirtualDFO() {
        int virtualFlag = aisMessage21.getVirtual();
        String text;
        if (virtualFlag == 0) {
            text = "Real AtoN at indicated position";
        } else {
            text = "Virtual AtoN, does not physically exist";
        }
        return new DecodedAisFieldObject(virtualFlag, text);
    }

    public DecodedAisFieldObject getAssignedDFO() {
        int assigned = aisMessage21.getAssigned();
        String text;
        if (assigned == 1) {
            text = "Station operating in assigned mode";
        } else {
            text = "Station operating in autonomous and continuous mode"; //default
        }
        return new DecodedAisFieldObject(assigned, text);
    }

    public String getNameExt() {
        return aisMessage21.getNameExt();
    }

    //endregion

    //region Setters

    public void setAtonTypeDFO(DecodedAisFieldObject atonTypeDFO) {
        this.atonTypeDFO = atonTypeDFO;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosAccDFO(DecodedAisFieldObject posAccDFO) {
        this.posAccDFO = posAccDFO;
    }

    public void setPosDFO(DecodedAisPosition posDFO) {
        this.posDFO = posDFO;
    }

    public void setDimBowDFO(DecodedAisFieldObject dimBowDFO) {
        this.dimBowDFO = dimBowDFO;
    }

    public void setDimSternDFO(DecodedAisFieldObject dimSternDFO) {
        this.dimSternDFO = dimSternDFO;
    }

    public void setDimPortDFO(DecodedAisFieldObject dimPortDFO) {
        this.dimPortDFO = dimPortDFO;
    }

    public void setDimStarboardDFO(DecodedAisFieldObject dimStarboardDFO) {
        this.dimStarboardDFO = dimStarboardDFO;
    }

    public void setPosTypeDFO(DecodedAisFieldObject posTypeDFO) {
        this.posTypeDFO = posTypeDFO;
    }

    public void setUtcSecDFO(DecodedAisFieldObject utcSecDFO) {
        this.utcSecDFO = utcSecDFO;
    }

    public void setOffPositionDFO(DecodedAisFieldObject offPositionDFO) {
        this.offPositionDFO = offPositionDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setVirtualDFO(DecodedAisFieldObject virtualDFO) {
        this.virtualDFO = virtualDFO;
    }

    public void setAssignedDFO(DecodedAisFieldObject assignedDFO) {
        this.assignedDFO = assignedDFO;
    }

    public void setNameExt(String nameExt) {
        this.nameExt = nameExt;
    }

    //endregion
}
