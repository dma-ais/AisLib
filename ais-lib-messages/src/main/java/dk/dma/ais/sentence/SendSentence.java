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
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessage14;

/**
 * Abstract base class for ABM and BBM sentences
 */
public abstract class SendSentence extends EncapsulatedSentence {

    /**
     * The number of six bit characters to allow in each message part. Number based on the maximum size of resulting
     * sentence part which may not exceed 80 characters.
     */
    private static final int DATA_SENTENCE_MAX_LENGTH = 47;

    protected void encode() {
        super.encode();
        encodedFields.add(5, Integer.toString(msgId));
    }

    /**
     * Set binary content from binary application specific message
     *
     * @param msg the msg
     * @throws SixbitException the sixbit exception
     */
    public void setBinaryData(AisBinaryMessage msg) throws SixbitException {
        this.msgId = msg.getMsgId();
        SixbitEncoder encoder = msg.getBinaryData();
        this.setSixbitString(encoder.encode());
        this.setPadBits(encoder.getPadBits());
    }

    /**
     * Set binary content from AIS message 12
     *
     * @param msg the msg
     * @throws SixbitException the sixbit exception
     */
    public void setTextData(AisMessage12 msg) throws SixbitException {
        this.msgId = msg.getMsgId();
        setText(msg.getMessage());
    }

    /**
     * Set binary content from AIS message 14
     *
     * @param msg the msg
     * @throws SixbitException the sixbit exception
     */
    public void setTextData(AisMessage14 msg) throws SixbitException {
        this.msgId = msg.getMsgId();
        setText(msg.getMessage());
    }

    /**
     * Set the binary content from string of text
     * 
     * @param str
     * @throws SixbitException
     */
    private void setText(String str) throws SixbitException {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addString(str);
        this.setSixbitString(encoder.encode());
        this.setPadBits(encoder.getPadBits());
    }

    /**
     * Split sentence to multiple sentences to agree with the 80 character max
     *
     * @return array of sentences
     */
    public SendSentence[] split() {
        // Make sure there is a sequence number
        if (sequence == null) {
            sequence = 0;
        }

        int sentenceCount = sixbitString.length() / DATA_SENTENCE_MAX_LENGTH + 1;
        SendSentence[] sentences = new SendSentence[sentenceCount];
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

            SendSentence sendSentence;
            if (this instanceof Abm) {
                Abm abm = new Abm();
                abm.setDestination(((Abm) this).getDestination());
                sendSentence = abm;
            } else {
                sendSentence = new Bbm();
            }

            sendSentence.setMsgId(msgId);
            sendSentence.setSequence(sequence);
            sendSentence.setTotal(sentenceCount);
            sendSentence.setNum(i + 1);
            sendSentence.setSixbitString(partEncoded);
            sendSentence.setPadBits(partPadBits);
            sendSentence.setChannel(channel);
            sendSentence.setTalker(talker);

            sentences[i] = sendSentence;
        }

        return sentences;
    }

}
