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

    /**
     * The Name.
     */
    protected String name;
    /**
     * The Callsign.
     */
    protected String callsign;
    /**
     * The Ship type.
     */
    protected byte shipType;
    /**
     * The Ship type cargo.
     */
    protected ShipTypeCargo shipTypeCargo;
    /**
     * The Dimensions.
     */
    protected AisTargetDimensions dimensions;

    /**
     * Instantiates a new Ais vessel static.
     */
    public AisVesselStatic() {
        super();
    }

    /**
     * Update.
     *
     * @param staticMessage the static message
     */
    public void update(AisStaticCommon staticMessage) {
        this.name = AisMessage.trimText(staticMessage.getName());
        this.callsign = AisMessage.trimText(staticMessage.getCallsign());
        this.shipType = (byte) staticMessage.getShipType();
        this.shipTypeCargo = new ShipTypeCargo(this.shipType);
        this.dimensions = new AisTargetDimensions(staticMessage);
        super.update(staticMessage);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets callsign.
     *
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * Sets callsign.
     *
     * @param callsign the callsign
     */
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    /**
     * Gets ship type.
     *
     * @return the ship type
     */
    public byte getShipType() {
        return shipType;
    }

    /**
     * Sets ship type.
     *
     * @param shipType the ship type
     */
    public void setShipType(byte shipType) {
        this.shipType = shipType;
    }

    /**
     * Gets ship type cargo.
     *
     * @return the ship type cargo
     */
    public ShipTypeCargo getShipTypeCargo() {
        return shipTypeCargo;
    }

    /**
     * Sets ship type cargo.
     *
     * @param shipTypeCargo the ship type cargo
     */
    public void setShipTypeCargo(ShipTypeCargo shipTypeCargo) {
        this.shipTypeCargo = shipTypeCargo;
    }

    /**
     * Gets dimensions.
     *
     * @return the dimensions
     */
    public AisTargetDimensions getDimensions() {
        return dimensions;
    }

    /**
     * Sets dimensions.
     *
     * @param dimensions the dimensions
     */
    public void setDimensions(AisTargetDimensions dimensions) {
        this.dimensions = dimensions;
    }

}
