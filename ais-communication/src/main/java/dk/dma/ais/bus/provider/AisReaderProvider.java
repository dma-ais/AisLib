package dk.dma.ais.bus.provider;

import dk.dma.ais.bus.AisBus;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.enav.util.function.Consumer;

public class AisReaderProvider extends AisBusProvider implements Consumer<AisPacket> {
    
    private AisReader aisReader;

    public AisReaderProvider(AisBus aisBus, AisReader aisReader) {
        super(aisBus);
        this.aisReader = aisReader;        
        // Register self as handler of packets
        aisReader.registerPacketHandler(this);
    }
    
    /**
     * Receive packet from reader
     */
    @Override
    public void accept(AisPacket packet) {
        // Push to bus
        push(packet);        
    }

    
    @Override
    public void start() {
        // Start the reader
        aisReader.start();
    }

}
