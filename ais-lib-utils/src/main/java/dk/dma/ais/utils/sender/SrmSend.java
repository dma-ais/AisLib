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
package dk.dma.ais.utils.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.reader.SendRequest;
import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

public final class SrmSend implements Consumer<Abk> {

    private static final Logger LOG = LoggerFactory.getLogger(SrmSend.class);

    private Abk abk;
    private boolean abkReceived;
    private AisReader aisReader;

    private SrmSend(String hostPort) {
        aisReader = AisReaders.createReader(hostPort);
        // Start reader thread
        aisReader.start();
    }

    /**
     * Receive the ABK result
     */
    public void accept(Abk abk) {
        synchronized (this) {
            this.abk = abk;
            this.abkReceived = true;
        }
    }

    private void sendMessage(int destination, String message) {
        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(destination);
        msg12.setMessage(message);

        LOG.info("msg12: " + msg12);
        // Make send request
        SendRequest sendRequest = new SendRequest(msg12, 1, destination);

        // Wait for connected
        while (aisReader.getStatus() == AisReader.Status.DISCONNECTED) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOG.info("Send request");
        try {
            aisReader.send(sendRequest, this);
        } catch (SendException e) {
            e.printStackTrace();
        }
        LOG.info("Busy wait for result");

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                if (abkReceived) {
                    break;
                }
            }

        }

        LOG.info("ABK received: " + abk);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length < 3) {
            usage();
            System.exit(1);
        }
        String hostPort = args[0];
        int destination = Integer.parseInt(args[1]);
        String message = args[2];

        SrmSend srmSend = new SrmSend(hostPort);
        srmSend.sendMessage(destination, message);

        System.exit(0);
    }

    public static void usage() {
        System.out.println("Usage: SrmSend <host:port> <destination> <message>");

    }

}
