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
package dk.dma.ais.filter;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import java.util.function.Consumer;

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
        AisTcpReader reader1 = AisReaders.createReader("ais163.sealan.dk:65262");
        AisTcpReader reader2 = AisReaders.createReader("10.10.5.144:65061");

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
        AisTcpReader reader1 = AisReaders.createReader("ais163.sealan.dk:4712");

        reader1.registerHandler(filter);
        reader1.start();
        reader1.join();
    }

}
