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
package dk.dma.ais.binary;

import java.util.Arrays;

/**
 * Class to represent a binary array with utility methods to add and extract values
 */
public class BinArray {

    /** Precompiled list of int to six bit mappings. */
    private static final int[] INT_TO_SIX_BIT;

    private boolean[] bitSet = new boolean[1024];

    private int length;
    private int readPtr;

    static {
        int[] toSixbit = new int[256 * 256]; // we actually only use 256, but we parse chars around instead of bytes
        for (int chr = 0; chr < toSixbit.length; chr++) {
            if (chr < 48 || chr > 119 || chr > 87 && chr < 96) {
                toSixbit[chr] = -1;
            } else if (chr < 0x60) {
                toSixbit[chr] = chr - 48 & 0x3F;
            } else {
                toSixbit[chr] = chr - 56 & 0x3F;
            }
        }
        INT_TO_SIX_BIT = toSixbit;
    }

    /**
     * Ensures that the BinArray can hold enough bits.
     * 
     * @param bitsRequired
     *            the minimum acceptable number of bits.
     */
    private void ensureCapacity(int bitsRequired) {
        if (bitSet.length < bitsRequired) {
            // Allocate larger of doubled size or required size
            int request = Math.max(2 * bitSet.length, bitsRequired);
            bitSet = Arrays.copyOf(bitSet, request);
        }
    }

    /**
     * Append bits from a sixbit encoded string
     *
     * @param str     the str
     * @param padBits the pad bits
     * @throws SixbitException the sixbit exception
     */
    public void appendSixbit(String str, int padBits) throws SixbitException {
        if (str.length() == 0) {
            return;
        }
        int len = str.length() * 6 - padBits;
        int length = this.length;
        ensureCapacity(length + len);
        boolean[] bitSet = this.bitSet; // store it in a local variable

        int slen = str.length() - 1;
        for (int i = 0; i < slen; i++) {
            char chr = str.charAt(i);
            int binVal = INT_TO_SIX_BIT[chr];
            if (binVal == -1) {
                throw new SixbitException("Illegal sixbit ascii char: " + chr);
            }
            bitSet[length] = (binVal & 32) > 0;
            bitSet[length + 1] = (binVal & 16) > 0;
            bitSet[length + 2] = (binVal & 8) > 0;
            bitSet[length + 3] = (binVal & 4) > 0;
            bitSet[length + 4] = (binVal & 2) > 0;
            bitSet[length + 5] = (binVal & 1) > 0;
            length += 6;
        }

        // Process the last char which might be padded
        char chr = str.charAt(slen);
        int binVal = INT_TO_SIX_BIT[chr];
        if (binVal == -1) {
            throw new SixbitException("Illegal sixbit ascii char: " + chr);
        }
        int bits = 6 - padBits;
        switch (bits) {
        case 6:
            bitSet[length + 5] = (binVal & 1) > 0;
        case 5:
            bitSet[length + 4] = (binVal & 2) > 0;
        case 4:
            bitSet[length + 3] = (binVal & 4) > 0;
        case 3:
            bitSet[length + 2] = (binVal & 8) > 0;
        case 2:
            bitSet[length + 1] = (binVal & 16) > 0;
        case 1:
            bitSet[length] = (binVal & 32) > 0;
        }
        this.length = length + bits;
    }

    /**
     * Append another binary array
     *
     * @param binArray the bin array
     */
    public void append(BinArray binArray) {
        int len = binArray.length;
        ensureCapacity(length + len);
        System.arraycopy(binArray.bitSet, 0, bitSet, length, len);
        length += len;
    }

    /**
     * Append value with number of bits bits
     *
     * @param val  the val
     * @param bits the bits
     */
    public void append(long val, int bits) {
        long powMask = 1;
        ensureCapacity(length + bits);
        for (int i = length + bits - 1; i >= length; i--) {
            bitSet[i] = (val & powMask) > 0;
            powMask <<= 1;
        }
        length += bits;
    }

    /**
     * Get six bit string representation of the next len six bit characters and move read ptr
     *
     * @param len the len
     * @return string string
     * @throws SixbitException the sixbit exception
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
     * @param bits the bits
     * @return val
     * @throws SixbitException the sixbit exception
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
     * @param from the from
     * @param to   the to
     * @return val
     * @throws SixbitException the sixbit exception
     */
    public long getVal(int from, int to) throws SixbitException {
        if (to >= length) {
            throw new SixbitException(length + " is not enough bits. At least " + to + " expected.");
        }
        long val = 0;
        long powMask = 1;
        for (int i = to; i >= from; i--) {
            if (bitSet[i]) {
                val += powMask;
            }
            powMask <<= 1;
        }
        return val;
    }

    /**
     * Get bit length
     *
     * @return length
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the position of the read ptr within the array
     *
     * @return read ptr
     */
    public int getReadPtr() {
        return readPtr;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return bitSet.length;
    }

    /**
     * Returns true if there are more bits to read
     *
     * @return boolean
     */
    public boolean hasMoreBits() {
        return readPtr < length - 1;
    }

    /**
     * Convert six bit int value to character
     *
     * @param val the val
     * @return int
     * @throws SixbitException the sixbit exception
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
     * @param val the val
     * @return int
     * @throws SixbitException the sixbit exception
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

    /**
     * Done reading.
     */
    public void doneReading() {
        readPtr = 0;
    }
}
