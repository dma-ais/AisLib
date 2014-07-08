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

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.message.AisMessage6;
import dk.dma.ais.message.binary.RouteSuggestionReply;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.sentence.Abk;

public class SendAsmTest {

    @Test
    public void emptyTest() {

    }

    // @Test
    public void sendRouteSuggestionReply() throws InterruptedException, SendException {
        String hostPort = "aistrans1.fomfrv.dk:4001";
        // int destination = 219015063; // DAMSA1
        // int destination = 992199007; // ais-frv-obo
        int destination = 992199100; // SOK

        // Make AisReader instance and start
        AisReader aisReader = AisReaders.createReader(hostPort);
        aisReader.start();
        Thread.sleep(3000);
        if (aisReader.getStatus() != Status.CONNECTED) {
            Assert.fail("Could not connect to AIS source within 3 secs");
        }

        // Make ASM
        RouteSuggestionReply routeSuggestionReply = new RouteSuggestionReply();
        routeSuggestionReply.setRefMsgLinkId(1);
        routeSuggestionReply.setResponse(2);

        // Make ais message 6
        AisMessage6 msg6 = new AisMessage6();
        msg6.setDestination(destination);
        msg6.setAppMessage(routeSuggestionReply);
        msg6.setRetransmit(0);

        // Send
        Abk abk = aisReader.send(msg6, 1, destination);
        // We are now guaranteed to have ABK
        System.out.println("ABK: " + abk);
        Assert.assertTrue(abk.isSuccess());
        aisReader.stopReader();
    }

}
