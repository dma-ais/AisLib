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
package dk.dma.ais.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisReaderGroupTest {

    @Test(expected = IllegalArgumentException.class)
    public void parseFailNoHostNamePorts1() {
        AisReaderGroup.parseSource("sdsd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseFailNoHostNamePorts2() {
        AisReaderGroup.parseSource("sdsd=");
    }

    @Test
    public void parseOneHost() {
        AisTcpReader r = AisReaderGroup.parseSource("sdsd=ff:123");
        assertSame(AisTcpReader.class, r.getClass());
        assertEquals("ff", r.getHostname());
        assertEquals(123, r.getPort());
        assertEquals("sdsd", r.getSourceId());
    }

    @Test
    public void parseTwoHosts() {
        AisTcpReader r = AisReaderGroup.parseSource("sdsd=ff:123, dd:1235");
        assertSame(RoundRobinAisTcpReader.class, r.getClass());
        RoundRobinAisTcpReader tr = (RoundRobinAisTcpReader) r;
        assertEquals("ff", tr.hostnames.get(0));
        assertEquals("dd", tr.hostnames.get(1));
        assertEquals(123, tr.ports.get(0).intValue());
        assertEquals(1235, tr.ports.get(1).intValue());
        assertEquals(2, tr.getHostCount());
        assertEquals("sdsd", r.getSourceId());
    }

}
