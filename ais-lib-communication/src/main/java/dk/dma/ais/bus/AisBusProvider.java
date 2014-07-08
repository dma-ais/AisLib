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
package dk.dma.ais.bus;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;

@ThreadSafe
public abstract class AisBusProvider extends AisBusSocket {

    public AisBusProvider() {
        this(false);
    }

    public AisBusProvider(boolean blocking) {
        super(blocking);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    /**
     * Helper method to push to bus
     * 
     * @param packet
     */
    protected void push(AisPacket packet) {
        // Do filtering, transformation and filtering
        packet = handleReceived(packet);
        if (packet == null) {
            return;
        }
        // Push to the bus
        if (!getAisBus().push(packet, blocking)) {
            status.overflow();
        }
    }

}
