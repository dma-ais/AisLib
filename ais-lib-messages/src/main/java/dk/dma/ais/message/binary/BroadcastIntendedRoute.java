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
import dk.dma.ais.binary.SixbitException;

/**
 * ASM for broadcasting a vessels intended route
 */
public class BroadcastIntendedRoute extends RouteExchange {

    /**
     * The constant DAC.
     */
    public static final int DAC = 219;
    /**
     * The constant FI.
     */
    public static final int FI = 1;

    /**
     * Instantiates a new Broadcast intended route.
     */
    public BroadcastIntendedRoute() {
        super(DAC, FI);
    }

    /**
     * Instantiates a new Broadcast intended route.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public BroadcastIntendedRoute(BinArray binArray) throws SixbitException {
        super(DAC, FI, binArray);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BroadcastIntendedRoute [");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
