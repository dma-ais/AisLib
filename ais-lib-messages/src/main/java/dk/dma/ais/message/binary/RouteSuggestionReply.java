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
     * 0 = Accept – Ship intends to adjust intended route 1 = Reject – Ship does not intend to adjust intended route 2 =
     * Noted – Ship acknowledges reception, but cannot, or will not, consider the recommendation.
     */
    private int response; // 6 bits

    /**
     * Instantiates a new Route suggestion reply.
     */
    public RouteSuggestionReply() {
        super(0, 32);
    }

    /**
     * Instantiates a new Route suggestion reply.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
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

    /**
     * Gets msg link id.
     *
     * @return the msg link id
     */
    public int getMsgLinkId() {
        return msgLinkId;
    }

    /**
     * Sets msg link id.
     *
     * @param msgLinkId the msg link id
     */
    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    /**
     * Gets ref msg link id.
     *
     * @return the ref msg link id
     */
    public int getRefMsgLinkId() {
        return refMsgLinkId;
    }

    /**
     * Sets ref msg link id.
     *
     * @param refMsgLinkId the ref msg link id
     */
    public void setRefMsgLinkId(int refMsgLinkId) {
        this.refMsgLinkId = refMsgLinkId;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public int getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
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
