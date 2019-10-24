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
package dk.dma.ais.configuration.transform;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.ReplayTransformer;

/**
 * The type Replay transform configuration.
 */
@XmlRootElement
public class ReplayTransformConfiguration extends TransformerConfiguration {

    private double speedup = 1;

    /**
     * Instantiates a new Replay transform configuration.
     */
    public ReplayTransformConfiguration() {
        super();
    }

    /**
     * Gets speedup.
     *
     * @return the speedup
     */
    public double getSpeedup() {
        return speedup;
    }

    /**
     * Sets speedup.
     *
     * @param speedup the speedup
     */
    public void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

    @Override
    @XmlTransient
    public IAisPacketTransformer getInstance() {
        return new ReplayTransformer(speedup);
    }

}
