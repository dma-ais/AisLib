/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.encode;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage8;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.message.binary.AreaNotice;
import dk.dma.ais.message.binary.BroadcastAreaNotice;
import dk.dma.ais.message.binary.SubArea;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

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
            int result = vdm.parse(vdms[i]);
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
        System.out.println("msg 8 application: " + appMsg);

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
