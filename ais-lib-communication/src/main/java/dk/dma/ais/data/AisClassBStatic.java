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
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.ShipTypeCargo;

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
    
    public AisClassBStatic(AisMessage19 msg19) {
        super();
        update(msg19);
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
    
    public void update(AisMessage19 msg19) {
        this.name = AisMessage.trimText(msg19.getName());
        this.callsign = AisMessage.trimText(msg19.getCallsign());
        this.shipType = (byte) msg19.getShipType();
        this.shipTypeCargo = new ShipTypeCargo(this.shipType);
        this.dimensions = new AisTargetDimensions(msg19);
        super.update((AisMessage) msg19);
    }

}
