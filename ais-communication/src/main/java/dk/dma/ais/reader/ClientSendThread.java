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

import dk.dma.ais.sentence.Abk;
import dk.dma.enav.util.function.Consumer;

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
public class ClientSendThread extends Thread implements Consumer<Abk> {

    protected AisReader aisReader;
    protected SendRequest sendRequest;
    protected Abk abk;
    protected Boolean abkReceived = false;
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
            synchronized (abkReceived) {
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
        synchronized (abkReceived) {
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
        synchronized (abkReceived) {
            if (abkReceived) {
                return abk.getResult();
            }
        }
        return null;
    }

    public boolean isSuccess() {
        synchronized (abkReceived) {
            if (!abkReceived) {
                return false;
            }
        }
        return abk.isSuccess();
    }

}
