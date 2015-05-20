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
package dk.dma.ais.packet;

import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.proprietary.ProprietaryFactory;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.ais.sentence.Vdm;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class to parse lines in a stream containing VDM sentences. The class will deliver packets containing complete VDM and
 * associated comment blocks and proprietary tags.
 */
@NotThreadSafe
public class AisPacketParser {

    /** The logger */
    private static final Logger LOG = LoggerFactory.getLogger(AisPacketParser.class);

    private static final int SENTENCE_TRACE_COUNT = 20;

    /** List of the raw lines of the AIS packet. */
    private final ArrayList<String> packetLines = new ArrayList<>();

    private final ArrayDeque<String> sentenceTrace = new ArrayDeque<>(SENTENCE_TRACE_COUNT);

    /** Possible proprietary tags for current VDM. */
    private final ArrayDeque<IProprietaryTag> tags = new ArrayDeque<>();

    /** A received VDO/VDM */
    private Vdm vdm = new Vdm();
    
    /**
     * Sentence line parser
     */
    private SentenceLine sentenceLine = new SentenceLine();

    void newVdm() {
        vdm = new Vdm();
        tags.clear();
        packetLines.clear();
    }

    /**
     * Handle a single line. If a complete packet is assembled the package will be returned. Otherwise null is returned.
     * 
     * @param line
     * @return
     * @throws SentenceException
     */
    public AisPacket readLine(String line) throws SentenceException {
        return readLine(line, false);
    }

    /**
     * If an out of sequence packet is encountered, the parsing will be restarted at the out of sequence packet
     * 
     * @param line
     * @param retry
     * @return
     * @throws SentenceException
     */
    private AisPacket readLine(String line, boolean retry) throws SentenceException {

        if (!retry) {
            // Save line for later trace
            while (sentenceTrace.size() > SENTENCE_TRACE_COUNT) {
                sentenceTrace.removeFirst();
            }
            sentenceTrace.addLast(line);
        }
        
        sentenceLine.parse(line);

        // Ignore everything else than sentences
        if (!sentenceLine.hasSentence()) {
            // Gracefully ignore empty lines
            if (line.length() == 0) {
                newVdm();
                return null;
            }
            // Special case is a single comment without sentence
            if (CommentBlock.hasCommentBlock(line)) {
                packetLines.add(line);
                try {
                    vdm.addSingleCommentBlock(line);
                } catch (SentenceException e) {
                    newVdm();
                    throw new SentenceException(e, sentenceTrace);
                }
                return null;
            } else {
                // Non sentence line
                newVdm();
                throw new SentenceException("Non sentence line in stream: " + line, sentenceTrace);
            }
        }

        // Add line to raw packet
        packetLines.add(line);

        // Check if proprietary line
        if (sentenceLine.isProprietary()) {
            // Try to parse with one of the registered factories in
            // META-INF/services/dk.dma.ais.proprietary.ProprietaryFactory
            IProprietaryTag tag = ProprietaryFactory.parseTag(sentenceLine);
            if (tag != null) {
                tags.add(tag);
            }
            return null;
        }

        // Check if VDM. If not the possible current VDM is broken.
        if (!sentenceLine.isFormatter("VDM", "VDO")) {
            newVdm();
            return null;
        }

        // Parse VDM
        int result;
        try {
            result = vdm.parse(sentenceLine);
        } catch (SentenceException e) {
            newVdm();
            // Do a single retry with the current line. The faulty sentence may be the last, not this one.
            if (!retry) {
                LOG.debug("Discarding current sentence group. New start: " + e.getMessage());
                return readLine(line, true);
            }
            throw new SentenceException(e, sentenceTrace);
        }

        // If not complete package wait for more
        if (result != 0) {
            return null;
        }

        // Complete package have been read

        // Put proprietary tags on vdm
        if (tags.size() > 0) {
            vdm.setTags(new LinkedList<>(tags));
        }

        // Make packet
        AisPacket packet = new AisPacket(vdm, StringUtils.join(packetLines, "\r\n"));

        newVdm();

        return packet;
    }
}
