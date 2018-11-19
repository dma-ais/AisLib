package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.CommonFieldDecoderHelper;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisDate;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage5;

@SuppressWarnings("unused")
@Decodes(className = AisMessage5.class)
public class AisMessage5Decoder extends AisStaticCommonDecoder {

    private transient AisMessage5 aisMessage5;
    
    private DecodedAisFieldObject versionDFO;
    private long imo;
    private DecodedAisFieldObject posTypeDFO;
    private DecodedAisFieldObject etaDFO;
    private DecodedAisFieldObject draughtDFO;
    private String dest;
    private DecodedAisFieldObject dteDFO;
    
    public AisMessage5Decoder(AisMessage5 aisMessage5) {
        super(aisMessage5);
        this.aisMessage5 = aisMessage5;
    }

    //region Getters

    public DecodedAisFieldObject getVersionDFO() {
        int version = aisMessage5.getVersion();
        String text;
        if (version == 0) {
            text = "Station compliant with Recommendation ITU-R M.1371-1";
        } else if (version == 1){
            text = "station compliant with Recommendation ITU-R M.1371-3";
        } else {
            text = "Station compliant with future editions";
        }
        return new DecodedAisFieldObject(version, text);
    }

    public long getImo() {
        return aisMessage5.getImo();
    }

    public DecodedAisFieldObject getPosTypeDFO() {
        int code = aisMessage5.getPosType();
        return new DecodedAisFieldObject(code, EPFDFixType.get(code).prettyPrint());
    }

    public DecodedAisDate getEtaDFO() {
        return new DecodedAisDate(aisMessage5.getEtaDate());
    }

    public DecodedAisFieldObject getDraughtDFO() {
        int draught = aisMessage5.getDraught();
        String text;
        if (draught == 0) {
            text = "Not available";
        } else if (draught == 255) {
            text = "Draught 25.5 m or greater";
        } else {
            double dbl = draught/10.0;
            text = "Draught is " + dbl + " m";
        }
        return new DecodedAisFieldObject(draught, text);
    }

    public String getDest() {
        return aisMessage5.getDest();
    }

    public DecodedAisFieldObject getDteDFO() {
        int dte = aisMessage5.getDte();
        return CommonFieldDecoderHelper.getDteDFO(dte);
    }

    //endregion
    
    //region Setters

    public void setVersionDFO(DecodedAisFieldObject versionDFO) {
        this.versionDFO = versionDFO;
    }

    public void setImo(long imo) {
        this.imo = imo;
    }

    public void setPosTypeDFO(DecodedAisFieldObject posTypeDFO) {
        this.posTypeDFO = posTypeDFO;
    }

    public void setEtaDFO(DecodedAisFieldObject etaDFO) {
        this.etaDFO = etaDFO;
    }

    public void setDraughtDFO(DecodedAisFieldObject draughtDFO) {
        this.draughtDFO = draughtDFO;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setDteDFO(DecodedAisFieldObject dteDFO) {
        this.dteDFO = dteDFO;
    }

    //endregion
}
