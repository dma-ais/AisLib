package dk.dma.ais.bus;

import dk.dma.ais.packet.AisPacket;

/**
 * An entry on the AIS bus with possibilities for further metadata
 */
// TODO concurrency handling
public class AisBusEntry {

    private final AisPacket packet;
    private final long timestamp;

    public AisBusEntry(AisPacket packet) {
        this.packet = packet;
        this.timestamp = System.currentTimeMillis();
    }

    public AisPacket getPacket() {
        return packet;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

}
