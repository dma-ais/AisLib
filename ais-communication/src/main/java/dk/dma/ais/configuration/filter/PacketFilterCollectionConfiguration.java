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

    @XmlElement(name = "collectionFilter")
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

    public void setFilters(List<FilterConfiguration> filters) {
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
