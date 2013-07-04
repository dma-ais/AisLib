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
 * Safety related broadcast message as defined by ITU-R M.1371-4
 */
public class AisMessage14 extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int spare; // 2 bit: Spare
    private String message; // Max 968 bit - 161 characters

    public AisMessage14() {
        super(14);
    }

    public AisMessage14(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        if (binArray.getLength() < 40 || binArray.getLength() > 1008) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + binArray.getLength());
        }
        super.parse(binArray);
        this.spare = (int) binArray.getVal(2);
        this.message = binArray.getString((binArray.getLength() - 40) / 6);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(spare, 2);
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
        message = binArray.getString(binArray.length() / 6);
    }

    public int getSpare() {
        return spare;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", message=");
        builder.append(message);
        builder.append(", spare=");
        builder.append(spare);
        builder.append("]");
        return builder.toString();
    }

}
