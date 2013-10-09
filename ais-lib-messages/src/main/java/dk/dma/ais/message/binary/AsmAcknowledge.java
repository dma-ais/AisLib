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
package dk.dma.ais.message.binary;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;

/**
 * Application acknowledge ASM
 */
public class AsmAcknowledge extends AisApplicationMessage {

    /**
     * DAC code of received FM
     * 
     * Recommended to be spare
     */
    private int receivedDac; // 10 bits
    /**
     * FI code of received FM
     */
    private int receivedFi; // 6 bits
    /**
     * Sequence number in the message being acknowledged as properly received 0 = default (no sequence number) 1-2 047 =
     * sequence number of received FM
     */
    private int textSequenceNum; // 11 bits
    /**
     * 0 = received but AI not available 1 = AI available
     */
    private int aiAvailable; // 1 bit
    /**
     * 0 = unable to respond 1 = reception acknowledged 2 = response to follow 3 = able to respond but currently
     * inhibited 4-7 = spare for future use
     */
    private int aiResponse; // 3 bits
    private int spare; // 49 bits

    public AsmAcknowledge() {
        super(1, 5);
    }

    public AsmAcknowledge(BinArray binArray) throws SixbitException {
        super(1, 5, binArray);
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.receivedDac = (int) binArray.getVal(10);
        this.receivedFi = (int) binArray.getVal(6);
        this.textSequenceNum = (int) binArray.getVal(11);
        this.aiAvailable = (int) binArray.getVal(1);
        this.aiResponse = (int) binArray.getVal(3);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(receivedDac, 10);
        encoder.addVal(receivedFi, 6);
        encoder.addVal(textSequenceNum, 11);
        encoder.addVal(aiAvailable, 1);
        encoder.addVal(aiResponse, 3);
        encoder.addVal(0, 49);
        return encoder;
    }

    public int getReceivedDac() {
        return receivedDac;
    }

    public void setReceivedDac(int receivedDac) {
        this.receivedDac = receivedDac;
    }

    public int getReceivedFi() {
        return receivedFi;
    }

    public void setReceivedFi(int receivedFi) {
        this.receivedFi = receivedFi;
    }

    public int getTextSequenceNum() {
        return textSequenceNum;
    }

    public void setTextSequenceNum(int textSequenceNum) {
        this.textSequenceNum = textSequenceNum;
    }

    public int getAiAvailable() {
        return aiAvailable;
    }

    public void setAiAvailable(int aiAvailable) {
        this.aiAvailable = aiAvailable;
    }

    public int getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(int aiResponse) {
        this.aiResponse = aiResponse;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", aiAvailable=");
        builder.append(aiAvailable);
        builder.append(", aiResponse=");
        builder.append(aiResponse);
        builder.append(", receivedDac=");
        builder.append(receivedDac);
        builder.append(", receivedFi=");
        builder.append(receivedFi);
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", textSequenceNum=");
        builder.append(textSequenceNum);
        builder.append("]");
        return builder.toString();
    }

}
