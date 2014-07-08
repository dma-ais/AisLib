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

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.transform.AisPacketTaggingTransformer;
import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

@XmlRootElement
public class TaggingTransformerConfiguration extends TransformerConfiguration {

    private Policy tagPolicy;
    private PacketTaggingConfiguration tagging = new PacketTaggingConfiguration();
    private HashMap<String, String> extraTags = new HashMap<>();

    public TaggingTransformerConfiguration() {

    }

    @XmlElement(required = true)
    public Policy getTagPolicy() {
        return tagPolicy;
    }

    public void setTagPolicy(Policy tagPolicy) {
        this.tagPolicy = tagPolicy;
    }
    
    public PacketTaggingConfiguration getTagging() {
        return tagging;
    }
    
    public void setTagging(PacketTaggingConfiguration tagging) {
        this.tagging = tagging;
    }

    @XmlElement(name = "extraTag")
    public HashMap<String, String> getExtraTags() {
        return extraTags;
    }

    public void setExtraTags(HashMap<String, String> extraTags) {
        this.extraTags = extraTags;
    }
    
    @Override
    public IAisPacketTransformer getInstance() {
        if (tagPolicy == null) {
            tagPolicy = Policy.PREPEND_MISSING;
        }
        if (tagging == null) {
            tagging = new PacketTaggingConfiguration();
        }
        AisPacketTaggingTransformer tagTrans = new AisPacketTaggingTransformer(tagPolicy, tagging.getInstance());
        tagTrans.addExtraTags(extraTags);
        return tagTrans;
    }

}
