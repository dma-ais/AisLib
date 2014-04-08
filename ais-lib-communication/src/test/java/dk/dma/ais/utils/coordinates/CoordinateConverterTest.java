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
package dk.dma.ais.utils.coordinates;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinateConverterTest {

    CoordinateConverter transformer;

    @Before
    public void setUp() {
        transformer = new CoordinateConverter(12, 56);
    }

    @Test
    public void testLon2x() throws Exception {
        assertEquals(0.0, transformer.lon2x(12, 56), 1e-10);
        assertEquals(6204.034306340757, transformer.lon2x(12.1, 56), 1e-10);
        assertEquals(0.0, transformer.lon2x(12, 56.1), 1e-10);
        assertEquals(-6204.034306340658, transformer.lon2x(11.9, 56), 1e-10);
        assertEquals(0.0, transformer.lon2x(12, 55.9), 1e-10);
    }

    @Test
    public void testLat2y() throws Exception {
        assertEquals(0.0, transformer.lat2y(12, 56), 1e-10);
        assertEquals(4.4884447780805115, transformer.lat2y(12.1, 56), 1e-10);
        assertEquals(11094.62855321888, transformer.lat2y(12, 56.1), 1e-10);
        assertEquals(4.4884447780805115, transformer.lat2y(11.9, 56), 1e-10);
        assertEquals(-11094.628553220642, transformer.lat2y(12, 55.9), 1e-10);
    }

    @Test
    public void testX2Lon() throws Exception {
        assertEquals(12.0, transformer.x2Lon(0.0, 0.0), 1e-10);
        assertEquals(12.1, transformer.x2Lon(6204.034306340757, 4.4884447780805115), 1e-10);
        assertEquals(12.0, transformer.x2Lon(0.0, 11094.62855321888), 1e-10);
        assertEquals(11.9, transformer.x2Lon(-6204.034306340658, 4.4884447780805115), 1e-10);
        assertEquals(12.0, transformer.x2Lon(0.0, -11094.628553220642), 1e-10);
    }

    @Test
    public void testY2Lat() throws Exception {
        assertEquals(56.0, transformer.y2Lat(0.0, 0.0), 1e-10);
        assertEquals(56.0, transformer.y2Lat(6204.034306340757, 4.4884447780805115), 1e-10);
        assertEquals(56.1, transformer.y2Lat(0.0, 11094.62855321888), 1e-10);
        assertEquals(56.0, transformer.y2Lat(-6204.034306340658, 4.4884447780805115), 1e-10);
        assertEquals(55.9, transformer.y2Lat(0.0, -11094.628553220642), 1e-10);
    }

    @Test
    public void testCompass() {
        assertEquals(90.0, CoordinateConverter.compass2cartesian(  0.0), 1e-10);
        assertEquals(0.0, CoordinateConverter.compass2cartesian( 90.0), 1e-10);
        assertEquals(270.0, CoordinateConverter.compass2cartesian(180.0), 1e-10);
        assertEquals(180.0, CoordinateConverter.compass2cartesian(270.0), 1e-10);

        assertEquals(90.0, CoordinateConverter.cartesian2compass(  0.0), 1e-10);
        assertEquals(0.0, CoordinateConverter.cartesian2compass( 90.0), 1e-10);
        assertEquals(270.0, CoordinateConverter.cartesian2compass(180.0), 1e-10);
        assertEquals(180.0, CoordinateConverter.cartesian2compass(270.0), 1e-10);

        assertEquals(37.0, CoordinateConverter.compass2cartesian(CoordinateConverter.cartesian2compass(37.0)), 1e-10);
        assertEquals(117.0, CoordinateConverter.compass2cartesian(CoordinateConverter.cartesian2compass(117.0)), 1e-10);
        assertEquals(273.0, CoordinateConverter.compass2cartesian(CoordinateConverter.cartesian2compass(273.0)), 1e-10);
        assertEquals(92.0, CoordinateConverter.compass2cartesian(CoordinateConverter.cartesian2compass(92.0)), 1e-10);
        assertEquals(359.0, CoordinateConverter.compass2cartesian(CoordinateConverter.cartesian2compass(359.0)), 1e-10);
    }
}
