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

package dk.dma.ais.tracker.scenarioTracker;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by tbsalling on 07/04/14.
 */
public class ScenarioTrackerTest {

    AisPacketReader packetReader;
    ScenarioTracker scenarioTracker;

    @Before
    public void setUp() throws Exception {
        Path testdata = Paths.get("src/test/resources/replay_dump.txt");
        packetReader = AisPacketReader.createFromFile(testdata, true);
        scenarioTracker = new ScenarioTracker();
    }

    @Test
    public void testScenarioBegin() throws IOException {
        scenarioTracker.subscribeToPacketReader(packetReader);
        Date scenarioBegin = scenarioTracker.scenarioBegin();
        System.out.println("Scenario begin: " + scenarioBegin.toString());
        assertEquals(1363598349499L, scenarioBegin.getTime());
    }

    @Test
    public void testScenarioEnd() throws IOException {
        scenarioTracker.subscribeToPacketReader(packetReader);
        Date scenarioEnd = scenarioTracker.scenarioEnd();
        System.out.println("Scenario end: " + scenarioEnd.toString());
        assertEquals(1363598370922L, scenarioEnd.getTime());
    }

    @Test
    public void testGetTargets() throws IOException {
        scenarioTracker.subscribeToPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        assertEquals(34, scenario.size());
    }

    @Test
    public void testDimensions() throws IOException {
        scenarioTracker.subscribeToPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        ScenarioTracker.Target[] targets = scenario.toArray(new ScenarioTracker.Target[0]);
        assertEquals(72, targets[30].getToBow());
        assertEquals(10, targets[30].getToStern());
        assertEquals(8, targets[30].getToPort());
        assertEquals(4, targets[30].getToStarboard());
    }

    @Test
    public void testGetName() throws IOException {
        scenarioTracker.subscribeToPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        ScenarioTracker.Target[] targets = scenario.toArray(new ScenarioTracker.Target[0]);
        assertEquals("2190048", targets[0].getName());
        assertEquals("2655135", targets[1].getName());
        assertEquals("212204000", targets[2].getName());
        assertEquals("219000119", targets[3].getName());
        assertEquals("MOSVIK", targets[30].getName());
    }

    @Test
    public void testGetPositionReportAt() {
        final String[] NMEA_TEST_STRINGS = {
            // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:38 CEST 2014]
            // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33024530,6010902) = (33024530,6010902), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=2, subMessage=1427]
            "$PGHP,1,2014,4,10,13,30,38,88,219,,2190067,1,26*1E\r\n" +
            "!BSVDM,1,1,,B,13@ng7P01dPeo0dOOb4WnVAp0`FC,0*26",

            // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:31:18 CEST 2014]
            // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33023417,6010117) = (33023417,6010117), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=199, utcSec=60, slotTimeout=0, subMessage=2263]
            "$PGHP,1,2014,4,10,13,31,18,678,219,,2190067,1,03*23\r\n" +
            "!BSVDM,1,1,,B,13@ng7P01dPen`:OOUfGnV?p0PSG,0*03"
        };

        assertEquals(0, scenarioTracker.getTargets().size());

        scenarioTracker.update(AisPacket.from(NMEA_TEST_STRINGS[0]));
        scenarioTracker.update(AisPacket.from(NMEA_TEST_STRINGS[1]));

        assertEquals(1, scenarioTracker.getTargets().size());
        ScenarioTracker.Target target = scenarioTracker.getTargets().iterator().next();

        Iterator<ScenarioTracker.Target.PositionReport> positionReportIterator = target.getPositionReports().iterator();
        ScenarioTracker.Target.PositionReport pr1 = positionReportIterator.next();
        ScenarioTracker.Target.PositionReport pr2 = positionReportIterator.next();
        final long t1 = pr1.getTimestamp();
        final long t2 = pr2.getTimestamp();

        assertNotEquals(pr1.getLatitude(), pr2.getLatitude(), 0.001);
        assertNotEquals(pr1.getLongitude(), pr2.getLongitude(), 0.001);
        assertEquals(1397136638088L, t1);
        assertEquals(1397136678678L, t2);
        assertEquals(false, pr1.isEstimated());
        assertEquals(false, pr2.isEstimated());

        final long t = (t2-t1)/2 + t1;  // Half-way between t1 and t2
        assertEquals(1397136658383L, t);

        ScenarioTracker.Target.PositionReport interpolatedPosition = target.getPositionReportAt(new Date(t), 5);
        assertEquals((pr1.getLatitude() + pr2.getLatitude())/2, interpolatedPosition.getLatitude(), 1e-16);
        assertEquals((pr1.getLongitude() + pr2.getLongitude())/2, interpolatedPosition.getLongitude(), 1e-16);
        assertEquals(200, interpolatedPosition.getHeading());
        assertEquals(t, interpolatedPosition.getTimestamp());
        assertEquals(true, interpolatedPosition.isEstimated());

        ScenarioTracker.Target.PositionReport estimatedPosition = target.getPositionReportAt(new Date(t), 20);
        assertEquals(true, estimatedPosition.isEstimated());

        ScenarioTracker.Target.PositionReport reportedPosition = target.getPositionReportAt(new Date(t), 21);
        assertEquals(false, reportedPosition.isEstimated());
        assertEquals(pr1, reportedPosition);
    }

}
