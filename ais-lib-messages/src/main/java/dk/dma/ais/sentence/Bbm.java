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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage14;
import dk.dma.ais.message.AisMessage8;

/**
 * Broadcast Binary and safety related Message as defined by IEC 61162 Sentence to encapsulate AIS message 8 and 14 for
 * sending
 */
public class Bbm extends SendSentence {

    /**
     * Instantiates a new Bbm.
     */
    public Bbm() {
        formatter = "BBM";
        channel = '0';
    }

    /**
     * Is bbm boolean.
     *
     * @param line the line
     * @return the boolean
     */
    public static boolean isBbm(String line) {
        return line.indexOf("!AIBBM") >= 0 || line.indexOf("!BSBBM") >= 0;
    }

    /**
     * Get encoded sentence
     */
    @Override
    public String getEncoded() {
        super.encode();
        return finalEncode();
    }

    /**
     * Parse int.
     *
     * @param line the line
     * @return the int
     * @throws SentenceException the sentence exception
     * @throws SixbitException   the sixbit exception
     */
    public int parse(String line) throws SentenceException, SixbitException {
        return parse(new SentenceLine(line));
    }

    /**
     * Implemented parse method. See {@link EncapsulatedSentence}
     * @throws SixbitException a SixbitException
     */
    @Override
    public int parse(SentenceLine sl) throws SentenceException, SixbitException {

        // Do common parsing
        super.baseParse(sl);

        // Check VDM / VDO
        if (!this.formatter.equals("BBM")) {
            throw new SentenceException("Not BBM sentence");
        }

        // Check that there at least 8 fields
        if (sl.getFields().size() < 9) {
            throw new SentenceException("Sentence does not have at least 8 fields");
        }

        // Message id
        this.msgId = Integer.parseInt(sl.getFields().get(5));

        // Padding bits
        int padBits = Sentence.parseInt(sl.getFields().get(7));

        // Six bit field
        this.sixbitString.append(sl.getFields().get(6));
        binArray.appendSixbit(sl.getFields().get(6), padBits);

        if (completePacket) {
            return 0;
        }

        return 1;
    }

    /**
     * Gets ais message.
     *
     * @param mmsi   the mmsi
     * @param repeat the repeat
     * @return the ais message
     * @throws SentenceException the sentence exception
     * @throws SixbitException   the sixbit exception
     */
    public AisMessage getAisMessage(int mmsi, int repeat) throws SentenceException, SixbitException {
        AisMessage aisMessage;
        if (msgId == 14) {
            AisMessage14 msg14 = new AisMessage14();
            msg14.setUserId(mmsi);
            msg14.setRepeat(repeat);
            msg14.setMessage(binArray);
            aisMessage = msg14;
        } else if (msgId == 8) {
            AisMessage8 msg8 = new AisMessage8();
            msg8.setRepeat(repeat);
            msg8.setUserId(mmsi);
            msg8.setBinary(binArray);
            aisMessage = msg8;

        } else {
            throw new SentenceException("BBM can only contain AIS message 8 or 14");
        }

        return aisMessage;
    }

    /**
     * Make a single VDM from this BBM
     *
     * @param mmsi   the mmsi
     * @param repeat the repeat
     * @return vdm
     * @throws SixbitException   the sixbit exception
     * @throws SentenceException the sentence exception
     */
    public Vdm makeVdm(int mmsi, int repeat) throws SixbitException, SentenceException {
        AisMessage aisMessage = getAisMessage(mmsi, repeat);
        Vdm vdm = new Vdm();
        vdm.setMsgId(getMsgId());
        vdm.setMessageData(aisMessage);
        vdm.setSequence(getSequence());
        vdm.setChannel(getChannel());
        return vdm;
    }

}
