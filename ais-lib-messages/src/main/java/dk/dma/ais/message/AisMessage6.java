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
 * AIS message 6
 * 
 * Addressed binary message as defined by ITU-R M.1371-4
 * 
 */
public class AisMessage6 extends AisBinaryMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int seqNum; // 2 bits: sequence number
    private long destination; // 30 bits: Destination MMSI
    private int retransmit; // 1 bit: Retransmit flag

    public AisMessage6() {
        super(6);
    }

    public AisMessage6(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray sixbit = vdm.getBinArray();
        if (sixbit.getLength() < 88 || sixbit.getLength() > 1008) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + sixbit.getLength());
        }
        super.parse(sixbit);
        this.seqNum = (int) sixbit.getVal(2);
        this.destination = sixbit.getVal(30);
        this.retransmit = (int) sixbit.getVal(1);
        this.spare = (int) sixbit.getVal(1);
        this.dac = (int) sixbit.getVal(10);
        this.fi = (int) sixbit.getVal(6);
        this.data = sixbit;
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(seqNum, 2);
        encoder.addVal(destination, 30);
        encoder.addVal(retransmit, 1);
        encoder.addVal(spare, 1);
        encoder.addVal(dac, 10);
        encoder.addVal(fi, 6);
        encoder.append(appMessage.getEncoded());
        return encoder;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", retransmit=");
        builder.append(retransmit);
        builder.append(", seqNum=");
        builder.append(seqNum);
        builder.append("]");
        return builder.toString();
    }

}
