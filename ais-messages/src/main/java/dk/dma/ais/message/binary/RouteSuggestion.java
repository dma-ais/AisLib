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
 * ASM for suggesting a route to a vessel
 */
public class RouteSuggestion extends RouteExchange {

    public enum RouteType {
        MANDATORY(1), RECOMMENDED(2), ALTERNATIVE(3);

        private int type;

        private RouteType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public static final int DAC = 219;
    public static final int FI = 2;

    private int msgLinkId; // 10 bits: Source specific running number linking
                           // binary messages
    private int routeType; // 5 bits

    public RouteSuggestion() {
        super(DAC, FI);
    }

    public RouteSuggestion(BinArray binArray) throws SixbitException {
        super(DAC, FI, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgLinkId, 10);
        encoder.addVal(routeType, 5);
        super.encode(encoder);
        return encoder;
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.waypoints = new ArrayList<>();
        this.msgLinkId = (int) binArray.getVal(10);
        this.routeType = (int) binArray.getVal(5);
        super.parse(binArray);
    }

    public int getMsgLinkId() {
        return msgLinkId;
    }

    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
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
        builder.append("RouteSuggestion [");
        builder.append(super.toString());
        builder.append(", msgLinkId=");
        builder.append(msgLinkId);
        builder.append(", routeType=");
        builder.append(routeType);
        return builder.toString();
    }

}
