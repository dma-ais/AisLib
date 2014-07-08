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
package dk.dma.ais.reader;

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessage14;
import dk.dma.ais.sentence.Abm;
import dk.dma.ais.sentence.Bbm;
import dk.dma.ais.sentence.SendSentence;

/**
 * Class to represent an AIS send request. A send request is defined by AIS message, sequence number and a destination
 * that is zero for broadcasting.
 */
public class SendRequest {

    private AisMessage aisMessage;
    private List<String> prefixSentences = new ArrayList<>();
    private int sequence;
    private int destination;
    private String talker;

    public SendRequest(AisMessage aisMessage, int sequence, int destination) {
        this.aisMessage = aisMessage;
        this.sequence = sequence;
        this.destination = destination;
    }

    public SendRequest(AisMessage aisMessage, int sequence) {
        this(aisMessage, sequence, 0);
    }

    /**
     * Generate sentences to send. ABM or BBM. c
     * 
     * @return list of actual sentences
     */
    public String[] createSentences() throws SendException {
        SendSentence sendSentence = null;
        // Determine if broadcast or addressed
        switch (aisMessage.getMsgId()) {
        case 6:
        case 7:
        case 12:
        case 13:
            Abm abm = new Abm();
            abm.setDestination(destination);
            sendSentence = abm;
            break;
        case 8:
        case 14:
            sendSentence = new Bbm();
            break;
        default:
            throw new SendException("AIS message " + aisMessage.getMsgId() + " cannot be used for sending");
        }

        // Set sequence
        sendSentence.setSequence(sequence);

        if (talker != null) {
            sendSentence.setTalker(talker);
        }

        try {
            // Handle binary data
            if (aisMessage instanceof AisBinaryMessage) {
                sendSentence.setBinaryData((AisBinaryMessage) aisMessage);
            }
            // Handle text messages
            else if (aisMessage.getMsgId() == 12) {
                sendSentence.setTextData((AisMessage12) aisMessage);
            } else if (aisMessage.getMsgId() == 14) {
                sendSentence.setTextData((AisMessage14) aisMessage);
            }
            // TODO Handle 7 and 13
        } catch (SixbitException e) {
            throw new SendException("Failed to create send sentence: " + e.getMessage());
        }

        // Split into sentences
        SendSentence[] sentences = sendSentence.split();
        String[] encodedSentences = new String[sentences.length + prefixSentences.size()];
        for (int i = 0; i < prefixSentences.size(); i++) {
            encodedSentences[i] = prefixSentences.get(i);
        }
        for (int i = 0; i < sentences.length; i++) {
            encodedSentences[i + prefixSentences.size()] = sentences[i].getEncoded();
        }

        return encodedSentences;
    }

    public AisMessage getAisMessage() {
        return aisMessage;
    }

    public void setAisMessage(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void addPrefixSentence(String sentence) {
        this.prefixSentences.add(sentence);
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SendRequest [aisMessage=");
        builder.append(aisMessage);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", sequence=");
        builder.append(sequence);
        builder.append("]");
        return builder.toString();
    }

}
