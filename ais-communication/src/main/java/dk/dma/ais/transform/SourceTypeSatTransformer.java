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
package dk.dma.ais.transform;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.NotThreadSafe;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.packet.AisPacketTagging.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

/**
 * Transformer that maps a number of proprietary taggings into
 * source type SATELLITE tagging 
 */
@NotThreadSafe
public class SourceTypeSatTransformer implements IAisPacketTransformer {
    
    /**
     * Set of Gatehouse region ids that should map into source type SAT
     */
    private final Set<String> satGhRegions = new HashSet<>();
    
    /**
     * Comment block source ('s') that maps in to source type SAT. A source 
     * just need to be contained in the source string
     */
    private final Set<String> satSources = new HashSet<>();    
    
    private final AisPacketTagging satTagging; 
    private final AisPacketTaggingTransformer transformer;
    
    public SourceTypeSatTransformer() {
        satTagging = new AisPacketTagging();
        satTagging.setSourceType(SourceType.SATELLITE);
        transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, satTagging);
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
    
    public Set<String> getSatGhRegions() {
        return satGhRegions;
    }
    
    public Set<String> getSatSources() {
        return satSources;
    }

}
