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
package dk.dma.ais.packet;

import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.sentence.SentenceException;
import java.util.function.Predicate;
import org.junit.Before;
import org.junit.Test;

import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AisPacketFiltersStatefulTest {

    AisPacket pkgStatic, pkgPosition1, pkgPosition2;

    @Before
    public void setup() throws SentenceException {
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
        pkgStatic = AisPacket.readFromString("$PGHP,1,2014,4,1,7,38,30,96,219,,2190052,1,40*12\n!BSVDM,2,1,1,A,53@ofrT2BKPhu`=D000h4pLDh4p@00000000000t60h994wo07U0@DTs,0*34\n!BSVDM,2,2,1,A,ll3i4hRQDQh0000,2*74");

        /*
            ---------------------------
            $PGHP,1,2014,4,1,7,38,54,573,219,,2190051,1,50*2C
            !BSVDM,1,1,,A,13@ofrP01lPiwLdOJKP4eCs`00SA,0*50
            + timetamp 2014-04-01 09:38:54 +0200
            + GatehouseSourceTag [baseMmsi=2190051, country=DK, region=, timestamp=Tue Apr 01 09:38:54 CEST 2014]
            + [msgId=1, repeat=0, userId=219016938, cog=1205, navStatus=0, pos=(32938880,6552470) = (32938880,6552470), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=116, spare=0, syncState=0, trueHeading=125, utcSec=52, slotTimeout=0, subMessage=2257]
            ---------------------------
        */
        pkgPosition1 = AisPacket.readFromString("$PGHP,1,2014,4,1,7,38,54,573,219,,2190051,1,50*2C\n!BSVDM,1,1,,A,13@ofrP01lPiwLdOJKP4eCs`00SA,0*50");

        /*
            ---------------------------
            $PGHP,1,2014,4,1,7,39,3,372,219,,2190051,1,68*13
            !BSVDM,1,1,,B,13@ofrP01lPiwb8OJJrTd3r204rL,0*68
            + timetamp 2014-04-01 09:39:03 +0200
            + GatehouseSourceTag [baseMmsi=2190051, country=DK, region=, timestamp=Tue Apr 01 09:39:03 CEST 2014]
            + [msgId=1, repeat=0, userId=219016938, cog=1200, navStatus=0, pos=(32938730,6552900) = (32938730,6552900), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=116, spare=0, syncState=0, trueHeading=125, utcSec=1, slotTimeout=1, subMessage=3740]
            ---------------------------
        */
        pkgPosition2 = AisPacket.readFromString("$PGHP,1,2014,4,1,7,39,3,372,219,,2190051,1,68*13\n!BSVDM,1,1,,B,13@ofrP01lPiwb8OJJrTd3r204rL,0*68");

        assertTrue(pkgStatic.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(pkgPosition1.tryGetAisMessage() instanceof AisMessage1);
        assertTrue(pkgPosition2.tryGetAisMessage() instanceof AisMessage1);

        assertTrue(((AisMessage5) pkgStatic.tryGetAisMessage()).getImo() > 0);

        assertTrue(pkgStatic.tryGetAisMessage().getUserId() == pkgPosition1.tryGetAisMessage().getUserId());
        assertTrue(pkgStatic.tryGetAisMessage().getUserId() == pkgPosition2.tryGetAisMessage().getUserId());
    }

    // --- IMO
    
    @Test
    public void imoCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.imo = 1234567");
        assertFilterPredicate(false, filter1, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(false, filter1, pkgStatic);    // Reject, because this packet contains imo = 9596428
        assertFilterPredicate(false, filter1, pkgPosition1); // Still reject, because second packet learned us that this mmsi -> imo 9596428
        assertFilterPredicate(false, filter1, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> imo 9596428

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.imo = " + ((AisMessage5) pkgStatic.tryGetAisMessage()).getImo());
        assertFilterPredicate(false, filter2, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(true,  filter2, pkgStatic);    // Accept, because this packet contains imo = 9596428
        assertFilterPredicate(true,  filter2, pkgPosition1); // Still accept, because second packet learned us that this mmsi -> imo 9596428
        assertFilterPredicate(true,  filter2, pkgPosition2); // Still accept, because second packet learned us that this mmsi -> imo 9596428
    }

    @Test
    public void imoInRange() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.imo = 1000..1999");
        assertFilterPredicate(false, filter1, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(false, filter1, pkgStatic);    // Reject, because this packet contains imo = 9596428 out of range
        assertFilterPredicate(false, filter1, pkgPosition1); // Still reject, because second packet learned us that this mmsi -> imo out of range
        assertFilterPredicate(false, filter1, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> imo out of range

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.imo = 9596420..9596430");
        assertFilterPredicate(false, filter2, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(true, filter2, pkgStatic);     // Accept, because this packet contains imo = 9596428 in range
        assertFilterPredicate(true, filter2, pkgPosition1);  // Accept, because second packet learned us that this mmsi -> imo in range
        assertFilterPredicate(true, filter2, pkgPosition2);  // Accept, because second packet learned us that this mmsi -> imo in range
    }

    @Test
    public void imoInList() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.imo = 1234567, 9596429, 9596430");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains imo = 9596428 out of range
        assertFilterPredicate(false, filter, pkgPosition1); // Still reject, because second packet learned us that this mmsi -> imo out of range
        assertFilterPredicate(false, filter, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> imo out of range

        filter = parseExpressionFilter("t.imo != 1234567, 9596429, 9596430");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(true, filter, pkgStatic);    // Reject, because this packet contains imo = 9596428 out of range
        assertFilterPredicate(true, filter, pkgPosition1); // Still reject, because second packet learned us that this mmsi -> imo out of range
        assertFilterPredicate(true, filter, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> imo out of range

        filter = parseExpressionFilter("t.imo = 1234567, 9596428, 9596429, 9596430");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(true,  filter, pkgStatic);    // Accept, because this packet contains imo = 9596428 in range
        assertFilterPredicate(true,  filter, pkgPosition1); // Accept, because second packet learned us that this mmsi -> imo in range
        assertFilterPredicate(true,  filter, pkgPosition2); // Accept, because second packet learned us that this mmsi -> imo in range

        filter = parseExpressionFilter("t.imo != 1234567, 9596428, 9596429, 9596430");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */,  filter, pkgPosition1); // Reject, because we know nothing of this target's imo yet
        assertFilterPredicate(false,  filter, pkgStatic);    // Accept, because this packet contains imo = 9596428 in range
        assertFilterPredicate(false,  filter, pkgPosition1); // Accept, because second packet learned us that this mmsi -> imo in range
        assertFilterPredicate(false,  filter, pkgPosition2); // Accept, because second packet learned us that this mmsi -> imo in range
    }

    // --- ship type

    @Test
    public void shipTypeCompare() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.type = 59");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = 60");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.type != 60");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type >= 60");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.type < 70");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);     // Accept, because this packet contains shiptype = 60
        assertFilterPredicate(true, filter, pkgPosition1);  // Accept, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(true, filter, pkgPosition2);  // Accept, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = passenger");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition2);

        /*
        TODO
        filter = parseExpressionFilter("t.type != passenger");
        assertFilterPredicate(true,  filter, pkgPosition1); // Accept, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        */

        filter = parseExpressionFilter("t.type = tanker");
        assertFilterPredicate(false,  filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because this packet contains shiptype = 60 ()
        assertFilterPredicate(false, filter, pkgPosition1);  // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2);  // Reject, because second packet learned us that this mmsi -> shiptype 60

        /*
        TODO
        filter = parseExpressionFilter("t.type != tanker");
        assertFilterPredicate(true,  filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        */
    }

    @Test
    public void shipTypeInListOrRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.type = 59..61");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);    // Accept, because this packet contains shiptype = 60
        assertFilterPredicate(true, filter, pkgPosition1); // Accept, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(true, filter, pkgPosition2); // Accept, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type != 55..61");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = 59,60,61");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);     // Accept, because this packet contains shiptype = 60
        assertFilterPredicate(true, filter, pkgPosition1);  // Accept, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(true, filter, pkgPosition2);  // Accept, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type != 59,60,61");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */,  filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1);  // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2);  // Reject, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = 59,43,61");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = tanker, passenger, cargo");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(true, filter, pkgStatic);     // Accept, because this packet contains shiptype = 60
        assertFilterPredicate(true, filter, pkgPosition1);  // Accept, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(true, filter, pkgPosition2);  // Accept, because second packet learned us that this mmsi -> shiptype 60

        filter = parseExpressionFilter("t.type = tanker, cargo");
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because we know nothing of this target's shiptype yet
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because this packet contains shiptype = 60
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because second packet learned us that this mmsi -> shiptype 60
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> shiptype 60
    }

    // --- Navigational status

    @Test
    public void navstatCompare() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.navstat = 1");
        assertFilterPredicate(false,  filter, pkgStatic);   // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because this packet contains navstat = 0
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because second packet learned us that this mmsi -> navstat 0
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> navstat 0

        filter = parseExpressionFilter("t.navstat = 0");
        assertFilterPredicate(false, filter, pkgStatic); // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat != 0");
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(false, filter, pkgPosition1);  // Reject, because this packet contains navstat = 0
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because second packet learned us that this mmsi -> navstat 0
        assertFilterPredicate(false, filter, pkgPosition2);  // Reject, because second packet learned us that this mmsi -> navstat 0

        filter = parseExpressionFilter("t.navstat >= 0");
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat < 3");
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(true, filter, pkgPosition1);  // Accept, because this packet contains navstat = 0
        assertFilterPredicate(true, filter, pkgStatic);     // Accept, because second packet learned us that this mmsi -> navstat 0
        assertFilterPredicate(true, filter, pkgPosition2);  // Accept, because second packet learned us that this mmsi -> navstat 0

        filter = parseExpressionFilter("t.navstat = UNDER_WAY_USING_ENGINE");
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat = at_anchor");
        assertFilterPredicate(false, filter, pkgStatic);     // Reject, because we know nothing of this target's navstat yet
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        /*
        TODO
        filter = parseExpressionFilter("t.navstat != passenger");
        assertFilterPredicate(true,  filter, pkgStatic); // Accept, because we know nothing of this target's navstat yet
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        */

        /*
        TODO
        filter = parseExpressionFilter("t.navstat != tanker");
        assertFilterPredicate(true,  filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        */
    }

    @Test
    public void navstatInListOrRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.navstat = 0..2");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat != 0..2");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat = 0,1,2");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat != 0,1,2");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgStatic);
        assertFilterPredicate(false,  filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat = 1,2,3");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat = UNDER_WAY_USING_ENGINE, at_anchor, moored");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.navstat = at_anchor, moored");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);
    }

    // --- SOG

    @Test
    public void sogCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.sog > 12");
        assertFilterPredicate(false,  filter1, pkgStatic);   // Reject, because we know nothing of this target's sog yet
        assertFilterPredicate(false, filter1, pkgPosition1); // Reject, because this packet contains sog = 11.6
        assertFilterPredicate(false, filter1, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> sog = 11.6
        assertFilterPredicate(false, filter1, pkgStatic);    // Still reject, because second packet learned us that this mmsi -> sog = 11.6

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.sog >= 11.5");
        assertFilterPredicate(false, filter2, pkgStatic);    // Reject, because we know nothing of this target's sog yet
        assertFilterPredicate(true,  filter2, pkgPosition1); // Accept, because this packet contains sog = 11.6
        assertFilterPredicate(true,  filter2, pkgPosition2); // Still accept, because second packet learned us that this mmsi -> sog = 11.6
        assertFilterPredicate(true,  filter2, pkgStatic);    // Still accept, because second packet learned us that this mmsi -> sog = 11.6
    }

    @Test
    public void sogInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.sog = 11.5..11.9");
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because we know nothing of this target's sog yet
        assertFilterPredicate(true,  filter, pkgPosition1); // Accept, because this packet contains sog = 11.6 in range
        assertFilterPredicate(true,  filter, pkgPosition2); // Accept, because second packet learned us that this mmsi -> sog in range
        assertFilterPredicate(true,  filter, pkgStatic);    // Accept, because second packet learned us that this mmsi -> sog in range

        filter = parseExpressionFilter("t.sog = 12.5..20.0");
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because we know nothing of this target's sog yet
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because this packet contains sog = 11.6 out of range
        assertFilterPredicate(false, filter, pkgPosition2); // Still reject, because second packet learned us that this mmsi -> sog out of range
        assertFilterPredicate(false, filter, pkgStatic);    // Still reject, because second packet learned us that this mmsi -> sog out of range

        filter = parseExpressionFilter("t.sog != 11.5..11.9");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgStatic);    // Reject, because we know nothing of this target's sog yet
        assertFilterPredicate(false, filter, pkgPosition1); // Reject, because this packet contains sog = 11.6 in range
        assertFilterPredicate(false, filter, pkgPosition2); // Reject, because second packet learned us that this mmsi -> sog in range
        assertFilterPredicate(false, filter, pkgStatic);    // Reject, because second packet learned us that this mmsi -> sog in range
    }

    // --- COG

    @Test
    public void cogCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.cog > 120.9");
        assertFilterPredicate(false, filter1, pkgStatic);
        assertFilterPredicate(false, filter1, pkgPosition1);
        assertFilterPredicate(false, filter1, pkgPosition2);
        assertFilterPredicate(false, filter1, pkgStatic);

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.cog >= 120.0");
        assertFilterPredicate(false, filter2, pkgStatic);
        assertFilterPredicate(true,  filter2, pkgPosition1);
        assertFilterPredicate(true,  filter2, pkgPosition2);
        assertFilterPredicate(true,  filter2, pkgStatic);
    }

    @Test
    public void cogInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.cog = 120.0..120.9");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgPosition2);
        assertFilterPredicate(true, filter, pkgStatic);

        filter = parseExpressionFilter("t.cog != 120.0..120.9");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);

        filter = parseExpressionFilter("t.cog = 180..190");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // --- True heading

    @Test
    public void hdgCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.hdg > 180");
        assertFilterPredicate(false, filter1, pkgStatic);    // Unknown hgd -> blocked
        assertFilterPredicate(false, filter1, pkgPosition1); // Known, non-matching hgd -> blocked
        assertFilterPredicate(false, filter1, pkgPosition2); // Known, non-matching hgd -> blocked
        assertFilterPredicate(false, filter1, pkgStatic);    // Known, non-matching hgd -> blocked

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.hdg >= 120");
        assertFilterPredicate(false, filter2, pkgStatic);    // Unknown hgd -> blocked
        assertFilterPredicate(true,  filter2, pkgPosition1);
        assertFilterPredicate(true,  filter2, pkgPosition2);
        assertFilterPredicate(true,  filter2, pkgStatic);
    }

    @Test
    public void hdgInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.hdg = 120..130");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true,  filter, pkgPosition1);
        assertFilterPredicate(true,  filter, pkgPosition2);
        assertFilterPredicate(true,  filter, pkgStatic);

        filter = parseExpressionFilter("t.hdg != 120..130");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */,  filter, pkgStatic);    // hdg unknown
        assertFilterPredicate(false,  filter, pkgPosition1);
        assertFilterPredicate(false,  filter, pkgPosition2);
        assertFilterPredicate(false,  filter, pkgStatic);

        filter = parseExpressionFilter("t.hdg = 1..99");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // --- draught

    @Test
    public void draughtCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.draught > 3.5");
        assertFilterPredicate(false, filter1, pkgPosition1); // Unknown draught
        assertFilterPredicate(false, filter1, pkgStatic);
        assertFilterPredicate(false, filter1, pkgPosition2);
        assertFilterPredicate(false, filter1, pkgStatic);

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.draught < 3.5");
        assertFilterPredicate(false, filter2, pkgPosition1); // Unknown draught
        assertFilterPredicate(true,  filter2, pkgStatic);
        assertFilterPredicate(true,  filter2, pkgPosition2);
        assertFilterPredicate(true,  filter2, pkgStatic);
    }

    @Test
    public void draugtInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.draught = 2.5..3.5");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);
        assertFilterPredicate(true, filter, pkgStatic);

        filter = parseExpressionFilter("t.draught != 2.5..3.5");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);

        filter = parseExpressionFilter("t.draught = 5.0..9.9");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // --- latitude

    @Test
    public void latCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.lat > 54.90");
        assertFilterPredicate(false,  filter1, pkgStatic);
        assertFilterPredicate(false, filter1, pkgPosition1);
        assertFilterPredicate(false, filter1, pkgPosition2);
        assertFilterPredicate(false, filter1, pkgStatic);

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.lat >= 54.88");
        assertFilterPredicate(false,  filter2, pkgStatic);
        assertFilterPredicate(true,  filter2, pkgPosition1);
        assertFilterPredicate(true,  filter2, pkgPosition2);
        assertFilterPredicate(true,  filter2, pkgStatic);
    }

    @Test
    public void latInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.lat = 54.88..55.00");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true,  filter, pkgPosition1);  // 54.898133333333334
        assertFilterPredicate(true,  filter, pkgPosition2);
        assertFilterPredicate(true,  filter, pkgStatic);

        filter = parseExpressionFilter("t.lat != 54.88..55.00");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */,  filter, pkgStatic);
        assertFilterPredicate(false,  filter, pkgPosition1);
        assertFilterPredicate(false,  filter, pkgPosition2);
        assertFilterPredicate(false,  filter, pkgStatic);

        filter = parseExpressionFilter("t.lat = -10.0..20.0");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // --- longitude

    @Test
    public void lonCompare() {
        Predicate<AisPacket> filter1 = parseExpressionFilter("t.lon > 11.0");
        assertFilterPredicate(false, filter1, pkgStatic);    // Unknown lon
        assertFilterPredicate(false, filter1, pkgPosition1); // 10.920783042907715
        assertFilterPredicate(false, filter1, pkgPosition2);
        assertFilterPredicate(false, filter1, pkgStatic);

        Predicate<AisPacket> filter2 = parseExpressionFilter("t.lon >= 10.8");
        assertFilterPredicate(false, filter2, pkgStatic);    // Unknown lon
        assertFilterPredicate(true,  filter2, pkgPosition1);
        assertFilterPredicate(true,  filter2, pkgPosition2);
        assertFilterPredicate(true,  filter2, pkgStatic);
    }

    @Test
    public void lonInRange() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.lon = 10.0..10.95");
        assertFilterPredicate(false, filter, pkgStatic);    // Unknown lon
        assertFilterPredicate(true,  filter, pkgPosition1); // 10.920783042907715
        assertFilterPredicate(true,  filter, pkgPosition2);
        assertFilterPredicate(true,  filter, pkgStatic);

        filter = parseExpressionFilter("t.lon != 10.0..10.95");
        assertFilterPredicate(true /* TODO AISAB-103: return false for undefined value */, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);

        filter = parseExpressionFilter("t.lon = 0..1");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgPosition2);
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // --- Name

    @Test
    public void nameCompare() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.name = LANGELAND");
        assertFilterPredicate(false, filter, pkgPosition1); // Unknown name; therefore blocked
        assertFilterPredicate(true,  filter, pkgStatic);    // Matching name; therefore passing
        assertFilterPredicate(true,  filter, pkgPosition2); // Matching name; therefore passing

        /* TODO
        filter = parseExpressionFilter("t.name not like L?NGELAND");
        assertFilterPredicate(true,  filter, pkgPosition1);
        assertFilterPredicate(false,  filter, pkgStatic);
        assertFilterPredicate(false,  filter, pkgPosition2);
        */

        filter = parseExpressionFilter("t.name ~ L?NGELAND");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true,  filter, pkgStatic);
        assertFilterPredicate(true,  filter, pkgPosition2);

        filter = parseExpressionFilter("t.name ~ L?WEIRDO");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        filter = parseExpressionFilter("t.name LIKE L*D");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true,  filter, pkgStatic);
        assertFilterPredicate(true,  filter, pkgPosition2);

        filter = parseExpressionFilter("t.name LIKE L*X");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

    }

    // ---

    @Test
    public void callsignCompare() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.cs = OZCU");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.cs ~ O*U");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.cs ~ OZ?U");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(true, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition2);

        filter = parseExpressionFilter("t.cs LIKE O*X");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);

        filter = parseExpressionFilter("t.cs LIKE Z*");
        assertFilterPredicate(false, filter, pkgPosition1);
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition2);
    }

    // ---

    @Test
    public void positionWithinBbox() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.pos within bbox(54.5, 10.5, 55.0, 11.0)");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);  // 54.898133333333334
        assertFilterPredicate(true, filter, pkgPosition2);  // 10.920783042907715
        assertFilterPredicate(true, filter, pkgStatic);

        filter = parseExpressionFilter("t.pos within bbox(54.0, 10.0, 54.5, 10.5)");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);  // 54.898133333333334
        assertFilterPredicate(false, filter, pkgPosition2);  // 10.920783042907715
        assertFilterPredicate(false, filter, pkgStatic);
    }

    @Test
    public void positionWithinCircle() {
        Predicate<AisPacket> filter = parseExpressionFilter("t.pos within circle(54.898, 10.921, 1000)");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(true, filter, pkgPosition1);  // 54.898133333333334
        assertFilterPredicate(true, filter, pkgPosition2);  // 10.920783042907715
        assertFilterPredicate(true, filter, pkgStatic);

        filter = parseExpressionFilter("t.pos within circle(54.0, 10.0, 1000)");
        assertFilterPredicate(false, filter, pkgStatic);
        assertFilterPredicate(false, filter, pkgPosition1);  // 54.898133333333334
        assertFilterPredicate(false, filter, pkgPosition2);  // 10.920783042907715
        assertFilterPredicate(false, filter, pkgStatic);
    }

    // ---

    @Test
    public void testMessageExpressionFilterBlocksMessagesNotCarryingTheFilteredTypeOfInformation() {
        // Position reports
        assertFilterPredicate(false, parseExpressionFilter("t.sog >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.cog >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.hdg >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.lat >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.lon >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.navstat >= 0"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.pos within circle(56,11,250000)"), pkgStatic);
        assertFilterPredicate(false, parseExpressionFilter("t.pos within bbox(50,09,60,12)"), pkgStatic);

        // Static messages
        assertFilterPredicate(false, parseExpressionFilter("t.draught > 0"), pkgPosition1);
        assertFilterPredicate(false, parseExpressionFilter("t.imo > 0"), pkgPosition1);
        assertFilterPredicate(false, parseExpressionFilter("t.type > 0"), pkgPosition1);
        assertFilterPredicate(false, parseExpressionFilter("t.name like *"), pkgPosition1);
        assertFilterPredicate(false, parseExpressionFilter("t.cs like *"), pkgPosition1);
    }

    // ---

    private static void assertFilterPredicate(boolean expectedResult, Predicate<AisPacket> filter, AisPacket aisPacket) {
        System.out.println("Testing \"" + filter.toString() + "\" to be " + expectedResult + " for " + aisPacket.tryGetAisMessage().toString());
        assertEquals(expectedResult, filter.test(aisPacket));
    }

    // TODO
    // Test that two+ indepedent stateful filters can co-exist

}
