package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.enums.EPFDFixType;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage24;

@Decodes(className = AisMessage24.class)
public class AisMessage24Decoder extends AisStaticCommonDecoder {
    private transient AisMessage24 aisMessage24;
    private String vendorId;


    public AisMessage24Decoder(AisMessage24 aisMessage24) {
        super(aisMessage24);
        this.aisMessage24 = aisMessage24;
    }

    public int getPartNumber() {
        return aisMessage24.getPartNumber();
    }

    public void setPartNumber(int partNumber) {
    }

    public String getVendorId() {
        if (aisMessage24.getVendorId() != null && aisMessage24.getVendorId().contains("@")) {
            return aisMessage24.getVendorId().replace("@", "");
        }
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public DecodedAisFieldObject getPosType() {
        int code = aisMessage24.getPosType();
        return new DecodedAisFieldObject(code, EPFDFixType.get(code).prettyPrint());
    }

    public void setPosType(DecodedAisFieldObject posType) {
    }
}
