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

import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceBasestation;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceCountry;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceId;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceRegion;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceType;
import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
  *
  * @author Kasper Nielsen
  */
public class AisPacketFiltersTest {

    AisPacket p1;
    AisPacket p2;
    AisPacket p3;
    AisPacket p5;

    @Before
     public void setup() {
        p1 = AisTestPackets.p1();
        p2 = AisTestPackets.p2();
        p3 = AisTestPackets.p3();
        p5 = AisTestPackets.p5();
    }

    @Test
    public void testfilterOnSourceId() {
        assertTrue(filterOnSourceId("AISD").test(p3));
        assertTrue(filterOnSourceId("AD", "AISD", "DFF").test(p3));
        assertFalse(filterOnSourceId("AD", "ASD", "DFF").test(p3));

        assertFalse(filterOnSourceId("AD", "AISD", "DFF").test(p1)); // no source id
        p1.getTags().setSourceId("DFF");
        assertTrue(filterOnSourceId("AD", "AISD", "DFF").test(p1));
    }

    @Test
    public void testFilterOnSourceType() {
        assertFalse(filterOnSourceType(SourceType.SATELLITE).test(p1));
        assertFalse(filterOnSourceType(SourceType.TERRESTRIAL).test(p1));
        p1.getTags().setSourceType(SourceType.SATELLITE);
        assertTrue(filterOnSourceType(SourceType.SATELLITE).test(p1));
        assertFalse(filterOnSourceType(SourceType.TERRESTRIAL).test(p1));
        p1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertFalse(filterOnSourceType(SourceType.SATELLITE).test(p1));
        assertTrue(filterOnSourceType(SourceType.TERRESTRIAL).test(p1));
    }

    @Test
    public void testFilterOnSourceBaseStation() {
        assertTrue(AisPacketFilters.filterOnSourceBasestation("2190047").test(p1));
        assertTrue(filterOnSourceBasestation(2190047).test(p1));
        assertFalse(AisPacketFilters.filterOnSourceBasestation("2190046").test(p1));
        assertFalse(filterOnSourceBasestation(2190046).test(p1));
        assertTrue(AisPacketFilters.filterOnSourceBasestation("123322", "2190047", "1233223").test(p1));
        assertTrue(filterOnSourceBasestation(12323, 2190047, 1230393).test(p1));

        assertFalse(filterOnSourceBasestation(2190046).test(p2));
        assertFalse(filterOnSourceBasestation(2190046).test(p2));
    }

    @Test
    public void testFilterOnCountry() {
        assertTrue(filterOnSourceCountry(Country.getByCode("DNK")).test(p1));
        assertFalse(filterOnSourceCountry(Country.getByCode("NLD")).test(p1));

        assertFalse(filterOnSourceCountry(Country.getByCode("DNK")).test(p2));
        assertTrue(filterOnSourceCountry(Country.getByCode("NLD")).test(p2));
        p3.getTags().setSourceCountry(null);
        assertFalse(filterOnSourceCountry(Country.getByCode("DNK")).test(p3));
        assertFalse(filterOnSourceCountry(Country.getByCode("DNK")).test(p3));
    }

    @Test
    public void testFilterOnSourceRegion() {
        assertFalse(filterOnSourceRegion("0").test(p1));
        assertTrue(filterOnSourceRegion("0").test(p2));
        assertFalse(filterOnSourceRegion("1").test(p2));
    }

    @Test
    public void testParseExpressionFilter() {
       //
       assertFilterExpression(true, p3, "s.id = AISD, SD");

       p1.getTags().setSourceType(SourceType.TERRESTRIAL);
       assertFilterExpression(true, p1, "s.type = LIVE");
       assertFilterExpression(true, p1, "s.type = SAT, LIVE");
       assertFilterExpression(false, p1, "s.type = SAT");

       //
       assertFilterExpression(true, p3, "s.id = AISD, SD");
       assertFilterExpression(false, p3, "s.id = AFISD, SD");
       assertFilterExpression(true, p3, "(s.id = AISD, SD) | s.country = DNK");
       assertFilterExpression(true, p1, "(s.id = AISD, SD) | s.country = DNK");
       assertFilterExpression(false, p2, "(s.id = AISD, SD) | s.country = DNK");
       assertFilterExpression(true, p2, "(s.id = AISD, SD) | (s.country = DNK, NLD)");
       assertFilterExpression(true, p2, "s.country = NLD & s.region = 0");
       assertFilterExpression(true, p2, "s.country = DNK | s.region = 3,4,ERER,0");

       assertFilterExpression(false, p1, "s.country = DNK & s.bs =2190047 & s.type = SAT");
       p1.getTags().setSourceType(SourceType.SATELLITE);
       assertFilterExpression(true, p1, "s.country = DNK & s.bs =2190047 & s.type = SAT");
       assertFilterExpression(true, p1, "s.country = DNK & s.bs =3,4,4,5,5,2190047 & s.type = SAT");
    }

    @Test
    public void testMessagePassesExpressionFilterForMessagesCarryingTheFilteredTypeOfInformation() {
        assertTrue(p2.tryGetAisMessage() instanceof AisMessage5);
        assertTrue(((AisMessage5) p2.tryGetAisMessage()).getImo() > 0);

        assertTrue(p5.tryGetAisMessage() instanceof AisPositionMessage);
        assertTrue(((AisPositionMessage) p5.tryGetAisMessage()).getNavStatus() == 0);

        // Position reports
        assertFilterExpression(true, p2, "m.sog = 0");
        assertFilterExpression(true, p2, "m.cog = 0");
        assertFilterExpression(true, p2, "m.hdg = 0");
        assertFilterExpression(true, p2, "m.lat = 0");
        assertFilterExpression(true, p2, "m.lon = 0");
        assertFilterExpression(true, p2, "m.navstat = 1");
        assertFilterExpression(true, p2, "m.pos within circle(0,0,1)");

        assertFilterExpression(false, p5, "m.sog = 0");
        assertFilterExpression(false, p5, "m.cog = 0");
        assertFilterExpression(false, p5, "m.hdg = 0");
        assertFilterExpression(false, p5, "m.lat = 0");
        assertFilterExpression(false, p5, "m.lon = 0");
        assertFilterExpression(false, p5, "m.navstat = 1");
        assertFilterExpression(false, p5, "m.pos within circle(0,0,1)");

        // Static messages
        assertFilterExpression(true, p5, "m.draught = 0");
        assertFilterExpression(true, p5, "m.imo = 0");
        assertFilterExpression(true, p5, "m.type = 0");
        assertFilterExpression(true, p5, "m.name = 0");
        assertFilterExpression(true, p5, "m.cs = 0");

        assertFilterExpression(false, p2, "m.draught = 0");
        assertFilterExpression(false, p2, "m.imo = 0");
        assertFilterExpression(false, p2, "m.type = 0");
        assertFilterExpression(false, p2, "m.name = 0");
        assertFilterExpression(false, p2, "m.cs = 0");
    }

    @Test
    public void testParseExpressionFilterStrings() {
        assertFilterExpression(true, p5, "m.name = a");
        assertFilterExpression(true, p5, "m.name = '0'");
        assertFilterExpression(true, p5, "m.name = 0");

        assertFilterExpression(true, p1, "m.name = 'DIANA'");  // Ignore case
        assertFilterExpression(true, p1, "m.name = 'diana'");
        assertFilterExpression(false, p1, "m.name = 'dianax'");

        assertFilterExpression(false, p2, "m.name = 'CONTI SING'"); // with space
        assertFilterExpression(true, p2, "m.name = 'CONTI SINGA'"); // with space
    }

    @Test
    public void testParseExpressionFilterIntRanges() {
        assertFilterExpression(true, p5, "m.id = 0 .. 4");
        assertFilterExpression(true, p5, "m.id @ 0 .. 4");
        assertFilterExpression(true, p5, "m.id IN 0 .. 4");
        assertFilterExpression(true, p5, "m.id in 0 .. 4");
        assertFilterExpression(true, p5, "m.id in (0 .. 4)");
        assertFilterExpression(true, p5, "m.id in 0..4");
        assertFilterExpression(true, p5, "m.id in (0..4)");
        assertFilterExpression(false, p5, "m.id in -5..-2");
    }

    @Test
    @Ignore
    public void testParseExpressionFilterNumberRanges() {
        // TODO
        assertTrue(p5.tryGetAisMessage() instanceof AisPositionMessage);
        assertFilterExpression(true, p5, "m.id in 0.0 .. 4.1");
        assertFilterExpression(true, p5, "m.id in (0.0 .. 4.1)");
        assertFilterExpression(true, p5, "m.id in 0.0..4.1");
        assertFilterExpression(true, p5, "m.id in (0.0..4.1)");
        assertFilterExpression(false, p5, "m.id in -5.0..-2.1");
        assertFilterExpression(false, p5, "m.id in -5..-2.1");
        assertFilterExpression(false, p5, "m.id in -5.0..-2");
    }

    @Test
    public void testParseExpressionFilterIntLists() {
        assertFilterExpression(true, p5, "m.id = 0,1,4");
        assertFilterExpression(true, p5, "m.id @ 0,1,4");
        assertFilterExpression(true, p5, "m.id IN 0,1,4");
        assertFilterExpression(true, p5, "m.id in 0,1,4");
        assertFilterExpression(true, p5, "m.id in (0,1,4)");
        assertFilterExpression(true, p5, "m.name in ('A','BCD','Z')");
        assertFilterExpression(true, p5, "m.name in 'A','BCD','Z'");
        assertFilterExpression(true, p5, "m.name in (A,BCD,Z)");
    }

     @Test
     public void testAndOrExpressions() {
        assertFilterExpression(true, p5, "m.sog > 6.0 & m.sog < 7.0");
        assertFilterExpression(true, p5, "m.sog >= 6.6 & m.sog < 6.7");
        assertFilterExpression(false, p5, "m.sog > 6.6 & m.sog < 7.0");
        assertFilterExpression(false, p5, "m.sog > 6.0 & m.sog < 6.6");

        // p5: sog = 66, cog=3500, msgId=1, mmsi=576048000, hdg=355, lat=37.912166666666664, lon=-122.42298333333333
        assertFilterExpression(true, p5, "m.sog > 6.0 | m.sog < 7.0");
        assertFilterExpression(false, p5, "m.sog > 6.6 | m.sog < 6.4");

        assertFilterExpression(false, p5, "m.sog > 6.6 & m.sog < 6.7");
        assertFilterExpression(true, p5, "m.sog >= 6.6 & m.sog < 6.7");
        assertFilterExpression(true, p5, "m.sog >= 6.1 & m.sog <= 6.9");
        assertFilterExpression(false, p5, "m.sog >= 6.7 & m.sog <= 6.8");

        assertFilterExpression(true, p5, "m.id = 1 & m.sog >= 6.1 & m.sog <= 6.9");
        assertFilterExpression(false, p5, "m.id = 2 & m.sog >= 6.1 & m.sog <= 6.9");

        assertFilterExpression(true, p5, "m.id = 2 | m.sog >= 6.1");
        assertFilterExpression(true, p5, "m.id = 1 | m.sog < 6.1");
        assertFilterExpression(false, p5, "m.id = 2 & m.sog >= 6.1");
        assertFilterExpression(false, p5, "m.id = 1 & m.sog < 6.1");

        assertFilterExpression(true, p5, "m.sog > 6.0 & m.hdg > 350 & m.id = 1");
        assertFilterExpression(false, p5, "m.lat in 55..56 & m.lon in 11..12 & m.sog > 6.0 & m.hdg > 350 & m.id = 2");
        assertFilterExpression(false, p5, "m.lat in 37..38 & m.lon in -123..-122 & m.sog > 7.0 & m.hdg > 350 & m.id = 1");
        assertFilterExpression(true, p5, "m.lat in 37..38 & m.lon in -123..-122 & m.sog > 6.0 & m.hdg > 350 & m.id = 1");

        assertFilterExpression(false, p5, "s.type=SAT|s.id=AISD|s.id=MSSIS");
        assertFilterExpression(true, p5, "s.type!=SAT|s.id!=AISD|s.id!=MSSIS");
    }

    @Test
    public void testParseExpressionFilterLabels /* enums? */() {
        assertFilterExpression(false, p1, "m.type = NONEXISTENTLABEL");
        assertFilterExpression(false, p1, "m.type = TANKER");
        assertFilterExpression(true, p1, "m.type = MILITARY");
        assertFilterExpression(true, p1, "m.type = military");
        assertFilterExpression(true, p1, "m.type = 'MILITARY'");
        assertFilterExpression(true, p1, "m.type in MILITARY, TANKER, HSC, FISHING");
        assertFilterExpression(true, p1, "m.type in TANKER, MILITARY, HSC, FISHING");
        assertFilterExpression(true, p1, "m.type in tanker, MILITARY, HSC, FISHING");
        assertFilterExpression(true, p1, "m.type in tanker, military, HSC, FISHING");
        assertFilterExpression(true, p1, "m.type in 'tanker', 'military', HSC");
        assertFilterExpression(false, p1, "m.type in 'tanker', 'militar', HSC");
        assertFilterExpression(false, p1, "m.type in PASSENGER, TANKER, HSC, FISHING");
    }

    @Test
    public void testExpressionFilterComparisons() {
        testComparison(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testComparison(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
        testComparison(p2, "m.imo", ((AisMessage5) p2.tryGetAisMessage()).getImo());
        testComparison(p5, "m.sog", ((IVesselPositionMessage) p5.tryGetAisMessage()).getSog() / 10.0f);
        testComparison(p5, "m.cog", ((IVesselPositionMessage) p5.tryGetAisMessage()).getCog() / 10.0f);
        testComparison(p5, "m.hdg", ((IVesselPositionMessage) p5.tryGetAisMessage()).getTrueHeading());
        testComparison(p5, "m.lon", ((IPositionMessage) p5.tryGetAisMessage()).getPos().getLongitudeDouble());
        testComparison(p5, "m.lat", ((IPositionMessage) p5.tryGetAisMessage()).getPos().getLatitudeDouble());
        testComparison(p1, "m.draught", ((AisMessage5) p1.tryGetAisMessage()).getDraught() / 10.0f);
        testComparison(p1, "m.name", ((AisMessage5) p1.tryGetAisMessage()).getName());
        testComparison(p1, "m.cs", ((AisMessage5) p1.tryGetAisMessage()).getCallsign());
        testComparison(p1, "m.type", ((AisMessage5) p1.tryGetAisMessage()).getShipType());
        testComparison(p5, "m.navstat", ((AisPositionMessage) p5.tryGetAisMessage()).getNavStatus());
    }

    @Test
    public void testExpressionFilter() {
        test(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        test(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
        test(p2, "m.imo", ((AisMessage5) p2.tryGetAisMessage()).getImo());
        test(p2, "m.name", ((AisMessage5) p2.tryGetAisMessage()).getName());
        test(p2, "m.cs", ((AisMessage5) p2.tryGetAisMessage()).getCallsign());
        test(p2, "m.type", ((AisMessage5) p2.tryGetAisMessage()).getShipType());
        test(p2, "m.type", new ShipTypeCargo(((AisMessage5) p2.tryGetAisMessage()).getShipType()).getShipType().toString());
        test(p5, "m.navstat", ((AisPositionMessage) p5.tryGetAisMessage()).getNavStatus());
        test(p5, "m.navstat", NavigationalStatus.get(((AisPositionMessage) p5.tryGetAisMessage()).getNavStatus()));
    }

    @Test
    public void testExpressionFilterInRange() {
        testInRange(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testInRange(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
        testInRange(p2, "m.imo", ((AisMessage5) p2.tryGetAisMessage()).getImo());
        testInRange(p5, "m.hdg", ((IVesselPositionMessage) p5.tryGetAisMessage()).getTrueHeading());
        testInRange(p5, "m.lat", ((IVesselPositionMessage) p5.tryGetAisMessage()).getPos().getLatitudeDouble());
        testInRange(p5, "m.lon", ((IVesselPositionMessage) p5.tryGetAisMessage()).getPos().getLongitudeDouble());
        testInRange(p2, "m.type", ((AisMessage5) p2.tryGetAisMessage()).getShipType());
        testInRange(p5, "m.navstat", ((AisPositionMessage) p5.tryGetAisMessage()).getNavStatus());
    }

    @Test
    public void testCastNumericFilterExpressionToStringForStringFields() {
        // Test casting from float,int to string
        assertFilterExpression(false, p2, "m.name = 0");
        assertFilterExpression(false, p2, "m.name = 0.0");
    }

    @Test
    public void testParseExpressionFilterNegative() {
        assertFilterExpression(true, p3, "s.id != SD");
        assertFilterExpression(false, p3, "s.id != AISD");
        assertFilterExpression(false, p3, "s.id != AISD, SD");
        assertFilterExpression(true, p3, "s.id != AFISD, SD");
        assertFilterExpression(true, p2, "(s.id != AISD, SD) | s.country != DNK");
        assertFilterExpression(true, p2, "(s.id != AISD, SD) | (s.country != DNK, NLD)");
        assertFilterExpression(false, p3, "s.country != NLD");
        p1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertFilterExpression(false, p1, "s.type != LIVE");
        assertFilterExpression(false, p2, "s.region != 0");
    }

    @Test
    public void testMessagePositionWithinCircle() {
        AisPacket packet = p5;
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

        // Message with no position information passes
        assertTrue(p1.tryGetAisMessage() instanceof AisMessage5);
        assertFilterExpression(true, p1, "m.pos within circle(" + centerLatitude + ", " + centerLongitude + ", " + "2000)");
    }

    @Test
    public void testMessagePositionWithinBbox() {
        AisPacket packet = p5;
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

        // Message with no position information passes
        assertTrue(p1.tryGetAisMessage() instanceof AisMessage5);
        assertFilterExpression(true, p1, "m.pos within bbox(" + bboxWithPositionInside.getMinLat() + ", " + bboxWithPositionInside.getMinLon() + ", " + +bboxWithPositionInside.getMaxLat() + ", " + bboxWithPositionInside.getMaxLon() + ")");
    }

    @Test
    public void testMessageTimeCompareTo() {
        // p1: 2013-03-13T12:41:00.000+0100
        testComparison(p1, "m.year", 2013);
        testComparison(p1, "m.month", 3);
        testComparison(p1, "m.dom", 13);
        testComparison(p1, "m.dow", 3);
        testComparison(p1, "m.hour", 12);
        testComparison(p1, "m.minute", 41);

        assertFilterExpression(true, p1, "m.month = 03");      assertFilterExpression(false, p1, "m.month != 03");
        assertFilterExpression(true, p1, "m.month = mar");     assertFilterExpression(false, p1, "m.month != mar");
        assertFilterExpression(true, p1, "m.month = MAR");     assertFilterExpression(false, p1, "m.month != MAR");
        assertFilterExpression(true, p1, "m.month = MARCH");   assertFilterExpression(false, p1, "m.month != MARCH");
        assertFilterExpression(true, p1, "m.dow = wed");       assertFilterExpression(false, p1, "m.dow != wed");
        assertFilterExpression(true, p1, "m.dow = WED");       assertFilterExpression(false, p1, "m.dow != WED");
        assertFilterExpression(true, p1, "m.dow = WEDNESDAY"); assertFilterExpression(false, p1, "m.dow != WEDNESDAY");
    }

    @Test
    public void testMessageTimeInRange() {
       // p1: 2013-03-13T12:41:00.000+0100
       assertFilterExpression(true, p1, "m.year in 2011..2014");
       assertFilterExpression(true, p1, "m.month in 2..4");       assertFilterExpression(false, p1, "m.month not in 2..4");
       // TODO ? assertFilterExpression(true, p1, "m.month in feb..may");   assertFilterExpression(false, p1, "m.month not in feb..may");
       assertFilterExpression(true, p1, "m.dom in 10..15");
       assertFilterExpression(true, p1, "m.dow in 2..4");         assertFilterExpression(false, p1, "m.dow not in 2..4");
       // TODO ? assertFilterExpression(true, p1, "m.dow in mon..fri");     assertFilterExpression(false, p1, "m.dow not in mon..fri");
       assertFilterExpression(true, p1, "m.hour = 12..13");
       assertFilterExpression(true, p1, "m.minute = 40..49");
    }

    @Test
    public void testMessageTimeInIntList() {
       // p1: 2013-03-13T12:41:00.000+0100
       assertFilterExpression(true, p1, "m.year in 2011..2014");     assertFilterExpression(false, p1, "m.year not in 2013, 2014");
       assertFilterExpression(true, p1, "m.month in 2,3,4");         assertFilterExpression(false, p1, "m.month not in 2,3,4");
       assertFilterExpression(true, p1, "m.dom in 12,13,14");
       assertFilterExpression(true, p1, "m.dow in 2, 3, 4");         assertFilterExpression(false, p1, "m.dow not in 2,3,4");
       assertFilterExpression(true, p1, "m.hour = 12, 13");
       assertFilterExpression(true, p1, "m.minute = 40,41,42,43,44,45,49");
    }

    @Test
    public void testMessageTimeInStringList() {
       // p1: 2013-03-13T12:41:00.000+0100
       assertFilterExpression(true, p1, "m.month in jan,feb,mar");            assertFilterExpression(false, p1, "m.month not in jan,feb,mar");
       assertFilterExpression(true, p1, "m.month in JAN,feb,MAR");            assertFilterExpression(false, p1, "m.month not in JAN,feb,MAR");
       assertFilterExpression(true, p1, "m.month in JAN,febRuary,MARCH");     assertFilterExpression(false, p1, "m.month not in JAN,febRuary,MARCH");
       assertFilterExpression(true, p1, "m.dow in mon,tue,wed");              assertFilterExpression(false, p1, "m.dow not in mon,tue,wed");
       assertFilterExpression(true, p1, "m.dow in monday,tuesday,Wednesday"); assertFilterExpression(false, p1, "m.dow not in monday,tuesday,Wednesday");
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
