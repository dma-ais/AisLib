package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisMessage7Test {

    @Test
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse(
            "!AIVDM,1,1,,A,701uEO1:CovP00,4*6A"
        );

        AisMessage message = AisMessage.getInstance(vdm);
        assertTrue(message instanceof AisMessage7);
        AisMessage7 msg7 = (AisMessage7) message;
        assertEquals(2655135, msg7.getUserId());
        assertEquals(4, msg7.getMsgId());
    }

}
