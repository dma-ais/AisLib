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

import dk.dma.ais.packet.AisPacket;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: tbsalling
 * Date: 26/11/13
 * Time: 15.26
 * To change this template use File | Settings | File Templates.
 */
public class ReplayDownSampleFilterTest {
    @Test
    public void testIdenticalPositionReportsRejected() throws Exception {
        // Both packets from MMSI 219001917
        AisPacket aisPacket1 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50\n" +
                "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E\n"
        );
        AisPacket aisPacket2 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50\n" +
                "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E\n"
        );

        ReplayDownSampleFilter filter = new ReplayDownSampleFilter(23 /* secs */);
        assertFalse(filter.rejectedByFilter(aisPacket1));
        assertTrue(filter.rejectedByFilter(aisPacket2));
    }

    @Test
    public void testClosePositionReportsRejected() throws Exception {
        // Both packets from MMSI 219001917 - separated by ~24 seconds
        AisPacket aisPacket1 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50\n" +
                        "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E\n"
        );
        AisPacket aisPacket2 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,33,43,219,,2190073,1,42*23\n" +
                        "!BSVDM,1,1,,A,33@nl?@PAFPk;ptPw;;aaWS00000,0*42\n"
        );

        ReplayDownSampleFilter filter = new ReplayDownSampleFilter(23 /* secs */);

        assertFalse(filter.rejectedByFilter(aisPacket1));
        assertFalse(filter.rejectedByFilter(aisPacket2));
    }

    @Test
    public void testDistantPositionReportsAccepted() throws Exception {
        // Both packets from MMSI 219001917 - separated by ~24 seconds
        AisPacket aisPacket1 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50\n" +
                        "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E\n"
        );
        AisPacket aisPacket2 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,33,43,219,,2190073,1,42*23\n" +
                        "!BSVDM,1,1,,A,33@nl?@PAFPk;ptPw;;aaWS00000,0*42\n"
        );

        ReplayDownSampleFilter filter = new ReplayDownSampleFilter(25 /* secs */);

        assertFalse(filter.rejectedByFilter(aisPacket1));
        assertTrue(filter.rejectedByFilter(aisPacket2));
    }

    @Test
    public void testStaticAndPositionalReportsIndependentlyFiltered() throws Exception {
        // All packets from MMSI 219002176
        AisPacket aisPacket1 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190068,1,14*2F\n" +
                "!BSVDM,1,1,,B,13@nm@0P020j0M<PAj0qwOvD00S3,0*14\n"
        );
        AisPacket aisPacket2 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,20,797,219,,2190074,1,64*1C\n" +
                "!BSVDM,1,1,,B,13@nojo000PhMgnQ1cG=OD4b0@<:,0*64\n"
        );
        AisPacket aisPacket3Static = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,29,689,219,,2190074,1,54*18\n" +
                "!BSVDM,2,1,0,B,53@nojh00003E58b220lTF0l4hDpF2222222220N0@=236<mP74jhAiC,0*32\n" +
                "!BSVDM,2,2,0,B,`88888888888880,2*66\n"
        );
        AisPacket aisPacket4 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,30,828,219,,2190074,1,05*11\n" +
                "!BSVDM,1,1,,A,13@nojo000PhMghQ1cG=al4v0@B2,0*05\n"
        );
        AisPacket aisPacket5 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,39,18,219,,2190072,1,44*20\n" +
                "!BSVDM,1,1,,B,13@nojo000PhMgbQ1cG=cl5>00SP,0*44\n"
        );

        ReplayDownSampleFilter filter = new ReplayDownSampleFilter(10 /* secs */);

        assertFalse(filter.rejectedByFilter(aisPacket1));
        assertFalse(filter.rejectedByFilter(aisPacket2));
        assertFalse(filter.rejectedByFilter(aisPacket3Static));
        assertFalse(filter.rejectedByFilter(aisPacket4));
        assertTrue(filter.rejectedByFilter(aisPacket5));

    }

    public void testMultipleMmsiInterleaved() throws Exception {
        // Both packets from MMSI 219001917 - separated by ~24 seconds
        AisPacket aisPacket11 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50\n" +
                        "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E\n"
        );
        AisPacket aisPacket12 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,33,43,219,,2190073,1,42*23\n" +
                        "!BSVDM,1,1,,A,33@nl?@PAFPk;ptPw;;aaWS00000,0*42\n"
        );

        // Both packets from MMSI 219002176
        AisPacket aisPacket21 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,20,797,219,,2190074,1,64*1C\n" +
                        "!BSVDM,1,1,,B,13@nojo000PhMgnQ1cG=OD4b0@<:,0*64\n"
        );

        AisPacket aisPacket22 = AisPacket.readFromString(
                "$PGHP,1,2013,11,21,16,45,30,828,219,,2190074,1,05*11\n" +
                        "!BSVDM,1,1,,A,13@nojo000PhMghQ1cG=al4v0@B2,0*05\n"
        );

        ReplayDownSampleFilter filter = new ReplayDownSampleFilter(20 /* secs */);

        assertFalse(filter.rejectedByFilter(aisPacket11));
        assertFalse(filter.rejectedByFilter(aisPacket21));
        assertFalse(filter.rejectedByFilter(aisPacket12));
        assertTrue(filter.rejectedByFilter(aisPacket22));
    }

}

/**
    Some useful test data

 MMSI =  219002176

 $PGHP,1,2013,11,21,16,45,9,784,219,,2190068,1,14*2F
 !BSVDM,1,1,,B,13@nm@0P020j0M<PAj0qwOvD00S3,0*14
 [msgId=1, repeat=0, userId=219002176, cog=2557, navStatus=0, pos=(33845763,6554534) = (33845763,6554534), posAcc=0, raim=0, specialManIndicator=0, rot=128, sog=2, spare=0, syncState=0, trueHeading=511, utcSec=10, slotTimeout=0, subMessage=2243]
 —
 $PGHP,1,2013,11,21,16,45,20,797,219,,2190074,1,64*1C
 !BSVDM,1,1,,B,13@nojo000PhMgnQ1cG=OD4b0@<:,0*64
 [msgId=1, repeat=0, userId=219002827, cog=3453, navStatus=7, pos=(34630492,6352379) = (34630492,6352379), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=0, spare=0, syncState=0, trueHeading=130, utcSec=21, slotTimeout=4, subMessage=778]
 —
 $PGHP,1,2013,11,21,16,45,29,689,219,,2190074,1,54*18
 !BSVDM,2,1,0,B,53@nojh00003E58b220lTF0l4hDpF2222222220N0@=236<mP74jhAiC,0*32
 !BSVDM,2,2,0,B,`88888888888880,2*66
 [msgId=5, repeat=0, userId=219002827, callsign=5QRJ   , dest=SKAGEN              , dimBow=2, dimPort=2, dimStarboard=3, dimStern=13, draught=28, dte=0, eta=576864, imo=0, name=MIE MALENE          , posType=1, shipType=30, spare=0, version=0]
 —
 $PGHP,1,2013,11,21,16,45,30,828,219,,2190074,1,05*11
 !BSVDM,1,1,,A,13@nojo000PhMghQ1cG=al4v0@B2,0*05
 [msgId=1, repeat=0, userId=219002827, cog=3495, navStatus=7, pos=(34630492,6352376) = (34630492,6352376), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=0, spare=0, syncState=0, trueHeading=130, utcSec=31, slotTimeout=4, subMessage=1154]
 —
 $PGHP,1,2013,11,21,16,45,39,18,219,,2190072,1,44*20
 !BSVDM,1,1,,B,13@nojo000PhMgbQ1cG=cl5>00SP,0*44
 [msgId=1, repeat=0, userId=219002827, cog=3503, navStatus=7, pos=(34630492,6352373) = (34630492,6352373), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=0, spare=0, syncState=0, trueHeading=130, utcSec=39, slotTimeout=0, subMessage=2272]
 —
 $PGHP,1,2013,11,21,16,45,50,671,219,,2190074,1,73*14
 !BSVDM,1,1,,A,13@nojo000PhMgdQ1cFekT3V0D1s,0*73
 [msgId=1, repeat=0, userId=219002827, cog=3534, navStatus=7, pos=(34630490,6352374) = (34630490,6352374), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=0, spare=0, syncState=0, trueHeading=129, utcSec=51, slotTimeout=5, subMessage=123]
 —

 MMSI =  219001917

 $PGHP,1,2013,11,21,16,45,9,784,219,,2190073,1,5E*50
 !BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E
 [msgId=3, repeat=0, userId=219001917, cog=2460, navStatus=0, pos=(34589707,6709962) = (34589707,6709962), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=85, spare=0, syncState=0, trueHeading=244, utcSec=9, keep=0, numSlots=0, slotIncrement=1999]
 —
 $PGHP,1,2013,11,21,16,45,33,43,219,,2190073,1,42*23
 !BSVDM,1,1,,A,33@nl?@PAFPk;ptPw;;aaWS00000,0*42
 [msgId=3, repeat=0, userId=219001917, cog=2470, navStatus=0, pos=(34589486,6709022) = (34589486,6709022), posAcc=1, raim=0, specialManIndicator=0, rot=129, sog=86, spare=0, syncState=0, trueHeading=241, utcSec=32, keep=0, numSlots=0, slotIncrement=0]
 —
 $PGHP,1,2013,11,21,16,46,3,88,219,,2190074,1,10*14
 !BSVDM,1,1,,B,33@nl?@PAFPk;BBPw:7aaWV6003h,0*10
 [msgId=3, repeat=0, userId=219001917, cog=2470, navStatus=0, pos=(34589214,6707785) = (34589214,6707785), posAcc=1, raim=0, specialManIndicator=0, rot=129, sog=86, spare=0, syncState=0, trueHeading=243, utcSec=3, keep=0, numSlots=0, slotIncrement=15]
 —
 $PGHP,1,2013,11,21,16,46,3,494,219,,2190073,1,47*28
 !BSVDM,1,1,,B,33@nl?@PAFPk;A6Pw:59aWT600sP,0*47
 [msgId=3, repeat=0, userId=219001917, cog=2470, navStatus=0, pos=(34589204,6707747) = (34589204,6707747), posAcc=1, raim=0, specialManIndicator=0, rot=129, sog=86, spare=0, syncState=0, trueHeading=242, utcSec=3, keep=0, numSlots=0, slotIncrement=238]


 */
