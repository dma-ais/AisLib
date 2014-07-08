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
package dk.dma.ais.filter;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.message.AisMessage;
import dk.dma.enav.model.Country;

/**
 * Filter based on the country of the AIS target
 */
@ThreadSafe
public class TargetCountryFilter extends MessageFilterBase {

    /**
     * Set of allowed countries by their ISO 3166 three letter code
     */
    private final Set<String> allowedCountries = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    
    public TargetCountryFilter() {
        
    }

    @Override
    public boolean rejectedByFilter(AisMessage message) {
        // Get country of sender
        Country country = Country.getCountryForMmsi(message.getUserId());
        // Reject unknown countries
        if (country == null) {
            return true;
        }
        return !allowedCountries.contains(country.getThreeLetter());
    }
    
    public void addCountry(Country country) {
        allowedCountries.add(country.getThreeLetter());
    }
    
    public void removeCountry(Country country) {
        allowedCountries.remove(country.getThreeLetter());
    }

}
