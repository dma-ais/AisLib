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
        assertEquals(AisTargetType.A, new AisMessage27().getTargetType());

        AisMessage1 msg = new AisMessage1();
        msg.setUserId(970_015_654);
        assertEquals(AisTargetType.SART, msg.getTargetType());
    }
    
}
