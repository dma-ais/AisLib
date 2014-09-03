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

import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.TimeZone;

import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceBasestation;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceCountry;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceId;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceRegion;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceType;

import static dk.dma.ais.packet.AisPacketFilters.filterOnTargetCountry;

import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Kasper Nielsen
 */
public class AisPacketFiltersTest {

    AisPacket pkgStatic1;
    AisPacket pkgStatic2;
    AisPacket pkgStatic3;
    AisPacket pkgPosition1;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Copenhagen")); // Filter expressions are in DK local time
    }

    @Before
    public void setup() {
        pkgStatic1 = AisTestPackets.p1();
        pkgStatic2 = AisTestPackets.p2();
        pkgStatic3 = AisTestPackets.p3();
        pkgPosition1 = AisTestPackets.p5();

        assertTrue(pkgStatic1.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(pkgStatic2.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(pkgStatic3.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(pkgPosition1.tryGetAisMessage() instanceof AisPositionMessage);
    }

    @Test
    public void testfilterOnSourceId() {
        assertTrue(filterOnSourceId("AISD").test(pkgStatic3));
        assertTrue(filterOnSourceId("AD", "AISD", "DFF").test(pkgStatic3));
        assertFalse(filterOnSourceId("AD", "ASD", "DFF").test(pkgStatic3));

        assertFalse(filterOnSourceId("AD", "AISD", "DFF").test(pkgStatic1)); // no source id
        pkgStatic1.getTags().setSourceId("DFF");
        assertTrue(filterOnSourceId("AD", "AISD", "DFF").test(pkgStatic1));
    }

    @Test
    public void testFilterOnSourceType() {
        assertFalse(filterOnSourceType(SourceType.SATELLITE).test(pkgStatic1));
        assertFalse(filterOnSourceType(SourceType.TERRESTRIAL).test(pkgStatic1));
        pkgStatic1.getTags().setSourceType(SourceType.SATELLITE);
        assertTrue(filterOnSourceType(SourceType.SATELLITE).test(pkgStatic1));
        assertFalse(filterOnSourceType(SourceType.TERRESTRIAL).test(pkgStatic1));
        pkgStatic1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertFalse(filterOnSourceType(SourceType.SATELLITE).test(pkgStatic1));
        assertTrue(filterOnSourceType(SourceType.TERRESTRIAL).test(pkgStatic1));
    }

    @Test
    public void testFilterOnSourceBaseStation() {
        assertTrue(AisPacketFilters.filterOnSourceBasestation("2190047").test(pkgStatic1));
        assertTrue(filterOnSourceBasestation(2190047).test(pkgStatic1));
        assertFalse(AisPacketFilters.filterOnSourceBasestation("2190046").test(pkgStatic1));
        assertFalse(filterOnSourceBasestation(2190046).test(pkgStatic1));
        assertTrue(AisPacketFilters.filterOnSourceBasestation("123322", "2190047", "1233223").test(pkgStatic1));
        assertTrue(filterOnSourceBasestation(12323, 2190047, 1230393).test(pkgStatic1));

        assertFalse(filterOnSourceBasestation(2190046).test(pkgStatic2));
        assertFalse(filterOnSourceBasestation(2190046).test(pkgStatic2));
    }

    @Test
    public void testFilterSourceOnCountry() {
        assertTrue(filterOnSourceCountry(Country.getByCode("DNK")).test(pkgStatic1));
        assertFalse(filterOnSourceCountry(Country.getByCode("NLD")).test(pkgStatic1));

        assertFalse(filterOnSourceCountry(Country.getByCode("DNK")).test(pkgStatic2));
        assertTrue(filterOnSourceCountry(Country.getByCode("NLD")).test(pkgStatic2));
        pkgStatic3.getTags().setSourceCountry(null);
        assertFalse(filterOnSourceCountry(Country.getByCode("DNK")).test(pkgStatic3));
     
    }
    
    @Test
    public void testFilterOnTargetCountry() {
        assertTrue(filterOnTargetCountry(Country.getByCode("DNK")).test(pkgStatic1));
        assertFalse(filterOnTargetCountry(Country.getByCode("NLD")).test(pkgStatic1));
    }


    @Test
    public void testFilterOnSourceRegion() {
        assertFalse(filterOnSourceRegion("0").test(pkgStatic1));
        assertTrue(filterOnSourceRegion("0").test(pkgStatic2));
        assertFalse(filterOnSourceRegion("1").test(pkgStatic2));
    }

    @Test
    public void testParseExpressionFilter() {
        //
        assertFilterExpression(true, pkgStatic3, "s.id = AISD, SD");

        pkgStatic1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertFilterExpression(true, pkgStatic1, "s.type = LIVE");
        assertFilterExpression(true, pkgStatic1, "s.type = SAT, LIVE");
        assertFilterExpression(false, pkgStatic1, "s.type = SAT");

        //
        assertFilterExpression(true, pkgStatic3, "s.id = AISD, SD");
        assertFilterExpression(false, pkgStatic3, "s.id = AFISD, SD");
        assertFilterExpression(true, pkgStatic3, "(s.id = AISD, SD) | s.country = DNK");
        assertFilterExpression(true, pkgStatic1, "(s.id = AISD, SD) | s.country = DNK");
        assertFilterExpression(false, pkgStatic2, "(s.id = AISD, SD) | s.country = DNK");
        assertFilterExpression(true, pkgStatic2, "(s.id = AISD, SD) | (s.country = DNK, NLD)");
        assertFilterExpression(true, pkgStatic2, "s.country = NLD & s.region = 0");
        assertFilterExpression(true, pkgStatic2, "s.country = DNK | s.region = 3,4,ERER,0");

        assertFilterExpression(false, pkgStatic1, "s.country = DNK & s.bs =2190047 & s.type = SAT");
        pkgStatic1.getTags().setSourceType(SourceType.SATELLITE);
        assertFilterExpression(true, pkgStatic1, "s.country = DNK & s.bs =2190047 & s.type = SAT");
        assertFilterExpression(true, pkgStatic1, "s.country = DNK & s.bs =3,4,4,5,5,2190047 & s.type = SAT");
        
        assertFilterExpression(true, pkgStatic1, "t.country=DNK & m.country=DNK");
        assertFilterExpression(true, pkgStatic1, "t.country in DEU | m.country in DNK");
        
        
        
    }

    @Test
    public void testMessageExpressionFilterBlocksMessagesNotCarryingTheFilteredTypeOfInformation() {
        assertTrue(pkgStatic2.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(((AisMessage5) pkgStatic2.tryGetAisMessage()).getImo() > 0);

        assertTrue(pkgPosition1.tryGetAisMessage() instanceof AisPositionMessage);
        assertTrue(((AisPositionMessage) pkgPosition1.tryGetAisMessage()).getNavStatus() == 0);

        // Position reports
        assertFilterExpression(false, pkgStatic2, "m.sog >= 0");
        assertFilterExpression(false, pkgStatic2, "m.cog >= 0");
        assertFilterExpression(false, pkgStatic2, "m.hdg >= 0");
        assertFilterExpression(false, pkgStatic2, "m.lat >= 0");
        assertFilterExpression(false, pkgStatic2, "m.lon >= 0");
        assertFilterExpression(false, pkgStatic2, "m.navstat >= 0");
        assertFilterExpression(false, pkgStatic2, "m.pos within circle(56,11,250000)");
        assertFilterExpression(false, pkgStatic2, "m.pos within bbox(50,09,60,12)");

        // Static messages
        assertFilterExpression(false, pkgPosition1, "m.draught >= 0");
        assertFilterExpression(false, pkgPosition1, "m.imo >= 0");
        assertFilterExpression(false, pkgPosition1, "m.type >= 0");
        assertFilterExpression(false, pkgPosition1, "m.name like *");
        assertFilterExpression(false, pkgPosition1, "m.cs like *");
    }

    @Test
    public void testParseExpressionFilterStrings() {
        assertFilterExpression(false, pkgPosition1, "m.name = a");
        assertFilterExpression(false, pkgPosition1, "m.name = '0'");
        assertFilterExpression(false, pkgPosition1, "m.name = 0");

        assertFilterExpression(true, pkgStatic1, "m.name = 'DIANA'");  // Ignore case
        assertFilterExpression(true, pkgStatic1, "m.name = 'diana'");
        assertFilterExpression(false, pkgStatic1, "m.name = 'dianax'");

        assertFilterExpression(false, pkgStatic2, "m.name = 'CONTI SING'"); // with space
        assertFilterExpression(true, pkgStatic2, "m.name = 'CONTI SINGA'"); // with space
        
        assertFilterExpression(false, pkgStatic1, "m.country = USA");
        assertFilterExpression(true, pkgStatic1, "m.country != USA");
        
        assertFilterExpression(true, pkgStatic1, "t.country = DNK");
    }
    

    @Test
    public void testParseExpressionFilterGlobs() {
        assertFilterExpression(true, pkgStatic1, "m.name like D*");
        assertFilterExpression(true, pkgStatic1, "m.name LIKE DI*");
        assertFilterExpression(true, pkgStatic1, "m.name LIKE DI?NA");
        assertFilterExpression(true, pkgStatic1, "m.name ~ D*A");
        assertFilterExpression(true, pkgStatic1, "m.name ~ 'DIA*'");

        assertFilterExpression(true, pkgStatic1, "m.cs like OVF?");
        assertFilterExpression(true, pkgStatic1, "m.cs like O*");
        assertFilterExpression(false, pkgStatic1, "m.cs like M*");

        assertFilterExpression(false, pkgStatic1, "m.name like a*");
        assertFilterExpression(false, pkgStatic1, "m.name LIKE a*");
        assertFilterExpression(false, pkgStatic1, "m.name LIKE DI??NA");
        assertFilterExpression(false, pkgStatic1, "m.name ~ a*");
        assertFilterExpression(false, pkgStatic1, "m.name ~ 'a*'");
    }

    @Test
    public void testParseExpressionFilterIntRanges() {
        assertFilterExpression(true, pkgPosition1, "m.id = 0 .. 4");
        assertFilterExpression(true, pkgPosition1, "m.id @ 0 .. 4");
        assertFilterExpression(true, pkgPosition1, "m.id IN 0 .. 4");
        assertFilterExpression(true, pkgPosition1, "m.id in 0 .. 4");
        assertFilterExpression(true, pkgPosition1, "m.id in (0 .. 4)");
        assertFilterExpression(true, pkgPosition1, "m.id in 0..4");
        assertFilterExpression(true, pkgPosition1, "m.id in (0..4)");
        assertFilterExpression(false, pkgPosition1, "m.id in -5..-2");
    }

    @Test
    @Ignore
    public void testParseExpressionFilterNumberRanges() {
        // TODO
        assertFilterExpression(true, pkgPosition1, "m.id in 0.0 .. 4.1");
        assertFilterExpression(true, pkgPosition1, "m.id in (0.0 .. 4.1)");
        assertFilterExpression(true, pkgPosition1, "m.id in 0.0..4.1");
        assertFilterExpression(true, pkgPosition1, "m.id in (0.0..4.1)");
        assertFilterExpression(false, pkgPosition1, "m.id in -5.0..-2.1");
        assertFilterExpression(false, pkgPosition1, "m.id in -5..-2.1");
        assertFilterExpression(false, pkgPosition1, "m.id in -5.0..-2");
    }

    @Test
    public void testParseExpressionFilterIntLists() {
        assertFilterExpression(true, pkgPosition1, "m.id = 0,1,4");
        assertFilterExpression(true, pkgPosition1, "m.id @ 0,1,4");
        assertFilterExpression(true, pkgPosition1, "m.id IN 0,1,4");
        assertFilterExpression(true, pkgPosition1, "m.id in 0,1,4");
        assertFilterExpression(true, pkgPosition1, "m.id in (0,1,4)");
    }

    @Test
    public void testParseExpressionFilterStringLists() {
        assertFilterExpression(true, pkgStatic1, "m.name in ('A','DIANA','Z')");
        assertFilterExpression(true, pkgStatic1, "m.name in 'A','DIANA','Z'");
        assertFilterExpression(true, pkgStatic1, "m.name in (A,DIANA,Z)");
        assertFilterExpression(true, pkgStatic1, "m.name @ (A,DIANA,Z)");
        assertFilterExpression(true, pkgStatic1, "m.name @ A,DIANA,Z");
        assertFilterExpression(true, pkgStatic1, "m.name = (A,DIANA,Z)");
        assertFilterExpression(true, pkgStatic1, "m.name = A,DIANA,Z");
        
        assertFilterExpression(true, pkgStatic1, "m.country in "+Country.getCountryForMmsi(pkgStatic1.tryGetAisMessage().getUserId()).getThreeLetter());
        assertFilterExpression(false, pkgStatic1, "m.country in USA");
    }

    @Test
    public void testAndOrExpressions() {
        assertFilterExpression(true, pkgPosition1, "m.sog > 6.0 & m.sog < 7.0");
        assertFilterExpression(true, pkgPosition1, "m.sog >= 6.6 & m.sog < 6.7");
        assertFilterExpression(false, pkgPosition1, "m.sog > 6.6 & m.sog < 7.0");
        assertFilterExpression(false, pkgPosition1, "m.sog > 6.0 & m.sog < 6.6");

        // pkgPosition1: sog = 66, cog=3500, msgId=1, mmsi=576048000, hdg=355, lat=37.912166666666664, lon=-122.42298333333333
        assertFilterExpression(true, pkgPosition1, "m.sog > 6.0 | m.sog < 7.0");
        assertFilterExpression(false, pkgPosition1, "m.sog > 6.6 | m.sog < 6.4");

        assertFilterExpression(false, pkgPosition1, "m.sog > 6.6 & m.sog < 6.7");
        assertFilterExpression(true, pkgPosition1, "m.sog >= 6.6 & m.sog < 6.7");
        assertFilterExpression(true, pkgPosition1, "m.sog >= 6.1 & m.sog <= 6.9");
        assertFilterExpression(false, pkgPosition1, "m.sog >= 6.7 & m.sog <= 6.8");

        assertFilterExpression(true, pkgPosition1, "m.id = 1 & m.sog >= 6.1 & m.sog <= 6.9");
        assertFilterExpression(false, pkgPosition1, "m.id = 2 & m.sog >= 6.1 & m.sog <= 6.9");

        assertFilterExpression(true, pkgPosition1, "m.id = 2 | m.sog >= 6.1");
        assertFilterExpression(true, pkgPosition1, "m.id = 1 | m.sog < 6.1");
        assertFilterExpression(false, pkgPosition1, "m.id = 2 & m.sog >= 6.1");
        assertFilterExpression(false, pkgPosition1, "m.id = 1 & m.sog < 6.1");

        assertFilterExpression(true, pkgPosition1, "m.sog > 6.0 & m.hdg > 350 & m.id = 1");
        assertFilterExpression(false, pkgPosition1, "m.lat in 55..56 & m.lon in 11..12 & m.sog > 6.0 & m.hdg > 350 & m.id = 2");
        assertFilterExpression(false, pkgPosition1, "m.lat in 37..38 & m.lon in -123..-122 & m.sog > 7.0 & m.hdg > 350 & m.id = 1");
        assertFilterExpression(true, pkgPosition1, "m.lat in 37..38 & m.lon in -123..-122 & m.sog > 6.0 & m.hdg > 350 & m.id = 1");

        assertFilterExpression(false, pkgPosition1, "s.type=SAT|s.id=AISD|s.id=MSSIS");
        assertFilterExpression(true, pkgPosition1, "s.type!=SAT|s.id!=AISD|s.id!=MSSIS");
    }

    @Test
    public void testParseExpressionFilterLabels /* enums? */() {
        assertFilterExpression(false, pkgStatic1, "m.type = NONEXISTENTLABEL");
        assertFilterExpression(false, pkgStatic1, "m.type = TANKER");
        assertFilterExpression(true, pkgStatic1, "m.type = MILITARY");
        assertFilterExpression(true, pkgStatic1, "m.type = military");
        assertFilterExpression(true, pkgStatic1, "m.type = 'MILITARY'");
        assertFilterExpression(true, pkgStatic1, "m.type in MILITARY, TANKER, HSC, FISHING");
        assertFilterExpression(true, pkgStatic1, "m.type in TANKER, MILITARY, HSC, FISHING");
        assertFilterExpression(true, pkgStatic1, "m.type in tanker, MILITARY, HSC, FISHING");
        assertFilterExpression(true, pkgStatic1, "m.type in tanker, military, HSC, FISHING");
        assertFilterExpression(true, pkgStatic1, "m.type in 'tanker', 'military', HSC");
        assertFilterExpression(false, pkgStatic1, "m.type in 'tanker', 'militar', HSC");
        assertFilterExpression(false, pkgStatic1, "m.type in PASSENGER, TANKER, HSC, FISHING");
        assertFilterExpression(false, pkgStatic1, "m.type = PASSENGER, TANKER, HSC, FISHING");
    }

    @Test
    public void testExpressionFilterComparisons() {
        testComparison(pkgStatic1, "m.id", pkgStatic1.tryGetAisMessage().getMsgId());
        testComparison(pkgStatic1, "m.mmsi", pkgStatic1.tryGetAisMessage().getUserId());
        testComparison(pkgStatic2, "m.imo", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getImo());
        testComparison(pkgPosition1, "m.sog", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getSog() / 10.0f);
        testComparison(pkgPosition1, "m.cog", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getCog() / 10.0f);
        testComparison(pkgPosition1, "m.hdg", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getTrueHeading());
        testComparison(pkgPosition1, "m.lon", ((IPositionMessage) pkgPosition1.tryGetAisMessage()).getPos().getLongitudeDouble());
        testComparison(pkgPosition1, "m.lat", ((IPositionMessage) pkgPosition1.tryGetAisMessage()).getPos().getLatitudeDouble());
        testComparison(pkgStatic1, "m.draught", ((AisMessage5) pkgStatic1.tryGetAisMessage()).getDraught() / 10.0f);
        testComparison(pkgStatic1, "m.name", ((AisMessage5) pkgStatic1.tryGetAisMessage()).getName());
        testComparison(pkgStatic1, "m.cs", ((AisMessage5) pkgStatic1.tryGetAisMessage()).getCallsign());
        testComparison(pkgStatic1, "m.type", ((AisMessage5) pkgStatic1.tryGetAisMessage()).getShipType());
        testComparison(pkgPosition1, "m.navstat", ((AisPositionMessage) pkgPosition1.tryGetAisMessage()).getNavStatus());
    }

    @Test
    public void testExpressionFilter() {
        test(pkgStatic1, "m.id", pkgStatic1.tryGetAisMessage().getMsgId());
        test(pkgStatic1, "m.mmsi", pkgStatic1.tryGetAisMessage().getUserId());
        test(pkgStatic2, "m.imo", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getImo());
        test(pkgStatic2, "m.name", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getName());
        test(pkgStatic2, "m.cs", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getCallsign());
        test(pkgStatic2, "m.type", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getShipType());
        test(pkgStatic2, "m.type", new ShipTypeCargo(((AisMessage5) pkgStatic2.tryGetAisMessage()).getShipType()).getShipType().toString());
        test(pkgPosition1, "m.navstat", ((AisPositionMessage) pkgPosition1.tryGetAisMessage()).getNavStatus());
        test(pkgPosition1, "m.navstat", NavigationalStatus.get(((AisPositionMessage) pkgPosition1.tryGetAisMessage()).getNavStatus()));
    }

    @Test
    public void testExpressionFilterInRange() {
        testInRange(pkgStatic1, "m.id", pkgStatic1.tryGetAisMessage().getMsgId());
        testInRange(pkgStatic1, "m.mmsi", pkgStatic1.tryGetAisMessage().getUserId());
        testInRange(pkgStatic2, "m.imo", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getImo());
        testInRange(pkgPosition1, "m.hdg", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getTrueHeading());
        testInRange(pkgPosition1, "m.lat", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getPos().getLatitudeDouble());
        testInRange(pkgPosition1, "m.lon", ((IVesselPositionMessage) pkgPosition1.tryGetAisMessage()).getPos().getLongitudeDouble());
        testInRange(pkgStatic2, "m.type", ((AisMessage5) pkgStatic2.tryGetAisMessage()).getShipType());
        testInRange(pkgPosition1, "m.navstat", ((AisPositionMessage) pkgPosition1.tryGetAisMessage()).getNavStatus());
    }

    @Test
    public void testCastNumericFilterExpressionToStringForStringFields() {
        // Test casting from float,int to string
        assertFilterExpression(false, pkgStatic2, "m.name = 0");
        assertFilterExpression(false, pkgStatic2, "m.name = 0.0");
    }

    @Test
    public void testParseExpressionFilterNegative() {
        assertFilterExpression(true, pkgStatic3, "s.id != SD");
        assertFilterExpression(false, pkgStatic3, "s.id != AISD");
        assertFilterExpression(false, pkgStatic3, "s.id != AISD, SD");
        assertFilterExpression(true, pkgStatic3, "s.id != AFISD, SD");
        assertFilterExpression(true, pkgStatic2, "(s.id != AISD, SD) | s.country != DNK");
        assertFilterExpression(true, pkgStatic2, "(s.id != AISD, SD) | (s.country != DNK, NLD)");
        assertFilterExpression(false, pkgStatic3, "s.country != NLD");
        pkgStatic1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertFilterExpression(false, pkgStatic1, "s.type != LIVE");
        assertFilterExpression(false, pkgStatic2, "s.region != 0");
    }

    @Test
    public void testMessagePositionWithinCircle() {
        AisPacket packet = pkgPosition1;
        IPositionMessage msg = (IPositionMessage) packet.tryGetAisMessage();
        Position center = msg.getPos().getGeoLocation();
        double centerLatitude = center.getLatitude();
        double centerLongitude = center.getLongitude();

        Position pos2100MetersNE = CoordinateSystem.CARTESIAN.pointOnBearing(center, 2100, 45);
        Position pos2000MetersNE = CoordinateSystem.CARTESIAN.pointOnBearing(center, 2000, 45);
        Position pos1900MetersNE = CoordinateSystem.CARTESIAN.pointOnBearing(center, 1900, 45);

        assertFilterExpression(true, packet, "m.pos within circle(" + centerLatitude + ", " + centerLongitude + ", " + "2000)");
        assertFilterExpression(false, packet, "m.pos within circle(" + pos2100MetersNE.getLatitude() + ", " + pos2100MetersNE.getLongitude() + ", " + "2000)");
        assertFilterExpression(false, packet, "m.pos within circle(" + pos2000MetersNE.getLatitude() + ", " + pos2000MetersNE.getLongitude() + ", " + "2000)");
        assertFilterExpression(true, packet, "m.pos within circle(" + pos1900MetersNE.getLatitude() + ", " + pos1900MetersNE.getLongitude() + ", " + "2000)");

        // Message with no position information is blocked
        assertTrue(pkgStatic1.tryGetAisMessage() instanceof AisMessage5);
        assertFilterExpression(false, pkgStatic1, "m.pos within circle(" + centerLatitude + ", " + centerLongitude + ", " + "2000)");
    }

    @Test
    public void testMessagePositionWithinBbox() {
        AisPacket packet = pkgPosition1;
        IPositionMessage msg = (IPositionMessage) packet.tryGetAisMessage();
        Position position = msg.getPos().getGeoLocation();

        int width = 2000;

        Position ne = CoordinateSystem.CARTESIAN.pointOnBearing(position, width / 2, 45);
        Position sw = CoordinateSystem.CARTESIAN.pointOnBearing(position, width / 2, 45 + 180);
        BoundingBox bboxWithPositionInside = BoundingBox.create(ne, sw, CoordinateSystem.CARTESIAN);

        Position nw = CoordinateSystem.CARTESIAN.pointOnBearing(position, width, 45);
        Position se = CoordinateSystem.CARTESIAN.pointOnBearing(nw, width, 135);
        BoundingBox bboxWithPositionOutside = BoundingBox.create(nw, se, CoordinateSystem.CARTESIAN);

        assertFilterExpression(true, packet, "m.pos within bbox(" + bboxWithPositionInside.getMinLat() + ", " + bboxWithPositionInside.getMinLon() + ", " + +bboxWithPositionInside.getMaxLat() + ", " + bboxWithPositionInside.getMaxLon() + ")");
        assertFilterExpression(false, packet, "m.pos within bbox(" + bboxWithPositionOutside.getMinLat() + ", " + bboxWithPositionOutside.getMinLon() + ", " + +bboxWithPositionOutside.getMaxLat() + ", " + bboxWithPositionOutside.getMaxLon() + ")");

        // Message with no position information is blocked
        assertTrue(pkgStatic1.tryGetAisMessage() instanceof AisMessage5);
        assertFilterExpression(false, pkgStatic1, "m.pos within bbox(" + bboxWithPositionInside.getMinLat() + ", " + bboxWithPositionInside.getMinLon() + ", " + +bboxWithPositionInside.getMaxLat() + ", " + bboxWithPositionInside.getMaxLon() + ")");
    }

    @Test
    public void testMessageTimeCompareTo() {
        // pkgStatic: 2013-03-13T12:41:00.000+0100
        testComparison(pkgStatic1, "m.year", 2013);
        testComparison(pkgStatic1, "m.month", 3);
        testComparison(pkgStatic1, "m.dom", 13);
        testComparison(pkgStatic1, "m.dow", 3);
        testComparison(pkgStatic1, "m.hour", 12);
        testComparison(pkgStatic1, "m.minute", 41);

        assertFilterExpression(true, pkgStatic1, "m.month = 03");      assertFilterExpression(false, pkgStatic1, "m.month != 03");
        assertFilterExpression(true, pkgStatic1, "m.month = mar");     assertFilterExpression(false, pkgStatic1, "m.month != mar");
        assertFilterExpression(true, pkgStatic1, "m.month = MAR");     assertFilterExpression(false, pkgStatic1, "m.month != MAR");
        assertFilterExpression(true, pkgStatic1, "m.month = MARCH");   assertFilterExpression(false, pkgStatic1, "m.month != MARCH");
        assertFilterExpression(true, pkgStatic1, "m.dow = wed");       assertFilterExpression(false, pkgStatic1, "m.dow != wed");
        assertFilterExpression(true, pkgStatic1, "m.dow = WED");       assertFilterExpression(false, pkgStatic1, "m.dow != WED");
        assertFilterExpression(true, pkgStatic1, "m.dow = WEDNESDAY"); assertFilterExpression(false, pkgStatic1, "m.dow != WEDNESDAY");
    }

    @Test
    public void testMessageTimeInRange() {
        // pkgStatic: 2013-03-13T12:41:00.000+0100
        assertFilterExpression(true, pkgStatic1, "m.year in 2011..2014");
        assertFilterExpression(true, pkgStatic1, "m.month in 2..4");       assertFilterExpression(false, pkgStatic1, "m.month not in 2..4");
        // TODO ? assertFilterExpression(true, pkgStatic, "m.month in feb..may");   assertFilterExpression(false, pkgStatic, "m.month not in feb..may");
        assertFilterExpression(true, pkgStatic1, "m.dom in 10..15");
        assertFilterExpression(true, pkgStatic1, "m.dow in 2..4");         assertFilterExpression(false, pkgStatic1, "m.dow not in 2..4");
        // TODO ? assertFilterExpression(true, pkgStatic, "m.dow in mon..fri");     assertFilterExpression(false, pkgStatic, "m.dow not in mon..fri");
        assertFilterExpression(true, pkgStatic1, "m.hour = 12..13");
        assertFilterExpression(true, pkgStatic1, "m.minute = 40..49");
    }

    @Test
    public void testMessageTimeInIntList() {
        // pkgStatic: 2013-03-13T12:41:00.000+0100
        assertFilterExpression(true, pkgStatic1, "m.year in 2011..2014");     assertFilterExpression(false, pkgStatic1, "m.year not in 2013, 2014");
        assertFilterExpression(true, pkgStatic1, "m.month in 2,3,4");         assertFilterExpression(false, pkgStatic1, "m.month not in 2,3,4");
        assertFilterExpression(true, pkgStatic1, "m.dom in 12,13,14");
        assertFilterExpression(true, pkgStatic1, "m.dow in 2, 3, 4");         assertFilterExpression(false, pkgStatic1, "m.dow not in 2,3,4");
        assertFilterExpression(true, pkgStatic1, "m.hour = 12, 13");
        assertFilterExpression(true, pkgStatic1, "m.minute = 40,41,42,43,44,45,49");
    }

    @Test
    public void testMessageTimeInStringList() {
        // pkgStatic: 2013-03-13T12:41:00.000+0100
        assertFilterExpression(true, pkgStatic1, "m.month in jan,feb,mar");            assertFilterExpression(false, pkgStatic1, "m.month not in jan,feb,mar");
        assertFilterExpression(true, pkgStatic1, "m.month @ jan,feb,mar");             assertFilterExpression(false, pkgStatic1, "m.month !@ jan,feb,mar");
        assertFilterExpression(true, pkgStatic1, "m.month = jan,feb,mar");             assertFilterExpression(false, pkgStatic1, "m.month != jan,feb,mar");
        assertFilterExpression(true, pkgStatic1, "m.month = (jan,feb,mar)");           assertFilterExpression(false, pkgStatic1, "m.month != (jan,feb,mar)");
        assertFilterExpression(true, pkgStatic1, "m.month in JAN,feb,MAR");            assertFilterExpression(false, pkgStatic1, "m.month not in JAN,feb,MAR");
        assertFilterExpression(true, pkgStatic1, "m.month in JAN,febRuary,MARCH");     assertFilterExpression(false, pkgStatic1, "m.month not in JAN,febRuary,MARCH");
        assertFilterExpression(true, pkgStatic1, "m.dow in mon,tue,wed");              assertFilterExpression(false, pkgStatic1, "m.dow not in mon,tue,wed");
        assertFilterExpression(true, pkgStatic1, "m.dow in monday,tuesday,Wednesday"); assertFilterExpression(false, pkgStatic1, "m.dow not in monday,tuesday,Wednesday");
        assertFilterExpression(true, pkgStatic1, "m.dow = monday,tuesday,Wednesday");  assertFilterExpression(false, pkgStatic1, "m.dow != monday,tuesday,Wednesday");
    }

    private static void assertFilterExpression(boolean expectedResult, AisPacket aisPacket, String filterExpression) {
        System.out.println("Testing \"" + filterExpression + "\" to be " + expectedResult + " for " + aisPacket.tryGetAisMessage().toString());
        assertEquals(expectedResult, parseExpressionFilter(filterExpression).test(aisPacket));
    }

    private static void test(AisPacket testPacket, String field, Object fieldValue) {
        Object otherValue1, otherValue2;
        if (fieldValue instanceof Integer) {
            otherValue1 = new Integer(((Integer) fieldValue) + 4325);
            otherValue2 = new Integer(((Integer) fieldValue) - 1);
        } else if (fieldValue instanceof Long) {
            otherValue1 = new Long(((Long) fieldValue) + 4325L);
            otherValue2 = new Long(((Long) fieldValue) - 1L);
        } else if (fieldValue instanceof String) {
            otherValue1 = "VALUE1";
            otherValue2 = "VALUE2";
            fieldValue = ((String) fieldValue).replace('@', ' ').trim();
            if (((String) fieldValue).contains(" ")) {
                fieldValue = "'" + fieldValue + "'";
            }
        } else if (fieldValue instanceof Enum) {
            otherValue1 = "VALUE1";
            otherValue2 = "VALUE2";
            fieldValue = ((Enum) fieldValue).name();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + fieldValue.getClass());
        }

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + "," + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(true, testPacket, field + " IN (" + otherValue1 + "," + fieldValue + "," + otherValue2 + ")");
        assertFilterExpression(true, testPacket, field + " @ " + otherValue1 + "," + otherValue2 + ", " + fieldValue + "");
        // TODO assertFilterExpression(true, testPacket, field + "@" + fieldValue + ", " + otherValue1 + ", " + otherValue2 + "");
        assertFilterExpression(true, testPacket, field + " @ (" + fieldValue + ", " + otherValue1 + ", " + otherValue2 + ")");
        // TODO assertFilterExpression(true, testPacket, field + "@(" + fieldValue + "," + otherValue1 + "," + otherValue2 + ")");

        assertFilterExpression(false, testPacket, field + " in " + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(false, testPacket, field + " IN (" + otherValue1 + "," + otherValue2 + ")");
        assertFilterExpression(false, testPacket, field + " @ " + otherValue1 + "," + otherValue2 + "");
        // TODO assertFilterExpression(false, testPacket, field + "@" + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(false, testPacket, field + " @ (" + otherValue1 + "," + otherValue2 + ")");
        // TODO assertFilterExpression(false, testPacket, field + "@(" + otherValue1 + "," + otherValue2 + ")");
    }

    private static void testInRange(AisPacket testPacket, String field, Number fieldValue) {
        Number below, above;
        if (fieldValue instanceof Integer) {
            below = new Integer(((Integer) fieldValue) - 10);
            above = new Integer(((Integer) fieldValue) + 10);
        } else if (fieldValue instanceof Long) {
            below = new Long(((Long) fieldValue) - 10L);
            above = new Long(((Long) fieldValue) + 10L);
        } else if (fieldValue instanceof Float) {
            below = new Float(((Float) fieldValue) - 10.0f);
            above = new Float(((Float) fieldValue) + 10.0f);
        } else if (fieldValue instanceof Double) {
            below = new Float(((Double) fieldValue) - 10.0f);
            above = new Float(((Double) fieldValue) + 10.0f);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + fieldValue.getClass());
        }

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + above);
        assertFilterExpression(true, testPacket, field + " in " + below + ".." + fieldValue);
        assertFilterExpression(true, testPacket, field + " in " + below + ".." + above);
        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + fieldValue);

        assertFilterExpression(false, testPacket, field + " in " + (below.longValue() - 10) + ".." + below);
        assertFilterExpression(false, testPacket, field + " in " + above + ".." + (above.longValue() + 10));

        assertFilterExpression(true, testPacket, field + " in (" + fieldValue + ".." + above + ")");
        assertFilterExpression(true, testPacket, field + " IN (" + fieldValue + ".." + above + ")");
        assertFilterExpression(true, testPacket, field + " @ (" + fieldValue + ".." + above + ")");

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + above + "");
        assertFilterExpression(true, testPacket, field + " IN " + fieldValue + ".." + above + "");
        assertFilterExpression(true, testPacket, field + " @ " + fieldValue + ".." + above + "");

        assertFilterExpression(true, testPacket, field + "@" + fieldValue + ".." + above + "");
    }

    private static void testComparison(AisPacket testPacket, String field, Object fieldValue) {
        Object largerValue, lesserValue;
        if (fieldValue instanceof Integer) {
            lesserValue = new Integer(((Integer) fieldValue) - 1);
            largerValue = new Integer(((Integer) fieldValue) + 1);
        } else if (fieldValue instanceof Long) {
            lesserValue = new Long(((Long) fieldValue) - 1L);
            largerValue = new Long(((Long) fieldValue) + 1L);
        } else if (fieldValue instanceof Float) {
            lesserValue = new Float(((Float) fieldValue) - 1.0f);
            largerValue = new Float(((Float) fieldValue) + 1.0f);
        } else if (fieldValue instanceof Double) {
            lesserValue = new Double(((Double) fieldValue) - 1.0);
            largerValue = new Double(((Double) fieldValue) + 1.0);
        } else if (fieldValue instanceof String) {
            fieldValue = ((String) fieldValue).replace('@', ' ').trim();
            lesserValue = "0";
            largerValue = "Z" + fieldValue.toString();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + fieldValue.getClass());
        }

        // Test = operator
        assertFilterExpression(true, testPacket, field + " = " + fieldValue);
        assertFilterExpression(false, testPacket, field + " = " + largerValue);
        assertFilterExpression(false, testPacket, field + " = " + lesserValue);

        // Test != operator
        assertFilterExpression(false, testPacket, field + " != " + fieldValue);
        assertFilterExpression(true, testPacket, field + " != " + largerValue);

        // Test > operator
        assertFilterExpression(true, testPacket, field + " > " + lesserValue);
        assertFilterExpression(false, testPacket, field + "> " + fieldValue);
        assertFilterExpression(false, testPacket, field + "  > " + largerValue);

        // Test >= operator
        assertFilterExpression(true, testPacket, field + " >= " + lesserValue);
        assertFilterExpression(true, testPacket, field + " >= " + fieldValue);
        assertFilterExpression(false, testPacket, field + " >= " + largerValue);

        // Test < operator
        assertFilterExpression(false, testPacket, field + " < " + lesserValue);
        assertFilterExpression(false, testPacket, field + " < " + fieldValue);
        assertFilterExpression(true, testPacket, field + " < " + largerValue);

        // Test <= operator
        assertFilterExpression(false, testPacket, field + " <= " + lesserValue);
        assertFilterExpression(true, testPacket, field + " <= " + fieldValue);
        assertFilterExpression(true, testPacket, field + " <= " + largerValue);
    }
}
