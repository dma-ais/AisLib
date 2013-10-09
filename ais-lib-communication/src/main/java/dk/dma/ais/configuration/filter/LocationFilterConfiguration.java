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

import dk.dma.ais.configuration.filter.geometry.GeometryConfiguration;
import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.LocationFilter;

@XmlRootElement
public class LocationFilterConfiguration extends FilterConfiguration {

    private List<GeometryConfiguration> geometries = new ArrayList<>();

    public LocationFilterConfiguration() {

    }

    @XmlElement(name = "geometry")
    public List<GeometryConfiguration> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<GeometryConfiguration> geometries) {
        this.geometries = geometries;
    }

    @Override
    @XmlTransient
    public IPacketFilter getInstance() {
        LocationFilter locFilter = new LocationFilter();
        for (GeometryConfiguration geo : geometries) {
            locFilter.addFilterGeometry(geo.getPredicate());
        }
        return locFilter;
    }

}
