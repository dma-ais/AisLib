package dk.dma.ais.bus;

import dk.dma.ais.packet.AisPacket;

/**
 * An entry on the AIS bus with possibilities for further metadata
 */
public class AisBusEntry {
	
	private final AisPacket packet;
	
	public AisBusEntry(AisPacket packet) {
		this.packet = packet;
	}
	
	public AisPacket getPacket() {
		return packet;
	}
	
}
