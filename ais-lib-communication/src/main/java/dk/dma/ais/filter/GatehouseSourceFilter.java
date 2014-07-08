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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<String, HashSet<String>> filter = new ConcurrentHashMap<>();

    public GatehouseSourceFilter() {

    }

    @Override
    public boolean rejectedByFilter(AisMessage message) {
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

    public boolean isEmpty() {
        return filter.size() == 0;
    }

    public void addFilterValue(String filterName, String value) {
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
