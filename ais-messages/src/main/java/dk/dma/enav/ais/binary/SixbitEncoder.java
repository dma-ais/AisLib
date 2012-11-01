/* Copyright (c) 2011 Danish Maritime Safety Administration
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
package dk.dma.enav.ais.binary;

/**
 * Class to encode into a six bit string
 */
public class SixbitEncoder {

    /**
     * The internal representation as binary arrya
     */
    private BinArray binArray = new BinArray();

    /**
     * The number of padding bits
     */
    private int padBits = 0;

    public SixbitEncoder() {}

    /**
     * Add a value using bits number of bits
     * 
     * @param value
     * @param bits
     */
    public void addVal(long value, int bits) {
        binArray.append(value, bits);
    }

    /**
     * Add string
     * 
     * @param str
     */
    public void addString(String str) {
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            if (c >= 64) {
                c -= 64;
            }
            addVal(c, 6);
        }
    }

    /**
     * Add string and a number of spaces to fill length characters
     * 
     * @param str
     * @param length
     */
    public void addString(String str, int length) {
        int i = 0;
        for (; i < str.length(); i++) {
            int c = str.charAt(i);
            if (c >= 64) {
                c -= 64;
            }
            addVal(c, 6);
        }
        for (; i < length; i++) {
            addVal(' ', 6);
        }
    }

    /**
     * Append another encoder
     * 
     * @param encoder
     */
    public void append(SixbitEncoder encoder) {
        binArray.append(encoder.binArray);
    }

    /**
     * Append a binary array
     * 
     * @param ba
     */
    public void append(BinArray ba) {
        binArray.append(ba);
    }

    /**
     * Get encoded six bit string
     * 
     * @return string
     * @throws SixbitException
     */
    public String encode() throws SixbitException {
        StringBuilder buf = new StringBuilder();
        int start = 0;
        int stop = 0;
        while (start < binArray.getLength()) {
            stop = start + 6 - 1;
            if (stop >= binArray.getLength()) {
                padBits = stop - binArray.getLength() + 1;
                stop = binArray.getLength() - 1;
            }
            int value = BinArray.intToSixbit((int) binArray.getVal(start, stop));
            buf.append((char) value);
            start = stop + 1;
        }
        return buf.toString();
    }

    /**
     * The number of padding bits
     * 
     * @return
     */
    public int getPadBits() {
        return padBits;
    }

    /**
     * Get bit length
     * 
     * @return
     */
    public int getLength() {
        return binArray.getLength();
    }

    /**
     * Get the underlying binary array
     * 
     * @return
     */
    public BinArray getBinArray() {
        return binArray;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SixbitEncoder [binArray=");
        builder.append(binArray);
        builder.append(", padBits=");
        builder.append(padBits);
        builder.append("]");
        return builder.toString();
    }

}
