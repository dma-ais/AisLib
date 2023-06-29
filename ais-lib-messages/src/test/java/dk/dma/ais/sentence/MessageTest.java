/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.sentence;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.message.AisMessage6;
import dk.dma.ais.message.AisMessage8;
import dk.dma.ais.message.AisMessage27;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.binary.Capability;
import dk.dma.ais.message.binary.MetHyd11;

public class MessageTest {

    @Test
    public void testMessage1() throws SentenceException, SixbitException, AisMessageException {

        Vdm vdm = new Vdm();
        int result = vdm.parse("!AIVDM,1,1,,B,19NS7Sp02wo?HETKA2K6mUM20<L=,0*27\r\n");
        Assert.assertEquals("vdm parse failed", 0, result);
        Assert.assertEquals("wrong message id", 1, vdm.getMsgId());
        AisMessage1 msg = (AisMessage1) AisMessage.getInstance(vdm);
        Assert.assertEquals("msgid", 1, msg.getMsgId());
        Assert.assertEquals("repeat", 0, msg.getRepeat());
        Assert.assertEquals("userid", 636012431, msg.getUserId());
        Assert.assertEquals("nav_status", 8, msg.getNavStatus());
        Assert.assertEquals("rot", 0, msg.getRot());
        Assert.assertEquals("sog", 191, msg.getSog());
        Assert.assertEquals("pos_acc", 1, msg.getPosAcc());
        Assert.assertEquals("longitude", -73481550, msg.getPos().getLongitude());
        Assert.assertEquals("latitude", 28590700, msg.getPos().getLatitude());
        Assert.assertEquals("cog", 1750, msg.getCog());
        Assert.assertEquals("true_heading", 174, msg.getTrueHeading());
        Assert.assertEquals("utc_sec", 33, msg.getUtcSec());
        Assert.assertEquals("special_man_indicator", 0, msg.getSpecialManIndicator());
        Assert.assertEquals("spare", 0, msg.getSpare());
        Assert.assertEquals("raim", 0, msg.getRaim());
        Assert.assertEquals("sync_state", 0, msg.getSyncState());
        Assert.assertEquals("slot_timeout", 3, msg.getSlotTimeout());
        Assert.assertEquals("sub_message", 1805, msg.getSubMessage());
    }

    @Test
    public void testMessage4() throws SentenceException, SixbitException, AisMessageException {

        Vdm vdm = new Vdm();
        int result = vdm.parse("!AIVDM,1,1,,A,403OwpiuIKl:Ro=sbvK=CG700<3b,0*5E");

        Assert.assertEquals("vdm parse failed", 0, result);
        Assert.assertEquals("wrong message id", 4, vdm.getMsgId());
        AisMessage4 msg = (AisMessage4) AisMessage.getInstance(vdm);
        Assert.assertEquals("msgid", 4, msg.getMsgId());
        Assert.assertEquals("repeat", 0, msg.getRepeat());
        Assert.assertEquals("userid", 3669987, msg.getUserId());
        Assert.assertEquals("utc_year", 2006, msg.getUtcYear());
        Assert.assertEquals("utc_month", 5, msg.getUtcMonth());
        Assert.assertEquals("utc_day", 23, msg.getUtcDay());
        Assert.assertEquals("utc_hour", 20, msg.getUtcHour());
        Assert.assertEquals("utc_minute", 10, msg.getUtcMinute());
        Assert.assertEquals("utc_second", 34, msg.getUtcSecond());
        Assert.assertEquals("pos_acc", 1, msg.getPosAcc());
        Assert.assertEquals("longitude", -73671329, msg.getPos().getLongitude());
        Assert.assertEquals("latitude", 28529500, msg.getPos().getLatitude());
        Assert.assertEquals("pos_type", 7, msg.getPosType());
        Assert.assertEquals("spare", 0, msg.getSpare());
        Assert.assertEquals("raim", 0, msg.getRaim());
        Assert.assertEquals("sync_state", 0, msg.getSyncState());
        Assert.assertEquals("slot_timeout", 3, msg.getSlotTimeout());
        Assert.assertEquals("sub_message", 234, msg.getSubMessage());
    }

    @Test
    public void testMessage6() throws SentenceException, SixbitException, AisMessageException {

        Vdm vdm = new Vdm();
        int result = vdm.parse("!BSVDM,1,1,,B,63@oSO00QNUB00=:>hTB6P,4*27");
        Assert.assertEquals("vdm parse failed", 0, result);
        Assert.assertEquals("wrong message id", 6, vdm.getMsgId());
        AisMessage6 msg = (AisMessage6) AisMessage.getInstance(vdm);
        Assert.assertEquals("msgid", 6, msg.getMsgId());
        Assert.assertEquals("repeat", 0, msg.getRepeat());
        Assert.assertEquals("userid", 219014012, msg.getUserId());
        Assert.assertEquals("dac", 0, msg.getDac());
        Assert.assertEquals("fi", 3, msg.getFi());
        Assert.assertEquals("destination", 2194004, msg.getDestination());
        Assert.assertEquals("retransmit", 1, msg.getRetransmit());

        Capability capability = (Capability) msg.getApplicationMessage();
        Assert.assertNotNull(capability);
        Assert.assertEquals("reqDac", 296, capability.getReqDac());
    }

    @Test
    public void testMessage8() throws SentenceException, SixbitException, AisMessageException {

        // Swedish met hyd

        Vdm vdm = new Vdm();
        int result = vdm.parse("!BSVDM,1,1,,A,802R5Ph0BkC>ohEUKTltOwwwwwwwwwwwwwwPA>08r@2Tlwwwwwwwwwwwwwt,2*7F");
        Assert.assertEquals("vdm parse failed", 0, result);
        Assert.assertEquals("wrong message id", 8, vdm.getMsgId());
        AisMessage8 msg = (AisMessage8) AisMessage.getInstance(vdm);
        System.out.println(msg);
        Assert.assertEquals("msgid", 8, msg.getMsgId());
        Assert.assertEquals("repeat", 0, msg.getRepeat());
        Assert.assertEquals("userid", 2655619, msg.getUserId());
        Assert.assertEquals("dac", 1, msg.getDac());
        Assert.assertEquals("fi", 11, msg.getFi());

        MetHyd11 metHyd = (MetHyd11) msg.getApplicationMessage();
        Assert.assertNotNull(metHyd);

        System.out.println("length of data: " + msg.getData().getLength());
        System.out.println("bits index    : " + msg.getData().getReadPtr());

        System.out.println("pos: " + metHyd.getPos().getLatitude() / 1000.0 / 60.0 + " "
                + metHyd.getPos().getLongitude() / 1000.0 / 60.0);

        System.out.println(metHyd);

        // Irish met hyd
        vdm = new Vdm();
        result = vdm.parse("!BSVDM,2,1,6,A,8@2Ha<00Bk8oOgmG0gU7V3:cErrwwwdjwwwwwwwwwwwwwwwwwwwwwwww,0*1C");
        Assert.assertEquals("vdm parse failed", 1, result);
        result = vdm.parse("!BSVDM,2,2,6,A,wt0,2*38");
        Assert.assertEquals("vdm parse failed", 0, result);
        Assert.assertEquals("wrong message id", 8, vdm.getMsgId());
        msg = (AisMessage8) AisMessage.getInstance(vdm);
        System.out.println("Irish MetHyd: " + msg);
        Assert.assertEquals("msgid", 8, msg.getMsgId());
        Assert.assertEquals("repeat", 1, msg.getRepeat());
        Assert.assertEquals("userid", 2500912, msg.getUserId());
        Assert.assertEquals("dac", 1, msg.getDac());
        Assert.assertEquals("fi", 11, msg.getFi());

        metHyd = (MetHyd11) msg.getApplicationMessage();
        Assert.assertNotNull(metHyd);
        System.out.println("Irish MetHyd: " + metHyd);

    }

    @Test
    public void testMsg27() throws SentenceException, AisMessageException, SixbitException {
        String sentence = "!AIVDM,1,1,,A,KLr:8BvSGc`=CSLA,0*52";
        Vdm vdm = new Vdm();
        int result = vdm.parse(sentence);
        Assert.assertEquals("vdm parse failed", result, 0);
        Assert.assertEquals("wrong message id", 27, vdm.getMsgId());
        AisMessage27 msg = (AisMessage27) AisMessage.getInstance(vdm);
        Assert.assertEquals(msg.getCog(), 452);
        Assert.assertEquals(msg.getGnssPosStatus(), 0);
        Assert.assertEquals(msg.getPos().getRawLatitude(), 67239);
        Assert.assertEquals(msg.getPos().getRawLongitude(), 55214);
        Assert.assertEquals(msg.getMsgId(), 27);
        Assert.assertEquals(msg.getNavStatus(), 10);
        Assert.assertEquals(msg.getPosAcc(), 1);
        Assert.assertEquals(msg.getRaim(), 1);
        Assert.assertEquals(msg.getSog(), 6);
        Assert.assertEquals(msg.getSpare(), 1);
    }

    @Test
    public void decodeMsg5Test() throws SentenceException, SixbitException, AisMessageException {
        String line1 = "!BSVDM,2,1,1,B,53@oTQ@2Ad4tuL9S:21=@EHq>2222098D4dE:20l1p?555o@0>QTQA1DR@j@,0*66";
        String line2 = "!BSVDM,2,2,1,B,H8888888880,2*4F";

        Vdm vdm = new Vdm();
        int result = vdm.parse(line1);
        Assert.assertEquals("Failed to parse sentenceStr 5 first line", result, 1);

        result = vdm.parse(line2);
        Assert.assertEquals("Failed to parse sentenceStr 5 second line", result, 0);

        AisMessage msg = AisMessage.getInstance(vdm);
        Assert.assertEquals("Not sentenceStr 5", msg.getMsgId(), 5);

    }

    @Test
    public void decodeMsg24Test() throws SentenceException, SixbitException, AisMessageException {
        String line = "!BSVDM,1,1,,A,H3uFAjTT653hhhiC;1GPPP1`0220,0*62";
        Vdm vdm = new Vdm();
        int result = vdm.parse(line);
        Assert.assertEquals("Failed to parse sentenceStr 24", result, 0);
        AisMessage msg = AisMessage.getInstance(vdm);
        Assert.assertEquals("Not sentenceStr 24", msg.getMsgId(), 24);
    }

    @Test
    public void decodeMssisTimestamp() throws Exception {
        String sentence = "!AIVDM,1,1,,B,14pWHb?P03rwO<F:RQOnROw<25bd,0*3E,1357134218\r\n";
        Vdm vdm = new Vdm();
        int result = vdm.parse(sentence);
        Assert.assertEquals("Failed to parse", result, 0);
        Date timestamp = vdm.getMssisTimestamp();
        Assert.assertEquals(1357134218000L, timestamp.getTime());

        sentence = "!AIVDM,1,1,,B,14pWHb?P03rwO<F:RQOnROw<25bd,0*3E";
        vdm = new Vdm();
        result = vdm.parse(sentence);
        Assert.assertEquals("Failed to parse", result, 0);
        timestamp = vdm.getMssisTimestamp();
        Assert.assertTrue(timestamp == null);

        sentence = "!AIVDM,1,1,,B,14pWHb?P03rwO<F:RQOnROw<25bd,0*3E,1357134218,1357134220,1357134222";
        vdm = new Vdm();
        result = vdm.parse(sentence);
        Assert.assertEquals("Failed to parse", result, 0);
        timestamp = vdm.getMssisTimestamp();
        Assert.assertEquals(1357134218000L, timestamp.getTime());
    }

    @Test
    public void decodeMsg17() throws SentenceException, AisMessageException, SixbitException {
        String sentence = "!AIVDM,1,1,,,A0476C1=qHWw82JrVgH`4?mmsRL1,0*09";
        Vdm vdm = new Vdm();
        int result = vdm.parse(sentence);
        Assert.assertEquals("Failed to parse", result, 0);
        AisMessage msg = AisMessage.getInstance(vdm);
        System.out.println("sentenceStr: " + msg);
    }

    @Test
    public void decodeMsg19() throws SentenceException, AisMessageException, SixbitException {
        String sentence = "!AIVDM,1,1,,A,C6:`MUh00:;5umTN;<UIM7v0jc0hBM0@bOSQkWf00000S3P62R0P,0*05";
        Vdm vdm = new Vdm();
        int result = vdm.parse(sentence);
        Assert.assertEquals("Failed to parse", result, 0);
        AisMessage msg = AisMessage.getInstance(vdm);
        System.out.println("sentenceStr: " + msg);
    }
}
