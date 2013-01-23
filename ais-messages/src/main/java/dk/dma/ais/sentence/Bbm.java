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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage14;
import dk.dma.ais.message.AisMessage8;

/**
 * Broadcast Binary and safety related Message as defined by IEC 61162 Sentence to encapsulate AIS message 8 and 14 for
 * sending
 */
public class Bbm extends SendSentence {

    public Bbm() {
        super();
        formatter = "BBM";
        channel = '0';
    }

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
     * Implemented parse method. See {@link EncapsulatedSentence}
     */
    @Override
    public int parse(String line) throws SentenceException, SixbitException {

        // Do common parsing
        super.baseParse(line);

        // Check VDM / VDO
        if (!this.formatter.equals("BBM")) {
            throw new SentenceException("Not BBM sentence");
        }

        // Check that there at least 8 fields
        if (fields.length < 9) {
            throw new SentenceException("Sentence does not have at least 8 fields");
        }

        // Message id
        this.msgId = Integer.parseInt(fields[5]);

        // Padding bits
        int padBits = Sentence.parseInt(fields[7]);

        // Six bit field
        this.sixbitString += fields[6];
        binArray.appendSixbit(fields[6], padBits);

        if (completePacket) {
            return 0;
        }

        return 1;
    }

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
     * @param mmsi
     * @param repeat
     * @return
     * @throws SixbitException
     * @throws SentenceException
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
