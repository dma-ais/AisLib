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
 * Request for UTC/Date information from an AIS base station.
 */
public class AisMessage10 extends AisMessage {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Not used. Should be set to zero. Reserved for future use.
	 */
	private int spare1 = 0; // 2 bits

	/**
	 * Request destination MMSI.
	 */
	protected int destination; // 30 bit: MMSI number
	
	/**
	 * Not used. Should be set to zero. Reserved for future use.
	 */
	private int spare2 = 0; // 2 bits

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