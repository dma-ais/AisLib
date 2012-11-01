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
package dk.dma.enav.ais.message.binary;

import dk.dma.enav.ais.binary.BinArray;
import dk.dma.enav.ais.binary.SixbitException;

/**
 * ASM for broadcasting a vessels intended route
 */
public class BroadcastIntendedRoute extends RouteExchange {

    public static final int DAC = 219;
    public static final int FI = 1;

    public BroadcastIntendedRoute() {
        super(DAC, FI);
    }

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
