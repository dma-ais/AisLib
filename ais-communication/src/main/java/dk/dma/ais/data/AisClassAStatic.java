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

import java.util.Date;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;

/**
 * Class to represent class A statics
 */
public class AisClassAStatic extends AisVesselStatic {

    private static final long serialVersionUID = 1L;

    private Integer imoNo;
    private String destination;
    private Date eta;
    private byte posType;
    private Double draught;
    private byte version;
    private byte dte;

    public AisClassAStatic() {
        super();
    }

    public AisClassAStatic(AisMessage5 msg5) {
        super();
        update(msg5);
    }

    public void update(AisMessage5 msg5) {
        this.destination = AisMessage.trimText(msg5.getDest());
        if (this.destination.length() == 0) {
            this.destination = null;
        }
        this.draught = (msg5.getDraught() == 0 ? null : msg5.getDraught() / 10.0);
        this.eta = msg5.getEtaDate();
        this.posType = (byte) msg5.getPosType();
        this.version = (byte) msg5.getVersion();
        this.dte = (byte) msg5.getDte();
        if (msg5.getImo() > 0) {
            this.imoNo = (int) msg5.getImo();
        }
        super.update(msg5);
    }

    public Integer getImoNo() {
        return imoNo;
    }

    public void setImoNo(Integer imoNo) {
        this.imoNo = imoNo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public byte getPosType() {
        return posType;
    }

    public void setPosType(byte posType) {
        this.posType = posType;
    }

    public Double getDraught() {
        return draught;
    }

    public void setDraught(Double draught) {
        this.draught = draught;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getDte() {
        return dte;
    }

    public void setDte(byte dte) {
        this.dte = dte;
    }

}
