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

import java.util.ArrayList;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;

/**
 * Abstract base class for route information ASM DAC=1, FI=27,28
 */
public abstract class RouteInformation extends RouteMessage {

    public enum RouteType {
        NOT_AVAIABLE(0), MANDATORY(1), RECOMMENDED(2), ALTERNATIVE(3), RECOMMENDED_THROUGH_ICE(4), SHIP_ROUTE(5), CANCELLATION(
                31);

        private int type;

        private RouteType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private int msgLinkId; // 10 bits: Source specific running number linking
                           // binary messages
    private int senderClassification; // 3 bits: 0=ship, 1=authority
    private int routeType; // 5 bits

    public RouteInformation(int dac, int fi) {
        super(dac, fi);
        this.waypoints = new ArrayList<>();
    }

    public RouteInformation(int dac, int fi, BinArray binArray) throws SixbitException {
        super(dac, fi, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgLinkId, 10);
        encoder.addVal(senderClassification, 3);
        encoder.addVal(routeType, 5);
        super.encode(encoder);
        return encoder;
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.waypoints = new ArrayList<>();
        this.msgLinkId = (int) binArray.getVal(10);
        this.senderClassification = (int) binArray.getVal(3);
        this.routeType = (int) binArray.getVal(5);
        super.parse(binArray);
    }

    public int getMsgLinkId() {
        return msgLinkId;
    }

    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    public int getSenderClassification() {
        return senderClassification;
    }

    public void setSenderClassification(int senderClassification) {
        this.senderClassification = senderClassification;
    }

    public int getRouteType() {
        return routeType;
    }

    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", msgLinkId=");
        builder.append(msgLinkId);
        builder.append(", routeType=");
        builder.append(routeType);
        builder.append(", senderClassification=");
        builder.append(senderClassification);
        return builder.toString();
    }

}
