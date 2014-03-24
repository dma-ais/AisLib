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

import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceBaseStation;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceCountry;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceId;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceRegion;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceType;
import static dk.dma.ais.packet.AisPacketFilters.parseSourceFilter;
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
    public void testFilterOnSourceId() {
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
        assertTrue(filterOnSourceBaseStation("2190047").test(p1));
        assertTrue(filterOnSourceBaseStation(2190047).test(p1));
        assertFalse(filterOnSourceBaseStation("2190046").test(p1));
        assertFalse(filterOnSourceBaseStation(2190046).test(p1));
        assertTrue(filterOnSourceBaseStation("123322", "2190047", "1233223").test(p1));
        assertTrue(filterOnSourceBaseStation(12323, 2190047, 1230393).test(p1));

        assertFalse(filterOnSourceBaseStation(2190046).test(p2));
        assertFalse(filterOnSourceBaseStation(2190046).test(p2));
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
    public void testMessagePassesSourceFilterIfNotCarryingTheFilteredTypeOfInformation() {
        assertTrue(p1.tryGetAisMessage() instanceof AisMessage5);
        assertFilterExpression(true, p1, "m.sog > 20.0");
        assertFilterExpression(true, p1, "m.cog > 90");
        assertFilterExpression(true, p1, "m.hdg > 90");
        assertFilterExpression(true, p1, "m.lat > 55.5");
        assertFilterExpression(true, p1, "m.lon > 55.5");
        assertTrue(p5.tryGetAisMessage() instanceof AisMessage1);
        assertFilterExpression(true, p5, "m.draught > 0.1");
    }

    @Test
    public void testParseSourceFilterStrings() {
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
    public void testParseSourceFilterIntRanges() {
        assertFilterExpression(true, p5, "m.id in 0 .. 4");
        assertFilterExpression(true, p5, "m.id in (0 .. 4)");
        assertFilterExpression(true, p5, "m.id in 0..4");
        assertFilterExpression(true, p5, "m.id in (0..4)");
        assertFilterExpression(false, p5, "m.id in -5..-2");
    }

    @Test
    @Ignore
    public void testParseSourceFilterNumberRanges() {
        // TODO
        assertTrue(p5.tryGetAisMessage() instanceof AisMessage1);
        assertFilterExpression(true, p5, "m.id in 0.0 .. 4.1");
        assertFilterExpression(true, p5, "m.id in (0.0 .. 4.1)");
        assertFilterExpression(true, p5, "m.id in 0.0..4.1");
        assertFilterExpression(true, p5, "m.id in (0.0..4.1)");
        assertFilterExpression(false, p5, "m.id in -5.0..-2.1");
        assertFilterExpression(false, p5, "m.id in -5..-2.1");
        assertFilterExpression(false, p5, "m.id in -5.0..-2");
    }

    @Test
    public void testParseSourceFilterLists() {
        assertFilterExpression(true, p5, "m.id in 0,1,4");
        assertFilterExpression(true, p5, "m.id in (0,1,4)");
        assertFilterExpression(true, p5, "m.name in ('A','BCD','Z')");
        assertFilterExpression(true, p5, "m.name in 'A','BCD','Z'");
        assertFilterExpression(true, p5, "m.name in (A,BCD,Z)");
    }

    @Test
    public void testCompositeSourceFilterExpressions() {
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
    }

    @Test
    public void testParseSourceFilterLabels /* enums? */ () {
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
    public void testParseSourceFilter() {
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
    public void testSourceFilterComparisons() {
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
       /*
        'navstat'
        'time'
        '*/
    }

    @Test
    public void testSourceFilterInList() {
        testInList(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testInList(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
        testInList(p2, "m.imo", ((AisMessage5) p2.tryGetAisMessage()).getImo());
        testInList(p2, "m.name", ((AisMessage5) p2.tryGetAisMessage()).getName());
        testInList(p2, "m.cs", ((AisMessage5) p2.tryGetAisMessage()).getCallsign());
        testInList(p2, "m.type", ((AisMessage5) p2.tryGetAisMessage()).getShipType());
    }

    @Test
    public void testSourceFilterInRange() {
        testInRange(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testInRange(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
        testInRange(p2, "m.imo", ((AisMessage5) p2.tryGetAisMessage()).getImo());
        testInRange(p5, "m.hdg", ((IVesselPositionMessage) p5.tryGetAisMessage()).getTrueHeading());
        testInRange(p5, "m.lat", ((IVesselPositionMessage) p5.tryGetAisMessage()).getPos().getLatitudeDouble());
        testInRange(p5, "m.lon", ((IVesselPositionMessage) p5.tryGetAisMessage()).getPos().getLongitudeDouble());
        testInRange(p2, "m.type", ((AisMessage5) p2.tryGetAisMessage()).getShipType());
    }

    private static void assertFilterExpression(boolean expectedResult, AisPacket aisPacket, String filterExpression) {
        System.out.println("Testing \"" + filterExpression + "\" to be " + expectedResult + " for " + aisPacket.tryGetAisMessage().toString());
        assertEquals(expectedResult, parseSourceFilter(filterExpression).test(aisPacket));
    }

    private static void testInList(AisPacket testPacket, String field, Object fieldValue) {
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
            fieldValue = ((String) fieldValue).replace('@',' ').trim();
            if (((String) fieldValue).contains(" ")) {
                fieldValue  = "'" + fieldValue + "'";
            }
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

        assertFilterExpression(false, testPacket, field + " in " + (below.longValue()-10) + ".." + below);
        assertFilterExpression(false, testPacket, field + " in " + above + ".." + (above.longValue()+10));

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
            fieldValue = ((String) fieldValue).replace('@',' ').trim();
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

    @Test
    public void testCastNumericFilterExpressionToStringForStringFields() {
        // Test casting from float,int to string
        assertFilterExpression(false, p2, "m.name = 0");
        assertFilterExpression(false, p2, "m.name = 0.0");
    }

    @Test
    public void testParseSourceFilterNegative() {
        assertFilterExpression(false, p3, "s.id != AISD, SD");
        assertFilterExpression(true, p3, "s.id != AFISD, SD");
        assertFilterExpression(true, p2, "(s.id != AISD, SD) | s.country != DNK");
        assertFilterExpression(true, p2, "(s.id != AISD, SD) | (s.country != DNK, NLD)");
        assertFilterExpression(false, p2, "s.country != NLD");
    }
}
