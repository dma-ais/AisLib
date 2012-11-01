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
package dk.dma.ais.reader;

import dk.dma.ais.sentence.Abk;

/**
 * Interface for classes able to receive the result of a send operation
 */
public interface ISendResultListener {

    /**
     * Returns received ABK. If no ABK is received within timeout null is returned.
     * 
     * Make this a simple method that only sets a status field or the like, as this method is run from the ABK handling
     * thread.
     * 
     * @param abk
     */
    void sendResult(Abk abk);

}
