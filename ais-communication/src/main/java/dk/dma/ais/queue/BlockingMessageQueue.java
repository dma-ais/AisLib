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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of a IMessageQueue using a Java ArrayBlockingQueue
 */
public class BlockingMessageQueue<T> implements IMessageQueue<T> {

    private static final int DEFAULT_MAX_SIZE = 1000;

    private int limit;
    private BlockingQueue<T> queue;

    public BlockingMessageQueue() {
        this(DEFAULT_MAX_SIZE);
    }

    public BlockingMessageQueue(int limit) {
        this.limit = limit;
        this.queue = new ArrayBlockingQueue<>(limit);
    }

    @Override
    public int push(T content) throws MessageQueueOverflowException {
        if (!queue.offer(content)) {
            throw new MessageQueueOverflowException();
        }
        return queue.size();
    }

    @Override
    public T pull() {
        T entry = null;
        do {
            try {
                entry = queue.take();
            } catch (InterruptedException e) {
            }
        } while (entry == null);
        return entry;
    }

    @Override
    public List<T> pull(int maxElements) {
        List<T> list = new ArrayList<>();
        // Wait for element to become available
        try {
            list.add(queue.take());
        } catch (InterruptedException e) {
            return list;
        }
        // Get up to maxElements - 1 more
        queue.drainTo(list, maxElements - 1);
        return list;
    }

    @Override
    public List<T> pullAll() {
        return pull(Integer.MAX_VALUE);
    }

    public int getLimit() {
        return limit;
    }

}
