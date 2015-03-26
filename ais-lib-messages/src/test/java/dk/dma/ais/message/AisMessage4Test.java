package dk.dma.ais.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

public class AisMessage4Test {

    @Test(expected = AisMessageException.class)
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,401uEPQuK0P230Cl=6MEqEi000S:00,4*61"
                );

        AisMessage.getInstance(vdm);
    }

    @Test
    public void test2() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse(
                "!AIVDM,1,1,,A,402R3WiuH@jaaPtgjhOgcA7000S:,0*7D"
                );

        AisMessage message = AisMessage.getInstance(vdm);
        assertTrue(message instanceof AisMessage4);
        AisMessage4 msg4 = (AisMessage4) message;
        assertEquals(2655135, msg4.getUserId());
        assertEquals(4, msg4.getMsgId());
        assertEquals(new AisPosition(33286980,7962200), msg4.getPos());
        assertEquals(1, msg4.getPosAcc());
        assertEquals(7, msg4.getPosType());
        assertEquals(1136140901L, msg4.getDate().toInstant().getEpochSecond());
        assertEquals(1, msg4.getUtcDay());
        assertEquals(18, msg4.getUtcHour());
        assertEquals(41, msg4.getUtcMinute());
        assertEquals(41, msg4.getUtcSecond());
        assertEquals(1, msg4.getUtcMonth());
        assertEquals(2006, msg4.getUtcYear());
    }
}