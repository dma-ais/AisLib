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
 * Abstract base class for route information ASM DAC=219, FI=1,2
 */
public abstract class RouteExchange extends RouteMessage {

    /**
     * Instantiates a new Route exchange.
     *
     * @param dac the dac
     * @param fi  the fi
     */
    public RouteExchange(int dac, int fi) {
        super(dac, fi);
    }

    /**
     * Instantiates a new Route exchange.
     *
     * @param dac      the dac
     * @param fi       the fi
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public RouteExchange(int dac, int fi, BinArray binArray) throws SixbitException {
        super(dac, fi, binArray);
    }

}
