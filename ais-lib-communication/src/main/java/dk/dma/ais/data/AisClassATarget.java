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
package dk.dma.ais.data;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;

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
        if (AisClassBTarget.isClassBPosOrStatic(aisMessage) || AisBsTarget.isBsReport(aisMessage)
                || AisAtonTarget.isAtonReport(aisMessage)) {
            throw new IllegalArgumentException("Trying to update class A target with report of other target type");
        }
        // Ignore everything but class A pos and static reports (could be ASM messages etc)
        if (!isClassAPosOrStatic(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    public AisClassAPosition getClassAPosition() {
        return (AisClassAPosition) this.vesselPosition;
    }

    public AisClassAStatic getClassAStatic() {
        return (AisClassAStatic) this.vesselStatic;
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

}
