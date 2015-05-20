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

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;
import net.jcip.annotations.Immutable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

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
         * Make new tagging as a merge of current and given tagging, overriding duplicate tags already in the current
         * tagging
         */
        MERGE_OVERRIDE,
        /**
         * Make new tagging as a merge of current and given tagging, preserving duplicate tags already in the current
         * tagging
         */
        MERGE_PRESERVE;

        public static Policy fromString(String str) {
            if (str.equalsIgnoreCase("PREPEND_MISSING")) {
                return PREPEND_MISSING;
            } else if (str.equalsIgnoreCase("REPLACE")) {
                return REPLACE;
            } else if (str.equalsIgnoreCase("MERGE_OVERRIDE")) {
                return MERGE_OVERRIDE;
            } else if (str.equalsIgnoreCase("MERGE_PRESERVE")) {
                return MERGE_PRESERVE;
            } else {
                throw new IllegalArgumentException("Unknow tagging policy: " + str);
            }
        }
    }

    private final Policy policy;
    private final AisPacketTags tagging;
    private final Map<String, String> extraTags = new HashMap<>();

    /**
     * Constructor taking policy and the tagging to be used
     * 
     * @param policy
     * @param tagging
     */
    public AisPacketTaggingTransformer(Policy policy, AisPacketTags tagging) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(tagging);
        this.policy = policy;
        this.tagging = tagging;
    }

    /**
     * Get map of optional extra tags to be included in the tagging
     * 
     * @return map
     */
    public Map<String, String> getExtraTags() {
        return extraTags;
    }

    public void addExtraTags(Map<String, String> tags) {
        extraTags.putAll(tags);
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
        // Add extra tags
        addExtraTags(cb, packet, false);
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), false), "\r\n");
        if (!cb.isEmpty()) {
            sentences = cb.encode() + "\r\n" + sentences;
        }
        return newPacket(packet, sentences);
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
        // Add extra tags
        addExtraTags(cb, packet, true);
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), false), "\r\n");
        if (!cb.isEmpty()) {
            sentences = cb.encode() + "\r\n" + sentences;
        }
        return newPacket(packet, sentences);
    }

    private AisPacket replaceTransform(AisPacket packet) {
        AisPacketTags newTagging = new AisPacketTags(tagging);
        newTagging.setTimestamp(packet.getTimestamp());
        CommentBlock cb = newTagging.getCommentBlock();
        // Add extra tags
        addExtraTags(cb, packet, true);
        String sentences = StringUtils.join(cropSentences(packet.getStringMessageLines(), true), "\r\n");
        if (!cb.isEmpty()) {
            sentences = cb.encode() + "\r\n" + sentences;
        }
        return newPacket(packet, sentences);
    }

    private AisPacket prependTransform(AisPacket packet) {
        // What is missing
        AisPacketTags addedTagging = packet.getTags().mergeMissing(tagging);
        CommentBlock cb = addedTagging.getCommentBlock();
        // Add extra tags
        addExtraTags(cb, packet, false);
        // Only make new packet if comment block to prepend
        if (cb.isEmpty()) {
            return packet;
        }
        String newCb = cb.encode();
        return newPacket(packet, newCb + "\r\n" + packet.getStringMessage());
    }

    /**
     * Add extra tags to comment block
     * 
     * @param cb
     * @param override
     *            existing
     */
    private void addExtraTags(CommentBlock cb, AisPacket packet, boolean override) {
        CommentBlock currentCb = null;
        Vdm vdm = packet.getVdm();
        if (vdm != null) {
            currentCb = vdm.getCommentBlock();
        }
        for (Entry<String, String> entry : extraTags.entrySet()) {
            if (override || currentCb == null || !currentCb.contains(entry.getKey())) {
                cb.addString(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Remove any thing else than sentences (and proprietary is chosen)
     * 
     * @param rawPacket
     * @param removeProprietary
     * @return
     */
    public static List<String> cropSentences(List<String> rawLines, boolean removeProprietary) {
        List<String> croppedLines = new ArrayList<>();
        for (String line : rawLines) {
            int start = line.indexOf('!');
            if (start < 0) {
                start = line.indexOf('$');
                if (removeProprietary && start >= 0 && line.indexOf("$P") >= 0) {
                    continue;
                }
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
        return AisPacket.from(newRawMessage);
    }

}
