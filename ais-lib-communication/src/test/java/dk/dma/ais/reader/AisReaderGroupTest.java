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
        AisReaders.parseSource("sdsd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseFailNoHostNamePorts2() {
        AisReaders.parseSource("sdsd=");
    }

    @Test
    public void parseOneHost() {
        AisTcpReader r = AisReaders.parseSource("sdsd=ff:123");
        assertSame(AisTcpReader.class, r.getClass());
        assertEquals("ff", r.getHostname());
        assertEquals(123, r.getPort());
        assertEquals("sdsd", r.getSourceId());
    }

    @Test
    public void parseTwoHosts() {
        AisTcpReader tr = AisReaders.parseSource("sdsd=ff:123, dd:1235");
        assertEquals("ff", tr.hosts.get(0).getHost());
        assertEquals("dd", tr.hosts.get(1).getHost());
        assertEquals(123, tr.hosts.get(0).getPort());
        assertEquals(1235, tr.hosts.get(1).getPort());
        assertEquals(2, tr.getHostCount());
        assertEquals("sdsd", tr.getSourceId());
    }

}
