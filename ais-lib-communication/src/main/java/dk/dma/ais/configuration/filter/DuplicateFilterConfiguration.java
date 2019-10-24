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

import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.filter.IPacketFilter;

/**
 * The type Duplicate filter configuration.
 */
@XmlRootElement
public class DuplicateFilterConfiguration extends FilterConfiguration {

    /**
     * Window size in milliseconds
     */
    private long windowSize = 10000;

    /**
     * Instantiates a new Duplicate filter configuration.
     */
    public DuplicateFilterConfiguration() {

    }

    /**
     * Gets window size.
     *
     * @return the window size
     */
    public long getWindowSize() {
        return windowSize;
    }

    /**
     * Sets window size.
     *
     * @param windowSize the window size
     */
    public void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }
    
    @Override
    @XmlTransient
    public IPacketFilter getInstance() {
        return new DuplicateFilter(windowSize);
    }

}
