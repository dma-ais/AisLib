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
package dk.dma.ais.bus.configuration.transform;

import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.packet.AisPacketTagging.SourceType;
import dk.dma.enav.model.Country;

public class PacketTaggingConfiguration {

    private String sourceId;
    private Integer sourceBs;
    private String sourceCountry;
    private String sourceType;

    public PacketTaggingConfiguration() {

    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getSourceBs() {
        return sourceBs;
    }

    public void setSourceBs(Integer sourceBs) {
        this.sourceBs = sourceBs;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
    @XmlTransient
    public AisPacketTagging getInstance() {
        AisPacketTagging tagging = new AisPacketTagging();
        tagging.setSourceId(sourceId);
        tagging.setSourceBs(sourceBs);
        if (sourceCountry != null) {
            Country cntr = Country.getByCode(sourceCountry);
            if (cntr == null) {
                throw new IllegalArgumentException("Unknow ISO 3166 country: " + sourceCountry);
            }
            tagging.setSourceCountry(cntr);
        }
        if (sourceType != null) {
            tagging.setSourceType(SourceType.fromString(sourceType));
        }
        return tagging;
    }

}
