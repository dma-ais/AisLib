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

public class BroadcastRouteInformation extends RouteInformation {

    public BroadcastRouteInformation() {
        super(1, 27);
    }

    public BroadcastRouteInformation(BinArray binArray) throws SixbitException {
        super(1, 27, binArray);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BroadcastRouteInformation [");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
