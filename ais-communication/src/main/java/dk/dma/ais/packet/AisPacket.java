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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import com.google.common.hash.Hashing;

import dk.dma.ais.message.AisMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacket {
    // Jeg vil helst ikke gemme AisMessage da den er mutable.
    // Kan vi vaere sikker paa at den ikke bliver modificeret undervejs??
    private volatile AisMessage aisMessage;
    private final long insertTimestamp;
    private final String stringMessage;

    /**
     * @param stringMessage
     * @param aisMessage
     * @param receiveTimestamp
     */
    public AisPacket(String stringMessage, long receiveTimestamp) {
        this.stringMessage = requireNonNull(stringMessage);
        this.insertTimestamp = receiveTimestamp;
    }

    /**
     * Calculates a 128 bit hash on the received package.
     * 
     * @return a 128 hash on the received package
     */
    public byte[] calculateHash128() {
        return Hashing.murmur3_128().hashString(stringMessage).asBytes();
    }

    public AisMessage getAisMessage() {
        return aisMessage;
    }

    public long getReceiveTimestamp() {
        return insertTimestamp;
    }

    public String getStringMessage() {
        return stringMessage;
    }

    public static AisPacket from(String message, long received) {
        return new AisPacket(message, received);
    }
}
