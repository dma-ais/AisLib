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
