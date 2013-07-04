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
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.AisMessage24;

/**
 * Class to represent a class B vessel target
 */
public class AisClassBTarget extends AisVesselTarget {

    private static final long serialVersionUID = 1L;

    public AisClassBTarget() {
        super();
    }

    @Override
    public void update(AisMessage aisMessage) {
        // Throw error if message is from other type of target
        if (AisClassATarget.isClassAPosOrStatic(aisMessage) || AisBsTarget.isBsReport(aisMessage)
                || AisAtonTarget.isAtonReport(aisMessage)) {
            throw new IllegalArgumentException("Trying to update class B target with report of other target type");
        }
        // Ignore everything but class B pos and static reports (could be ASM messages etc)
        if (!isClassBPosOrStatic(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    public AisClassBPosition getClassBPosition() {
        return (AisClassBPosition) this.vesselPosition;
    }

    public AisClassBStatic getClassBStatic() {
        return (AisClassBStatic) this.vesselStatic;
    }

    /**
     * Determine if AIS message is class B position report or static report
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isClassBPosOrStatic(AisMessage aisMessage) {
        return aisMessage instanceof AisMessage18 || aisMessage instanceof AisMessage24 || aisMessage instanceof AisMessage19;
    }

}
