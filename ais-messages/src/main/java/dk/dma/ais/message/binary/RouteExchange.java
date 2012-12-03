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
package dk.dma.ais.message.binary;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitException;

/**
 * Abstract base class for route information ASM DAC=219, FI=1,2
 */
public abstract class RouteExchange extends RouteMessage {

    public RouteExchange(int dac, int fi) {
        super(dac, fi);
    }

    public RouteExchange(int dac, int fi, BinArray binArray) throws SixbitException {
        super(dac, fi, binArray);
    }

}
