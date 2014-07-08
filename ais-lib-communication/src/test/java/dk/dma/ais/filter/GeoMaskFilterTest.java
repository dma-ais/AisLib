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

package dk.dma.ais.filter;

import com.google.common.collect.Lists;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeoMaskFilterTest {

    /*
        ---------------------------
        $PGHP,1,2014,4,1,7,38,30,96,219,,2190052,1,40*12
        !BSVDM,2,1,1,A,53@ofrT2BKPhu`=D000h4pLDh4p@00000000000t60h994wo07U0@DTs,0*34
        !BSVDM,2,2,1,A,ll3i4hRQDQh0000,2*74
        + timetamp 2014-04-01 09:38:30 +0200
        + GatehouseSourceTag [baseMmsi=2190052, country=DK, region=, timestamp=Tue Apr 01 09:38:30 CEST 2014]
        + [msgId=5, repeat=0, userId=219016938, callsign=OZCU@@@, dest=TAARS/SPODSBJERG@@@@, dimBow=48, dimPort=9, dimStarboard=9, dimStern=48, draught=30, dte=0, eta=261568, imo=9596428, name=LANGELAND@@@@@@@@@@@, posType=1, shipType=60, spare=0, version=1]
        ---------------------------
    */
    final AisPacket pkgStatic = AisPacket.from("$PGHP,1,2014,4,1,7,38,30,96,219,,2190052,1,40*12\n!BSVDM,2,1,1,A,53@ofrT2BKPhu`=D000h4pLDh4p@00000000000t60h994wo07U0@DTs,0*34\n!BSVDM,2,2,1,A,ll3i4hRQDQh0000,2*74");

    /*
        ---------------------------
        GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:38 CEST 2014]
        [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33024530,6010902) = (33024530,6010902), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=2, subMessage=1427]
        "$PGHP,1,2014,4,10,13,30,38,88,219,,2190067,1,26*1E\r\n" +
        "!BSVDM,1,1,,B,13@ng7P01dPeo0dOOb4WnVAp0`FC,0*26"
        ---------------------------
    */
    final AisPacket pkgInsideBBox1 = AisPacket.from("$PGHP,1,2014,4,10,13,30,38,88,219,,2190067,1,26*1E\r\n!BSVDM,1,1,,B,13@ng7P01dPeo0dOOb4WnVAp0`FC,0*26");

    /*
        ---------------------------
        GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:31:18 CEST 2014]
        [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33023417,6010117) = (33023417,6010117), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=199, utcSec=60, slotTimeout=0, subMessage=2263]
        "$PGHP,1,2014,4,10,13,31,18,678,219,,2190067,1,03*23\r\n" +
        "!BSVDM,1,1,,B,13@ng7P01dPen`:OOUfGnV?p0PSG,0*03"

        ---------------------------
    */
    final AisPacket pkgOutsideAllBBoxes = AisPacket.from("$PGHP,1,2014,4,10,13,31,18,678,219,,2190067,1,03*23\r\n!BSVDM,1,1,,B,13@ng7P01dPen`:OOUfGnV?p0PSG,0*03");

    GeoMaskFilter geoMaskFilter;

    @Before
    public void before() {
        AisPacket p1 = pkgInsideBBox1;
        AisPacket p2 = pkgOutsideAllBBoxes;

        System.out.println("p1: " + ((AisMessage1) (p1.tryGetAisMessage())).getValidPosition().toString() + " " + ((AisMessage1) (p1.tryGetAisMessage())).getValidPosition().getLatitude() + " " + ((AisMessage1) (p1.tryGetAisMessage())).getValidPosition().getLongitude());
        System.out.println("p2: " + ((AisMessage1) (p2.tryGetAisMessage())).getValidPosition().toString() + " " + ((AisMessage1) (p2.tryGetAisMessage())).getValidPosition().getLatitude() + " " + ((AisMessage1) (p2.tryGetAisMessage())).getValidPosition().getLongitude());

        BoundingBox bbox1 = BoundingBox.create(Position.create(55.040, 10.018), Position.create(55.1, 10.1), CoordinateSystem.CARTESIAN);
        BoundingBox bbox2 = BoundingBox.create(Position.create(56.5, 12.5), Position.create(56.6, 12.6), CoordinateSystem.CARTESIAN);
        BoundingBox bbox3 = BoundingBox.create(Position.create(56.8, 12.8), Position.create(56.9, 12.9), CoordinateSystem.CARTESIAN);

        assertTrue(bbox1.contains(((AisMessage1) (p1.tryGetAisMessage())).getValidPosition()));
        assertFalse(bbox2.contains(((AisMessage1) (p1.tryGetAisMessage())).getValidPosition()));
        assertFalse(bbox3.contains(((AisMessage1) (p1.tryGetAisMessage())).getValidPosition()));

        assertFalse(bbox1.contains(((AisMessage1) (p2.tryGetAisMessage())).getValidPosition()));
        assertFalse(bbox2.contains(((AisMessage1) (p2.tryGetAisMessage())).getValidPosition()));
        assertFalse(bbox3.contains(((AisMessage1) (p2.tryGetAisMessage())).getValidPosition()));

        geoMaskFilter = new GeoMaskFilter(
                Lists.newArrayList(bbox1, bbox2, bbox3)
        );
    }

    @Test
    public void packetsOutsideBoundingBoxAreNotRejected() {
        assertFalse(geoMaskFilter.rejectedByFilter(pkgOutsideAllBBoxes));
    }

    @Test
    public void packetsInBoundingBoxIsRejected() {
        assertTrue(geoMaskFilter.rejectedByFilter(pkgInsideBBox1));
    }

    @Test
    public void staticPacketsAreNotRejected() {
        assertFalse(geoMaskFilter.rejectedByFilter(pkgStatic));
    }

}
