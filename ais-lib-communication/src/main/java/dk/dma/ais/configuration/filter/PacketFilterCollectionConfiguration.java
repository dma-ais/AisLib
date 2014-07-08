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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.PacketFilterCollection;

@XmlRootElement
public class PacketFilterCollectionConfiguration extends FilterConfiguration {
    //TODO fix this into an enumeration type or something usable in PacketFilterCollection aswell
    private static final int TYPE_AND = 0;
    private int filterType = TYPE_AND;

    @XmlElement(name = "filterCollection")
    private List<FilterConfiguration> collection = new ArrayList<>();

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public List<FilterConfiguration> getCollection() {
        return collection;
    }

    public void set(List<FilterConfiguration> filters) {
        this.collection = filters;
    }

    public void addFilterConfiguration(FilterConfiguration c) {
        this.collection.add(c);
    }

    @Override
    @XmlTransient
    public IPacketFilter getInstance() {

        PacketFilterCollection pfc = new PacketFilterCollection();

        for (FilterConfiguration fc : collection) {
            pfc.addFilter(fc.getInstance());
        }

        pfc.setFilterType(filterType);

        return pfc;
    }

}
