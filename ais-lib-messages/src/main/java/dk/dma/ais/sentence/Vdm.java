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
package dk.dma.ais.sentence;

import java.util.regex.Pattern;

import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;

/**
 * Class to represent VDM and VDO sentences as defined by IEC 61162
 */
public class Vdm extends EncapsulatedSentence {

    /**
     * The number of six bit characters to allow in each message part. Number based on the maximum size of resulting
     * sentence part which may not exceed 80 characters.
     */
    private static final int DATA_SENTENCE_MAX_LENGTH = 61;

    /**
     * Pattern for recognizing VDM/VDO sentences
     */
    private static final Pattern VDM_PATTERN = Pattern.compile("^.*!..VD(M|O).*$", Pattern.DOTALL);

    /**
     * Determines is this is VDM or VDO
     */
    private boolean ownMessage;

    /**
     * Implemented parse method. See {@link EncapsulatedSentence}
     */
    @Override
    public int parse(String line) throws SentenceException {

        // Do common parsing
        super.baseParse(line);

        // Check VDM / VDO
        if (this.formatter.equals("VDO")) {
            this.ownMessage = true;
        } else {
            if (!this.formatter.equals("VDM")) {
                throw new SentenceException("Not VDM or VDO sentence");
            }
        }

        // Check that there at least 8 fields
        if (fields.length < 8) {
            throw new SentenceException("Sentence does not have at least 8 fields");
        }

        // Channel, relaxed may be null
        if (fields[4].length() > 0) {
            this.channel = fields[4].charAt(0);
        } else {
            this.channel = 0;
        }

        // Padding bits
        int padBits = Sentence.parseInt(fields[6]);

        // Six bit field
        this.sixbitString += fields[5];
        try {
            binArray.appendSixbit(fields[5], padBits);
        } catch (SixbitException e) {
            throw new SentenceException("Invalid sixbit in VDM: " + e.getMessage() + ": " + line);
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
     * @param line
     * @return
     */
    public static boolean isVdm(String line) {
        return VDM_PATTERN.matcher(line).matches();
    }

    /**
     * Determine if VDO instead of VDM
     * 
     * @return
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
     * 
     * If all VDM fields are used, 61 chars are left for encoded AIS message
     * 
     * @param aisMessage
     * @param sequence
     * @return array of sentence parts
     * @throws IllegalArgumentException
     * @throws SixbitException
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
     * @return
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
