package dk.dma.ais.bus.provider;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.util.function.Consumer;

/**
 * Provider that allows to push packets onto the bus 
 */
@ThreadSafe
public class CollectorProvider extends AisBusProvider implements Consumer<AisPacket> {
    
    public CollectorProvider() {
        
    }
    
    @Override
    public void accept(AisPacket packet) {
        push(packet);        
    }

    @Override
    public void cancel() {
                
    }

}
