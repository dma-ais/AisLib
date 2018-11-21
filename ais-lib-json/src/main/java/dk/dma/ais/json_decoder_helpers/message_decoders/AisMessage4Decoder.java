package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage4;

@SuppressWarnings("unused")
@Decodes(className = AisMessage4.class)
public class AisMessage4Decoder extends UTCDateResponseMessageDecoder {

    private transient AisMessage4 aisMessage4;

    public AisMessage4Decoder(AisMessage4 aisMessage4) {
        super(aisMessage4);
        this.aisMessage4 = aisMessage4;
    }
}
