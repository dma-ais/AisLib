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
package dk.dma.ais.message;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.Vdm;

/**
 * Addressed safety related message as defined by ITU-R M.1371-4
 */
public class AisMessage12 extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int seqNum; // 2 bits: sequence number
    private long destination; // 30 bits: Destination MMSI
    private int retransmit; // 1 bit: Retransmit flag
    private int spare; // 1 bit: Spare
    private String message; // Max 936 bit - 156 characters

    public AisMessage12() {
        super(12);
    }

    public AisMessage12(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        if (binArray.getLength() < 72 || binArray.getLength() > 1008) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + binArray.getLength());
        }
        super.parse(binArray);
        this.seqNum = (int) binArray.getVal(2);
        this.destination = binArray.getVal(30);
        this.retransmit = (int) binArray.getVal(1);
        this.spare = (int) binArray.getVal(1);
        this.message = binArray.getString((binArray.getLength() - 72) / 6);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(seqNum, 2);
        encoder.addVal(destination, 30);
        encoder.addVal(retransmit, 1);
        encoder.addVal(spare, 1);
        encoder.addString(message);
        return encoder;
    }

    /**
     * Set message from a binary array
     * 
     * @param binArray
     * @throws SixbitException
     */
    public void setMessage(BinArray binArray) throws SixbitException {
        message = binArray.getString(binArray.getLength() / 6);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", message=");
        builder.append(message);
        builder.append(", retransmit=");
        builder.append(retransmit);
        builder.append(", seqNum=");
        builder.append(seqNum);
        builder.append(", spare=");
        builder.append(spare);
        builder.append("]");
        return builder.toString();
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }

    public int getRetransmit() {
        return retransmit;
    }

    public void setRetransmit(int retransmit) {
        this.retransmit = retransmit;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
