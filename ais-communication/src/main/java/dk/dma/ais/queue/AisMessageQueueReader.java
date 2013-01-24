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

/**
 * Thread class to read from a message queue and delegating to and IAisHandler
 */
public class AisMessageQueueReader extends Thread {

    private IAisQueueEntryHandler aisHandler;
    private IAisMessageQueue queue;

    public AisMessageQueueReader(IAisQueueEntryHandler aisHandler, IAisMessageQueue queue) {
        this.aisHandler = aisHandler;
        this.queue = queue;
    }

    @Override
    public void run() {
        // Read loop
        while (true) {
            aisHandler.receive(queue.pull());
        }
    }

    public IAisMessageQueue getQueue() {
        return queue;
    }

}
