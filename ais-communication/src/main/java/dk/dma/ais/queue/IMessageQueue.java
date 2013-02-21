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
package dk.dma.ais.queue;

import java.util.List;

import net.jcip.annotations.ThreadSafe;

/**
 * Queue for transporting various data. Suited for AIS use.
 */
@ThreadSafe
public interface IMessageQueue<T> {

    /**
     * Push AisMessage onto the queue
     * 
     * @param aisMessage
     * @return the number of elements on the queue after the insertion
     * @throws MessageQueueOverflowException
     *             when capacity limit has been reached
     */
    int push(T content) throws MessageQueueOverflowException;

    /**
     * Pull message from the queue. This must be implemented as a blocking call.
     * 
     * @return AisMessageQueueEntry
     */
    T pull();

    /**
     * Pull up to maxElements from queue. This must be a blocking call.
     * 
     * @param l list to add elements to 
     * @param maxElements
     * @return list with added elements
     */
    List<T> pull(List<T> l, int maxElements);

    /**
     * Pull all current message on the queue. This must be a blocking call.
     * 
     * @param l list to add elements to 
     * @return list with added elements
     */
    List<T> pullAll(List<T> c);

}
