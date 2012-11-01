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
import dk.dma.enav.ais.message.AisStaticCommon;
import dk.dma.enav.ais.message.ShipTypeCargo;

/**
 * Class to represent vessel static information
 */
public abstract class AisVesselStatic extends AisReport {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected String callsign;
    protected byte shipType;
    protected ShipTypeCargo shipTypeCargo;
    protected AisTargetDimensions dimensions;

    public AisVesselStatic() {
        super();
    }

    public void update(AisStaticCommon staticMessage) {
        this.name = AisMessage.trimText(staticMessage.getName());
        this.callsign = AisMessage.trimText(staticMessage.getCallsign());
        this.shipType = (byte) staticMessage.getShipType();
        this.shipTypeCargo = new ShipTypeCargo(this.shipType);
        this.dimensions = new AisTargetDimensions(staticMessage);
        super.update(staticMessage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public byte getShipType() {
        return shipType;
    }

    public void setShipType(byte shipType) {
        this.shipType = shipType;
    }

    public ShipTypeCargo getShipTypeCargo() {
        return shipTypeCargo;
    }

    public void setShipTypeCargo(ShipTypeCargo shipTypeCargo) {
        this.shipTypeCargo = shipTypeCargo;
    }

    public AisTargetDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(AisTargetDimensions dimensions) {
        this.dimensions = dimensions;
    }

}
