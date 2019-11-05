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
package dk.dma.ais.sentence;

import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;

/**
 * Class to represent VDM and VDO sentences as defined by IEC 61162
 */
public class Vdm extends EncapsulatedSentence {

    /**
     * The number of six bit characters to allow in each message part. Number based on the maximum size of resulting sentence part
     * which may not exceed 80 characters.
     */
    private static final int DATA_SENTENCE_MAX_LENGTH = 61;

    /**
     * Determines is this is VDM or VDO
     */
    private boolean ownMessage;

    /**
     * Helper method parsing line to SentenceLine and passing to parse
     *
     * @param line the line
     * @return int
     * @throws SentenceException the sentence exception
     */
    public int parse(String line) throws SentenceException {
        return parse(new SentenceLine(line));
    }

    /**
     * Implemented parse method. See {@link EncapsulatedSentence}
     */
    @Override
    public int parse(SentenceLine sl) throws SentenceException {
        // Do common parsing
        super.baseParse(sl);

        // Check VDM / VDO
        if (sl.isFormatter("VDO")) {
            this.ownMessage = true;
        } else {
            if (!sl.isFormatter("VDM")) {
                throw new SentenceException("Not VDM or VDO sentence");
            }
        }

        // Check that there at least 8 fields
        if (sl.getFields().size() < 8) {
            throw new SentenceException("Sentence does not have at least 8 fields");
        }

        // Channel, relaxed may be null
        if (sl.getFields().get(4).length() > 0) {
            this.channel = sl.getFields().get(4).charAt(0);
        } else {
            this.channel = 0;
        }

        // Padding bits
        int padBits = Sentence.parseInt(sl.getFields().get(6));

        // Six bit field
        this.sixbitString.append(sl.getFields().get(5));
        try {
            binArray.appendSixbit(sl.getFields().get(5), padBits);
        } catch (SixbitException e) {
            throw new SentenceException("Invalid sixbit in VDM: " + e.getMessage() + ": " + sl.getLine());
        }

        // Complete packet?
        if (completePacket) {
            try {
                this.msgId = (int) binArray.getVal(6);
            } catch (SixbitException e) {
                throw new SentenceException("Not enough bits for msgid");
            }
            return 0;
        }

        return 1;
    }

    /**
     * Determine if line seems to contain VDM or VDO sentence
     *
     * @param line the line
     * @return boolean
     */
    public static boolean isVdm(String line) {
        int sentenceStart = line.indexOf('!');
        if (sentenceStart < 0) {
            return false;
        }
        int formatterStart = line.indexOf("VDM");
        if (formatterStart < 0) {
            formatterStart = line.indexOf("VDO");
        }
        return formatterStart == sentenceStart + 3;
    }

    /**
     * Determine if VDO instead of VDM
     *
     * @return boolean
     */
    public boolean isOwnMessage() {
        return ownMessage;
    }

    /**
     * Get encoded sentence as a single line
     */
    @Override
    public String getEncoded() {
        formatter = isOwnMessage() ? "VDO" : "VDM";
        super.encode();
        return finalEncode();
    }

    /**
     * Make max 80 chars length sentences from AIS message given sequence number
     * <p>
     * If all VDM fields are used, 61 chars are left for encoded AIS message
     *
     * @param aisMessage the ais message
     * @param sequence   the sequence
     * @return array of sentence parts
     * @throws SixbitException the sixbit exception
     */
    public static String[] createSentences(AisMessage aisMessage, int sequence) throws SixbitException {
        // Encode the AIS message to get full string
        SixbitEncoder encoder = aisMessage.getEncoded();
        String encoded = encoder.encode();
        int padBits = encoder.getPadBits();

        // Number of sentences necessary
        int sentenceCount = encoded.length() / DATA_SENTENCE_MAX_LENGTH + 1;
        String[] sentences = new String[sentenceCount];

        // Split the string
        for (int i = 0; i < sentenceCount; i++) {
            int start = i * DATA_SENTENCE_MAX_LENGTH;
            int end;
            int partPadBits = 0;
            if (i < sentenceCount - 1) {
                end = start + DATA_SENTENCE_MAX_LENGTH;
            } else {
                end = encoded.length();
                partPadBits = padBits;
            }
            String partEncoded = encoded.substring(start, end);

            Vdm vdm = new Vdm();
            vdm.setMsgId(aisMessage.getMsgId());
            vdm.setTalker("AI");
            vdm.setTotal(sentenceCount);
            vdm.setNum(i + 1);
            vdm.setSequence(sequence);
            vdm.setSixbitString(partEncoded);
            vdm.setPadBits(partPadBits);

            sentences[i] = vdm.getEncoded();
        }

        return sentences;
    }

    /**
     * Split single VDM into possible multiple VDM's to adherne to the 80 character max
     *
     * @return vdm [ ]
     */
    public Vdm[] createSentences() {
        // Make sure there is a sequence number
        if (sequence == null) {
            sequence = 0;
        }

        int sentenceCount = sixbitString.length() / DATA_SENTENCE_MAX_LENGTH + 1;
        Vdm[] sentences = new Vdm[sentenceCount];
        // Split the string
        for (int i = 0; i < sentenceCount; i++) {
            int start = i * DATA_SENTENCE_MAX_LENGTH;
            int end;
            int partPadBits = 0;
            if (i < sentenceCount - 1) {
                end = start + DATA_SENTENCE_MAX_LENGTH;
            } else {
                end = sixbitString.length();
                partPadBits = padBits;
            }
            String partEncoded = sixbitString.substring(start, end);

            Vdm vdm = new Vdm();
            vdm.setMsgId(msgId);
            vdm.setTalker("AI");
            vdm.setTotal(sentenceCount);
            vdm.setNum(i + 1);
            vdm.setSequence(sequence);
            vdm.setSixbitString(partEncoded);
            vdm.setPadBits(partPadBits);
            vdm.setChannel(channel);

            sentences[i] = vdm;
        }

        return sentences;
    }

}
