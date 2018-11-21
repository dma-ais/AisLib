package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisMessage1;

@SuppressWarnings("unused")
@Decodes(className = AisMessage1.class)
public class AisMessage1Decoder extends AisPositionMessageDecoder{

    private transient AisMessage1 aisMessage1;

    public AisMessage1Decoder(AisMessage1 aisMessage1) {
        super(aisMessage1);
        this.aisMessage1 = aisMessage1;
    }
}
