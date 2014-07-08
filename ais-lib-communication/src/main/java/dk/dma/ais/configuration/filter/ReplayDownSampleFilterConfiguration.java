/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.configuration.filter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.ReplayDownSampleFilter;

@XmlRootElement
public class ReplayDownSampleFilterConfiguration extends FilterConfiguration {

    /**
     * Sampling rate in seconds
     */
    private long samplingRate = 60;

    public ReplayDownSampleFilterConfiguration() {

    }

    public ReplayDownSampleFilterConfiguration(long samplingRate) {
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
        return new ReplayDownSampleFilter(samplingRate);
    }

}
