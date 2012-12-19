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
package dk.dma.ais.proprietary;

import java.util.Date;

import dk.dma.enav.model.Country;

/**
 * Interface for proprietary source tags
 */
public interface IProprietarySourceTag extends IProprietaryTag {

    /**
     * Time of message receival at source
     * 
     * @return
     */
    Date getTimestamp();

    /**
     * Country origin of message
     * 
     * @return
     */
    Country getCountry();

    /**
     * Unique region identifier
     * 
     * @return
     */
    String getRegion();

    /**
     * Base station MMSI
     * 
     * @return
     */
    Integer getBaseMmsi();

}
