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

import dk.dma.ais.bus.consumer.StdoutConsumer;
import dk.dma.ais.bus.provider.RoundRobinTcpClient;

public class AisBusTest {

    //@Test
    public void tcpConsumer() {
        // Make ais bus configuration
        // TODO

        // Make ais bus
        AisBus aisBus = new AisBus();
        // Start AisBus
        aisBus.start();

        // Make consumer
        AisBusConsumer consumer = new StdoutConsumer(aisBus);
        // Start consumer
        // TODO necessary?
        // Register consumer
        aisBus.registerConsumer(consumer);

        // Make providers
        AisBusProvider provider1 = RoundRobinTcpClient.create(aisBus, "ais163.sealan.dk:65262", 10, 10);
        AisBusProvider provider2 = RoundRobinTcpClient.create(aisBus, "iala63.sealan.dk:4712,iala68.sealan.dk:4712", 10, 10);
        AisBusProvider provider3 = RoundRobinTcpClient.create(aisBus, "10.10.5.124:25251", 10, 10);
        AisBusProvider provider4 = RoundRobinTcpClient.create(aisBus, "10.10.5.144:65061", 10, 10);

        // Start providers
        provider1.start();
        provider2.start();
        provider3.start();
        provider4.start();

        try {
            aisBus.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
