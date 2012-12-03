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

import dk.dma.enav.model.Country;

/**
 * Class to data about the source of an AIS target
 */
public class AisTargetSourceData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sourceType = "LIVE";
    private Country sourceCountry;
    private String sourceRegion;
    private Long sourceBs;
    private String sourceSystem;
    private Date lastReport;
    private Date created;

    public AisTargetSourceData() {
        this.created = new Date();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Country getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(Country sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public String getSourceRegion() {
        return sourceRegion;
    }

    public void setSourceRegion(String sourceRegion) {
        this.sourceRegion = sourceRegion;
    }

    public Long getSourceBs() {
        return sourceBs;
    }

    public void setSourceBs(Long sourceBs) {
        this.sourceBs = sourceBs;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
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

    public boolean isSatData() {
        return sourceType != null && sourceType.equals("SAT");
    }

}
