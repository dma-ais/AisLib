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

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisTargetType;

/**
 * Class to represent a class A vessel target
 */
public class AisClassATarget extends AisVesselTarget {

    private static final long serialVersionUID = 1L;

    public AisClassATarget() {
        super();
    }

    @Override
    public void update(AisMessage aisMessage) {
        // Throw error if message is from other type of target
        if (isMessageCompatible(aisMessage)) {
            throw new IllegalArgumentException("Trying to update class A target with report of other target type");
        }
        // Ignore everything but class A pos and static reports (could be ASM messages etc)
        if (!isClassAPosOrStatic(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    @JsonIgnore
    public AisClassAPosition getClassAPosition() {
        return (AisClassAPosition) this.vesselPosition;
    }

    @JsonIgnore
    public AisClassAStatic getClassAStatic() {
        return (AisClassAStatic) this.vesselStatic;
    }

    @Override
    public AisTargetType getTargetType() {
        return AisTargetType.A;
    }

    /**
     * Determine if AIS message is class A position report or static report
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isClassAPosOrStatic(AisMessage aisMessage) {
        return aisMessage instanceof AisPositionMessage || aisMessage instanceof AisMessage5;
    }

    @Override
    public boolean isMessageCompatible(AisMessage aisMessage) {
        return AisClassBTarget.isClassBPosOrStatic(aisMessage) || AisBsTarget.isBsReport(aisMessage)
                || AisAtonTarget.isAtonReport(aisMessage);
    }

}
