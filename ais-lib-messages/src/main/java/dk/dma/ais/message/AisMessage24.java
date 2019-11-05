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
 * <p>
 * Static data report as defined by ITU-R M.1371-4
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
     * Type of electronic position fixing device: 0 = undefined (default) 1 = GPS 2 = GLONASS 3 = combined GPS/GLONASS 4
     * = Loran-C 5 = Chayka 6 = integrated navigation system 7 = surveyed 8 = Galileo, 9-14 = not used 15 = internal
     * GNSS
     */
    int posType; // 4 bits

    /**
     * Spare bits
     */
    int spare; // 2 bits

    /**
     * Instantiates a new Ais message 24.
     */
    public AisMessage24() {
        super(24);
    }

    /**
     * Instantiates a new Ais message 24.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage24(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    /**
     * Parse.
     *
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
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
        this.posType = (int) binArray.getVal(4);
        this.spare = (int) binArray.getVal(2);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(partNumber, 2);

        // Part A
        if (partNumber == 0) {
            encoder.addString(name, 20);
            return encoder;
        }

        // Part B
        encoder.addVal(shipType, 8);
        encoder.addString(vendorId, 7);
        encoder.addString(callsign, 7);
        encoder.addVal(dimBow, 9);
        encoder.addVal(dimStern, 9);
        encoder.addVal(dimPort, 6);
        encoder.addVal(dimStarboard, 6);
        encoder.addVal(posType, 4);
        encoder.addVal(spare, 2);
        return encoder;
    }

    /**
     * Gets part number.
     *
     * @return the part number
     */
    public int getPartNumber() {
        return partNumber;
    }

    /**
     * Sets part number.
     *
     * @param partNumber the part number
     */
    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * Gets vendor id.
     *
     * @return the vendor id
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * Sets vendor id.
     *
     * @param vendorId the vendor id
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * Gets pos type.
     *
     * @return the pos type
     */
    public int getPosType() {
        return posType;
    }

    /**
     * Sets pos type.
     *
     * @param posType the pos type
     */
    public void setPosType(int posType) {
        this.posType = posType;
    }

    /**
     * Gets spare.
     *
     * @return the spare
     */
    public int getSpare() {
        return spare;
    }

    /**
     * Sets spare.
     *
     * @param spare the spare
     */
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
        builder.append(", posType=");
        builder.append(posType);
        builder.append(", spare=");
        builder.append(spare);
        builder.append("]");
        return builder.toString();
    }

}
