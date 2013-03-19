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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBus;

import net.jcip.annotations.ThreadSafe;

/**
 * Thread class to read from a message queue and delegating to a handler
 */
@ThreadSafe
public class MessageQueueReader<T> extends Thread {
    
    private static final Logger LOG = LoggerFactory.getLogger(AisBus.class);

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
        
        LOG.debug("Stopping message queue reader thread");
    }

    public IMessageQueue<T> getQueue() {
        return queue;
    }
    
    public IQueueEntryHandler<T> getHandler() {
        return handler;
    }

}
