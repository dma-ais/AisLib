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

import org.junit.Test;

import dk.dma.ais.bus.consumer.StdoutConsumer;
import dk.dma.ais.bus.provider.RoundRobinTcpClient;

public class AisBusTest {
    
    @Test
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
        
        // Make provider
        AisBusProvider provider = RoundRobinTcpClient.create(aisBus, "ais163.sealan.dk:4712", 10, 10);
        // Start provider
        provider.start();
        
        
        
        try {
            aisBus.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    

}
