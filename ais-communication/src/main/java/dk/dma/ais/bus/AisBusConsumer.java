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
package dk.dma.ais.bus;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.queue.IQueueEntryHandler;

@ThreadSafe
public abstract class AisBusConsumer extends AisBusSocket implements IQueueEntryHandler<AisBusElement> {

    public AisBusConsumer(AisBus aisBus) {
        super(aisBus);
    }

    @Override
    public final void receive(AisBusElement queueElement) {
        // Do filtering, transformation and filtering
        AisPacket packet = handleReceived(queueElement.getPacket());
        if (packet == null) {
            return;
        }
        queueElement.setPacket(packet);
        receiveFiltered(queueElement);
    }

    public abstract void receiveFiltered(AisBusElement queueElement);

}
