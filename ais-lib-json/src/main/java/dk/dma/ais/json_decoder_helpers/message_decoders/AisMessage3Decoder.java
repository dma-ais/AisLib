package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage3;

@SuppressWarnings("unused")
@Decodes(className = AisMessage3.class)
public class AisMessage3Decoder extends AisPositionMessageDecoder{

    private transient AisMessage3 aisMessage3;

    public AisMessage3Decoder(AisMessage3 aisMessage3) {
        super(aisMessage3);
        this.aisMessage3 = aisMessage3;
    }
}
