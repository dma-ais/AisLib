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
import dk.dma.enav.ais.message.AisMessage24;
import dk.dma.enav.ais.message.ShipTypeCargo;

/**
 * Class to represent class B statics
 */
public class AisClassBStatic extends AisVesselStatic {

    private static final long serialVersionUID = 1L;

    public AisClassBStatic() {
        super();
    }

    public AisClassBStatic(AisMessage24 msg24) {
        super();
        update(msg24);
    }

    public void update(AisMessage24 msg24) {
        if (msg24.getPartNumber() == 0) {
            this.name = AisMessage.trimText(msg24.getName());
        } else {
            this.callsign = AisMessage.trimText(msg24.getCallsign());
            this.shipType = (byte) msg24.getShipType();
            this.shipTypeCargo = new ShipTypeCargo(this.shipType);
            this.dimensions = new AisTargetDimensions(msg24);
        }
        super.update((AisMessage) msg24);
    }

}
