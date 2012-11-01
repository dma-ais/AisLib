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
package dk.dma.ais.message.binary;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;

/**
 * Route suggestion reply message
 */
public class RouteSuggestionReply extends AisApplicationMessage {

    private int msgLinkId; // 10 bits: Source specific running number linking
                           // birary messages
    private int refMsgLinkId; // 10 bits: The Message Linkage ID of the message
                              // responded to.
    /**
     * 0 = Accept Ship intends to adjust intended route 1 = Reject Ship does not intend to adjust intended route 2 =
     * Noted Ship acknowledges reception, but cannot, or will not, consider the recommendation.
     */
    private int response; // 6 bits

    public RouteSuggestionReply() {
        super(0, 32);
    }

    public RouteSuggestionReply(BinArray binArray) throws SixbitException {
        super(0, 32, binArray);
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.msgLinkId = (int) binArray.getVal(10);
        this.refMsgLinkId = (int) binArray.getVal(10);
        this.response = (int) binArray.getVal(6);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgLinkId, 10);
        encoder.addVal(refMsgLinkId, 10);
        encoder.addVal(response, 6);
        return encoder;
    }

    public int getMsgLinkId() {
        return msgLinkId;
    }

    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    public int getRefMsgLinkId() {
        return refMsgLinkId;
    }

    public void setRefMsgLinkId(int refMsgLinkId) {
        this.refMsgLinkId = refMsgLinkId;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", msgLinkId=");
        builder.append(msgLinkId);
        builder.append(", refMsgLinkId=");
        builder.append(refMsgLinkId);
        builder.append(", response=");
        builder.append(response);
        builder.append("]");
        return builder.toString();
    }

}
