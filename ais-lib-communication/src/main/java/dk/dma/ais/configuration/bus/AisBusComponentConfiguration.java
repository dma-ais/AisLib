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
package dk.dma.ais.configuration.bus;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.configuration.filter.FilterConfiguration;
import dk.dma.ais.configuration.transform.TransformerConfiguration;

/**
 * The type Ais bus component configuration.
 */
public abstract class AisBusComponentConfiguration {

    private List<FilterConfiguration> filters = new ArrayList<>();
        
    private List<TransformerConfiguration> transformers = new ArrayList<>();

    /**
     * Instantiates a new Ais bus component configuration.
     */
    public AisBusComponentConfiguration() {

    }

    /**
     * Gets filters.
     *
     * @return the filters
     */
    @XmlElement(name = "filter")
    public List<FilterConfiguration> getFilters() {
        return filters;
    }

    /**
     * Sets filters.
     *
     * @param filters the filters
     */
    public void setFilters(List<FilterConfiguration> filters) {
        this.filters = filters;
    }

    /**
     * Gets transformers.
     *
     * @return the transformers
     */
    @XmlElement(name = "transformer")
    public List<TransformerConfiguration> getTransformers() {
        return transformers;
    }

    /**
     * Sets transformers.
     *
     * @param transformers the transformers
     */
    public void setTransformers(List<TransformerConfiguration> transformers) {
        this.transformers = transformers;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public abstract AisBusComponent getInstance();

    /**
     * Configure.
     *
     * @param comp the comp
     */
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
