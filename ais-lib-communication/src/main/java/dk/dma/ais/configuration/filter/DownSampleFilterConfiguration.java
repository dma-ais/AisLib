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
package dk.dma.ais.configuration.filter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.filter.DownSampleFilter;
import dk.dma.ais.filter.IPacketFilter;

@XmlRootElement
public class DownSampleFilterConfiguration extends FilterConfiguration {

    /**
     * Sampling rate in seconds
     */
    private long samplingRate = 60;

    public DownSampleFilterConfiguration() {

    }

    public DownSampleFilterConfiguration(long samplingRate) {
        this.samplingRate = samplingRate;
    }

    public long getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(long samplingRate) {
        this.samplingRate = samplingRate;
    }
    
    
    @Override
    @XmlTransient
    public IPacketFilter getInstance() {
        return new DownSampleFilter(samplingRate);
    }

}
