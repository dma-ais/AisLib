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

import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.ThreadSafe;

/**
 * Thread class to read from a message queue and delegating to a handler
 */
@ThreadSafe
public class MessageQueueReader<T> extends Thread {
    
    private final IQueueEntryHandler<T> handler;
    private final IMessageQueue<T> queue;
    private final int pullMaxElements;

    public MessageQueueReader(IQueueEntryHandler<T> handler, IMessageQueue<T> queue) {
        this(handler, queue, 1);
    }

    public MessageQueueReader(IQueueEntryHandler<T> handler, IMessageQueue<T> queue, int pullMaxElements) {
        this.handler = handler;
        this.queue = queue;
        this.pullMaxElements = pullMaxElements;
    }

    @Override
    public void run() {
        List<T> list = new ArrayList<>();
        // Read loop
        while (true) {
            try {
                queue.pull(list, pullMaxElements);
            } catch (InterruptedException e) {                
                break;
            }
            for (T entry : list) {
                handler.receive(entry);
            }
            list.clear();
        }
        
    }

    public IMessageQueue<T> getQueue() {
        return queue;
    }
    
    public IQueueEntryHandler<T> getHandler() {
        return handler;
    }
    
    public void cancel() {
        this.interrupt();
    }

}
