package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisMessage8Test {

    @Test
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse("!BSVDM,1,1,,A,8000000Iv1@iH9wRNQB3QicpgvO?VBnrLH>=p7V5?h,4*28");

        AisMessage message = AisMessage11.getInstance(vdm);

        assertTrue(message instanceof AisMessage8);
        assertEquals(8, message.getMsgId());
        assertEquals(0, message.getUserId());
    }

}