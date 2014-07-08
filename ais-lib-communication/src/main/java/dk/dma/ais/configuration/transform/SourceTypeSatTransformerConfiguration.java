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
