package dk.dma.ais.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.message.binary.InlandVoyage;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

public class AisMessage8Test {

    @Test
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse("!BSVDM,1,1,,A,8000000Iv1@iH9wRNQB3QicpgvO?VBnrLH>=p7V5?h,4*28");

        AisMessage message = AisMessage.getInstance(vdm);

        assertTrue(message instanceof AisMessage8);
        assertEquals(8, message.getMsgId());
        assertEquals(0, message.getUserId());
    }
    
    @Test
    public void inlandVoyagetest() throws AisMessageException, SixbitException, SentenceException {
        Vdm inlandVdm = new Vdm();
        inlandVdm.parse("!AIVDM,1,1,,B,83aDoLPj2StdtMuN=Qb@ggbi00h0,0*7C");
        AisMessage message = AisMessage.getInstance(inlandVdm);
        
        assertTrue(message instanceof AisMessage8);
        
        AisMessage8 message8 = (AisMessage8) message;
        
        assertEquals(200, message8.getDac());
        assertEquals(10, message8.getFi());
        
        InlandVoyage inlandMessage = (InlandVoyage) AisApplicationMessage.getInstance(message8);
        
        assertEquals("2317586", inlandMessage.getVesselId());
        assertEquals(850, inlandMessage.getLengthOfShip());
        assertEquals(95, inlandMessage.getBeamOfShip());
        assertEquals(8022, inlandMessage.getCombinationType());
        assertEquals(1, inlandMessage.getHazardousCargo());
        assertEquals(0, inlandMessage.getDraught());
        assertEquals(1, inlandMessage.getLoadedOrUnloaded());
        assertEquals(1, inlandMessage.getQualityOfSpeedData());
        assertEquals(0, inlandMessage.getQualityOfCourseData());
        assertEquals(0, inlandMessage.getQualityOfHeadingData());
    }
}