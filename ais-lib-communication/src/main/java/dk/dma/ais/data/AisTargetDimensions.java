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

import java.io.Serializable;

import dk.dma.ais.message.AisStaticCommon;

/**
 * Class to represent dimensions of an AIS target with dimensions
 * <p>
 * TODO: Evaluate the parameters and set length and witdt accordingly. Make attributes to determine if dimensions is
 * available, and if reference point is available.
 */
public class AisTargetDimensions implements Serializable {

    private static final long serialVersionUID = 1L;

    private short dimBow;
    private short dimStern;
    private byte dimPort;
    private byte dimStarboard;

    /**
     * Instantiates a new Ais target dimensions.
     *
     * @param staticCommon the static common
     */
    public AisTargetDimensions(AisStaticCommon staticCommon) {
        this.dimBow = (short) staticCommon.getDimBow();
        this.dimStern = (short) staticCommon.getDimStern();
        this.dimPort = (byte) staticCommon.getDimPort();
        this.dimStarboard = (byte) staticCommon.getDimStarboard();

        // TODO
    }

    /**
     * Gets dim bow.
     *
     * @return the dim bow
     */
    public short getDimBow() {
        return dimBow;
    }

    /**
     * Sets dim bow.
     *
     * @param dimBow the dim bow
     */
    public void setDimBow(short dimBow) {
        this.dimBow = dimBow;
    }

    /**
     * Gets dim stern.
     *
     * @return the dim stern
     */
    public short getDimStern() {
        return dimStern;
    }

    /**
     * Sets dim stern.
     *
     * @param dimStern the dim stern
     */
    public void setDimStern(short dimStern) {
        this.dimStern = dimStern;
    }

    /**
     * Gets dim port.
     *
     * @return the dim port
     */
    public byte getDimPort() {
        return dimPort;
    }

    /**
     * Sets dim port.
     *
     * @param dimPort the dim port
     */
    public void setDimPort(byte dimPort) {
        this.dimPort = dimPort;
    }

    /**
     * Gets dim starboard.
     *
     * @return the dim starboard
     */
    public byte getDimStarboard() {
        return dimStarboard;
    }

    /**
     * Sets dim starboard.
     *
     * @param dimStarboard the dim starboard
     */
    public void setDimStarboard(byte dimStarboard) {
        this.dimStarboard = dimStarboard;
    }

}
