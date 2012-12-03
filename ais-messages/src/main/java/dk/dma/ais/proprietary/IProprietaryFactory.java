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

/**
 * General interface for proprietary factories
 */
public interface IProprietaryFactory {

    /**
     * Return if the line matches proprietary message for this vendor
     * 
     * @param line
     * @return
     */
    boolean match(String line);

    /**
     * Return the tag for line, if matches, otherwise null is returned
     * 
     * @param line
     * @return
     */
    IProprietaryTag getTag(String line);

}
