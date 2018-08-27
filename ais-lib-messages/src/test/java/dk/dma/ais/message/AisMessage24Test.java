package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisMessage24Test {

    /* https://fossies.org/linux/gpsd/test/sample.aivdm
        One pair of type A and Type B messages.
        Checked against the Maritec decoder.
        MessageID:         24
        RepeatIndicator:   0
        UserID:            271041815
        partnum:           0
        name:              PROGUY
    */
    private final String messagePartA = "!AIVDM,1,1,,A,H42O55i18tMET00000000000000,2*6D";

    /*
         MessageID:         24
         RepeatIndicator:   0
         UserID:            271041815
         partnum:           0
         shipandcargo:      60
         vendorid:          1D00014
         callsign:          TC6163
         dimA:              0
         dimB:              15
         dimC:              0
         dimD:              5
    */
    private final String messagePartB = "!AIVDM,1,1,,A,H42O55lti4hhhilD3nink000?050,0*40";

    @Test
    public void partATest() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(messagePartA);
        AisMessage aisMessage = AisMessage.getInstance(vdm);

        assertTrue(aisMessage instanceof AisMessage24);
        assertEquals(24, aisMessage.getMsgId());
        assertEquals(0, aisMessage.getRepeat());
        assertEquals(271041815, aisMessage.getUserId());
        assertEquals(0, ((AisMessage24)aisMessage).getPartNumber());
        assertEquals("PROGUY@@@@@@@@@@@@@@", ((AisMessage24) aisMessage).getName());
    }

    @Test
    public void partBTest() throws SentenceException, AisMessageException, SixbitException {
        Vdm vdm = new Vdm();
        vdm.parse(messagePartB);
        AisMessage aisMessage = AisMessage.getInstance(vdm);

        assertTrue(aisMessage instanceof AisMessage24);
        assertEquals(24, aisMessage.getMsgId());
        assertEquals(0, aisMessage.getRepeat());
        assertEquals(271041815, aisMessage.getUserId());
        assertEquals(1, ((AisMessage24) aisMessage).getPartNumber());
        assertEquals(60, ((AisMessage24) aisMessage).getShipType());
        assertEquals("1D00014", ((AisMessage24) aisMessage).getVendorId());
        assertEquals("TC6163@", ((AisMessage24) aisMessage).getCallsign());
        assertEquals(0, ((AisMessage24) aisMessage).getDimBow());
        assertEquals(0, ((AisMessage24) aisMessage).getDimPort());
        assertEquals(15, ((AisMessage24) aisMessage).getDimStern());
        assertEquals(5, ((AisMessage24) aisMessage).getDimStarboard());
        assertEquals(0, ((AisMessage24) aisMessage).getPosType());
        assertEquals(0, ((AisMessage24) aisMessage).getSpare());
    }
}
