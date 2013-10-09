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
package dk.dma.ais.configuration.filter;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.TargetCountryFilter;
import dk.dma.enav.model.Country;

@XmlRootElement
public class TargetCountryFilterConfiguration extends FilterConfiguration {
    
    private HashSet<String> allowedCountries = new HashSet<>();
    
    public TargetCountryFilterConfiguration() {
        
    }
    
    @XmlElement(name = "allowed")
    public HashSet<String> getAllowedCountries() {
        return allowedCountries;
    }
    
    public void setAllowedCountries(HashSet<String> allowedCountries) {
        this.allowedCountries = allowedCountries;
    }

    @Override
    public IPacketFilter getInstance() {
        TargetCountryFilter filter = new TargetCountryFilter();
        for (String strCountry : allowedCountries) {
            Country country = Country.getByCode(strCountry);
            if (country == null) {
                throw new IllegalArgumentException("Unknown country: " + strCountry);
            }
            filter.addCountry(country);
        }
        return filter;
    }

}
