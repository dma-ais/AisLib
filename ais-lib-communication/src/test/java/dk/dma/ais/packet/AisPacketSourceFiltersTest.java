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

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import org.junit.Before;
import org.junit.Test;

import static dk.dma.ais.packet.AisPacketSourceFiltersParser.parseSourceFilter;
import static org.junit.Assert.assertEquals;

public class AisPacketSourceFiltersTest {

    AisPacketSource src1;
    AisPacketSource src2_noBS;
    AisPacketSource src3_noCountry;

    @Before
    public void setup() {
        src1 = new AisPacketSource("AISD", 12345, Country.getByCode("DNK"), SourceType.TERRESTRIAL, "0");
        src2_noBS = new AisPacketSource("AISD", null, Country.getByCode("DNK"), SourceType.TERRESTRIAL, "0");
        src3_noCountry = new AisPacketSource("AISD", 12345, null, SourceType.TERRESTRIAL, "0");
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

        assertFilterExpression(false, src2_noBS, "s.bs = 12345");
    }

    @Test
    public void testfilterOnCountry() {
        assertFilterExpression(true, src1, "s.country = DNK");
        assertFilterExpression(true, src1, "s.country = DNK, NLD");
        assertFilterExpression(true, src1, "s.country = DNK, NLD, CZE");

        assertFilterExpression(true, src1, "s.country != CZE");
        assertFilterExpression(true, src1, "s.country != CZE, NLD");

        assertFilterExpression(false, src3_noCountry, "s.country = DNK");
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

    @Test(expected = NullPointerException.class)
    public void rejectsMessageRelatedExpressions() {
        assertFilterExpression(true, src1, "m.sog > 6.6");
    }

    private static void assertFilterExpression(boolean expectedResult, AisPacketSource aisPacketSource, String filterExpression) {
        System.out.println("Testing \"" + filterExpression + "\" to be " + expectedResult + " for " + aisPacketSource.toString());
        assertEquals(expectedResult, parseSourceFilter(filterExpression).test(aisPacketSource));
    }

}
