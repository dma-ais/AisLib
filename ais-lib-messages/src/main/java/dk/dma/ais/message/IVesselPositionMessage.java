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
package dk.dma.ais.message;

import dk.dma.enav.model.geometry.Position;

/**
 * Interface to capture the communalities between message 1,2,3 and 18
 */
public interface IVesselPositionMessage extends IPositionMessage {

    /**
     * Speed over ground
     * 
     * @return
     */
    int getSog();

    /**
     * Course over ground
     */
    int getCog();

    /**
     * True heading
     * 
     * @return
     */
    int getTrueHeading();

    /**
     * Returns a valid position if this message has a valid position, otherwise null.
     * 
     * @return a valid position if this message has a valid position, otherwise null
     */
    Position getValidPosition();

    /**
     * UTC sec
     * 
     * @return
     */
    int getUtcSec();

    /**
     * Determine if position is valid
     * 
     * @return
     */
    boolean isPositionValid();

    /**
     * Course over ground valid
     * 
     * @return
     */
    boolean isCogValid();

    /**
     * Speed over ground valid
     * 
     * @return
     */
    boolean isSogValid();

    /**
     * Heading valid
     * 
     * @return
     */
    boolean isHeadingValid();

    /**
     * Get raim
     * 
     * @return
     */
    int getRaim();

}
