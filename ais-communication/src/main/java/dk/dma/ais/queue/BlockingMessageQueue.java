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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.jcip.annotations.ThreadSafe;

/**
 * Implementation of a IMessageQueue using a Java ArrayBlockingQueue
 */
@ThreadSafe
public class BlockingMessageQueue<T> implements IMessageQueue<T> {

    private static final int DEFAULT_MAX_SIZE = 1000;

    private final int limit;
    private final BlockingQueue<T> queue;

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
    public T pull() throws InterruptedException {
        T entry = null;
        do {
            entry = queue.take();
        } while (entry == null);
        return entry;
    }

    @Override
    public List<T> pull(List<T> l, int maxElements) throws InterruptedException {
        // Wait for element to become available
        l.add(queue.take());
        // Get up to maxElements - 1 more
        queue.drainTo(l, maxElements - 1);
        return l;
    }

    @Override
    public List<T> pullAll(List<T> l) throws InterruptedException {
        return pull(l, Integer.MAX_VALUE);
    }

    public int getLimit() {
        return limit;
    }

}
