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

    /**
     * Instantiates a new Ais class a static.
     */
    public AisClassAStatic() {
        super();
    }

    /**
     * Instantiates a new Ais class a static.
     *
     * @param msg5 the msg 5
     */
    public AisClassAStatic(AisMessage5 msg5) {
        super();
        update(msg5);
    }

    /**
     * Update.
     *
     * @param msg5 the msg 5
     */
    public void update(AisMessage5 msg5) {
        this.destination = AisMessage.trimText(msg5.getDest());
        if (this.destination.length() == 0) {
            this.destination = null;
        }
        this.draught = msg5.getDraught() == 0 ? null : msg5.getDraught() / 10.0;
        this.eta = msg5.getEtaDate();
        this.posType = (byte) msg5.getPosType();
        this.version = (byte) msg5.getVersion();
        this.dte = (byte) msg5.getDte();
        if (msg5.getImo() > 0) {
            this.imoNo = (int) msg5.getImo();
        }
        super.update(msg5);
    }

    /**
     * Gets imo no.
     *
     * @return the imo no
     */
    public Integer getImoNo() {
        return imoNo;
    }

    /**
     * Sets imo no.
     *
     * @param imoNo the imo no
     */
    public void setImoNo(Integer imoNo) {
        this.imoNo = imoNo;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets eta.
     *
     * @return the eta
     */
    public Date getEta() {
        return eta;
    }

    /**
     * Sets eta.
     *
     * @param eta the eta
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * Gets pos type.
     *
     * @return the pos type
     */
    public byte getPosType() {
        return posType;
    }

    /**
     * Sets pos type.
     *
     * @param posType the pos type
     */
    public void setPosType(byte posType) {
        this.posType = posType;
    }

    /**
     * Gets draught.
     *
     * @return the draught
     */
    public Double getDraught() {
        return draught;
    }

    /**
     * Sets draught.
     *
     * @param draught the draught
     */
    public void setDraught(Double draught) {
        this.draught = draught;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public byte getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(byte version) {
        this.version = version;
    }

    /**
     * Gets dte.
     *
     * @return the dte
     */
    public byte getDte() {
        return dte;
    }

    /**
     * Sets dte.
     *
     * @param dte the dte
     */
    public void setDte(byte dte) {
        this.dte = dte;
    }

}
