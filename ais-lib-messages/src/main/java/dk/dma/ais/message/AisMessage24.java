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
 * AIS message 24
 * 
 * Static data report as defined by ITU-R M.1371-4
 * 
 */
public class AisMessage24 extends AisStaticCommon {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Identifier for the message part number
     */
    int partNumber; // 2 bits 0 - Part A 1 - Part B

    /*
     * Part B
     */

    /**
     * Unique identification of the Unit by a number as defined by the manufacturer (option; “@@@@@@@” = not available =
     * default)
     */
    String vendorId; // 7x6 (42) bits

    /**
     * Spare bits
     */
    int spare; // 6 bits

    public AisMessage24() {
        super(24);
    }

    public AisMessage24(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        if (binArray.getLength() < 160) {
            throw new AisMessageException("Message 24 wrong length " + binArray.getLength());
        }

        super.parse(binArray);

        this.partNumber = (int) binArray.getVal(2);

        // Handle part A
        if (partNumber == 0) {
            this.name = binArray.getString(20);
            return;
        }

        if (binArray.getLength() < 168) {
            throw new AisMessageException("Message 24 (Part B) wrong length " + binArray.getLength());
        }

        // Handle part B
        this.shipType = (int) binArray.getVal(8);
        this.vendorId = binArray.getString(7);
        this.callsign = binArray.getString(7);
        this.dimBow = (int) binArray.getVal(9);
        this.dimStern = (int) binArray.getVal(9);
        this.dimPort = (int) binArray.getVal(6);
        this.dimStarboard = (int) binArray.getVal(6);
        this.spare = (int) binArray.getVal(6);
    }

    @Override
    public SixbitEncoder getEncoded() {
        throw new UnsupportedOperationException();
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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
        builder.append(", partNumber=");
        builder.append(partNumber);
        builder.append(", callsign=");
        builder.append(callsign);
        builder.append(", dimBow=");
        builder.append(dimBow);
        builder.append(", dimPort=");
        builder.append(dimPort);
        builder.append(", dimStarboard=");
        builder.append(dimStarboard);
        builder.append(", dimStern=");
        builder.append(dimStern);
        builder.append(", name=");
        builder.append(name);
        builder.append(", shipType=");
        builder.append(shipType);
        builder.append(", spare=");
        builder.append(spare);
        builder.append("]");
        return builder.toString();
    }

}
