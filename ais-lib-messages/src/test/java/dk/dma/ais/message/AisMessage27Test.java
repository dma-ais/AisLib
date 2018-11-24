package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class AisMessage27Test {

    private final String rawMessage = "!AIVDM,1,1,,A,KCQ9r=hrFUnH7P00,0*41";

    private AisMessage getMessage() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(rawMessage);
        return AisMessage.getInstance(vdm);
    }

    @Test
    public void typeTest() throws SixbitException, SentenceException, AisMessageException {
        AisMessage message = getMessage();
        assertTrue(message instanceof AisMessage27);
    }

    @Test
    public void parseTest() throws SentenceException, AisMessageException, SixbitException {
        AisMessage27 aisMessage27 = (AisMessage27) getMessage();

        assertEquals(-154.201666, aisMessage27.getPos().getLongitudeDouble(), 1e-6);
        assertEquals(87.065, aisMessage27.getPos().getLatitudeDouble(), 1e-6);
        assertEquals(0, aisMessage27.getSog());
        assertEquals(0, aisMessage27.getCog());
        assertEquals(0, aisMessage27.getPosAcc());
        assertEquals(236091959, aisMessage27.getUserId());
        assertEquals(3, aisMessage27.getNavStatus());
        assertEquals(0, aisMessage27.getGnssPosStatus());
        assertEquals(0, aisMessage27.getSpare());
    }

    @Test
    public void encodedTest() throws SixbitException, SentenceException, AisMessageException {
        AisMessage27 message = (AisMessage27) getMessage();

        SixbitEncoder encoded = message.getEncoded();

        assertEquals(96, encoded.getLength());
        assertEquals("KCQ9r=hrFUnH7P00", encoded.encode());
    }

}
