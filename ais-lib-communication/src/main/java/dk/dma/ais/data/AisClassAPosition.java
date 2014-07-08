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

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;

/**
 * Class to represent a class A position
 */
public class AisClassAPosition extends AisVesselPosition {

    private static final long serialVersionUID = 1L;

    private Double rot;
    private byte navStatus;
    private byte specialManIndicator;

    public AisClassAPosition() {
    }

    public AisClassAPosition(AisPositionMessage posMessage) {
        update(posMessage);
    }

    /**
     * Update data object with data from AIS message
     * 
     * @param posMessage
     */
    public void update(AisPositionMessage posMessage) {
        rot = posMessage.isRotValid() ? (double) posMessage.getRot() : null;
        navStatus = (byte) posMessage.getNavStatus();
        specialManIndicator = (byte) posMessage.getSpecialManIndicator();
        super.update((IVesselPositionMessage) posMessage);
    }

    public Double getRot() {
        return rot;
    }

    public void setRot(Double rot) {
        this.rot = rot;
    }

    public byte getNavStatus() {
        return navStatus;
    }

    public void setNavStatus(byte navStatus) {
        this.navStatus = navStatus;
    }

    public byte getSpecialManIndicator() {
        return specialManIndicator;
    }

    public void setSpecialManIndicator(byte specialManIndicator) {
        this.specialManIndicator = specialManIndicator;
    }

}
