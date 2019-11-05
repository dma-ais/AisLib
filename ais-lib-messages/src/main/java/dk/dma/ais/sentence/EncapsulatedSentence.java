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

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;

/**
 * Abstract base class for encapsulating sentences VDM, ABM and BBM
 */
public abstract class EncapsulatedSentence extends Sentence {

    /**
     * The Msg id.
     */
    protected int msgId;
    /**
     * The Total.
     */
    protected int total;
    /**
     * The Sequence.
     */
    protected Integer sequence;
    private int lastSeq = -1;
    /**
     * The Num.
     */
    protected int num;
    /**
     * The Channel.
     */
    protected Character channel;
    /**
     * The Bin array.
     */
    protected BinArray binArray = new BinArray();
    /**
     * The Complete packet.
     */
    protected boolean completePacket;
    /**
     * The Sixbit string.
     */
    protected StringBuilder sixbitString = new StringBuilder();
    /**
     * The Pad bits.
     */
    protected int padBits;

    /**
     * Base parse method to be used by extending classes
     */
    @Override
    protected void baseParse(SentenceLine sl) throws SentenceException {
        super.baseParse(sl);

        // Should at least have four fields
        if (sl.getFields().size() < 4) {
            throw new SentenceException("Sentence have less than four fields");
        }

        // Get sentence count properties
        int thisTotal = Sentence.parseInt(sl.getFields().get(1));
        int thisNum = Sentence.parseInt(sl.getFields().get(2));
        int thisSeq = 0;
        if (sl.getFields().get(3).length() > 0) {
            // null sequence is not fatal
            thisSeq = Sentence.parseInt(sl.getFields().get(3));
        }

        if (lastSeq < 0) {
            // New group of sentences
            total = thisTotal;
            num = thisNum;
            sequence = thisSeq;
            lastSeq = thisSeq;
            if (num != 1 || num > total) {
                throw new SentenceException("Out of sequence sentence: " + sl.getLine());
            }
        } else {
            // Sentence part of existing group
            if (total != thisTotal || thisNum != num + 1 || thisSeq != lastSeq) {
                throw new SentenceException("Out of sequence sentence: " + sl.getLine());
            }
            num = thisNum;
        }

        // Are we done
        if (num == total) {
            completePacket = true;
            lastSeq = -1;
        }

    }

    /**
     * Encode method to be used by extending classes
     */
    protected void encode() {
        super.encode();
        encodedFields.add(Integer.toString(total));
        encodedFields.add(Integer.toString(num));
        String seq = sequence == null ? "" : Integer.toString(sequence);
        encodedFields.add(seq);
        encodedFields.add(channel != null ? Character.toString(channel) : "");
        encodedFields.add(sixbitString.toString());
        encodedFields.add(Integer.toString(padBits));
    }

    /**
     * Gets msg id.
     *
     * @return the msg id
     */
    public int getMsgId() {
        return msgId;
    }

    /**
     * Sets msg id.
     *
     * @param msgId the msg id
     */
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    /**
     * Gets sequence.
     *
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Sets sequence.
     *
     * @param sequence the sequence
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Get total number of actual sentences
     *
     * @return number of sentences
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Get sentence number
     *
     * @return num
     */
    public int getNum() {
        return num;
    }

    /**
     * Sets num.
     *
     * @param num the num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public Character getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(Character channel) {
        this.channel = channel;
    }

    /**
     * Get binary encapsulated data
     *
     * @return bin array
     */
    public BinArray getBinArray() {
        return binArray;
    }

    /**
     * Set binary encapsulated data
     *
     * @param binArray the bin array
     */
    public void setBinArray(BinArray binArray) {
        this.binArray = binArray;
    }

    /**
     * Set binary part and pad bits from encoder
     *
     * @param encoder the encoder
     * @throws SixbitException the sixbit exception
     */
    public void setEncodedMessage(SixbitEncoder encoder) throws SixbitException {
        sixbitString = new StringBuilder(encoder.encode());
        padBits = encoder.getPadBits();
    }

    /**
     * Set the binary encapsulated data from AIS message
     *
     * @param aisMessage the ais message
     * @throws SixbitException the sixbit exception
     */
    public void setMessageData(AisMessage aisMessage) throws SixbitException {
        this.msgId = aisMessage.getMsgId();
        SixbitEncoder encoder = aisMessage.getEncoded();
        sixbitString = new StringBuilder(encoder.encode());
        padBits = encoder.getPadBits();
    }

    /**
     * Gets pad bits.
     *
     * @return the pad bits
     */
    public int getPadBits() {
        return padBits;
    }

    /**
     * Is complete packet boolean.
     *
     * @return is complete packet has been received
     */
    public boolean isCompletePacket() {
        return completePacket;
    }

    /**
     * Set the six bit string of the sentence
     *
     * @param sixbitString the sixbit string
     */
    public void setSixbitString(String sixbitString) {
        this.sixbitString = new StringBuilder(sixbitString);
    }

    /**
     * Sets pad bits.
     *
     * @param padBits the pad bits
     */
    public void setPadBits(int padBits) {
        this.padBits = padBits;
    }

    /**
     * Gets sixbit string.
     *
     * @return the sixbit string
     */
    public String getSixbitString() {
        return sixbitString.toString();
    }

}
