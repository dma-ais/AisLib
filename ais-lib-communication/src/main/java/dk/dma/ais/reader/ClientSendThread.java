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

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

/**
 * Thread for clients to use for sending. E.g.
 * <p>
 * AisReader aisReader = new AisReader(...); aisReader.start();
 * <p>
 * SendRequest sendRequest = new SendRequest(...);
 * <p>
 * ClientSendThread client = new ClientSendThread(aisReader, sendRequest); client.send();
 * <p>
 * client.isSuccess();
 */
class ClientSendThread extends Thread implements Consumer<Abk> {

    /**
     * The Ais reader.
     */
    protected AisReader aisReader;
    /**
     * The Send request.
     */
    protected SendRequest sendRequest;
    /**
     * The Abk.
     */
    protected Abk abk;
    /**
     * The Abk received.
     */
    protected boolean abkReceived;
    /**
     * The Timeout.
     */
    protected long timeout = 60000; // 60 sec default

    /**
     * Instantiates a new Client send thread.
     *
     * @param aisReader   the ais reader
     * @param sendRequest the send request
     */
    public ClientSendThread(AisReader aisReader, SendRequest sendRequest) {
        this.aisReader = aisReader;
        this.sendRequest = sendRequest;
    }

    /**
     * Send abk.
     *
     * @return the abk
     * @throws SendException        the send exception
     * @throws InterruptedException the interrupted exception
     */
    public Abk send() throws SendException, InterruptedException {
        // Send message
        aisReader.send(sendRequest, this);

        // Start thread
        this.start();

        // Wait for thread to exit
        this.join();

        // If no ABK we have an exception
        if (abk == null) {
            throw new SendException("Timeout waiting for ABK");
        }

        return abk;
    }

    @Override
    public void run() {
        // Busy wait for ABK
        long start = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                if (abkReceived) {
                    break;
                }
            }
            if (timeout > 0) {
                if (System.currentTimeMillis() - start > timeout) {
                    break;
                }
            }
        }

        // If ABK does not exist now, it is because of timeout

    }

    @Override
    public void accept(Abk abk) {
        synchronized (this) {
            this.abk = abk;
            this.abkReceived = true;
        }
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets abk.
     *
     * @return the abk
     */
    public Abk getAbk() {
        return abk;
    }

    /**
     * Gets abk result.
     *
     * @return the abk result
     */
    public Abk.Result getAbkResult() {
        synchronized (this) {
            if (abkReceived) {
                return abk.getResult();
            }
        }
        return null;
    }

    /**
     * Is success boolean.
     *
     * @return the boolean
     */
    public boolean isSuccess() {
        synchronized (this) {
            if (!abkReceived) {
                return false;
            }
        }
        return abk.isSuccess();
    }

}
