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
package dk.dma.ais.bus.configuration.filter;

import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.bus.configuration.transform.PacketTaggingConfiguration;
import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.TaggingFilter;

@XmlRootElement
public class TaggingFilterConfiguration extends FilterConfiguration {
    
    private PacketTaggingConfiguration filterTagging = new PacketTaggingConfiguration();
    
    public TaggingFilterConfiguration() {
        
    }
    
    public PacketTaggingConfiguration getFilterTagging() {
        return filterTagging;
    }
    
    public void setFilterTagging(PacketTaggingConfiguration filterTagging) {
        this.filterTagging = filterTagging;
    }

    @Override
    public IPacketFilter getInstance() {
        return new TaggingFilter(filterTagging.getInstance());
    }

}
