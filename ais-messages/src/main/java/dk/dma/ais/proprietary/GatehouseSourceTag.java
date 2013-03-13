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
