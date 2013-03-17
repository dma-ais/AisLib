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
package dk.dma.ais.bus.configuration.filter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.filter.IPacketFilter;

@XmlRootElement
public class DuplicateFilterConfiguration extends FilterConfiguration {

    /**
     * Window size in milliseconds
     */
    private long windowSize = 10000;

    public DuplicateFilterConfiguration() {

    }

    public long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }
    
    @Override
    @XmlTransient
    public IPacketFilter getInstance() {
        return new DuplicateFilter(windowSize);
    }

}
