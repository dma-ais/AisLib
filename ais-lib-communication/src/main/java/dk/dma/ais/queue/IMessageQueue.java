/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
     * Push the specified element on the queue, waiting if necessary for space to become availably
     * @param content
     * @return
     * @throws InterruptedException
     */
    int put(T content) throws InterruptedException;

    /**
     * Pull message from the queue. This must be implemented as a blocking call.
     * 
     * @return AisMessageQueueEntry
     */
    T pull() throws InterruptedException;

    /**
     * Pull up to maxElements from queue. This must be a blocking call.
     * 
     * @param l list to add elements to 
     * @param maxElements
     * @return list with added elements
     */
    List<T> pull(List<T> l, int maxElements) throws InterruptedException;

    /**
     * Pull all current message on the queue. This must be a blocking call.
     * 
     * @param l list to add elements to 
     * @return list with added elements
     */
    List<T> pullAll(List<T> c) throws InterruptedException;

}
