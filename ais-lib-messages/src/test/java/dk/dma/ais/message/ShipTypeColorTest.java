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

package dk.dma.ais.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTypeColorTest {

    @Test
    public void testGetColor() throws Exception {
        assertEquals(ShipTypeColor.BLUE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.PASSENGER));
        assertEquals(ShipTypeColor.ORANGE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.FISHING));
        assertEquals(ShipTypeColor.GREY, ShipTypeColor.getColor(ShipTypeCargo.ShipType.UNDEFINED));
        assertEquals(ShipTypeColor.GREY, ShipTypeColor.getColor(ShipTypeCargo.ShipType.UNKNOWN));
        assertEquals(ShipTypeColor.GREEN, ShipTypeColor.getColor(ShipTypeCargo.ShipType.CARGO));
        assertEquals(ShipTypeColor.ORANGE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.FISHING));
        assertEquals(ShipTypeColor.PURPLE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.SAILING));
        assertEquals(ShipTypeColor.PURPLE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.PLEASURE));
        assertEquals(ShipTypeColor.RED, ShipTypeColor.getColor(ShipTypeCargo.ShipType.TANKER));
        assertEquals(ShipTypeColor.YELLOW, ShipTypeColor.getColor(ShipTypeCargo.ShipType.HSC));
        assertEquals(ShipTypeColor.YELLOW, ShipTypeColor.getColor(ShipTypeCargo.ShipType.WIG));
        assertEquals(ShipTypeColor.TURQUOISE, ShipTypeColor.getColor(ShipTypeCargo.ShipType.PILOT));
    }
}
