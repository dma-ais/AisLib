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
 * AIS message 7
 * 
 * Binary acknowledge message as defined by ITU-R M.1371-4
 * 
 */
public class AisMessage7 extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int spare; // 2 bits: Not used. Should be set to zero. Reserved
                           // for future use
    private long dest1; // 30 bits: MMSI number of first destination of this ACK
    private int seq1; // 2 bits: Sequence number of message to be acknowledged;
                      // 0-3
    private long dest2; // 30 bits: MMSI number of second destination of this
                        // ACK
    private int seq2; // 2 bits: Sequence number of message to be acknowledged;
                      // 0-3
    private long dest3; // 30 bits: MMSI number of third destination of this ACK
    private int seq3; // 2 bits: Sequence number of message to be acknowledged;
                      // 0-3
    private long dest4; // 30 bits: MMSI number of fourth destination of this
                        // ACK
    private int seq4; // 2 bits: Sequence number of message to be acknowledged;
                      // 0-3

    public AisMessage7() {
        super(7);
    }

    public AisMessage7(int num) {
        super(num);
    }

    public AisMessage7(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray sixbit = vdm.getBinArray();
        if (sixbit.getLength() < 72 || sixbit.getLength() > 168) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + sixbit.getLength());
        }
        super.parse(sixbit);
        this.spare = (int) sixbit.getVal(2);
        this.dest1 = sixbit.getVal(30);
        this.seq1 = (int) sixbit.getVal(2);
        if (!sixbit.hasMoreBits()) {
            return;
        }
        this.dest2 = sixbit.getVal(30);
        this.seq2 = (int) sixbit.getVal(2);
        if (!sixbit.hasMoreBits()) {
            return;
        }
        this.dest3 = sixbit.getVal(30);
        this.seq3 = (int) sixbit.getVal(2);
        if (!sixbit.hasMoreBits()) {
            return;
        }
        this.dest4 = sixbit.getVal(30);
        this.seq4 = (int) sixbit.getVal(2);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(spare, 2);
        encoder.addVal(dest1, 30);
        encoder.addVal(seq1, 2);
        encoder.addVal(dest2, 30);
        encoder.addVal(seq2, 2);
        encoder.addVal(dest3, 30);
        encoder.addVal(seq3, 2);
        encoder.addVal(dest4, 30);
        encoder.addVal(seq4, 2);
        return encoder;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public long getDest1() {
        return dest1;
    }

    public void setDest1(long dest1) {
        this.dest1 = dest1;
    }

    public int getSeq1() {
        return seq1;
    }

    public void setSeq1(int seq1) {
        this.seq1 = seq1;
    }

    public long getDest2() {
        return dest2;
    }

    public void setDest2(long dest2) {
        this.dest2 = dest2;
    }

    public int getSeq2() {
        return seq2;
    }

    public void setSeq2(int seq2) {
        this.seq2 = seq2;
    }

    public long getDest3() {
        return dest3;
    }

    public void setDest3(long dest3) {
        this.dest3 = dest3;
    }

    public int getSeq3() {
        return seq3;
    }

    public void setSeq3(int seq3) {
        this.seq3 = seq3;
    }

    public long getDest4() {
        return dest4;
    }

    public void setDest4(long dest4) {
        this.dest4 = dest4;
    }

    public int getSeq4() {
        return seq4;
    }

    public void setSeq4(int seq4) {
        this.seq4 = seq4;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", dest1=");
        builder.append(dest1);
        builder.append(", seq1=");
        builder.append(seq1);
        builder.append(", dest2=");
        builder.append(dest2);
        builder.append(", seq2=");
        builder.append(seq2);
        builder.append(", dest3=");
        builder.append(dest3);
        builder.append(", seq3=");
        builder.append(seq3);
        builder.append(", dest4=");
        builder.append(dest4);
        builder.append(", seq4=");
        builder.append(seq4);
        builder.append("]");
        return builder.toString();
    }

}
