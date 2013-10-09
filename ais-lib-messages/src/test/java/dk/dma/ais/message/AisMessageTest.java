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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests {@link AisMessage}
 * 
 * @author Kasper Nielsen
 */
public class AisMessageTest {

    @Test
    public void getTargetTypeTest() {
        assertEquals(AisTargetType.A, new AisMessage1().getTargetType());
        assertEquals(AisTargetType.A, new AisMessage2().getTargetType());
        assertEquals(AisTargetType.A, new AisMessage3().getTargetType());
        assertEquals(AisTargetType.BS, new AisMessage4().getTargetType());
        assertEquals(AisTargetType.A, new AisMessage5().getTargetType());
        assertEquals(null, new AisMessage6().getTargetType());
        assertEquals(null, new AisMessage7().getTargetType());
        assertEquals(null, new AisMessage8().getTargetType());
        assertEquals(null, new AisMessage9().getTargetType());
        assertEquals(null, new AisMessage10().getTargetType());
        assertEquals(null, new AisMessage12().getTargetType());
        assertEquals(null, new AisMessage13().getTargetType());
        assertEquals(null, new AisMessage14().getTargetType());
        assertEquals(null, new AisMessage17().getTargetType());
        assertEquals(AisTargetType.B, new AisMessage18().getTargetType());
        assertEquals(AisTargetType.B, new AisMessage19().getTargetType());
        assertEquals(AisTargetType.ATON, new AisMessage21().getTargetType());
        assertEquals(AisTargetType.B, new AisMessage24().getTargetType());

        AisMessage1 msg = new AisMessage1();
        msg.setUserId(970_015_654);
        assertEquals(AisTargetType.SART, msg.getTargetType());
    }
}
