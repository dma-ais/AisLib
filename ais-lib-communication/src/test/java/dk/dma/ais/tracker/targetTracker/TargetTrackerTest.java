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

package dk.dma.ais.tracker.targetTracker;

import dk.dma.ais.data.AisTarget;
import dk.dma.ais.data.AisVesselTarget;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.model.Country;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class TargetTrackerTest {

    AisReader aisReader;

    TargetTracker targetTracker;

    Map<Integer, AisTarget> aisVesselTargets;

    @Before
    public void setUp() throws Exception {
        aisVesselTargets = new HashMap<>();
        Path testdata = Paths.get("src/test/resources/");
        aisReader = AisReaders.createDirectoryReader(testdata.toString(), "*.txt", true);

        targetTracker = new TargetTracker();
        targetTracker.subscribeToPacketStream(aisReader.stream());

        aisReader.registerHandler(t -> {
            if (!aisVesselTargets.containsKey(t.getUserId())) {
                AisTarget k = AisTarget.createTarget(t);
                if (k instanceof AisVesselTarget) {
                    aisVesselTargets.put(t.getUserId(), k);
                }
            }
        });

        aisReader.start();
        // read all first
        aisReader.join();


    }

    @Test
    public void testExists()  {
        for (AisTarget t : aisVesselTargets.values()) {
            Objects.requireNonNull(targetTracker.get(Objects.requireNonNull(t.getMmsi())));
        }
    }

    /**
     * Test that all AisVesselTargets with names have names in TargetTracker
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testNames() throws IOException, InterruptedException {
        List<String> names = new ArrayList<String>();
        for (AisTarget t : aisVesselTargets.values()) {
            if (t instanceof AisVesselTarget) {
                if (((AisVesselTarget) t).getVesselStatic() != null) {
                    String n = ((AisVesselTarget) t).getVesselStatic().getName();
                    if (n != null && !n.equals("")) {

                        names.add(n);

                        assert ((AisVesselTarget) targetTracker.get(t.getMmsi()).getAisTarget()).getVesselStatic()
                                .getName() != null;

                    }
                }

            }
        }
    }

    @Test
    public void testGetPacketSourcesForMMSI() {
        Set<AisPacketSource> packetSourcesForMMSI = targetTracker.getPacketSourcesForMMSI(219008474);

        assertEquals(2, packetSourcesForMMSI.size());
        Object[] aisPacketSource = packetSourcesForMMSI.stream().toArray();

        assertNotEquals(((AisPacketSource) aisPacketSource[0]).getSourceBaseStation(), ((AisPacketSource) aisPacketSource[1]).getSourceBaseStation());

        assertEquals(2190074, ((AisPacketSource) aisPacketSource[0]).getSourceBaseStation().intValue());
        assertEquals(Country.getByCode("DNK"), ((AisPacketSource) aisPacketSource[0]).getSourceCountry());
        assertNull(((AisPacketSource) aisPacketSource[0]).getSourceId());
        assertEquals("", ((AisPacketSource) aisPacketSource[0]).getSourceRegion());
        assertNull(((AisPacketSource) aisPacketSource[0]).getSourceType());

        assertEquals(2190072, ((AisPacketSource) aisPacketSource[1]).getSourceBaseStation().intValue());
        assertEquals(Country.getByCode("DNK"), ((AisPacketSource) aisPacketSource[1]).getSourceCountry());
        assertNull(((AisPacketSource) aisPacketSource[1]).getSourceId());
        assertEquals("", ((AisPacketSource) aisPacketSource[1]).getSourceRegion());
        assertNull(((AisPacketSource) aisPacketSource[1]).getSourceType());
    }

    @Test
    public void testStream() {
        assertEquals(1788, targetTracker.stream().count()); // TODO How can this be different from targetTracker.size() ?
        assertEquals(1844, targetTracker.size());
        assertEquals(993310001, targetTracker.stream().mapToInt(t -> t.getMmsi()).max().orElse(-1));
        assertEquals(0, targetTracker.stream().mapToInt(t -> t.getMmsi()).min().orElse(-1));
    }

    @Test
    public void testStreamSequentialByDualPredicates() {
        assertEquals(1788, targetTracker.streamSequential(t -> true, src -> true).count());   // TODO How can this be different from targetTracker.size() ?

        Stream<TargetInfo> targetStream = targetTracker.streamSequential(
                src -> src.getSourceCountry() == Country.getByCode("GRL"),
                t -> true
        );
        System.out.println("Tracking:");
        targetStream.forEach(
                t -> {
                    System.out.print("   " + t.getAisTarget().getMmsi() + " ");
                    if (t.getAisTarget().getCountry() != null)
                        System.out.print(t.getAisTarget().getCountry().getThreeLetter() + " ");
                    System.out.print(t.getAisTarget().getTargetType() + " ");
                    if (t.getAisTarget() instanceof AisVesselTarget && t.hasStaticInfo()) {
                        System.out.print("CS:" + ((AisVesselTarget) t.getAisTarget()).getVesselStatic().getCallsign() + " NAME:" + ((AisVesselTarget) t.getAisTarget()).getVesselStatic().getName() + "");
                    } else {
                        System.out.print("<no static>");
                    }
                    System.out.println();
                }
        );

        // There are 7 vessels reported from Greenland
        assertEquals(7, targetTracker.streamSequential(
                src -> src.getSourceCountry() == Country.getByCode("GRL"),
                t -> true
        ).count());

        // 1 of these vessels has static info and can be filtered by name
        assertEquals(1, targetTracker.streamSequential(
                src -> src.getSourceCountry() == Country.getByCode("GRL"),
                targetInfo ->
                        targetInfo.getAisTarget() instanceof AisVesselTarget && (((AisVesselTarget) targetInfo.getAisTarget()).getVesselStatic() != null) ? ((AisVesselTarget) targetInfo.getAisTarget()).getVesselStatic().getName().equals("INUKSUK 1") : false
        ).count());
    }

}
