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
