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
package dk.dma.ais.data;

import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.IVesselPositionMessage;

/**
 * Class to represent a class B postion
 */
public class AisClassBPosition extends AisVesselPosition {

    private static final long serialVersionUID = 1L;

    public AisClassBPosition() {
        super();
    }

    public AisClassBPosition(AisMessage18 msg18) {
        super();
        update(msg18);
    }
    
    public AisClassBPosition(AisMessage19 msg19) {
        super();
        update(msg19);
    }

    /**
     * Update data object with data from AIS message
     * 
     * @param msg18
     */
    public void update(AisMessage18 msg18) {
        super.update((IVesselPositionMessage) msg18);
    }
    
    /**
     * Update data object with data from AIS message
     * 
     * @param msg19
     */
    public void update(AisMessage19 msg19) {
        super.update((IVesselPositionMessage) msg19);
    }

}
