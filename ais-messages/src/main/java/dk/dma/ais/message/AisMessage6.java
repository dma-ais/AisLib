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
