/*
 * Copyright 2011 Danish Maritime Authority. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY Danish Maritime Authority ``AS IS'' 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of Danish Maritime Authority.
 * 
 */
package dk.dma.ais.reader;

import dk.dma.ais.sentence.Abk;

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
public class ClientSendThread extends Thread implements ISendResultListener {

    protected AisReader aisReader;
    protected SendRequest sendRequest;
    protected Abk abk = null;
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
    public void sendResult(Abk abk) {
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
