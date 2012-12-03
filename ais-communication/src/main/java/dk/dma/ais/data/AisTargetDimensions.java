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

import java.io.Serializable;

import dk.dma.ais.message.AisStaticCommon;

/**
 * Class to represent dimensions of an AIS target with dimensions
 * 
 * TODO: Evaluate the parameters and set length and witdt accordingly. Make attributes to determine if dimensions is
 * available, and if reference point is available.
 * 
 */
public class AisTargetDimensions implements Serializable {

    private static final long serialVersionUID = 1L;

    private short dimBow;
    private short dimStern;
    private byte dimPort;
    private byte dimStarboard;

    public AisTargetDimensions(AisStaticCommon staticCommon) {
        this.dimBow = (short) staticCommon.getDimBow();
        this.dimStern = (short) staticCommon.getDimStern();
        this.dimPort = (byte) staticCommon.getDimPort();
        this.dimStarboard = (byte) staticCommon.getDimStarboard();

        // TODO
    }

    public short getDimBow() {
        return dimBow;
    }

    public void setDimBow(short dimBow) {
        this.dimBow = dimBow;
    }

    public short getDimStern() {
        return dimStern;
    }

    public void setDimStern(short dimStern) {
        this.dimStern = dimStern;
    }

    public byte getDimPort() {
        return dimPort;
    }

    public void setDimPort(byte dimPort) {
        this.dimPort = dimPort;
    }

    public byte getDimStarboard() {
        return dimStarboard;
    }

    public void setDimStarboard(byte dimStarboard) {
        this.dimStarboard = dimStarboard;
    }

}
