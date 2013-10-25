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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.enav.util.function.Consumer;

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
