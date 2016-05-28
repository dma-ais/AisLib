package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisMessage19Test {

    @Test
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDM,1,1,,B,C5N3SRgPEnJGEBT>NhWAwwo862PaLELTBJ:V00000000S0D:R220,0*0B");
        // http://fossies.org/linux/gpsd/test/sample.aivdm
        //        MessageID:         19
        //        774 #        RepeatIndicator:   0
        //        775 #        UserID:            367059850
        //        776 #        Spare:             0
        //        777 #        SOG:               8.7
        //        778 #        PositionAccuracy:  0
        //        779 #        longitude:         -88.8103916667
        //        780 #        latitude:          29.543695
        //        781 #        COG:               335.9
        //        782 #        TrueHeading:       511
        //        783 #        TimeStamp:         46
        //        784 #        Spare2:            0
        //        785 #        name:              CAPT.J.RIMES
        //        786 #        shipandcargo:      70
        //        787 #        dimA:              5
        //        788 #        dimB:              21
        //        789 #        dimC:              4
        //        790 #        dimD:              4
        //        791 #        fixtype:           1
        //        792 #        RAIM:              False
        //        793 #        DTE:               0
        //        794 #        Spare3:            0

        AisMessage message = AisMessage.getInstance(vdm);

        assertTrue(message instanceof AisMessage19);
        assertEquals(19, message.getMsgId());
        assertEquals(0, message.getRepeat());
        assertEquals(8.7, ((AisMessage19) message).getSog()/10f, 1e-6);
        assertEquals(0, ((AisMessage19) message).getPosAcc());
        assertEquals(29.543695, ((AisMessage19) message).getPos().getLatitudeDouble(), 1e-6);
        assertEquals(-88.810392, ((AisMessage19) message).getPos().getLongitudeDouble(), 1e-6);
        assertEquals(335.9, ((AisMessage19) message).getCog()/10f, 1e-3);
        assertEquals(511, ((AisMessage19) message).getTrueHeading());
        assertEquals(46, ((AisMessage19) message).getUtcSec());
        assertEquals("CAPT.J.RIMES@@@@@@@@", ((AisMessage19) message).getName());
        assertEquals(70, ((AisMessage19) message).getShipType());
        assertEquals(21, ((AisMessage19) message).getDimStern());
        assertEquals(5, ((AisMessage19) message).getDimBow());
        assertEquals(4, ((AisMessage19) message).getDimPort());
        assertEquals(4, ((AisMessage19) message).getDimStarboard());
        assertEquals(0, ((AisMessage19) message).getRaim());
        assertEquals(0, ((AisMessage19) message).getDte());
//        assertEquals(0, ((AisMessage19) message).getSpare1());
//        assertEquals(0, ((AisMessage19) message).getSpare2());
//        assertEquals(0, ((AisMessage19) message).getSpare3());

    }

}
