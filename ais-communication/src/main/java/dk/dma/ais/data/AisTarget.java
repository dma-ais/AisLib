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
import java.util.Date;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage21;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.proprietary.DmaSourceTag;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.enav.model.Country;

/**
 * Abstract class representing any AIS target
 */
public abstract class AisTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long mmsi;
    protected Country country;
    protected Date lastReport;
    protected Date created;
    protected AisTargetSourceData sourceData;

    public AisTarget() {
        this.created = new Date();
    }

    /**
     * Determine if target is alive based on ttl given in seconds
     * 
     * @param ttl
     * @return
     */
    public boolean isAlive(int ttl) {
        if (lastReport == null)
            return false;
        long elapsed = System.currentTimeMillis() - lastReport.getTime();
        return (elapsed / 1000 < ttl);
    }

    /**
     * Update target given AIS message
     * 
     * @param aisMessage
     */
    public void update(AisMessage aisMessage) {
        // Set last report time
        this.lastReport = new Date();
        // Set MMSI
        this.mmsi = (int) aisMessage.getUserId();
        // Set source data
        if (sourceData == null) {
            sourceData = new AisTargetSourceData();
        }
        if (aisMessage.getTags() != null) {
            for (IProprietaryTag tag : aisMessage.getTags()) {
                if (tag instanceof DmaSourceTag) {
                    DmaSourceTag dmaSourceTag = (DmaSourceTag) tag;
                    sourceData.setSourceSystem(dmaSourceTag.getSourceName());
                } else if (tag instanceof GatehouseSourceTag) {
                    GatehouseSourceTag ghTag = (GatehouseSourceTag) tag;
                    String sourceRegion = ghTag.getRegion();
                    if (sourceRegion != null) {
                        if (sourceRegion.length() == 0) {
                            sourceRegion = null;
                        } else {
                            // TODO This mapping should come from a tag in the future
                            if (sourceRegion.equals("802") || sourceRegion.equals("804") || sourceRegion.equals("808")) {
                                sourceData.setSourceType("SAT");
                            }
                        }
                    }
                    sourceData.setSourceRegion(sourceRegion);
                    sourceData.setSourceCountry(ghTag.getCountry());
                    if (ghTag.getBaseMmsi() != null) {
                        sourceData.setSourceBs(ghTag.getBaseMmsi());
                    }
                    sourceData.setLastReport(this.lastReport);
                }
            }
        }
        // Set country
        country = Country.getCountryForMmsi(aisMessage.getUserId());
    }

    public long getMmsi() {
        return mmsi;
    }

    public void setMmsi(long mmsi) {
        this.mmsi = mmsi;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getLastReport() {
        return lastReport;
    }

    public void setLastReport(Date lastReport) {
        this.lastReport = lastReport;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public AisTargetSourceData getSourceData() {
        return sourceData;
    }

    public void setSourceData(AisTargetSourceData sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Create new AIS target instance based on AIS message
     * 
     * @param aisMessage
     * @return
     */
    public static AisTarget createTarget(AisMessage aisMessage) {
        AisTarget target = null;
        if (aisMessage instanceof AisPositionMessage || aisMessage instanceof AisMessage5) {
            target = new AisClassATarget();
        } else if (aisMessage instanceof AisMessage18 || aisMessage instanceof AisMessage24) {
            target = new AisClassBTarget();
        } else if (aisMessage instanceof AisMessage4) {
            target = new AisBsTarget();
        } else if (aisMessage instanceof AisMessage21) {
            target = new AisAtonTarget();
        }
        return target;
    }

}
