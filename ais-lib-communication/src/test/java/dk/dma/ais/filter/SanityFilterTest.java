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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import java.util.function.Consumer;

public class SanityFilterTest {

    // @Test
    public void liveStreamTest() throws InterruptedException {
        AisTcpReader reader1 = AisReaders.createReader("ais163.sealan.dk:65262");
        AisTcpReader reader2 = AisReaders.createReader("10.10.5.144:65061");

        final SanityFilter sanityFilter = new SanityFilter();

        Consumer<AisPacket> handler = new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket packet) {
                AisMessage aisMessage = packet.tryGetAisMessage();
                if (aisMessage == null) {
                    return;
                }

                if (sanityFilter.rejectedByFilter(aisMessage)) {
                    // System.out.println("Rejected aisMessage: " + aisMessage + "\n" + packet + "\n---");

                }

            }
        };

        reader1.registerPacketHandler(handler);
        reader2.registerPacketHandler(handler);

        reader1.start();
        reader2.start();

        reader2.join();

    }

}
