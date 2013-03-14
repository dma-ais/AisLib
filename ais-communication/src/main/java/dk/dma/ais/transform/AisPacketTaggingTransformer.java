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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;

/**
 * Transformer that given a tagging instance and a policy transforms the tagging of an AisPacket
 */
@Immutable
public class AisPacketTaggingTransformer implements IAisPacketTransformer {

    /**
     * Policy used when tagging a packet
     */
    public enum Policy {
        /**
         * Add comment block before current packet with possibly tags not already in the tagging
         */
        PREPEND_MISSING,
        /**
         * Remove current tagging and add new
         */
        REPLACE,
        /**
         * Make new tagging as a merge of current and given tagging, overriding duplicate tags already in the current tagging
         */
        MERGE_OVERRIDE,
        /**
         * Make new tagging as a merge of current and given tagging, preserving duplicate tags already in the current tagging
         */
        MERGE_PRESERVE;
    }

    private final Policy policy;
    private final AisPacketTagging tagging;

    /**
     * Constructor taking policy and the tagging to be used
     * 
     * @param policy
     * @param tagging
     */
    public AisPacketTaggingTransformer(Policy policy, AisPacketTagging tagging) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(tagging);
        this.policy = policy;
        this.tagging = tagging;
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        switch (policy) {
        case PREPEND_MISSING:
            return prependTransform(packet);
        case REPLACE:
            return replaceTransform(packet);
        case MERGE_OVERRIDE:
            return mergeOverrideTransform(packet);
        case MERGE_PRESERVE:
            return mergePreserveTransform(packet);
        default:
            throw new IllegalArgumentException("Policy not implemented yet");
        }
    }
    
    private AisPacket mergePreserveTransform(AisPacket packet) {
        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return null;
        }
        CommentBlock cb = vdm.getCommentBlock();
        if (cb == null) {
            cb = new CommentBlock();
        }
        tagging.getCommentBlockPreserve(cb);
        
        String cbStr = cb.encode();
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), false), "\r\n");
        return newPacket(packet, cbStr + "\r\n" + sentences);
    }

    private AisPacket mergeOverrideTransform(AisPacket packet) {
        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return null;
        }
        CommentBlock cb = vdm.getCommentBlock();
        if (cb == null) {
            cb = new CommentBlock();
        }
        tagging.getCommentBlock(cb);
        
        String cbStr = cb.encode();
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), false), "\r\n");
        return newPacket(packet, cbStr + "\r\n" + sentences);
    }

    private AisPacket replaceTransform(AisPacket packet) {
        AisPacketTagging newTagging = new AisPacketTagging(tagging);
        newTagging.setTimestamp(packet.getTimestamp());
        
        String cb = newTagging.getCommentBlock().encode();
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), true), "\r\n");        
        return newPacket(packet, cb + "\r\n" + sentences);
    }
    
    private AisPacket prependTransform(AisPacket packet) {
        // What is missing
        AisPacketTagging addedTagging = AisPacketTagging.parse(packet).mergeMissing(tagging);
        if (addedTagging.isEmpty()) {
            return packet;
        }
        CommentBlock cb = addedTagging.getCommentBlock();
        String newCb = cb.encode();
        return newPacket(packet, newCb + "\r\n" + packet.getStringMessage());
    }
    
    
    /**
     * Remove any thing else than sentences (and proprietary is chosen)
     * @param rawPacket
     * @param removeProprietary
     * @return
     */
    public static List<String> cropSentences(List<String> rawLines, boolean removeProprietary) {
        List<String> croppedLines = new ArrayList<>();
        for (String line : rawLines) {
            int start = line.indexOf('!');
            if (start < 0) {
                if (removeProprietary) {
                    continue;
                }
                start = line.indexOf('$');
            }
            if (start < 0) {
                // Not a sentence line
                continue;
            }
            int end = line.indexOf('*', start);
            if (end < 0) {
                // Not a valid sentence line
                continue;
            }
            end += 3;
            if (end > line.length()) {
                // Not a valid sentence line
                continue;
            }
            croppedLines.add(line.substring(start, end));
        }
        return croppedLines;
    }
    
    private AisPacket newPacket(AisPacket oldPacket, String newRawMessage) {
        return new AisPacket(newRawMessage, oldPacket.getReceiveTimestamp());
    }

}
