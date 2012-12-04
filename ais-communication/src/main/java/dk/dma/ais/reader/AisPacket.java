/*
 * Copyright (c) 2008 Kasper Nielsen.
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
package dk.dma.ais.reader;

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
