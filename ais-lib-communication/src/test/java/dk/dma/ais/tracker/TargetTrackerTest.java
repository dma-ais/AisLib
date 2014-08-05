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

package dk.dma.ais.tracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import dk.dma.ais.data.AisTarget;
import dk.dma.ais.data.AisVesselTarget;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.util.function.Consumer;


public class TargetTrackerTest {

    AisReader aisReader;
    TargetTracker targetTracker;
    HashMap<Integer,AisTarget> aisVesselTargets;

    @Before
    public void setUp() throws Exception {
        aisVesselTargets = new HashMap<>();
        Path testdata = Paths.get("src/test/resources/");
        aisReader = AisReaders.createDirectoryReader(testdata.toString(), "*.txt", true);
        
        targetTracker = new TargetTracker();
        targetTracker.readFromStream(aisReader.stream());
        
        aisReader.registerHandler(new Consumer<AisMessage>() {
            
            @Override
            public void accept(AisMessage t) {
                if (aisVesselTargets.containsKey(t.getUserId())) {

                } else {
                    try {
                        AisTarget k = Objects.requireNonNull(AisTarget.createTarget(t));
                        if (k instanceof AisVesselTarget) {
                            aisVesselTargets.put(t.getUserId(), k);
                        }
                        
                    } catch(NullPointerException e) {
                        
                    } 
                }
            }
        });
        
        aisReader.start();
        //read all first
        aisReader.join();
        
        
    }

    @Test
    public void testExists() throws IOException, InterruptedException {
        for (AisTarget t: aisVesselTargets.values()) {
            Objects.requireNonNull(targetTracker.getNewest(Objects.requireNonNull(t.getMmsi())));
        }
    }
    
    /**
     * Test that all AisVesselTargets with names have names in TargetTracker
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testNames() throws IOException, InterruptedException {
        List<String> names = new ArrayList<String>();
        for (AisTarget t: aisVesselTargets.values()) {
            if (t instanceof AisVesselTarget) {
                if (((AisVesselTarget) t).getVesselStatic() != null) {
                    String n = ((AisVesselTarget) t).getVesselStatic().getName();
                    if (n != null && !n.equals("")) {
                        
                        names.add(n);
                        
                        assert ((AisVesselTarget)targetTracker.getNewest(t.getMmsi()).getAisTarget()).getVesselStatic().getName() != null;
                        
                        
                    }
                }
                
            }
        }


    }


}
