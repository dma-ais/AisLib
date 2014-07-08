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
