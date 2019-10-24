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

    /**
     * Instantiates a new Ais class a position.
     */
    public AisClassAPosition() {
    }

    /**
     * Instantiates a new Ais class a position.
     *
     * @param posMessage the pos message
     */
    public AisClassAPosition(AisPositionMessage posMessage) {
        update(posMessage);
    }

    /**
     * Update data object with data from AIS message
     *
     * @param posMessage the pos message
     */
    public void update(AisPositionMessage posMessage) {
        rot = posMessage.isRotValid() ? (double) posMessage.getRot() : null;
        navStatus = (byte) posMessage.getNavStatus();
        specialManIndicator = (byte) posMessage.getSpecialManIndicator();
        super.update((IVesselPositionMessage) posMessage);
    }

    /**
     * Gets rot.
     *
     * @return the rot
     */
    public Double getRot() {
        return rot;
    }

    /**
     * Sets rot.
     *
     * @param rot the rot
     */
    public void setRot(Double rot) {
        this.rot = rot;
    }

    /**
     * Gets nav status.
     *
     * @return the nav status
     */
    public byte getNavStatus() {
        return navStatus;
    }

    /**
     * Sets nav status.
     *
     * @param navStatus the nav status
     */
    public void setNavStatus(byte navStatus) {
        this.navStatus = navStatus;
    }

    /**
     * Gets special man indicator.
     *
     * @return the special man indicator
     */
    public byte getSpecialManIndicator() {
        return specialManIndicator;
    }

    /**
     * Sets special man indicator.
     *
     * @param specialManIndicator the special man indicator
     */
    public void setSpecialManIndicator(byte specialManIndicator) {
        this.specialManIndicator = specialManIndicator;
    }

}
