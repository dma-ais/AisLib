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
package dk.dma.ais.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;

/**
 * A thread class that handles the sending and the wait for an acknowledge.
 */
public class SendThread extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(SendThread.class);

    private static final long SLEEP_INTERVAL = 100;
    private static final long TIMEOUT = 15 * 1000; // 15 sec

    /**
     * Hash value for this send thread
     */
    private String hash;
    /**
     * The result listener wanting reply
     */
    private ISendResultListener resultListener;
    /**
     * The pool of send threads that this send thread is member of
     */
    private SendThreadPool sendThreadPool;
    /**
     * The returned ABK
     */
    private Abk abk;

    public SendThread(String hash, ISendResultListener resultListener, SendThreadPool sendThreadPool) {
        setDaemon(true);
        this.hash = hash;
        this.resultListener = resultListener;
        this.sendThreadPool = sendThreadPool;
    }

    /**
     * Set the result for this thread
     * 
     * @param abk
     */
    public synchronized void setAbk(Abk abk) {
        if (this.abk != null) {
            return;
        }
        LOG.debug("Setting ABK: " + abk);
        this.abk = abk;
    }

    private synchronized boolean checkForAbk() {
        if (abk == null) {
            return false;
        }

        // Ignore LATE_RECEPTION(4)
        if (abk.getResult() == Abk.Result.LATE_RECEPTION) {
            return false;
        }

        // Notify result listener
        LOG.debug("Notifying listeners of ABK: " + abk);
        resultListener.sendResult(abk);

        return true;
    }

    /**
     * Wait for result or timeout, using busy wait
     */
    @Override
    public void run() {
        long elapsed = 0;
        while (elapsed < TIMEOUT) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {}

            // See if result is known
            boolean abkReceived = checkForAbk();
            if (abkReceived) {
                break;
            }
            elapsed += SLEEP_INTERVAL;
        }

        // Check for timeout
        if (elapsed >= TIMEOUT) {
            resultListener.sendResult(null);
        }

        // Remove from pool
        sendThreadPool.removeThread(hash);
    }

}
