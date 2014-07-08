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
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.ShipTypeCargo;

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
