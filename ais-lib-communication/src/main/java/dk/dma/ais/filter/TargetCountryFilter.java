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
