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

import org.junit.Assert;
import org.junit.Test;

import dk.dma.enav.model.geometry.Position;

public class PosTest {

    /**
     * Method to unit test the ais position class and geo location class
     */
    @Test
    public void aisPosTest() {
        // Make AIS pos from raw AIS pos (DK pos)
        AisPosition aisPosition = new AisPosition(33366469, 7501031);
        testPos(aisPosition);

        // Irish sea (32421521,265106614) = (54.03586833333333,-5.54807)
        aisPosition = new AisPosition(32421521, 265106614);
        testPos(aisPosition);

        // China (18898271,72823445) = (31.497118333333333,121.37240833333334)
        aisPosition = new AisPosition(18898271, 72823445);
        testPos(aisPosition);

        // US (24602666,225058274) = (41.004443333333334,-72.29530333333334)
        aisPosition = new AisPosition(24602666, 225058274);
        testPos(aisPosition);

        // Australia (114468918,91953240) = (-32.914683333333336,153.2554)
        aisPosition = new AisPosition(114468918, 91953240);
        testPos(aisPosition);
    }

    private void testPos(AisPosition aisPosition) {
        System.out.println(aisPosition);
        // Get signed converted values
        long lat = aisPosition.getLatitude();
        long lon = aisPosition.getLongitude();
        // System.out.println("converted: (" + lat + "," + lon + ")");

        // Make new position from converted values
        AisPosition pos2 = new AisPosition();
        pos2.setLatitude(lat);
        pos2.setLongitude(lon);
        System.out.println(pos2);
        // The two should be equal
        Assert.assertEquals(aisPosition, pos2);

        Position geoLocation = aisPosition.getGeoLocation();

        // Make new postion from geo location
        AisPosition pos3 = new AisPosition(geoLocation);
        System.out.println(pos3);
        // The two should be equal
        Assert.assertEquals(aisPosition, pos3);

    }

}
