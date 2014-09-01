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
 * 
 * AisReader aisReader = new AisReader(...); aisReader.start();
 * 
 * SendRequest sendRequest = new SendRequest(...);
 * 
 * ClientSendThread client = new ClientSendThread(aisReader, sendRequest); client.send();
 * 
 * client.isSuccess();
 * 
 */
class ClientSendThread extends Thread implements Consumer<Abk> {

    protected AisReader aisReader;
    protected SendRequest sendRequest;
    protected Abk abk;
    protected boolean abkReceived;
    protected long timeout = 60000; // 60 sec default

    public ClientSendThread(AisReader aisReader, SendRequest sendRequest) {
        this.aisReader = aisReader;
        this.sendRequest = sendRequest;
    }

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

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Abk getAbk() {
        return abk;
    }

    public Abk.Result getAbkResult() {
        synchronized (this) {
            if (abkReceived) {
                return abk.getResult();
            }
        }
        return null;
    }

    public boolean isSuccess() {
        synchronized (this) {
            if (!abkReceived) {
                return false;
            }
        }
        return abk.isSuccess();
    }

}
