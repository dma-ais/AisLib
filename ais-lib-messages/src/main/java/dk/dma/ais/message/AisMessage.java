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
package dk.dma.ais.message;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.geometry.Position;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Abstract base class for all AIS messages
 */
public abstract class AisMessage implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * A set of all valid AIS message types.
     */
    public static final Set<Integer> VALID_MESSAGE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(1,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 17, 18, 19, 21, 24)));

    /**
     * The Msg id.
     */
    protected int msgId; // 6 bit: message id
    /**
     * The Repeat.
     */
    protected int repeat; // 2 bit: How many times message has been repeated
    /**
     * The User id.
     */
    protected int userId; // 30 bit: MMSI number
    /**
     * The Vdm.
     */
    protected transient Vdm vdm; // The VDM encapsulating the AIS message

    /**
     * Constructor given message id
     *
     * @param msgId the msg id
     */
    public AisMessage(int msgId) {
        this.msgId = msgId;
        this.repeat = 0;
    }

    /**
     * Constructor given VDM with AIS message
     *
     * @param vdm the vdm
     */
    public AisMessage(Vdm vdm) {
        this.vdm = vdm;
        this.msgId = vdm.getMsgId();
    }

    /**
     * Base parse method to be called by all extending classes
     *
     * @param binArray the bin array
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    protected void parse(BinArray binArray) throws AisMessageException, SixbitException {
        this.repeat = (int) binArray.getVal(2);
        this.userId = (int) binArray.getVal(30);
    }

    /**
     * Base encode method to be called by all extending classes
     *
     * @return SixbitEncoder sixbit encoder
     */
    protected SixbitEncoder encode() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgId, 6);
        encoder.addVal(repeat, 2);
        encoder.addVal(userId, 30);
        return encoder;
    }

    /**
     * Abstract method to be implemented by all extending classes
     *
     * @return SixbitEncoder encoded
     */
    public abstract SixbitEncoder getEncoded();

    /**
     * Gets msg id.
     *
     * @return the msg id
     */
    public int getMsgId() {
        return msgId;
    }

    /**
     * Gets repeat.
     *
     * @return the repeat
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * Sets repeat.
     *
     * @param repeat the repeat
     */
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Return the LAST source tag (closest to AIS sentence)
     *
     * @return source tag
     */
    public IProprietarySourceTag getSourceTag() {
        LinkedList<IProprietaryTag> tags = vdm == null ? null : vdm.getTags();
        if (tags == null) {
            return null;
        }
        // Iterate backwards
        for (Iterator<IProprietaryTag> iterator = tags.descendingIterator(); iterator.hasNext();) {
            IProprietaryTag tag = iterator.next();
            if (tag instanceof IProprietarySourceTag) {
                return (IProprietarySourceTag) tag;
            }
        }
        return null;
    }

    /**
     * Get all tags
     *
     * @return tags
     */
    public LinkedList<IProprietaryTag> getTags() {
        return vdm.getTags();
    }

    /**
     * Add tag (to front)
     *
     * @param tag the tag
     */
    public void setTag(IProprietaryTag tag) {
        LinkedList<IProprietaryTag> tags = vdm.getTags();
        if (tags == null) {
            tags = new LinkedList<>();
        }
        tags.addFirst(tag);
    }

    /**
     * Sets tags.
     *
     * @param tags the tags
     */
    public void setTags(LinkedList<IProprietaryTag> tags) {
        vdm.setTags(tags);
    }

    /**
     * Get VDM this message was encapsulated in
     *
     * @return Vdm vdm
     */
    public Vdm getVdm() {
        return vdm;
    }

    /**
     * Returns a valid position if this message has a valid position, otherwise null.
     *
     * @return a valid position if this message has a valid position, otherwise null
     */
    public Position getValidPosition() {
        return null;
    }

    /**
     * Returns the target type of the message or <code>null</code> if the message does not have a target type.
     *
     * @return the target type of the message or <code>null</code> if the message does not have a target type
     */
    public AisTargetType getTargetType() {
        // TODO do we need to check target type also, or is the mmsi number enough???
        if (userId >= 970_000_000 && userId <= 970_999_999) {
            return AisTargetType.SART;
        }
        Class<? extends AisMessage> type = getClass();
        if (AisMessage4.class.isAssignableFrom(type)) {
            return AisTargetType.BS;
        } else if (AisMessage21.class.isAssignableFrom(type)) {
            return AisTargetType.ATON;
        } else if (AisMessage18.class.isAssignableFrom(type) || AisMessage19.class.isAssignableFrom(type)
                || AisMessage24.class.isAssignableFrom(type)) {
            return AisTargetType.B;
        } else if (AisPositionMessage.class.isAssignableFrom(type) || AisMessage5.class.isAssignableFrom(type)
                || AisMessage27.class.isAssignableFrom(type)) {
            return AisTargetType.A;
        }
        return null;
    }

    /**
     * Given VDM return the encapsulated AIS message. To determine which message is returned use instanceof operator or
     * getMsgId() before casting.
     * <p>
     * Example: AisMessage aisMessage = AisMessage.getInstance(vmd); if (aisMessage instanceof AisPositionMessage) {
     * AisPositionMessage posMessage = (AisPositionMessage)aisMessage; } ...
     *
     * @param vdm the vdm
     * @return AisMessage instance
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public static AisMessage getInstance(Vdm vdm) throws AisMessageException, SixbitException {
        AisMessage message;

        switch (vdm.getMsgId()) {
        case 1:
            message = new AisMessage1(vdm);
            vdm.getBinArray().doneReading();
            break;
        case 2:
            message = new AisMessage2(vdm);
            break;
        case 3:
            message = new AisMessage3(vdm);
            vdm.getBinArray().doneReading();
            break;
        case 4:
            message = new AisMessage4(vdm);
            break;
        case 5:
            message = new AisMessage5(vdm);
            break;
        case 6:
            message = new AisMessage6(vdm);
            break;
        case 7:
            message = new AisMessage7(vdm);
            break;
        case 8:
            message = new AisMessage8(vdm);
            break;
        case 9:
            message = new AisMessage9(vdm);
            break;
        case 10:
            message = new AisMessage10(vdm);
            break;
        case 11:
            message = new AisMessage11(vdm);
            break;
        case 12:
            message = new AisMessage12(vdm);
            break;
        case 13:
            message = new AisMessage13(vdm);
            break;
        case 14:
            message = new AisMessage14(vdm);
            break;
        case 15:
            // TODO implement real message class
            message = new AisUnsupportedMessageType(vdm);
            break;
        case 16:
            // TODO implement real message class
            message = new AisUnsupportedMessageType(vdm);
            break;
        case 17:
            message = new AisMessage17(vdm);
            break;
        case 18:
            message = new AisMessage18(vdm);
            break;
        case 19:
            message = new AisMessage19(vdm);
            break;
        case 20:
            // TODO implement real message class
            message = new AisUnsupportedMessageType(vdm);
            break;
        case 21:
            message = new AisMessage21(vdm);
            break;
        case 22:
            // TODO implement real message class
            message = new AisUnsupportedMessageType(vdm);
            break;
        case 23:
            // TODO implement real message class
            message = new AisUnsupportedMessageType(vdm);
            break;
        case 24:
            message = new AisMessage24(vdm);
            break;
        case 27:
            message = new AisMessage27(vdm);
            break;
        default:
            throw new AisMessageException("Unknown AIS message id " + vdm.getMsgId());
        }

        return message;
    }

    /**
     * Utility to trim text from AIS message
     *
     * @param text the text
     * @return string
     */
    public static String trimText(String text) {
        if (text == null) {
            return null;
        }
        // Remove @
        int firstAt = text.indexOf("@");
        if (firstAt >= 0) {
            text = text.substring(0, firstAt);
        }
        // Trim leading and trailing spaces
        return text.trim();
    }

    /**
     * Method for reassembling original message appending possible proprietary source tags
     *
     * @return string
     */
    public String reassemble() {
        LinkedList<IProprietaryTag> tags = vdm.getTags();
        StringBuilder buf = new StringBuilder();
        if (tags != null) {
            for (IProprietaryTag tag : tags) {
                if (tag.getSentence() != null) {
                    buf.append(tag.getSentence() + "\r\n");
                }
            }
        }
        buf.append(getVdm().getOrgLinesJoined());
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[msgId=");
        builder.append(msgId);
        builder.append(", repeat=");
        builder.append(repeat);
        builder.append(", userId=");
        builder.append(userId);
        return builder.toString();
    }

}
