package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisMessage18Test {

    @Test
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse("!BSVDM,1,1,,B,B3@nk60000<EwnWpl=e1gwm5oP06,0*0D");

        AisMessage message = AisMessage11.getInstance(vdm);

        assertTrue(message instanceof AisMessage18);
        assertEquals(18, message.getMsgId());
        assertEquals(219001624, message.getUserId());
    }

}
