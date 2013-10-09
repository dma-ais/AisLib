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
 * Capability ASM
 */
public class Capability extends AisApplicationMessage {

    private int reqDac; // 10 bits
    private int spare; // 70 bits

    public Capability() {
        super(1, 3);
    }

    public Capability(BinArray binArray) throws SixbitException {
        super(1, 3, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(reqDac, 10);
        encoder.addVal(spare, 70);
        return encoder;
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.reqDac = (int) binArray.getVal(10);
    }

    public int getReqDac() {
        return reqDac;
    }

    public void setReqDac(int reqDac) {
        this.reqDac = reqDac;
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
        builder.append("[Capability: reqDac=");
        builder.append(reqDac);
        builder.append("]");
        return builder.toString();
    }

}
