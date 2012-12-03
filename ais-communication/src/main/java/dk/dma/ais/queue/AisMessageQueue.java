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
package dk.dma.ais.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import dk.dma.ais.message.AisMessage;

/**
 * Implementation of an AisMessageQueue using a Java ArrayBlockingQueue
 */
public class AisMessageQueue implements IAisMessageQueue {

    private int limit;
    private BlockingQueue<AisMessageQueueEntry> queue;

    public AisMessageQueue() {
        this(1000);
    }

    public AisMessageQueue(int limit) {
        this.limit = limit;
        this.queue = new ArrayBlockingQueue<>(limit);
    }

    @Override
    public int push(AisMessage aisMessage) throws AisMessageQueueOverflowException {
        if (!queue.offer(new AisMessageQueueEntry(aisMessage))) {
            throw new AisMessageQueueOverflowException();
        }
        return queue.size();
    }

    @Override
    public AisMessageQueueEntry pull() {
        AisMessageQueueEntry entry = null;
        do {
            try {
                entry = queue.take();
            } catch (InterruptedException e) {}
        } while (entry == null);
        return entry;
    }

    @Override
    public List<AisMessageQueueEntry> pullAll() {
        List<AisMessageQueueEntry> list = new ArrayList<>();
        queue.drainTo(list);
        return list;
    }

    public int getLimit() {
        return limit;
    }

}
