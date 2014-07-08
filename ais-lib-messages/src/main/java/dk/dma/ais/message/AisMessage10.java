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
 * Request for UTC/Date information from an AIS base station.
 */
public class AisMessage10 extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Not used. Should be set to zero. Reserved for future use.
     */
    private int spare1; // 2 bits

    /**
     * Request destination MMSI.
     */
    protected int destination; // 30 bit: MMSI number

    /**
     * Not used. Should be set to zero. Reserved for future use.
     */
    private int spare2; // 2 bits

    public AisMessage10() {
        super(10);
    }

    public AisMessage10(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse(vdm.getBinArray());
    }

    public void parse(BinArray binArray) throws AisMessageException, SixbitException {
        BinArray sixbit = vdm.getBinArray();
        if (sixbit.getLength() != 72) {
            throw new AisMessageException("Message 10 wrong length " + sixbit.getLength());
        }

        super.parse(binArray);

        this.spare1 = (int) binArray.getVal(2);
        this.destination = (int) binArray.getVal(30);
        this.spare2 = (int) binArray.getVal(2);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(this.spare1, 2);
        encoder.addVal(this.destination, 30);
        encoder.addVal(this.spare2, 2);
        return encoder;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", spare1=");
        builder.append(spare1);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", spare2=");
        builder.append(spare2);
        builder.append("]");
        return builder.toString();
    }

    public int getSpare1() {
        return spare1;
    }

    public void setSpare1(int spare1) {
        this.spare1 = spare1;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getSpare2() {
        return spare2;
    }

    public void setSpare2(int spare2) {
        this.spare2 = spare2;
    }

}
