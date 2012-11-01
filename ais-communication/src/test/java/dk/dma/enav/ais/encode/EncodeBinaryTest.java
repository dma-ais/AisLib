package dk.dma.enav.ais.encode;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.enav.ais.binary.SixbitEncoder;
import dk.dma.enav.ais.binary.SixbitException;
import dk.dma.enav.ais.message.AisMessage;
import dk.dma.enav.ais.message.AisMessage6;
import dk.dma.enav.ais.message.AisMessageException;
import dk.dma.enav.ais.message.binary.AsmAcknowledge;
import dk.dma.enav.ais.message.binary.Capability;
import dk.dma.enav.ais.sentence.Abm;
import dk.dma.enav.ais.sentence.SentenceException;
import dk.dma.enav.ais.sentence.Vdm;

public class EncodeBinaryTest {

    /**
     * Test encoding a binary AIS message
     * 
     * @throws BitExhaustionException
     * @throws IllegalArgumentException
     */
    @Test
    public void binaryEncode() throws IllegalArgumentException, SixbitException {
        Capability capability = new Capability();
        capability.setReqDac(296);
        SixbitEncoder encoder = capability.getEncoded();

        int destination = 992199007;

        AisMessage6 msg6 = new AisMessage6();
        msg6.setUserId(219015063);
        msg6.setDestination(destination);
        msg6.setRetransmit(0);
        msg6.setAppMessage(capability);
        encoder = msg6.getEncoded();
        String encoded = encoder.encode();
        String expected = "63@oWUkdShEt04=:000000000000";
        System.out.println("encoded:  " + encoded);
        System.out.println("expected: " + expected);
        System.out.println("pad bits: " + encoder.getPadBits());
        Assert.assertTrue(encoded.equals(expected));

        Vdm vdm = new Vdm();
        vdm.setTalker("AI");
        vdm.setTotal(1);
        vdm.setNum(1);
        vdm.setMessageData(msg6);
        vdm.setSequence(0);
        encoded = vdm.getEncoded();
        System.out.println("VDM encoded: " + encoded);

        Abm abm = new Abm();
        abm.setTalker("AI");
        abm.setTotal(1);
        abm.setNum(1);
        abm.setSequence(0);
        abm.setBinaryData(msg6);
        abm.setDestination(destination);
        encoded = abm.getEncoded();
        System.out.println("ABM encoded: " + encoded);

    }

    @Test
    public void encodeAsmAcknowledge() throws SixbitException, SentenceException, AisMessageException {
        AsmAcknowledge acknowledge = new AsmAcknowledge();
        acknowledge.setReceivedFi(28);
        acknowledge.setReceivedDac(2);
        acknowledge.setAiAvailable(1);
        acknowledge.setAiResponse(1);
        acknowledge.setTextSequenceNum(800);

        AisMessage6 msg6 = new AisMessage6();
        msg6.setDestination(992199013);
        msg6.setAppMessage(acknowledge);
        msg6.setRetransmit(0);

        String[] sentences = Vdm.createSentences(msg6, 0);
        Assert.assertEquals(1, sentences.length);
        Vdm vdm = new Vdm();
        int res = vdm.parse(sentences[0]);
        Assert.assertEquals(0, res);
        msg6 = (AisMessage6) AisMessage.getInstance(vdm);
        acknowledge = (AsmAcknowledge) msg6.getApplicationMessage();
        Assert.assertEquals(28, acknowledge.getReceivedFi());
        Assert.assertEquals(2, acknowledge.getReceivedDac());
        Assert.assertEquals(1, acknowledge.getAiAvailable());
        Assert.assertEquals(1, acknowledge.getAiResponse());
        Assert.assertEquals(800, acknowledge.getTextSequenceNum());
    }

}
