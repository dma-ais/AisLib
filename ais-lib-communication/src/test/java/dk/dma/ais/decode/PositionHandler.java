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
package dk.dma.ais.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import java.util.function.Consumer;

public class PositionHandler implements Consumer<AisMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(PositionHandler.class);

    /**
     * Method to handle incoming AIS messages
     * 
     * For debug out adjust log4j.xml level
     */
    public void accept(AisMessage aisMessage) {
        // Ignore everything but position reports
        if (aisMessage.getMsgId() > 3) {
            return;
        }

        // Just consider the position part
        AisPositionMessage aisPosMessage = (AisPositionMessage) aisMessage;
        LOG.debug(aisPosMessage.toString());

    }

}
