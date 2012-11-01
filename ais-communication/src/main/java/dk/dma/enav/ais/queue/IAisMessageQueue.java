/* Copyright (c) 2012 Danish Maritime Authority
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
package dk.dma.enav.ais.queue;

import java.util.List;

import dk.dma.enav.ais.message.AisMessage;

/**
 * Blocking queue as transportation medium of AisMessages.
 */
public interface IAisMessageQueue {

    /**
     * Push AisMessage onto the queue
     * 
     * @param aisMessage
     * @return the number of elements on the queue after the insertion
     * @throws AisMessageQueueOverflowException
     *             when capacity limit has been reached
     */
    public int push(AisMessage aisMessage) throws AisMessageQueueOverflowException;

    /**
     * Pull message from the queue. This must be implemented as a blocking call.
     * 
     * @return AisMessageQueueEntry
     */
    public AisMessageQueueEntry pull();

    /**
     * Pull all current message on the queue. This is not a blocking call.
     * 
     * @return
     */
    public List<AisMessageQueueEntry> pullAll();

}
