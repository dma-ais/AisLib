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
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;

/**
 * Transformer that anonymizer
 */
@ThreadSafe
public class AnonymousTransformer implements IAisPacketTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(AnonymousTransformer.class);

    /**
     * Map from MMSI to new anonymous MMSI
     */
    private final ConcurrentHashMap<Integer, Integer> mmsiMap = new ConcurrentHashMap<>();

    /**
     * Counter to keep track of MMSI to anonymous MMSI map
     */
    private volatile int counter;

    @Override
    public AisPacket transform(AisPacket packet) {
        // Get AIS message or discard
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return null;
        }

        // Transformation for all mesasges
        anonymize(message);

        // Transformation for general static data
        if (message instanceof AisStaticCommon) {
            anonymize((AisStaticCommon) message);
        }

        // Transformation for class A static data
        if (message instanceof AisMessage5) {
            anonymize((AisMessage5) message);
        }

        return createPacket(message, packet);
    }

    /**
     * Make a new packet from old packet and
     * 
     * @param message
     * @param packet
     * @return
     */
    private AisPacket createPacket(AisMessage message, AisPacket packet) {
        List<String> lines = new ArrayList<>(4);
        int sequence = message.getVdm().getSequence();

        // Handle binary message
        if (message instanceof AisBinaryMessage) {
            try {
                AisApplicationMessage appMessage = ((AisBinaryMessage) message).getApplicationMessage();
                if (appMessage == null) {
                    return null;
                }
            } catch (SixbitException e) {
                return null;
            }
        }

        String[] sentences;
        try {
            sentences = Vdm.createSentences(message, sequence);
        } catch (UnsupportedOperationException e) {
            LOG.debug("Encoding not supported by message type " + message.getMsgId());
            return null;
        } catch (SixbitException e) {
            LOG.error("Failed to encode AIS message: " + message + ": " + e.getMessage());
            return null;
        }

        // Add proprietary tags (or not?)
        if (message.getTags() != null) {
            for (IProprietaryTag tag : message.getTags()) {
                lines.add(tag.getSentence());
            }
        }

        // Add comment blocks
        CommentBlock cb = message.getVdm().getCommentBlock();
        if (cb != null) {
            lines.add(cb.encode());
        }

        // Add sentences
        for (String sentence : sentences) {
            lines.add(sentence);
        }

        return AisPacket.from(StringUtils.join(lines, "\r\n"), packet.getReceiveTimestamp());
    }

    /**
     * Do transformation basic for all AIS messages
     * 
     * @param message
     */
    private void anonymize(AisMessage message) {
        // Anonymize MMSI
        // Get or set mapping (the incrementation with not guarantee multiple increments
        // but this is not important)
        if (mmsiMap.putIfAbsent(message.getUserId(), counter) == null) {
            counter++;
        }
        int anonId = mmsiMap.get(message.getUserId());
        message.setUserId(anonId);
    }

    /**
     * 
     * @param posMessage
     */
    private void anonymize(AisStaticCommon message) {
        // Change name
        message.setName("SHIP" + message.getUserId());
        // Callsign
        message.setCallsign("C" + message.getUserId());
    }

    private void anonymize(AisMessage5 message) {
        // IMO
        message.setImo(message.getUserId());
        // Dest
        message.setDest("ETERNITY");
    }

}
