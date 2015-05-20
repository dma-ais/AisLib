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
package dk.dma.ais.encode;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage8;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.message.binary.AreaNotice;
import dk.dma.ais.message.binary.BroadcastAreaNotice;
import dk.dma.ais.message.binary.SubArea;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.ais.sentence.Vdm;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class for testing the encoding of area notice ASM
 * 
 */
public class EncodeAreaNoticeTest {
    // private ArrayList<SubArea> subareas = null;

    @Test
    public void areaNoticeEncode() throws SentenceException, SixbitException, AisMessageException {
        // Make ASM
        BroadcastAreaNotice areaNotice = new BroadcastAreaNotice();
        areaNotice.setNotice(0);
        areaNotice.setStartMonth(1);
        areaNotice.setStartDay(13);
        areaNotice.setStartHour(16);
        areaNotice.setStartMin(50);
        areaNotice.setDuration(35);
        ArrayList<SubArea> subareas = new ArrayList<>();

        SubArea subarea = new SubArea();
        subarea.setRawAreaShape(2);
        subarea.setRawScaleFactor(1);
        subarea.setRawLongitude(1233);
        subarea.setRawLatitude(14441);
        subarea.setRawPrecision(4);
        subarea.setRawRadius(12);
        subarea.setRawLeftBound(15);
        subarea.setRawRightBound(7);
        subarea.setSpare(0);
        subareas.add(subarea);
        areaNotice.addSubArea(subarea);
        areaNotice.setSubAreas(subareas);
        subarea = new SubArea();
        subarea.setRawAreaShape(1);
        subarea.setRawScaleFactor(4);
        subarea.setRawLongitude(12333);
        subarea.setRawLatitude(144421);
        subarea.setRawPrecision(2);
        subarea.setRawRadius(12);
        subarea.setRawLeftBound(15);
        subarea.setRawRightBound(7);
        subarea.setSpare(0);
        subareas.add(subarea);
        subarea = new SubArea();
        subarea.setRawAreaShape(3);
        subarea.setRawScaleFactor(4);
        subarea.setRawLongitude(12333);
        subarea.setRawLatitude(144421);
        subarea.setRawPrecision(2);
        subarea.setRawRadius(12);
        subarea.setRawLeftBound(15);
        subarea.setRawRightBound(7);
        subarea.setSpare(0);
        subareas.add(subarea);

        areaNotice.setSubAreas(subareas);

        // System.out.println(areaNotice.toString() + "raz dwa" +
        // subarea.toString());

        // TODO set rest of relevant fields

        // Make broadcast binary AIS message
        AisMessage8 msg8 = new AisMessage8();
        msg8.setUserId(992199007);

        msg8.setAppMessage(areaNotice);

        System.out.println("Area notice msg8: " + msg8);

        System.out.println("MSG8 = " + msg8);
        System.out.println("AreaNoticeMSG = " + areaNotice);

        // Make VDM sentences
        String[] vdms = Vdm.createSentences(msg8, 0);
        System.out.println("Area notice VDM encoded:\n" + StringUtils.join(vdms, "\r\n"));

        // Decode VDM sentences
        Vdm vdm = new Vdm();
        for (int i = 0; i < vdms.length; i++) {
            int result = vdm.parse(new SentenceLine(vdms[i]));
            if (i < vdms.length - 1) {
                Assert.assertEquals(result, 1);
            } else {
                Assert.assertEquals(result, 0);
            }

        }
        // Extract AisMessage6
        msg8 = (AisMessage8) AisMessage.getInstance(vdm);
        System.out.println("msg8 decoded: " + msg8);
        // Get the ASM
        AisApplicationMessage appMsg = msg8.getApplicationMessage();
        AreaNotice parsedAreNotice = (AreaNotice) appMsg;
        System.out.println("sentenceStr 8 application: " + appMsg);

        // Assert if mathes original
        Assert.assertEquals(parsedAreNotice.getDac(), 1);
        Assert.assertEquals(parsedAreNotice.getFi(), 22);
        Assert.assertEquals(parsedAreNotice.getMsgLinkId(), areaNotice.getMsgLinkId());
        Assert.assertEquals(parsedAreNotice.getStartMonth(), areaNotice.getStartMonth());
        Assert.assertEquals(parsedAreNotice.getStartDay(), areaNotice.getStartDay());
        Assert.assertEquals(parsedAreNotice.getStartHour(), areaNotice.getStartHour());
        Assert.assertEquals(parsedAreNotice.getStartMin(), areaNotice.getStartMin());
        Assert.assertEquals(parsedAreNotice.getDuration(), areaNotice.getDuration());
        for (int i = 0; i < 1; i++) {
            SubArea parsedSA = parsedAreNotice.getSubAreas().get(i);
            SubArea orgSA = subareas.get(i);
            Assert.assertTrue(parsedSA.equals(orgSA));
        }
        // TODO the rest of the fields

    }

}
