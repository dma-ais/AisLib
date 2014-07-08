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
package dk.dma.ais.data;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;

/**
 * Class to represent any vessel target
 */
public abstract class AisVesselTarget extends AisTarget {

    private static final long serialVersionUID = 1L;

    protected AisVesselStatic vesselStatic;
    protected AisVesselPosition vesselPosition;

    public AisVesselTarget() {
        super();
    }

    @Override
    public void update(AisMessage aisMessage) {
        if (aisMessage instanceof AisMessage5) {
            if (vesselStatic == null || !(vesselStatic instanceof AisClassAStatic)) {
                vesselStatic = new AisClassAStatic((AisMessage5) aisMessage);
            } else {
                ((AisClassAStatic) vesselStatic).update((AisMessage5) aisMessage);
            }
        } else if (aisMessage instanceof AisPositionMessage) {
            if (vesselPosition == null || !(vesselPosition instanceof AisClassAPosition)) {
                vesselPosition = new AisClassAPosition((AisPositionMessage) aisMessage);
            } else {
                ((AisClassAPosition) vesselPosition).update((AisPositionMessage) aisMessage);
            }
        } else if (aisMessage instanceof AisMessage18) {
            if (vesselPosition == null || !(vesselPosition instanceof AisClassBPosition)) {
                vesselPosition = new AisClassBPosition((AisMessage18) aisMessage);
            } else {
                ((AisClassBPosition) vesselPosition).update((AisMessage18) aisMessage);
            }           
        } else if (aisMessage instanceof AisMessage24) {
            if (vesselStatic == null || !(vesselStatic instanceof AisClassBStatic)) {
                vesselStatic = new AisClassBStatic((AisMessage24) aisMessage);
            } else {
                ((AisClassBStatic) vesselStatic).update((AisMessage24) aisMessage);
            }
        } else if (aisMessage instanceof AisMessage19) {
            if (vesselPosition == null || !(vesselPosition instanceof AisClassBPosition)) {
                vesselPosition = new AisClassBPosition((AisMessage19) aisMessage);
            } else {
                ((AisClassBPosition) vesselPosition).update((AisMessage19) aisMessage);
            }           
            if (vesselStatic == null || !(vesselStatic instanceof AisClassBStatic)) {
                vesselStatic = new AisClassBStatic((AisMessage19) aisMessage);
            } else {
                ((AisClassBStatic) vesselStatic).update((AisMessage19) aisMessage);
            }
        }

        super.update(aisMessage);
    }

    public AisVesselStatic getVesselStatic() {
        return vesselStatic;
    }

    public void setVesselStatic(AisVesselStatic vesselStatic) {
        this.vesselStatic = vesselStatic;
    }

    public AisVesselPosition getVesselPosition() {
        return vesselPosition;
    }

    public void setVesselPosition(AisVesselPosition vesselPosition) {
        this.vesselPosition = vesselPosition;
    }

}
