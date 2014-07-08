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
package dk.dma.ais.bus.consumer;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;

/**
 * Simple consumer that outputs to stdout
 */
@ThreadSafe
public class StdoutConsumer extends AisBusConsumer {

    public StdoutConsumer() {
        super();
    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        System.out.println(queueElement.getPacket().getStringMessage());
    }
    
    @Override
    public void cancel() {        
        super.cancel();
        setStopped();
    }
    
}
