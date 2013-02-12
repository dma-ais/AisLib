/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.bus;

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.filter.PacketFilterCollection;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.MessageQueueOverflowException;

public class AisBus extends Thread {
	
	/**
	 * Filters to apply to received packets
	 */
	private final PacketFilterCollection filters = new PacketFilterCollection();
	
	/**
	 * Queue to represent the bus
	 */
	private final IMessageQueue<AisBusEntry> busQueue;
	
	/**
	 * Collection of consumers of messages from the bus
	 */
	private final List<AisBusConsumer> consumers = new ArrayList<>();
	
	/**
	 * The maximum number of entries to distribute in one batch
	 */
	private final int readBatchSize = 1000; 

	public AisBus() {
		// TODO size from where
		// maybe configuration as argument
		
		busQueue = new BlockingMessageQueue<>();
	}
	
	/**
	 * Push entry onto the bus. Throws exception if the bus is overflowing
	 * @param entry
	 * @throws MessageQueueOverflowException 
	 */
	public void push(AisBusEntry entry) throws MessageQueueOverflowException {
		busQueue.push(entry);
	}
	
	public void registerConsumer(AisBusConsumer consumer) {
		// TODO where to handle queueing
		// How to handle overflow when queing is happening in ais bus
		// otherwise consumer could be called and handle queing itself
		// 
	}

	/**
	 * Thread method that distributes entries
	 */
	@Override
	public void run() {
		while (true) {
			// Consume from bus queue
			List<AisBusEntry> entries = busQueue.pull(readBatchSize);
			// Iterate through entries
			for (AisBusEntry entry : entries) {
				// Filter messages
				if (filters.rejectedByFilter(entry.getPacket())) {
					continue;
				}
				// Distribute to consumers
				// TODO
			}
		}
		
	}
	
}
