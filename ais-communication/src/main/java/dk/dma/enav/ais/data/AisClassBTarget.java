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
package dk.dma.enav.ais.data;

import dk.dma.enav.ais.message.AisMessage;
import dk.dma.enav.ais.message.AisMessage18;
import dk.dma.enav.ais.message.AisMessage24;

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
        // Check that update has same class as this
        if (!(aisMessage instanceof AisMessage18 || aisMessage instanceof AisMessage24)) {
            // Simply ignore as another vessel uses same MMSI
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

}
