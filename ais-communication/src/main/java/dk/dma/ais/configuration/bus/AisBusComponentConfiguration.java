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
package dk.dma.ais.configuration.bus;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.configuration.filter.FilterConfiguration;
import dk.dma.ais.configuration.transform.TransformerConfiguration;

public abstract class AisBusComponentConfiguration {

    private List<FilterConfiguration> filters = new ArrayList<>();
    
    private List<TransformerConfiguration> transformers = new ArrayList<>();

    public AisBusComponentConfiguration() {

    }

    @XmlElement(name = "filter")
    public List<FilterConfiguration> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterConfiguration> filters) {
        this.filters = filters;
    }
    
    @XmlElement(name = "transformer")
    public List<TransformerConfiguration> getTransformers() {
        return transformers;
    }
    
    public void setTransformers(List<TransformerConfiguration> transformers) {
        this.transformers = transformers;
    }
    
    public abstract AisBusComponent getInstance();
    
    protected void configure(AisBusComponent comp) {
        // Add filters
        for (FilterConfiguration filterConf : filters) {
            comp.getFilters().addFilter(filterConf.getInstance());
        }
        // Add transformers
        for (TransformerConfiguration transConf : transformers) {
            comp.getPacketTransformers().add(transConf.getInstance());
        }
    }

}
