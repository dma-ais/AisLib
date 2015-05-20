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
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.message.binary.BroadcastIntendedRoute;
import dk.dma.ais.sentence.Bbm;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.geometry.Position;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EncodeRouteExchangeTest {

    @Test
    public void encodeIntendedTest() throws SixbitException, SentenceException, AisMessageException {
        int userId = 992199007;

        // Make ASM
        BroadcastIntendedRoute route = new BroadcastIntendedRoute();
        route.setStartMonth(11);
        route.setStartDay(3);
        route.setStartHour(16);
        route.setStartMin(50);
        route.setDuration(35);
        // Add waypoints
        List<AisPosition> waypoints = new ArrayList<>();
        waypoints.add(new AisPosition(Position.create(55.845283333333334, 12.704933333333333)));
        waypoints.add(new AisPosition(Position.create(55.913383333333336, 12.6453)));
        waypoints.add(new AisPosition(Position.create(55.93476666666667, 12.644016666666667)));
        waypoints.add(new AisPosition(Position.create(55.97728333333333, 12.7015)));
        waypoints.add(new AisPosition(Position.create(56.00, 12.8)));
        waypoints.add(new AisPosition(Position.create(56.10, 12.9)));
        for (AisPosition aisPosition : waypoints) {
            route.addWaypoint(aisPosition);
        }

        // Make MSG8
        AisMessage8 msg8 = new AisMessage8();
        msg8.setUserId(userId);
        msg8.setAppMessage(route);

        System.out.println("Route message 8: " + msg8);

        // Make BBM sentences
        Bbm bbm = new Bbm();
        bbm.setTalker("AI");
        bbm.setTotal(1);
        bbm.setNum(1);
        bbm.setBinaryData(msg8);
        bbm.setSequence(0);
        String encoded = bbm.getEncoded();
        System.out.println("Route BBM encoded: " + encoded);

        // Make VDM sentences
        String[] vdms = Vdm.createSentences(msg8, 0);
        System.out.println("Route VDM encoded:\n" + StringUtils.join(vdms, "\r\n"));

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
        BroadcastIntendedRoute parsedRoute = (BroadcastIntendedRoute) appMsg;
        System.out.println("sentenceStr 8 application: " + appMsg);

        // Assert if mathes original
        Assert.assertEquals(parsedRoute.getWaypointCount(), waypoints.size());
        Assert.assertEquals(route.getDuration(), parsedRoute.getDuration());
        Assert.assertEquals(route.getStartMonth(), parsedRoute.getStartMonth());
        Assert.assertEquals(route.getStartDay(), parsedRoute.getStartDay());
        Assert.assertEquals(route.getStartHour(), parsedRoute.getStartHour());
        Assert.assertEquals(route.getStartMin(), parsedRoute.getStartMin());
        for (int i = 0; i < parsedRoute.getWaypointCount(); i++) {
            AisPosition parsedWp = parsedRoute.getWaypoints().get(i);
            AisPosition orgWp = waypoints.get(i);
            Assert.assertTrue(parsedWp.equals(orgWp));
        }

    }

}
