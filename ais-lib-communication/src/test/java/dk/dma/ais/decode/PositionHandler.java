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
package dk.dma.ais.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.enav.util.function.Consumer;

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
