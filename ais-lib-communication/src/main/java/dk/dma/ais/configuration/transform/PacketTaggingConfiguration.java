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
package dk.dma.ais.configuration.transform;

import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.packet.AisPacketTags.SourceType;
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
    public AisPacketTags getInstance() {
        AisPacketTags tagging = new AisPacketTags();
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
