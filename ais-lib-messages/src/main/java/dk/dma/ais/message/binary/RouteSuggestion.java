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
