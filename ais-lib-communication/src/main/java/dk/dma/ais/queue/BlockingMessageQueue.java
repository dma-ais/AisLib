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
    public int put(T content) throws InterruptedException {
        queue.put(content);
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
