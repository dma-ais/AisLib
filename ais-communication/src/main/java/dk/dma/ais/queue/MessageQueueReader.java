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

/**
 * Thread class to read from a message queue and delegating to a handler
 */
public class MessageQueueReader<T> extends Thread {

    private final IQueueEntryHandler<T> handler;
    private final IMessageQueue<T> queue;
    private final int bathSize;

    public MessageQueueReader(IQueueEntryHandler<T> handler, IMessageQueue<T> queue) {
        this(handler, queue, 1);
    }

    public MessageQueueReader(IQueueEntryHandler<T> handler, IMessageQueue<T> queue, int batchSize) {
        this.handler = handler;
        this.queue = queue;
        this.bathSize = batchSize;
    }

    @Override
    public void run() {
        // Read loop
        while (true) {
            List<T> list = queue.pull(bathSize);
            for (T entry : list) {
                handler.receive(entry);
            }
        }
    }

    public IMessageQueue<T> getQueue() {
        return queue;
    }

}
