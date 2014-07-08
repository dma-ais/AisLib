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
package dk.dma.ais.data;

import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.IVesselPositionMessage;

/**
 * Class to represent a class B postion
 */
public class AisClassBPosition extends AisVesselPosition {

    private static final long serialVersionUID = 1L;

    public AisClassBPosition() {
        super();
    }

    public AisClassBPosition(AisMessage18 msg18) {
        super();
        update(msg18);
    }
    
    public AisClassBPosition(AisMessage19 msg19) {
        super();
        update(msg19);
    }

    /**
     * Update data object with data from AIS message
     * 
     * @param msg18
     */
    public void update(AisMessage18 msg18) {
        super.update((IVesselPositionMessage) msg18);
    }
    
    /**
     * Update data object with data from AIS message
     * 
     * @param msg19
     */
    public void update(AisMessage19 msg19) {
        super.update((IVesselPositionMessage) msg19);
    }

}
