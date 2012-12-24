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
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.messaging.MaritimeMessage;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Abstract base class for all AIS messages
 */
public abstract class AisMessage extends MaritimeMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    protected int msgId; // 6 bit: message id
    protected int repeat; // 2 bit: How many times message has been repeated
    protected int userId; // 30 bit: MMSI number
    protected Vdm vdm; // The VDM encapsulating the AIS message

    /**
     * Constructor given message id
     * 
     * @param msgId
     */
    public AisMessage(int msgId) {
        this.msgId = msgId;
        this.repeat = 0;
    }

    /**
     * Constructor given VDM with AIS message
     * 
     * @param vdm
     */
    public AisMessage(Vdm vdm) {
        this.vdm = vdm;
        this.msgId = vdm.getMsgId();
    }

    /**
     * Base parse method to be called by all extending classes
     * 
     * @param binArray
     * @throws AisMessageException
     * @throws SixbitException
     */
    protected void parse(BinArray binArray) throws AisMessageException, SixbitException {
        this.repeat = (int) binArray.getVal(2);
        this.userId = (int) binArray.getVal(30);
    }

    /**
     * Base encode method to be called by all extending classes
     * 
     * @return SixbitEncoder
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
     * @return SixbitEncoder
     */
    public abstract SixbitEncoder getEncoded();

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Return the LAST source tag (closest to AIS sentence)
     * 
     * @return
     */
    public IProprietarySourceTag getSourceTag() {
    	LinkedList<IProprietaryTag> tags = vdm.getTags();
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
     * @return
     */
    public LinkedList<IProprietaryTag> getTags() {
        return vdm.getTags();
    }

    /**
     * Add tag (to front)
     * 
     * @param sourceTag
     */
    public void setTag(IProprietaryTag tag) {
    	LinkedList<IProprietaryTag> tags = vdm.getTags();
        if (tags == null) {
            tags = new LinkedList<>();
        }
        tags.addFirst(tag);
    }

    public void setTags(LinkedList<IProprietaryTag> tags) {
    	vdm.setTags(tags);
    }

    /**
     * Get VDM this message was encapsulated in
     * 
     * @return Vdm
     */
    public Vdm getVdm() {
        return vdm;
    }

    /**
     * Given VDM return the encapsulated AIS message. To determine which message is returned use instanceof operator or
     * getMsgId() before casting.
     * 
     * Example: AisMessage aisMessage = AisMessage.getInstance(vmd); if (aisMessage instanceof AisPositionMessage) {
     * AisPositionMessage posMessage = (AisPositionMessage)aisMessage; } ...
     * 
     * @param vdm
     * @return AisMessage
     * @throws AisMessageException
     * @throws SixbitException
     */
    public static AisMessage getInstance(Vdm vdm) throws AisMessageException, SixbitException {
        AisMessage message = null;

        switch (vdm.getMsgId()) {
        case 1:
            message = new AisMessage1(vdm);
            break;
        case 2:
            message = new AisMessage2(vdm);
            break;
        case 3:
            message = new AisMessage3(vdm);
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
            // TODO implement real message class
            message = new AisMessageDummy(vdm);
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
            message = new AisMessageDummy(vdm);
            break;
        case 16:
            // TODO implement real message class
            message = new AisMessageDummy(vdm);
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
            message = new AisMessageDummy(vdm);
            break;
        case 21:
            message = new AisMessage21(vdm);
            break;
        case 22:
            // TODO implement real message class
            message = new AisMessageDummy(vdm);
            break;
        case 23:
            // TODO implement real message class
            message = new AisMessageDummy(vdm);
            break;
        case 24:
            message = new AisMessage24(vdm);
            break;
        default:
            throw new AisMessageException("Unknown AIS message id " + vdm.getMsgId());
        }

        return message;
    }

    /**
     * Utility to trim text from AIS message
     * 
     * @param text
     * @return
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
     * @return
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
