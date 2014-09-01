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
package dk.dma.ais.bus.provider;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import java.util.function.Consumer;

/**
 * Provider that allows to push packets onto the bus 
 */
@ThreadSafe
public class CollectorProvider extends AisBusProvider implements Consumer<AisPacket> {
    
    public CollectorProvider() {
        super();
    }
    
    @Override
    public void accept(AisPacket packet) {
        push(packet);        
    }

    @Override
    public void cancel() {
                
    }

}
