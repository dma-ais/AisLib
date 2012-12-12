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
package dk.dma.ais.packet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beust.jcommander.Parameter;

import dk.dma.ais.message.AisMessage;
import dk.dma.app.util.function.Filter;

/**
 * This class provides common functionality for filtering {@link AisPacket AIS packets}.
 * 
 * @author Kasper Nielsen
 */
public class AisPacketFilter implements Filter<AisPacket> {

    @Parameter(names = "-start", description = "[Filter] Start date (inclusive), format == yyyy-MM-dd")
    private Date start;

    @Parameter(names = "-stop", description = "[Filter] Stop date (exclusive), format == yyyy-MM-dd")
    private Date stop;

    @Parameter(names = "-messagetype", description = "[Filter] The Ais message type")
    private List<Integer> messageTypes = new ArrayList<>();

    private boolean onlyValidMessages = false;

    String countries;

    // specify source
    // specify country
    // TimeOfDay??
    // include packets that are not proper ais messages
    // host

    /** {@inheritDoc} */
    @Override
    public boolean accept(AisPacket element) {
        AisMessage m=element.tryGetAisMessage();
        if (m==null) {
            //We can probably only handle source filtering?
            return start == null && stop != null && messageTypes.isEmpty() && onlyValidMessages;
        }
        if (!messageTypes.isEmpty() && !messageTypes.contains(m.getMsgId())) {
            return false;
        }
        return true;
    }
}
