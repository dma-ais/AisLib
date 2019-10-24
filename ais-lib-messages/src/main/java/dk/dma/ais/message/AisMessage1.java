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
 * AIS message 1
 * <p>
 * This class handles the content of an AIS class A transponders general position report as defined by ITU-R M.1371-4.
 * <p>
 * Generally the position report is handled in the super class but there are some specific purposes of this class. This
 * is mainly the communication state field, that requires some special attention.
 * <p>
 * The communication state field covers 19 bits and this is general to both message 1, message 2 and message 3. The
 * content is though different for the different communication states applied by the different message types.
 * <p>
 * This message type is using SOTDMA communication state, which is described below.
 * <p>
 * CommState - 19 bits sync state: 2 bits slot timeout: 3 bits sub message: 14 bits
 * <p>
 * The sub message depends upon the value of the slot timeout. The slot timeout is defined to be between 0 and 7 both
 * values included.
 * <p>
 * The sub message is defined as:
 * <p>
 * When slot timeout = 0 - Then the sub message is the slot offset: If the slot time-out value is 0 (zero) then the slot
 * offset should indicate the offset to the slot in which transmission will occur during the next frame. If the slot
 * offset is zero, the slot should be de-allocated after transmission
 * <p>
 * When slot timeout = 1 - Then the sub message is UTC hour and minute: If the station has access to UTC, the hour and
 * minute should be indicated in this sub message. Hour (0-23) should be coded in bits 13 to 9 of the sub message (bit
 * 13 is MSB). Minute (0-59) should be coded in bit 8 to 2 (bit 8 is MSB). Bit 1 and bit 0 are not used.
 * <p>
 * When slot timeout = 2,4,6 - Then the sub message is the slot number: Slot number used for this transmission (between
 * 0 and 2249).
 * <p>
 * When slot timeout = 3,5,7 - Then the sub message is the recieved stations: Number of other stations (not own station)
 * which the station currently is receiving (between 0 and 16383).
 */
public class AisMessage1 extends AisPositionMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * SOTDMA Slot Timeout Specifies frames remaining until a new slot is selected 0 means that this was the last
     * transmission in this slot 1-7 means that 1 to 7 frames respectively are left until slot change
     */
    protected int slotTimeout; // 3 bits

    /**
     * SOTDMA sub-message This field has many purposes see class description for help:
     */
    protected int subMessage; // 14 bits

    /**
     * Instantiates a new Ais message 1.
     */
    public AisMessage1() {
        super(1);
    }

    /**
     * Instantiates a new Ais message 1.
     *
     * @param msgType the msg type
     */
    AisMessage1(int msgType) {
        super(msgType);
    }

    /**
     * Instantiates a new Ais message 1.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage1(Vdm vdm) throws AisMessageException, SixbitException {
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
        super.parse(binArray);
        this.slotTimeout = (int) binArray.getVal(3);
        this.subMessage = (int) binArray.getVal(14);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(slotTimeout, 3);
        encoder.addVal(subMessage, 14);
        return encoder;
    }

    /**
     * Gets slot timeout.
     *
     * @return the slot timeout
     */
    public int getSlotTimeout() {
        return slotTimeout;
    }

    /**
     * Sets slot timeout.
     *
     * @param slotTimeout the slot timeout
     */
    public void setSlotTimeout(int slotTimeout) {
        this.slotTimeout = slotTimeout;
    }

    /**
     * Gets sub message.
     *
     * @return the sub message
     */
    public int getSubMessage() {
        return subMessage;
    }

    /**
     * Sets sub message.
     *
     * @param subMessage the sub message
     */
    public void setSubMessage(int subMessage) {
        this.subMessage = subMessage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", slotTimeout=");
        builder.append(slotTimeout);
        builder.append(", subMessage=");
        builder.append(subMessage);
        builder.append("]");
        return builder.toString();
    }

}
