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
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.enav.model.geometry.Position;

/**
 * Class for representing the common position of an AIS class A and class B target
 */
public class AisVesselPosition extends AisReport {

    private static final long serialVersionUID = 1L;

    /**
     * The Sog.
     */
    protected Double sog;
    /**
     * The Cog.
     */
    protected Double cog;
    /**
     * The Heading.
     */
    protected Double heading;
    /**
     * The Pos.
     */
    protected Position pos;
    /**
     * The Pos acc.
     */
    protected byte posAcc;
    /**
     * The Utc sec.
     */
    protected byte utcSec;
    /**
     * The Raim.
     */
    protected byte raim;

    /**
     * Instantiates a new Ais vessel position.
     */
    public AisVesselPosition() {
        super();
    }

    /**
     * Update.
     *
     * @param posMessage the pos message
     */
    public void update(IVesselPositionMessage posMessage) {
        sog = posMessage.isSogValid() ? posMessage.getSog() / 10.0 : null;
        cog = posMessage.isCogValid() ? posMessage.getCog() / 10.0 : null;
        heading = posMessage.isHeadingValid() ? (double) posMessage.getTrueHeading() : null;
        if (posMessage.isPositionValid()) {
            pos = posMessage.getPos().getGeoLocation();
        } else {
            pos = null;
        }
        posAcc = (byte) posMessage.getPosAcc();
        raim = (byte) posMessage.getRaim();
        utcSec = (byte) posMessage.getUtcSec();
        super.update((AisMessage) posMessage);
    }

    /**
     * Gets sog.
     *
     * @return the sog
     */
    public Double getSog() {
        return sog;
    }

    /**
     * Sets sog.
     *
     * @param sog the sog
     */
    public void setSog(Double sog) {
        this.sog = sog;
    }

    /**
     * Gets cog.
     *
     * @return the cog
     */
    public Double getCog() {
        return cog;
    }

    /**
     * Sets cog.
     *
     * @param cog the cog
     */
    public void setCog(Double cog) {
        this.cog = cog;
    }

    /**
     * Gets heading.
     *
     * @return the heading
     */
    public Double getHeading() {
        return heading;
    }

    /**
     * Sets heading.
     *
     * @param heading the heading
     */
    public void setHeading(Double heading) {
        this.heading = heading;
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Sets pos.
     *
     * @param pos the pos
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * Gets pos acc.
     *
     * @return the pos acc
     */
    public byte getPosAcc() {
        return posAcc;
    }

    /**
     * Sets pos acc.
     *
     * @param posAcc the pos acc
     */
    public void setPosAcc(byte posAcc) {
        this.posAcc = posAcc;
    }

    /**
     * Gets utc sec.
     *
     * @return the utc sec
     */
    public byte getUtcSec() {
        return utcSec;
    }

    /**
     * Sets utc sec.
     *
     * @param utcSec the utc sec
     */
    public void setUtcSec(byte utcSec) {
        this.utcSec = utcSec;
    }

    /**
     * Gets raim.
     *
     * @return the raim
     */
    public byte getRaim() {
        return raim;
    }

    /**
     * Sets raim.
     *
     * @param raim the raim
     */
    public void setRaim(byte raim) {
        this.raim = raim;
    }

}
