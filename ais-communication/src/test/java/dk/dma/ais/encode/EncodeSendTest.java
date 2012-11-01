package dk.dma.ais.encode;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.reader.SendRequest;
import dk.dma.ais.sentence.Abm;

public class EncodeSendTest {

    @Test
    public void encodeSrmAbmTest() throws SendException, SixbitException {
        int destination = 992199007;
        String message = "START TEST TEST TEST END";

        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(destination);
        msg12.setMessage(message);

        SendRequest sendRequest = new SendRequest(msg12, 1, destination);

        String[] sentences = sendRequest.createSentences();
        System.out.println("MSG12 SendRequest sentences:\n" + StringUtils.join(sentences, "\r\n"));

        Abm abm = new Abm();
        abm.setTextData(msg12);
        abm.setSequence(0);
        abm.setTotal(1);
        abm.setNum(1);
        String encoded = abm.getEncoded();
        System.out.println("MSG12 ABM encoded: " + encoded);
    }

}
