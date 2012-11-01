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
package dk.dma.enav.ais.message;

/**
 * Interface to capture the communalities between message 1,2,3 and 18
 */
public interface IGeneralPositionMessage {

    /**
     * Speed over ground
     * 
     * @return
     */
    public int getSog();

    /**
     * Course over ground
     */
    public int getCog();

    /**
     * Position
     * 
     * @return
     */
    public AisPosition getPos();

    /**
     * Position accuracy
     * 
     * @return
     */
    public int getPosAcc();

    /**
     * True heading
     * 
     * @return
     */
    public int getTrueHeading();

    /**
     * UTC sec
     * 
     * @return
     */
    public int getUtcSec();

    /**
     * Determine if position is valid
     * 
     * @return
     */
    public boolean isPositionValid();

    /**
     * Course over ground valid
     * 
     * @return
     */
    public boolean isCogValid();

    /**
     * Speed over ground valid
     * 
     * @return
     */
    public boolean isSogValid();

    /**
     * Heading valid
     * 
     * @return
     */
    public boolean isHeadingValid();

    /**
     * Get raim
     * 
     * @return
     */
    public int getRaim();

}
