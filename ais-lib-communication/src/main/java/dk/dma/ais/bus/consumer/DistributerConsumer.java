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
package dk.dma.ais.bus.consumer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.util.function.Consumer;

/**
 * Consumer that distributes consumed packets from the bus to
 * a set of packet consumers 
 */
@ThreadSafe
public class DistributerConsumer extends AisBusConsumer {
    
    private final List<Consumer<AisPacket>> consumers = new CopyOnWriteArrayList<>();
    
    public DistributerConsumer() {
        
    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        for (Consumer<AisPacket> consumer : consumers) {
            consumer.accept(queueElement.getPacket());
        }
    }
    
    public List<Consumer<AisPacket>> getConsumers() {
        return consumers;
    }

}
