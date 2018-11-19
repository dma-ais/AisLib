package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage11;

@SuppressWarnings("unused")
@Decodes(className = AisMessage11.class)
public class AisMessage11Decoder extends UTCDateResponseMessageDecoder {

    private transient AisMessage11 aisMessage11;

    public AisMessage11Decoder(AisMessage11 aisMessage11) {
        super(aisMessage11);
        this.aisMessage11 = aisMessage11;
    }
}


