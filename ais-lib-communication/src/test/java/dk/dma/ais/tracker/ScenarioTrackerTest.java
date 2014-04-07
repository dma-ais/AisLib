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

package dk.dma.ais.tracker;

import dk.dma.ais.packet.AisPacketReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

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
        scenarioTracker.readFromPacketReader(packetReader);
        Date scenarioBegin = scenarioTracker.scenarioBegin();
        System.out.println("Scenario begin: " + scenarioBegin.toString());
        assertEquals(1363598349499L, scenarioBegin.getTime());
    }

    @Test
    public void testScenarioEnd() throws IOException {
        scenarioTracker.readFromPacketReader(packetReader);
        Date scenarioEnd = scenarioTracker.scenarioEnd();
        System.out.println("Scenario end: " + scenarioEnd.toString());
        assertEquals(1363598370922L, scenarioEnd.getTime());
    }

    @Test
    public void testGetTargets() throws IOException {
        scenarioTracker.readFromPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        assertEquals(34, scenario.size());
    }

    @Test
    public void testDimensions() throws IOException {
        scenarioTracker.readFromPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        ScenarioTracker.Target[] targets = scenario.toArray(new ScenarioTracker.Target[0]);
        assertEquals(72, targets[30].getToBow());
        assertEquals(10, targets[30].getToStern());
        assertEquals(8, targets[30].getToPort());
        assertEquals(4, targets[30].getToStarboard());
    }

    @Test
    public void testGetName() throws IOException {
        scenarioTracker.readFromPacketReader(packetReader);
        Set<ScenarioTracker.Target> scenario = scenarioTracker.getTargets();
        ScenarioTracker.Target[] targets = scenario.toArray(new ScenarioTracker.Target[0]);
        assertEquals("2190048", targets[0].getName());
        assertEquals("2655135", targets[1].getName());
        assertEquals("212204000", targets[2].getName());
        assertEquals("219000119", targets[3].getName());
        assertEquals("MOSVIK", targets[30].getName());
    }

}
