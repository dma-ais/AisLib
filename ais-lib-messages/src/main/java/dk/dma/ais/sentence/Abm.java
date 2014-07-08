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
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessage6;

/**
 * Addressed Binary and safety related Message as defined by IEC 61162 Sentence to encapsulate AIS message 6 and 12 for
 * sending
 */
public class Abm extends SendSentence {

    private int destination;

    public Abm() {
        super();
        formatter = "ABM";
        channel = '0';
    }

    public static boolean isAbm(String line) {
        return line.indexOf("!AIABM") >= 0 || line.indexOf("!BSABM") >= 0;
    }

    /**
     * Get encoded sentence
     */
    @Override
    public String getEncoded() {
        super.encode();
        // Add destination
        encodedFields.add(4, Integer.toString(destination));
        return finalEncode();
    }
    
    /**
     * Helper method parsing line to SentenceLine and passing to parse
     * @param line
     * @return
     * @throws SentenceException
     * @throws SixbitException 
     */
    public int parse(String line) throws SentenceException, SixbitException {
        return parse(new SentenceLine(line));
    }

    /**
     * Implemented parse method. See {@link EncapsulatedSentence}
     * 
     * @throws SentenceException
     * @throws SixbitException
     */
    @Override
    public int parse(SentenceLine sl) throws SentenceException, SixbitException {
        // Do common parsing
        super.baseParse(sl);

        // Check ABM
        if (!this.formatter.equals("ABM")) {
            throw new SentenceException("Not ABM sentence");
        }

        // Check that there at least 9 fields
        if (sl.getFields().size() < 9) {
            throw new SentenceException("Sentence does not have at least 9 fields");
        }

        this.destination = Integer.parseInt(sl.getFields().get(4));

        // Channel, relaxed may be null
        if (sl.getFields().get(5).length() > 0) {
            this.channel = sl.getFields().get(5).charAt(0);
        } else {
            this.channel = 0;
        }

        // Message id
        this.msgId = Integer.parseInt(sl.getFields().get(6));

        // Padding bits
        int padBits = Sentence.parseInt(sl.getFields().get(8));

        // Six bit field
        this.sixbitString = new StringBuilder(sl.getFields().get(7));
        binArray.appendSixbit(sl.getFields().get(7), padBits);

        if (completePacket) {
            return 0;
        }

        return 1;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public AisMessage getAisMessage(int mmsi, int repeat, int retransmit) throws SentenceException, SixbitException {
        AisMessage aisMessage;
        if (msgId == 12) {
            AisMessage12 msg12 = new AisMessage12();
            msg12.setDestination(getDestination());
            msg12.setUserId(mmsi);
            msg12.setSeqNum(getSequence());
            msg12.setRepeat(repeat);
            msg12.setRetransmit(retransmit);
            msg12.setMessage(binArray);
            aisMessage = msg12;
        } else if (msgId == 6) {
            AisMessage6 msg6 = new AisMessage6();
            msg6.setSeqNum(getSequence());
            msg6.setDestination(getDestination());
            msg6.setUserId(mmsi);
            msg6.setBinary(binArray);
            aisMessage = msg6;
        } else {
            throw new SentenceException("ABM can only contain AIS message 6 or 12");
        }

        return aisMessage;
    }

    /**
     * Make a single VDM from this ABM
     * 
     * @param mmsi
     * @param repeat
     * @param retransmit
     * @return
     * @throws SixbitException
     * @throws SentenceException
     */
    public Vdm makeVdm(int mmsi, int repeat, int retransmit) throws SixbitException, SentenceException {
        AisMessage aisMessage = getAisMessage(mmsi, repeat, retransmit);

        Vdm vdm = new Vdm();
        vdm.setMsgId(getMsgId());
        vdm.setMessageData(aisMessage);
        vdm.setSequence(getSequence());
        vdm.setChannel(getChannel());

        return vdm;
    }

}
