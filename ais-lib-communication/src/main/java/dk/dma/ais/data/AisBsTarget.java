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
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.message.AisTargetType;

/**
 * Class to represent an AIS base station target
 */
public class AisBsTarget extends AisTarget {

    private static final long serialVersionUID = 1L;

    @Override
    public void update(AisMessage aisMessage) {
        // Throw error if message is from other type of target
        if (isMessageCompatible(aisMessage)) {
            throw new IllegalArgumentException("Trying to update BS target with report of other target type");
        }
        // Ignore everything but BS reports
        if (!isBsReport(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    @Override
    public AisTargetType getTargetType() {
        return AisTargetType.BS;
    }

    /**
     * Determine if message is BS report
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isBsReport(AisMessage aisMessage) {
        return aisMessage instanceof AisMessage4;
    }

    @Override
    public boolean isMessageCompatible(AisMessage aisMessage) {
        return AisClassATarget.isClassAPosOrStatic(aisMessage) || AisClassBTarget.isClassBPosOrStatic(aisMessage)
                || AisAtonTarget.isAtonReport(aisMessage);
    }

}
