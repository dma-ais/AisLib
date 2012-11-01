/* Copyright (c) 2011 Danish Maritime Safety Administration
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
package dk.dma.ais.reader;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;

/**
 * Class to hold a pool of send threads.
 */
public class SendThreadPool {

    private static final Logger LOG = LoggerFactory.getLogger(SendThreadPool.class);

    /**
     * The pool is implemented as a hash map
     */
    private final Map<String, SendThread> threads = new HashMap<>();

    public SendThreadPool() {}

    /**
     * Create a new send thread and register in pool
     * 
     * @param sendRequest
     * @param resultListener
     * @return
     */
    public synchronized SendThread createSendThread(SendRequest sendRequest, ISendResultListener resultListener) {
        // Generate hash value that uniquely defines message
        String hash = sendHash(sendRequest);

        // Create thread
        LOG.debug("Creating SendThread with hash: " + hash);
        SendThread sendThread = new SendThread(hash, resultListener, this);

        // Add thread to threads
        threads.put(hash, sendThread);
        LOG.debug("Threads in pool: " + threads.size());

        return sendThread;
    }

    /**
     * Handle received ABK
     * 
     * @param abk
     */
    public synchronized void handleAbk(Abk abk) {
        LOG.debug("Handling Abk: " + abk);
        // Find thread
        String hash = abkHash(abk);
        SendThread sendThread = threads.get(hash);
        if (sendThread == null) {
            LOG.debug("SendThread for abk " + abk + " does not exist");
            return;
        }
        // Set Abk for thread
        sendThread.setAbk(abk);
    }

    public synchronized void removeThread(String hash) {
        LOG.debug("Removing thread with hash: " + hash);
        threads.remove(hash);
        LOG.debug("Threads in pool: " + threads.size());
    }

    /**
     * Static method to generate hash from send request
     * 
     * @param sendRequest
     * @return seq+msg id+dest
     */
    public static String sendHash(SendRequest sendRequest) {
        int seq = sendRequest.getSequence();
        int msgId = sendRequest.getAisMessage().getMsgId();
        int destination = sendRequest.getDestination();
        return String.format("%d+%d+%d", seq, msgId, destination);
    }

    /**
     * Static method to generate hash from ABK
     * 
     * @param abk
     * @return seq+msg id+dest
     */
    public static String abkHash(Abk abk) {
        int seq = abk.getSequence();
        int msgId = abk.getMsgId();
        int destination = abk.getDestination();
        return String.format("%d+%d+%d", seq, msgId, destination);
    }

}
