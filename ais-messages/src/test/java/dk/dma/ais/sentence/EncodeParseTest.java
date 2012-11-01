package dk.dma.ais.sentence;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessage6;
import dk.dma.ais.message.AisMessage8;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.binary.AsmAcknowledge;
import dk.dma.ais.message.binary.BroadcastRouteInformation;
import dk.dma.ais.sentence.Abm;
import dk.dma.ais.sentence.Bbm;
import dk.dma.ais.sentence.SendSentence;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

public class EncodeParseTest {

    /**
     * Test parsing of encoded ABM message
     * 
     * @throws SixbitException
     * @throws SentenceException
     * @throws AisMessageException
     */
    @Test
    public void testAbmEncodeParse() throws SixbitException, SentenceException, AisMessageException {
        // Make ABM
        AisMessage12 msg12 = new AisMessage12();
        String msg = "LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE LONG MESSAGE";
        msg12.setMessage(msg);
        Abm abm = new Abm();
        abm.setTalker("AI");
        abm.setDestination(992199007);
        abm.setTextData(msg12);
        abm.setSequence(1);
        abm.setChannel(null);

        // Encode ABM
        SendSentence[] sentences = abm.split();
        List<String> strSentences = new ArrayList<>();
        for (SendSentence sendSentence : sentences) {
            strSentences.add(sendSentence.getEncoded());
        }
        Assert.assertEquals(2, strSentences.size());

        // Parse ABM
        abm = new Abm();
        int res = abm.parse(strSentences.get(0));
        Assert.assertEquals(1, res);
        res = abm.parse(strSentences.get(1));
        Assert.assertEquals(0, res);

        Assert.assertEquals(1, abm.getSequence());
        Assert.assertEquals(992199007, abm.getDestination());
        Assert.assertEquals(12, abm.getMsgId());

        // Make VDM
        Vdm[] vdms = abm.makeVdm(219000000, 0, 0).createSentences();
        String[] strVdms = new String[vdms.length];
        for (int i = 0; i < vdms.length; i++) {
            strVdms[i] = vdms[i].getEncoded();
        }
        Assert.assertEquals(2, strVdms.length);

        // Parse VDM
        Vdm vdm = new Vdm();
        Assert.assertEquals(1, vdm.parse(strVdms[0]));
        Assert.assertEquals(0, vdm.parse(strVdms[1]));

        // Get AIS message
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        Assert.assertEquals(12, aisMessage.getMsgId());
        msg12 = (AisMessage12) aisMessage;
        Assert.assertTrue(msg12.getMessage().equals(msg));
    }

    @Test
    public void testMsg6AbmDecode() throws SentenceException, SixbitException, AisMessageException {
        // Parse ABM
        Abm abm = new Abm();
        Assert.assertEquals(0, abm.parse("!AIABM,1,1,2,992199014,0,6,04D0G6EB00000000,0*72"));
        Assert.assertEquals(2, abm.getSequence());
        Assert.assertEquals(992199014, abm.getDestination());

        // Make VDM from ABM
        Vdm[] vdms = abm.makeVdm(219000000, 0, 0).createSentences();
        String[] sentences = new String[vdms.length];
        for (int i = 0; i < vdms.length; i++) {
            sentences[i] = vdms[i].getEncoded();
        }
        Assert.assertEquals(1, sentences.length);
        Assert.assertTrue(vdms[0].getChannel() == '0');

        // Parse VDM
        Vdm vdm = new Vdm();
        Assert.assertEquals(0, vdm.parse(sentences[0]));
        // Get AIS message
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        Assert.assertEquals(6, aisMessage.getMsgId());
        AisMessage6 msg6 = (AisMessage6) aisMessage;
        Assert.assertEquals(219000000, msg6.getUserId());
        Assert.assertEquals(992199014, msg6.getDestination());
        Assert.assertEquals(1, msg6.getDac());
        Assert.assertEquals(5, msg6.getFi());
        AsmAcknowledge asmAcknowledge = (AsmAcknowledge) msg6.getApplicationMessage();
        Assert.assertEquals(1, asmAcknowledge.getReceivedDac());
        Assert.assertEquals(28, asmAcknowledge.getReceivedFi());
        Assert.assertEquals(810, asmAcknowledge.getTextSequenceNum());
    }

    @Test
    public void testMsg12AbmDecode() throws SentenceException, SixbitException, AisMessageException {
        // Parse ABM
        Abm abm = new Abm();
        Assert.assertEquals(0, abm.parse("!AIABM,1,1,0,219015063,0,12,CD1BDPD5CDP6B?=P41=C1P5>4,0*0B"));
        Assert.assertEquals(0, abm.getSequence());
        Assert.assertEquals(219015063, abm.getDestination());

        // Make VDM from ABM
        Vdm[] vdms = abm.makeVdm(219000000, 0, 0).createSentences();
        String[] sentences = new String[vdms.length];
        for (int i = 0; i < vdms.length; i++) {
            sentences[i] = vdms[i].getEncoded();
        }
        Assert.assertEquals(1, sentences.length);
        Assert.assertTrue(vdms[0].getChannel() == '0');

        // Parse VDM
        Vdm vdm = new Vdm();
        Assert.assertEquals(0, vdm.parse(sentences[0]));
        // Get AIS message
        AisMessage aisMessage = AisMessage.getInstance(vdm);
        Assert.assertEquals(12, aisMessage.getMsgId());
        AisMessage12 msg12 = (AisMessage12) aisMessage;
        Assert.assertEquals(219000000, msg12.getUserId());
        Assert.assertEquals(219015063, msg12.getDestination());
        Assert.assertTrue(msg12.getMessage().equals("START TEST FROM DAMSA END"));
    }

    @Test
    public void testBbmDecode() throws SentenceException, SixbitException, AisMessageException {
        String line1 = "!AIBBM,4,1,0,0,8,05d00EOdJ0:gP3Vs6QwPH41kkE0wiKk0r3K<OqT:0M7Q`?u,0*2B";
        String line2 = "!AIBBM,4,2,0,0,8,v@0>UH67wFK@7BVP3wk@D3aQD1wv;p1lBoi04K<0qs8@P4r,0*2B";
        String line3 = "!AIBBM,4,3,0,0,8,f0LW3P@3w10>=;P83JE06u@644D:03IL122vTH1b7wQ1hWT,0*1F";
        String line4 = "!AIBBM,4,4,0,0,8,0kK10Q8G80He5R@jbE6,3*24";

        // Parse BBM
        Bbm bbm = new Bbm();
        Assert.assertEquals(1, bbm.parse(line1));
        Assert.assertEquals(1, bbm.parse(line2));
        Assert.assertEquals(1, bbm.parse(line3));
        Assert.assertEquals(0, bbm.parse(line4));

        // Make VDM from BBM
        Vdm[] vdms = bbm.makeVdm(219000000, 0).createSentences();
        String[] sentences = new String[vdms.length];
        for (int i = 0; i < vdms.length; i++) {
            sentences[i] = vdms[i].getEncoded();
        }
        Assert.assertEquals(3, sentences.length);
        Assert.assertTrue(vdms[0].getChannel() == '0');

        // Parse VDM
        Vdm vdm = new Vdm();
        Assert.assertEquals(1, vdm.parse(sentences[0]));
        Assert.assertEquals(1, vdm.parse(sentences[1]));
        Assert.assertEquals(0, vdm.parse(sentences[2]));

        AisMessage8 msg8 = (AisMessage8) AisMessage.getInstance(vdm);
        Assert.assertEquals(219000000, msg8.getUserId());
        Assert.assertEquals(1, msg8.getDac());
        Assert.assertEquals(27, msg8.getFi());
        BroadcastRouteInformation routeInformation = (BroadcastRouteInformation) msg8.getApplicationMessage();
        Assert.assertEquals(5, routeInformation.getRouteType());
        Assert.assertEquals(0, routeInformation.getSenderClassification());
        Assert.assertEquals(16, routeInformation.getWaypointCount());
    }

}
