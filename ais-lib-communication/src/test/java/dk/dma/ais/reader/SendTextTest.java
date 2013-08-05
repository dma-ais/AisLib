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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.reader.AisReader.Status;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.AnySentence;

public class SendTextTest {

    // @Test
    public void sendAddressedTextTest1() throws InterruptedException, SendException {
        // String hostPort = "aistrans1:4001";
        String hostPort = "localhost:4001";
        String message = "TEST FROM FRV";
        int destination = 219015063; // DAMSA1
        // int destination = 219593000; // Jens Sørensen
        // int destination = 219997000; // Poul Løwenørn
        sendAddressedTextSimple(hostPort, destination, message);
        sleep(2000);
    }

    // @Test
    public void sendAddressedTextTest2() throws InterruptedException, SendException {
        String hostPort = "aistrans1:4001";
        String message = "TEST FROM FRV";
        int destination = 219593000;
        sendAddressedTextSimple(hostPort, destination, message);
        sleep(2000);
    }

    // @Test
    public void sendAddressedTextTest3() throws InterruptedException, SendException {
        String hostPort = "aistrans1.fomfrv.dk:4001";
        String message = "TEST FROM DAMSA1";
        int destination = 992199001; // SOK
        // int destination = 992199007; // ais-frv-obo
        sendAddressedTextSimple(hostPort, destination, message);
        sleep(2000);
    }

    // @Test
    public void sendAddressedTextTest4() throws InterruptedException, SendException {
        // String hostPort = "aistrans1:4001";
        String hostPort = "localhost:4001";
        String message = "TEST FROM FRV";
        int destination = 219997000;
        sendAddressedTextSimple(hostPort, destination, message);
        sleep(2000);
    }

    // @Test
    public void sendWithPrefixSentence() throws SendException, InterruptedException {
        AnySentence sentence = new AnySentence();
        sentence.setDelimiter("$");
        sentence.setTalker("PGHP");
        sentence.setFormatter("");
        sentence.addField("23");
        sentence.addField("992199110");
        String virtMmsiSentence = sentence.getEncoded();

        List<String> prefixSentences = new ArrayList<>();
        prefixSentences.add(virtMmsiSentence);

        String hostPort = "localhost:4001";
        // String message = "TEST FROM FRV";
        String message = "A";
        // int destination = 219015063; // DAMSA1
        // int destination = 219593000; // Jens Sørensen
        // int destination = 219997000; // Poul Løwenørn
        int destination = 123456789;
        sendAddressedWithPrefix(hostPort, destination, message, prefixSentences);
        sleep(2000);

    }

    @Test
    public void emptyTest() {

    }

    private void sendAddressedWithPrefix(String hostPort, int destination, String message, List<String> prefixSentences)
            throws InterruptedException, SendException {
        // Make AisReader instance and start
        AisReader aisReader = AisReaders.createForHosts(hostPort);
        aisReader.start();
        Thread.sleep(3000);
        if (aisReader.getStatus() != Status.CONNECTED) {
            Assert.fail("Could not connect to AIS source within 3 secs");
        }

        // Make AIS message 12
        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(destination);
        msg12.setMessage(message);

        // Make sendrequest
        SendRequest sendRequest = new SendRequest(msg12, 2, destination);
        sendRequest.setTalker("PN");
        for (String prefixSentence : prefixSentences) {
            sendRequest.addPrefixSentence(prefixSentence);
        }

        // Send
        ClientSendThread clientSendThread = new ClientSendThread(aisReader, sendRequest);
        Abk abk = clientSendThread.send();

        // We are now guaranteed to have ABK
        System.out.println("ABK: " + abk);
        Assert.assertTrue(abk.isSuccess());
        aisReader.stopReader();
    }

    private void sendAddressedTextSimple(String hostPort, int destination, String message) throws InterruptedException,
            SendException {
        // Make AisReader instance and start
        AisReader aisReader = AisReaders.createForHosts(hostPort);
        aisReader.start();
        Thread.sleep(3000);
        if (aisReader.getStatus() != Status.CONNECTED) {
            Assert.fail("Could not connect to AIS source within 3 secs");
        }

        // Make AIS message 12
        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(destination);
        msg12.setMessage(message);

        // Send
        Abk abk = aisReader.send(msg12, 1, destination);

        // We are now guaranteed to have ABK
        System.out.println("ABK: " + abk);
        Assert.assertTrue(abk.isSuccess());
        aisReader.stopReader();
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
