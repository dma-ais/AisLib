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
 * AIS message 3
 * 
 * Special position report, response to interrogation;(Class A shipborne mobile equipment)
 * 
 * This class handles the content of an AIS class A transponders general position report as defined by ITU-R M.1371-4.
 * 
 * Generally the position report is handled in the super class but there are some ITDMA specific purposes of this class.
 * 
 */
public class AisMessage3 extends AisPositionMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int slotIncrement; // 13 bits : ITDMA Slot Increment
    private int numSlots; // 3 bits : ITDMA Number of Slots
    private int keep; // 1 bit : ITDMA Keep Flag

    public AisMessage3(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        super.parse(binArray);
        this.slotIncrement = (int) binArray.getVal(13);
        this.numSlots = (int) binArray.getVal(3);
        this.keep = (int) binArray.getVal(1);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(slotIncrement, 13);
        encoder.addVal(numSlots, 3);
        encoder.addVal(keep, 1);
        return encoder;
    }

    public int getSlotIncrement() {
        return slotIncrement;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public int getKeep() {
        return keep;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", keep=");
        builder.append(keep);
        builder.append(", numSlots=");
        builder.append(numSlots);
        builder.append(", slotIncrement=");
        builder.append(slotIncrement);
        builder.append("]");
        return builder.toString();
    }

}
