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
 * Broadcast area notice ASM
 */
public class BroadcastAreaNotice extends AreaNotice {

    public BroadcastAreaNotice() {
        super(22);
    }

    public BroadcastAreaNotice(BinArray binArray) throws SixbitException {
        super(22, binArray);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BroadcastAreaNotice [");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
