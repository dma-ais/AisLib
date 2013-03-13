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

import java.util.Objects;

import net.jcip.annotations.Immutable;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.sentence.CommentBlock;

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
        default:
            throw new IllegalArgumentException("Policy not implemented yet");
        }
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
    
    private AisPacket newPacket(AisPacket oldPacket, String newRawMessage) {
        return new AisPacket(newRawMessage, oldPacket.getReceiveTimestamp());
    }

}
