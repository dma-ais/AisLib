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
package dk.dma.ais.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

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
    private Consumer<Abk> resultListener;
    /**
     * The pool of send threads that this send thread is member of
     */
    private SendThreadPool sendThreadPool;
    /**
     * The returned ABK
     */
    private Abk abk;

    public SendThread(String hash, Consumer<Abk> resultListener, SendThreadPool sendThreadPool) {
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
        resultListener.accept(abk);

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
            resultListener.accept(null);
        }

        // Remove from pool
        sendThreadPool.removeThread(hash);
    }

}
