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

import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.filter.GatehouseSourceFilter;
import dk.dma.ais.filter.IPacketFilter;

@XmlRootElement
public class GatehouseSourceFilterConfiguration extends FilterConfiguration {

    private HashMap<String, String> filterEntries = new HashMap<>();

    public GatehouseSourceFilterConfiguration() {

    }
    
    public HashMap<String, String> getFilterEntries() {
        return filterEntries;
    }
    
    public void setFilterEntries(HashMap<String, String> filterEntries) {
        this.filterEntries = filterEntries;
    }

    @Override
    public IPacketFilter getInstance() {
        GatehouseSourceFilter sourceFilter = new GatehouseSourceFilter();
        for (Entry<String, String> entry : filterEntries.entrySet()) {
            sourceFilter.addFilterValue(entry.getKey(), entry.getValue());
        }
        return sourceFilter;
    }

}
