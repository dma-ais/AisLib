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

import static dk.dma.ais.packet.AisPacketSourceFiltersParser.parseSourceFilter;
import static org.junit.Assert.assertEquals;

public class AisPacketSourceFiltersTest {

    AisPacketSource src1;

    @Before
    public void setup() {
        src1 = new AisPacketSource("AISD", 12345, Country.getByCode("DNK"), SourceType.TERRESTRIAL, "0");
    }

    @Test
    public void testfilterOnSourceId() {
        assertFilterExpression(true, src1, "s.id = AISD");
        assertFilterExpression(true, src1, "s.id = AISD, SD");
        assertFilterExpression(false, src1, "s.id = HELCOM, SD");

        assertFilterExpression(true, src1, "s.id != HELCOM, SD");
        assertFilterExpression(false, src1, "s.id != HELCOM, AISD, SD");
    }

    @Test
    public void testfilterOnBasestation() {
        assertFilterExpression(true, src1, "s.bs = 12345");
        assertFilterExpression(true, src1, "s.bs >= 12345");
        assertFilterExpression(true, src1, "s.bs <= 12345");
        assertFilterExpression(false, src1, "s.bs > 12345");
        assertFilterExpression(false, src1, "s.bs < 12345");

        assertFilterExpression(true, src1, "s.bs = 12345, 67890");
        assertFilterExpression(true, src1, "s.bs = 12345, 67890, 56437");
        assertFilterExpression(true, src1, "s.bs = 67890, 12345, 56437");
        assertFilterExpression(false, src1, "s.bs = 67890, 56437");
    }

    @Test
    public void testfilterOnCountry() {
        assertFilterExpression(true, src1, "s.country = DNK");
        assertFilterExpression(true, src1, "s.country = DNK, NLD");
        assertFilterExpression(true, src1, "s.country = DNK, NLD, CZE");

        assertFilterExpression(true, src1, "s.country != CZE");
        assertFilterExpression(true, src1, "s.country != CZE, NLD");
    }

    @Test
    public void testfilterOnType() {
        assertFilterExpression(true, src1, "s.type = LIVE");
        assertFilterExpression(true, src1, "s.type = live");
        assertFilterExpression(true, src1, "s.type = LIVE, SAT");

        assertFilterExpression(true, src1, "s.type != SAT");
    }

    @Test
    public void testfilterOnRegion() {
        assertFilterExpression(true, src1, "s.region = 0");
        assertFilterExpression(true, src1, "s.region = '0'");
        assertFilterExpression(true, src1, "s.region = 0, 1");

        assertFilterExpression(true, src1, "s.region != 1");
    }

    @Test
    public void testfilterAnd() {
        assertFilterExpression(true, src1, "s.region = 0 & s.country = DNK & s.type = LIVE");
        assertFilterExpression(false, src1, "s.region = 0 & s.country = CZE & s.type = LIVE");
    }

    private static void assertFilterExpression(boolean expectedResult, AisPacketSource aisPacketSource, String filterExpression) {
        System.out.println("Testing \"" + filterExpression + "\" to be " + expectedResult + " for " + aisPacketSource.toString());
        assertEquals(expectedResult, parseSourceFilter(filterExpression).test(aisPacketSource));
    }

}
