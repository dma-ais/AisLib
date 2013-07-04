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
package dk.dma.ais.filter;

import junit.framework.Assert;

import org.junit.Test;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.reader.RoundRobinAisTcpReader;
import dk.dma.enav.util.function.Consumer;

public class DoubletFilterTest {

    @Test
    public void simpleTest() {

    }

    // @Test
    public void doubletFilterTest() throws InterruptedException {
        MessageHandlerFilter doubletFilter = new MessageHandlerFilter(new DuplicateFilter());

        doubletFilter.registerReceiver(new Consumer<AisMessage>() {
            private long lastReceived;

            @Override
            public void accept(AisMessage msg) {
                if (msg instanceof AisMessage4 && msg.getUserId() == 2190047) {
                    System.out.println("BS message");
                    // Message 4 from BS 2190047
                    long now = System.currentTimeMillis();
                    long elapsed = now - lastReceived;
                    if (elapsed < 3000) {
                        Assert.fail("Duplicate filter fail");
                    }
                    lastReceived = now;
                }

            }
        });

        // Connect to unfiltered sources
        RoundRobinAisTcpReader reader1 = new RoundRobinAisTcpReader();
        reader1.setCommaseparatedHostPort("ais163.sealan.dk:65262");
        RoundRobinAisTcpReader reader2 = new RoundRobinAisTcpReader();
        reader2.setCommaseparatedHostPort("10.10.5.144:65061");

        reader1.registerHandler(doubletFilter);
        reader2.registerHandler(doubletFilter);

        reader1.start();
        reader2.start();

        reader2.join();
    }

    // @Test
    public void downsampleTest() throws InterruptedException {
        MessageHandlerFilter filter = new MessageHandlerFilter(new DownSampleFilter());

        filter.registerReceiver(new Consumer<AisMessage>() {
            private long lastReceived;

            @Override
            public void accept(AisMessage msg) {
                if (msg instanceof AisMessage4 && msg.getUserId() == 2190047) {
                    System.out.println("BS message");
                    // Message 4 from BS 2190047
                    long now = System.currentTimeMillis();
                    long elapsed = now - lastReceived;
                    if (elapsed < 60000) {
                        Assert.fail("Duplicate filter fail");
                    }
                    lastReceived = now;
                }

            }
        });

        // Connect to unfiltered sources
        RoundRobinAisTcpReader reader1 = new RoundRobinAisTcpReader();
        reader1.setCommaseparatedHostPort("ais163.sealan.dk:4712");

        reader1.registerHandler(filter);
        reader1.start();
        reader1.join();
    }

}
