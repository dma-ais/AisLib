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
