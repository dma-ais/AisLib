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
package dk.dma.ais.binary;

import java.util.BitSet;

/**
 * Class to represent a binary array with utility methods to add and extract values
 */
public class BinArray extends BitSet {

    private static final long serialVersionUID = 1L;

    private int length = 0;
    private int readPtr = 0;

    public BinArray() {
        super();
    }

    /**
     * Append bits from a sixbit encoded string
     * 
     * @param str
     * @param padBits
     * @throws SixbitException
     */
    public void appendSixbit(String str, int padBits) throws SixbitException {
        for (int i = 0; i < str.length(); i++) {
            int binVal = sixbitToInt(str.charAt(i));
            int bits = 6;
            if (i == str.length() - 1) {
                bits -= padBits;
            }
            append(binVal, bits);
        }
    }

    /**
     * Append another binary array
     * 
     * @param binArray
     */
    public void append(BinArray binArray) {
        for (int i = 0; i < binArray.getLength(); i++) {
            set(length, binArray.get(i));
            length++;
        }
    }

    /**
     * Append value with number of bits bits
     * 
     * @param value
     * @param bits
     */
    public void append(long value, int bits) {
        long powMask = 1;
        for (int i = length + bits - 1; i >= length; i--) {
            set(i, (value & powMask) > 0);
            powMask <<= 1;
        }
        length += bits;
    }

    /**
     * Get six bit string representation of the next len six bit characters and move read ptr
     * 
     * @param len
     * @return string
     * @throws SixbitException
     */
    public String getString(int len) throws SixbitException {
        char[] resStr = new char[len];
        for (int i = 0; i < len; i++) {
            resStr[i] = (char) intToascii((char) getVal(6));
        }
        return new String(resStr);
    }

    /**
     * Get value from the next bits number of bits and move read pointer
     * 
     * @param bits
     * @return
     * @throws SixbitException
     */
    public long getVal(int bits) throws SixbitException {
        int to = readPtr + bits - 1;
        long res = getVal(readPtr, to);
        readPtr = to + 1;
        return res;
    }

    /**
     * Get value from bit position from and to
     * 
     * @param from
     * @param to
     * @return
     * @throws SixbitException
     */
    public long getVal(int from, int to) throws SixbitException {
        if (to >= length) {
            throw new SixbitException("Not enough bits");
        }
        long val = 0;
        long powMask = 1;
        for (int i = to; i >= from; i--) {
            if (get(i)) {
                val += powMask;
            }
            powMask <<= 1;
        }
        return val;
    }

    /**
     * Get bit length
     * 
     * @return
     */
    public int getLength() {
        return length;
    }

    @Override
    public int length() {
        return getLength();
    }

    /**
     * Get the position of the read ptr within the array
     * 
     * @return
     */
    public int getReadPtr() {
        return readPtr;
    }

    /**
     * Returns true if there are more bits to read
     * 
     * @return
     */
    public boolean hasMoreBits() {
        return readPtr < length - 1;
    }

    /**
     * Convert sixbit ascii char to integer value
     * 
     * @param chr
     * @return
     * @throws SixbitException
     */
    public static int sixbitToInt(int chr) throws SixbitException {
        if ((chr < 48) || (chr > 119) || ((chr > 87) && (chr < 96))) {
            throw new SixbitException("Illegal sixbit ascii char: " + chr);
        }
        if (chr < 0x60) {
            return (chr - 48) & 0x3F;
        } else {
            return (chr - 56) & 0x3F;
        }
    }

    /**
     * Convert six bit int value to character
     * 
     * @param val
     * @return
     * @throws SixbitException
     */
    public static int intToascii(int val) throws SixbitException {
        if (val > 63) {
            throw new SixbitException("Char value " + val + " not allowed");
        }
        if (val < 32) {
            return val + 64;
        } else {
            return val;
        }
    }

    /**
     * Convert a int value to a sixbit ascii value
     * 
     * @param val
     * @return
     * @throws SixbitException
     */
    public static int intToSixbit(int val) throws SixbitException {
        if (val > 63) {
            throw new SixbitException("Char value " + val + " not allowed");
        }
        if (val < 40) {
            return val + 48;
        } else {
            return val + 56;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BinArray [TODO");
        builder.append("]");
        return builder.toString();
    }

}
