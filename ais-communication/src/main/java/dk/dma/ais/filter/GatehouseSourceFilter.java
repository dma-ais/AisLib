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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.enav.model.Country;

/**
 * Filtering based on the source information attached to message.
 * 
 * @see dk.dma.ais.data.AisTargetSourceData
 */
@ThreadSafe
public class GatehouseSourceFilter extends MessageFilterBase {

    /**
     * Initialize allowed filter names
     */
    public static final Set<String> FILTER_NAMES;
    static {
        String[] filterNamesStr = { "basestation", "region", "targetCountry", "country" };
        FILTER_NAMES = new HashSet<>();
        for (String filterName : filterNamesStr) {
            FILTER_NAMES.add(filterName);
        }
    }

    /**
     * Map from filter name to set of accepted values
     */
    @GuardedBy("this")
    private final Map<String, HashSet<String>> filter = new HashMap<>();

    public GatehouseSourceFilter() {

    }

    @Override
    public synchronized boolean rejectedByFilter(AisMessage message) {
        if (isEmpty()) {
            return false;
        }

        // Get tag
        IProprietarySourceTag tag = message.getSourceTag();
        if (tag == null) {
            return true;
        }
        
        // Only use gatehouse tag
        if (!(tag instanceof GatehouseSourceTag)) {
            return true;
        }
        
        HashMap<String, String> tagMap = new HashMap<>();
        if (tag.getBaseMmsi() != null) {
            tagMap.put("basestation", Integer.toString(tag.getBaseMmsi()));
        }
        if (tag.getRegion() != null) {
            tagMap.put("region", tag.getRegion());
        }
        if (tag.getCountry() != null) {
            tagMap.put("country", tag.getCountry().getThreeLetter());
        }
        Country cntr = Country.getCountryForMmsi(message.getUserId());
        if (cntr != null) {
            tagMap.put("targetCountry", cntr.getThreeLetter());
        }

        for (String filterName : filter.keySet()) {
            HashSet<String> values = filter.get(filterName);
            // Get tag value
            String tagValue = tagMap.get(filterName);
            if (tagValue == null || !values.contains(tagValue)) {
                return true;
            }
        }

        return false;
    }

    public synchronized boolean isEmpty() {
        return filter.size() == 0;
    }

    public synchronized void addFilterValue(String filterName, String value) {
        if (!FILTER_NAMES.contains(filterName)) {
            throw new IllegalArgumentException("Unknown filter: " + filterName);
        }

        HashSet<String> filterValues = filter.get(filterName);
        if (filterValues == null) {
            filterValues = new HashSet<>();
            filter.put(filterName, filterValues);
        }
        filterValues.add(value);
    }

}
