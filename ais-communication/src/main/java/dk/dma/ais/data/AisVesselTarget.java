/* Copyright (c) 2012 Danish Maritime Authority
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
