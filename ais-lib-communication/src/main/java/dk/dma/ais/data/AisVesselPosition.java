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

    protected Double sog;
    protected Double cog;
    protected Double heading;
    protected Position pos;
    protected byte posAcc;
    protected byte utcSec;
    protected byte raim;

    public AisVesselPosition() {
        super();
    }

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

    public Double getSog() {
        return sog;
    }

    public void setSog(Double sog) {
        this.sog = sog;
    }

    public Double getCog() {
        return cog;
    }

    public void setCog(Double cog) {
        this.cog = cog;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public byte getPosAcc() {
        return posAcc;
    }

    public void setPosAcc(byte posAcc) {
        this.posAcc = posAcc;
    }

    public byte getUtcSec() {
        return utcSec;
    }

    public void setUtcSec(byte utcSec) {
        this.utcSec = utcSec;
    }

    public byte getRaim() {
        return raim;
    }

    public void setRaim(byte raim) {
        this.raim = raim;
    }

}
