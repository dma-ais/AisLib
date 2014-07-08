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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.jcip.annotations.NotThreadSafe;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;

/**
 * Encapsulation of the VDM lines containing a single AIS message including leading proprietary tags and comment/tag
 * blocks.
 * 
 * @author Kasper Nielsen
 */
@NotThreadSafe
public class AisPacket implements Comparable<AisPacket> {

    private final String rawMessage;
    private transient Vdm vdm;
    private transient AisPacketTags tags;
    private AisMessage message;
    private volatile long timestamp = Long.MIN_VALUE;

    private AisPacket(String stringMessage) {
        this.rawMessage = requireNonNull(stringMessage);
    }

    AisPacket(Vdm vdm, String stringMessage) {
        this(stringMessage);
        this.vdm = vdm;
    }

    public static AisPacket fromByteBuffer(ByteBuffer buffer) {
        int cap = buffer.remaining();
        byte[] buf = new byte[cap];
        buffer.get(buf);
        return fromByteArray(buf);
    }

    public static AisPacket fromByteArray(byte[] array) {
        return from(new String(array, StandardCharsets.US_ASCII));
    }

    public byte[] toByteArray() {
        return rawMessage.getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Returns the timestamp of the packet, or -1 if no timestamp is available.
     * 
     * @return the timestamp of the packet, or -1 if no timestamp is available
     */
    public long getBestTimestamp() {
        long timestamp = this.timestamp;
        if (timestamp == Long.MIN_VALUE) {
            Date date = getTimestamp();
            this.timestamp = timestamp = date == null ? -1 : date.getTime();
        }
        return timestamp;
    }

    public String getStringMessage() {
        return rawMessage;
    }

    public List<String> getStringMessageLines() {
        return Arrays.asList(rawMessage.split("\\r?\\n"));
    }

    /**
     * Get existing VDM or parse one from message string
     * 
     * @return Vdm
     */
    public Vdm getVdm() {
        if (vdm == null) {
            AisPacket packet;
            try {
                packet = readFromString(rawMessage);
                if (packet != null) {
                    vdm = packet.getVdm();
                }
            } catch (SentenceException e) {
                e.printStackTrace();
                return null;
            }
        }
        return vdm;
    }

    /**
     * Returns the tags of the packet.
     * 
     * @return the tags of the packet
     */
    public AisPacketTags getTags() {
        AisPacketTags tags = this.tags;
        if (tags == null) {
            return this.tags = AisPacketTags.parse(getVdm());
        }
        return tags;
    }

    // TODO fizx
    public AisMessage tryGetAisMessage() {
        try {
            return getAisMessage();
        } catch (AisMessageException | SixbitException ignore) {
            return null;
        }
    }

    /**
     * Try to get AIS message from packet
     * 
     * @return
     * @throws SixbitException
     * @throws AisMessageException
     */
    public AisMessage getAisMessage() throws AisMessageException, SixbitException {
        if (message != null || getVdm() == null) {
            return message;
        }
        return this.message = AisMessage.getInstance(getVdm());
    }

    /**
     * Check if VDM contains a valid AIS message
     * 
     * @return
     */
    public boolean isValidMessage() {
        return tryGetAisMessage() != null;
    }

    /**
     * Try to get timestamp for packet.
     * 
     * @return
     */
    public Date getTimestamp() {
        if (getVdm() == null) {
            return null;
        }
        return vdm.getTimestamp();
    }

    public PositionTime tryGetPositionTime() {
        AisMessage m = tryGetAisMessage();
        if (m instanceof IPositionMessage) {
            Position p = ((IPositionMessage) m).getPos().getGeoLocation();
            return p == null ? null : PositionTime.create(p, getBestTimestamp());
        }
        return null;

    }

    public static AisPacket from(String stringMessage) {
        return new AisPacket(stringMessage);
    }

    /**
     * Construct AisPacket from raw packet string
     * 
     * @param messageString
     * @param optional
     *            factory
     * @return
     * @throws SentenceException
     */
    public static AisPacket readFromString(String messageString) throws SentenceException {
        AisPacket packet = null;
        AisPacketParser packetReader = new AisPacketParser();
        // String[] lines = StringUtils.split(messageString, "\n");
        String[] lines = messageString.split("\\r?\\n");
        for (String line : lines) {
            packet = packetReader.readLine(line);
            if (packet != null) {
                return packet;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(AisPacket p) {
        return Long.compare(getBestTimestamp(), p.getBestTimestamp());
    }
}
