package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage2;

@SuppressWarnings("unused")
@Decodes(className = AisMessage2.class)
public class AisMessage2Decoder extends AisPositionMessageDecoder{

    private transient AisMessage2 aisMessage2;

    public AisMessage2Decoder(AisMessage2 aisMessage2) {
        super(aisMessage2);
        this.aisMessage2 = aisMessage2;
    }
}
