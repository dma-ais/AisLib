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
package dk.dma.ais.proprietary;

import java.util.Date;

import net.jcip.annotations.Immutable;

import dk.dma.enav.model.Country;

/**
 * An implementation of a IProprietarySourceTag for Gatehouse source tag
 */
@Immutable
public class GatehouseSourceTag implements IProprietarySourceTag {

    private final Integer baseMmsi;
    private final Country country;
    private final String region;
    private final Date timestamp;
    private final String orgSentence;

    public GatehouseSourceTag(Integer baseMmsi, Country country, String region, Date timestamp, String orgSentence) {
        this.baseMmsi = baseMmsi;
        this.country = country;
        this.region = region;
        this.timestamp = timestamp;
        this.orgSentence = orgSentence;
    }

    public Integer getBaseMmsi() {
        return baseMmsi;
    }

    public Country getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSentence() {
        return orgSentence;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GatehouseSourceTag [baseMmsi=");
        builder.append(baseMmsi);
        builder.append(", country=");
        builder.append(country != null ? country.getTwoLetter() : "null");
        builder.append(", region=");
        builder.append(region);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append("]");
        return builder.toString();
    }

}
