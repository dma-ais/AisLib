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
