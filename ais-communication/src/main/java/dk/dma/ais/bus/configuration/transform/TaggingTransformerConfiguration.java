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
package dk.dma.ais.bus.configuration.transform;

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
        AisPacketTaggingTransformer tagTrans = new AisPacketTaggingTransformer(tagPolicy, tagging.getInstance());
        tagTrans.addExtraTags(extraTags);
        return tagTrans;
    }

}
