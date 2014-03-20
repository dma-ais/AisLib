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

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import org.junit.Before;
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

    @Before
    public void setup() {
        p1 = AisTestPackets.p1();
        p2 = AisTestPackets.p2();
        p3 = AisTestPackets.p3();
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
    public void testParseSourceFilter() {
        //
//        assertTrue(parseSourceFilter("s.id = AISD, SD").test(p3));

        p1.getTags().setSourceType(SourceType.TERRESTRIAL);
        assertTrue(parseSourceFilter("s.type = LIVE").test(p1));
        assertTrue(parseSourceFilter("s.type = SAT, LIVE").test(p1));
        assertFalse(parseSourceFilter("s.type = SAT").test(p1));

        //
        assertTrue(parseSourceFilter("s.id = AISD, SD").test(p3));
        assertFalse(parseSourceFilter("s.id = AFISD, SD").test(p3));
        assertTrue(parseSourceFilter("(s.id = AISD, SD) | s.country = DNK").test(p3));
        assertTrue(parseSourceFilter("(s.id = AISD, SD) | s.country = DNK").test(p1));
        assertFalse(parseSourceFilter("(s.id = AISD, SD) | s.country = DNK").test(p2));
        assertTrue(parseSourceFilter("(s.id = AISD, SD) | (s.country = DNK, NLD)").test(p2));
        assertTrue(parseSourceFilter("s.country = NLD & s.region = 0").test(p2));
        assertTrue(parseSourceFilter("s.country = DNK | s.region = 3,4,ERER,0").test(p2));

        assertFalse(parseSourceFilter("s.country = DNK & s.bs =2190047 & s.type = SAT").test(p1));
        p1.getTags().setSourceType(SourceType.SATELLITE);
        assertTrue(parseSourceFilter("s.country = DNK & s.bs =2190047 & s.type = SAT").test(p1));
        assertTrue(parseSourceFilter("s.country = DNK & s.bs =3,4,4,5,5,2190047 & s.type = SAT").test(p1));

        testComparison(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testComparison(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());

        // Test in-list operator
        testInList(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testInList(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());

        // Test in-range operator
        testInRange(p1, "m.id", p1.tryGetAisMessage().getMsgId());
        testInRange(p1, "m.mmsi", p1.tryGetAisMessage().getUserId());
    }

    private static void assertFilterExpression(boolean expectedResult, AisPacket aisPacket, String filterExpression) {
        System.out.println("Testing \"" + filterExpression + "\" to be " + expectedResult + " for " + aisPacket.tryGetAisMessage().toString());
        assertEquals(expectedResult, parseSourceFilter(filterExpression).test(aisPacket));
    }

    private static void testInList(AisPacket testPacket, String field, int fieldValue) {
        int otherValue1 = fieldValue + 4325;
        int otherValue2 = fieldValue - 1;

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + "," + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(true, testPacket, field + " IN (" + otherValue1 + "," + fieldValue + "," + otherValue2 + ")");
        assertFilterExpression(true, testPacket, field + " @ " + otherValue1 + "," + otherValue2 + ", " + fieldValue + "");
        assertFilterExpression(true, testPacket, field + "@" + fieldValue + ", " + otherValue1 + ", " + otherValue2 + "");
        assertFilterExpression(true, testPacket, field + " @ (" + fieldValue + ", " + otherValue1 + ", " + otherValue2 + ")");
        assertFilterExpression(true, testPacket, field + "@(" + fieldValue + "," + otherValue1 + "," + otherValue2 + ")");

        assertFilterExpression(false, testPacket, field + " in " + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(false, testPacket, field + " IN (" + otherValue1 + "," + otherValue2 + ")");
        assertFilterExpression(false, testPacket, field + " @ " + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(false, testPacket, field + "@" + otherValue1 + "," + otherValue2 + "");
        assertFilterExpression(false, testPacket, field + " @ (" + otherValue1 + "," + otherValue2 + ")");
        assertFilterExpression(false, testPacket, field + "@(" + otherValue1 + "," + otherValue2 + ")");
    }

    private static void testInRange(AisPacket testPacket, String field, int fieldValue) {
        int below = fieldValue - 10;
        int above = fieldValue + 10;

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + above);
        assertFilterExpression(true, testPacket, field + " in " + below + ".." + fieldValue);
        assertFilterExpression(true, testPacket, field + " in " + below + ".." + above);
        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + fieldValue);

        assertFilterExpression(false, testPacket, field + " in " + (below-10) + ".." + below);
        assertFilterExpression(false, testPacket, field + " in " + above + ".." + (above+10));

        assertFilterExpression(true, testPacket, field + " in (" + fieldValue + ".." + above + ")");
        assertFilterExpression(true, testPacket, field + " IN (" + fieldValue + ".." + above + ")");
        assertFilterExpression(true, testPacket, field + " @ (" + fieldValue + ".." + above + ")");

        assertFilterExpression(true, testPacket, field + " in " + fieldValue + ".." + above + "");
        assertFilterExpression(true, testPacket, field + " IN " + fieldValue + ".." + above + "");
        assertFilterExpression(true, testPacket, field + " @ " + fieldValue + ".." + above + "");

        assertFilterExpression(true, testPacket, field + "@" + fieldValue + ".." + above + "");
    }

    private static void testComparison(AisPacket testPacket, String field, int fieldValue) {
        // Test = operator
        assertFilterExpression(true, testPacket, field + " = " + fieldValue);
        assertFilterExpression(false, testPacket, field + " = " + (fieldValue + 1));
        assertFilterExpression(false, testPacket, field + " = " + (fieldValue - 1));

        // Test != operator
        assertFilterExpression(false, testPacket, field + " != " + fieldValue);
        assertFilterExpression(true, testPacket, field + " != " + (fieldValue + 1));

        // Test > operator
        assertFilterExpression(true, testPacket, field + " > " + (fieldValue - 1));
        assertFilterExpression(false, testPacket, field + "> " + fieldValue);
        assertFilterExpression(false, testPacket, field + "  > " + (fieldValue + 1));

        // Test >= operator
        assertFilterExpression(true, testPacket, field + " >= " + (fieldValue - 1));
        assertFilterExpression(true, testPacket, field + " >= " + fieldValue);
        assertFilterExpression(false, testPacket, field + " >= " + (fieldValue + 1));

        // Test < operator
        assertFilterExpression(false, testPacket, field + " < " + (fieldValue - 1));
        assertFilterExpression(false, testPacket, field + " < " + fieldValue);
        assertFilterExpression(true, testPacket, field + " < " + (fieldValue + 1));

        // Test <= operator
        assertFilterExpression(false, testPacket, field + " <= " + (fieldValue - 1));
        assertFilterExpression(true, testPacket, field + " <= " + fieldValue);
        assertFilterExpression(true, testPacket, field + " <= " + (fieldValue + 1));
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
