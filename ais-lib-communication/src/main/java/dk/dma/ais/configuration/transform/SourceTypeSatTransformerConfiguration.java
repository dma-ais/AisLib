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
package dk.dma.ais.configuration.transform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.SourceTypeSatTransformer;

@XmlRootElement
public class SourceTypeSatTransformerConfiguration extends TransformerConfiguration {

    private List<String> satGhRegions = new ArrayList<>();
    private List<String> satSources = new ArrayList<>();

    public SourceTypeSatTransformerConfiguration() {

    }

    @XmlElement(name = "gh_region")
    public List<String> getSatGhRegions() {
        return satGhRegions;
    }

    public void setSatGhRegions(List<String> satGhRegions) {
        this.satGhRegions = satGhRegions;
    }

    @XmlElement(name = "source")
    public List<String> getSatSources() {
        return satSources;
    }

    public void setSatSources(List<String> satSources) {
        this.satSources = satSources;
    }

    @Override
    public IAisPacketTransformer getInstance() {
        return new SourceTypeSatTransformer(satGhRegions, satSources);
    }
    
}
