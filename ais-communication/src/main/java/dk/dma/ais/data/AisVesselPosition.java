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
