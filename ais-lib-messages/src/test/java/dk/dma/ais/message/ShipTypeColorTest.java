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
