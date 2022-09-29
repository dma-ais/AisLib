package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.PositionAccuracy;
import dk.dma.ais.json_decoder_helpers.enums.Raim;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage27;
import dk.dma.ais.message.NavigationalStatus;


@SuppressWarnings("unused")
@Decodes(className = AisMessage27.class)
public class AisMessage27Decoder extends AisMessageDecoder {

    private transient AisMessage27 aisMessage27;

    private DecodedAisFieldObject posAccDFO;
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject navStatusDFO;
    private DecodedAisPosition posDFO;
    private DecodedAisFieldObject sogDFO;
    private DecodedAisFieldObject cogDFO;
    private DecodedAisFieldObject gnssPosStatusDFO;

//    private DecodedAisFieldObject spare; //Not used


    public AisMessage27Decoder(AisMessage27 aisMessage27) {
        super(aisMessage27);
        this.aisMessage27 = aisMessage27;
    }

    //region Getter

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = aisMessage27.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = aisMessage27.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }

    public DecodedAisFieldObject getNavStatusDFO() {
        int navStatus = aisMessage27.getNavStatus();
        return new DecodedAisFieldObject(navStatus, NavigationalStatus.get(navStatus).prettyStatus());
    }

    public DecodedAisPosition getPosDFO() {
        return new DecodedAisPosition(aisMessage27.getPos());
    }

    public DecodedAisFieldObject getSogDFO() {
        int sog = aisMessage27.getSog();
        return CommonFieldDecoderHelper.getSogDFOMessage27(sog);
    }

    public DecodedAisFieldObject getCogDFO() {
        int cog = aisMessage27.getCog();
        return CommonFieldDecoderHelper.getCogDFOMessage27(cog);
    }

    public DecodedAisFieldObject getGnssPosStatusDFO() {
        int gnssPos = aisMessage27.getGnssPosStatus();
        String text;
        if (gnssPos == 0) {
            text = "Position is the current GNSS position";
        } else {
            text = "Reported position is not the current GNSS position"; //default
        }
        return new DecodedAisFieldObject(gnssPos, text);
    }

    //endregion

    //region Setter

    public void setPosAccDFO(DecodedAisFieldObject posAccDFO) {
        this.posAccDFO = posAccDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setNavStatusDFO(DecodedAisFieldObject navStatusDFO) {
        this.navStatusDFO = navStatusDFO;
    }

    public void setPosDFO(DecodedAisPosition posDFO) {
        this.posDFO = posDFO;
    }

    public void setSogDFO(DecodedAisFieldObject sogDFO) {
        this.sogDFO = sogDFO;
    }

    public void setCogDFO(DecodedAisFieldObject cogDFO) {
        this.cogDFO = cogDFO;
    }

    public void setGnssPosStatusDFO(DecodedAisFieldObject gnssPosStatusDFO) {
        this.gnssPosStatusDFO = gnssPosStatusDFO;
    }

    //endregion
}
