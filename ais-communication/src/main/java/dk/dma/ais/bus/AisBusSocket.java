package dk.dma.ais.bus;

import dk.dma.ais.filter.PacketFilterCollection;

public abstract class AisBusSocket {
	
	private final AisBus aisBus;
	
	/**
	 * Filters to apply to packets either incoming or outgoing
	 */
	private PacketFilterCollection filters = new PacketFilterCollection();
	
	public AisBusSocket(AisBus aisBus) {
		this.aisBus = aisBus;
	}
	
	

}
