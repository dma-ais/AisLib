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
package dk.dma.ais.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

/**
 * Transformer that maps a number of proprietary taggings into
 * source type SATELLITE tagging 
 */
@ThreadSafe
public class SourceTypeSatTransformer implements IAisPacketTransformer {
    
    /**
     * Set of Gatehouse region ids that should map into source type SAT
     */
    private final Set<String> satGhRegions;
    
    /**
     * Comment block source ('s') that maps in to source type SAT. A source 
     * just need to be contained in the source string
     */
    private final Set<String> satSources;
    
    private final AisPacketTags satTagging; 
    private final AisPacketTaggingTransformer transformer;
    
    public SourceTypeSatTransformer(Collection<String> satGhRegions, Collection<String> satSources) {
        this.satTagging = new AisPacketTags();
        this.satTagging.setSourceType(SourceType.SATELLITE);
        transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, satTagging);
        this.satGhRegions = new HashSet<>(satGhRegions);
        this.satSources = new HashSet<>(satSources);
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return packet;
        }
        // Try to determine from GH tag
        IProprietarySourceTag sourceTag = vdm.getSourceTag();
        if (sourceTag != null) {
            String region = sourceTag.getRegion();
            if (region != null && satGhRegions.contains(region)) {
                return transformer.transform(packet);
            }
        }
        // Try to determine from comment block source
        CommentBlock cb = vdm.getCommentBlock();
        if (cb != null) {
            String source = cb.getString("s");
            if (source != null) {
                for (String satSource : satSources) {
                    if (source.contains(satSource)) {
                        return transformer.transform(packet);
                    }
                }
            }
        }        
        return packet;
    }
    
}
