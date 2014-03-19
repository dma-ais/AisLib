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
        assertTrue(parseSourceFilter("id = AISD, SD").test(p3));
        assertFalse(parseSourceFilter("id = AFISD, SD").test(p3));
        assertTrue(parseSourceFilter("(id = AISD, SD) | country = DNK").test(p3));
        assertTrue(parseSourceFilter("(id = AISD, SD) | country = DNK").test(p1));
        assertFalse(parseSourceFilter("(id = AISD, SD) | country = DNK").test(p2));
        assertTrue(parseSourceFilter("(id = AISD, SD) | (country = DNK, NLD)").test(p2));
        assertTrue(parseSourceFilter("country = NLD & region = 0").test(p2));
        assertTrue(parseSourceFilter("country = DNK | region = 3,4,ERER,0").test(p2));

        assertFalse(parseSourceFilter("country = DNK & bs =2190047 & type = SAT").test(p1));
        p1.getTags().setSourceType(SourceType.SATELLITE);
        assertTrue(parseSourceFilter("country = DNK & bs =2190047 & type = SAT").test(p1));
        assertTrue(parseSourceFilter("country = DNK & bs =3,4,4,5,5,2190047 & type = SAT").test(p1));

        // Test = operator
        assertTrue(parseSourceFilter("m.mmsi = 220431000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi = 220431001").test(p1));
        assertTrue(parseSourceFilter("m.mmsi = 211235170").test(p2));
        assertFalse(parseSourceFilter("m.mmsi = 220431000").test(p2));

        // Test != operator
        assertFalse(parseSourceFilter("m.mmsi != 220431000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi != 220431001").test(p1));

        // Test > operator
        assertTrue(parseSourceFilter("m.mmsi > 220430999").test(p1));
        assertFalse(parseSourceFilter("m.mmsi > 220431000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi > 220431001").test(p1));

        // Test >= operator
        assertTrue(parseSourceFilter("m.mmsi >= 220430999").test(p1));
        assertTrue(parseSourceFilter("m.mmsi >= 220431000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi >= 220431001").test(p1));

        // Test < operator
        assertFalse(parseSourceFilter("m.mmsi < 220430999").test(p1));
        assertFalse(parseSourceFilter("m.mmsi < 220431000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi < 220431001").test(p1));

        // Test <= operator
        assertFalse(parseSourceFilter("m.mmsi <= 220430999").test(p1));
        assertTrue(parseSourceFilter("m.mmsi <= 220431000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi <= 220431001").test(p1));

        // Test in-list operator
        assertTrue(parseSourceFilter("m.mmsi in 220431000,211235170,002191000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi IN (211235170,220431000,002191000)").test(p1));
        assertTrue(parseSourceFilter("m.mmsi @ 211235170,002191000, 220431000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi@220431000, 211235170, 002191000").test(p1));
        assertTrue(parseSourceFilter("m.mmsi @ (220431000, 211235170, 002191000)").test(p1));
        assertTrue(parseSourceFilter("m.mmsi@(220431000,211235170,002191000)").test(p1));
        assertFalse(parseSourceFilter("m.mmsi in 211235170,002191000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi IN (211235170,002191000)").test(p1));
        assertFalse(parseSourceFilter("m.mmsi @ 211235170,002191000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi@211235170,002191000").test(p1));
        assertFalse(parseSourceFilter("m.mmsi @ (211235170,002191000)").test(p1));
        assertFalse(parseSourceFilter("m.mmsi@(211235170,002191000)").test(p1));
    }

    @Test
    public void testParseSourceFilterNegative() {
        assertFalse(parseSourceFilter("id != AISD, SD").test(p3));
        assertTrue(parseSourceFilter("id != AFISD, SD").test(p3));
        assertTrue(parseSourceFilter("(id != AISD, SD) | country != DNK").test(p2));
        assertTrue(parseSourceFilter("(id != AISD, SD) | (country != DNK, NLD)").test(p2));
        assertFalse(parseSourceFilter("country != NLD").test(p2));
    }
}
