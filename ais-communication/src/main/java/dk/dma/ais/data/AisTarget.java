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

import java.io.Serializable;
import java.util.Date;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage21;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.enav.model.Country;

/**
 * Abstract class representing any AIS target
 */
public abstract class AisTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int mmsi;
    protected Country country;
    protected Date lastReport;
    protected Date created;

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
        if (lastReport == null) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - lastReport.getTime();
        return elapsed / 1000 < ttl;
    }

    /**
     * Update target given AIS message
     * 
     * @param aisMessage
     */
    public void update(AisMessage aisMessage) {
        // Set MMSI
        this.mmsi = aisMessage.getUserId();
        
        this.lastReport = aisMessage.getVdm().getTimestamp();  
        if (this.lastReport == null) {
            this.lastReport = new Date();
        }
        
        // Set country
        country = Country.getCountryForMmsi(aisMessage.getUserId());
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
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

    /**
     * Determine if message is a message from target containing data about the target
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isTargetDataMessage(AisMessage aisMessage) {
        if (aisMessage instanceof IVesselPositionMessage) {
            return true;
        } else if (aisMessage instanceof AisStaticCommon) {
            return true;
        } else if (aisMessage instanceof AisMessage4) {
            return true;
        } else if (aisMessage instanceof AisMessage21) {
            return true;
        }
        return false;
    }

}
