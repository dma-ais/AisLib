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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisTestPackets;
import dk.dma.ais.reader.AisDirectoryReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import dk.dma.ais.sentence.SentenceException;

public class FuturePastFilterTest {

    @Ignore
    @Test
    public void liveStreamTest() throws InterruptedException {
        AisTcpReader reader1 = AisReaders.createReader("127.0.0.1:8001");

        /*
         * Note: what about the few ms between testing one packet and another.
         * The filters are based on separate System.currentTimeMillis(), thus in
         * theory they are not exactly binary opposites, when testing.
         */
        final FutureFilter f = new FutureFilter(60000);
        final PastFilter p = new PastFilter(60000);

        reader1.registerPacketHandler(packet -> {
            if (packet != null && f.rejectedByFilter(packet)) {

                assertFalse(p.rejectedByFilter(packet));

                System.out.println("Message was rejected");
                System.out.println("Current Date: " + new Date());
                System.out.println("Message Date:"
                        + new Date(packet.getBestTimestamp()));
                System.out.println(packet.getStringMessage());
            }

            if (packet != null && !f.rejectedByFilter(packet)) {
                long timestamp = packet.getBestTimestamp();
                long now = new Date().getTime();
                assertTrue(now + 60000 > timestamp);
            }

            if (packet != null && p.rejectedByFilter(packet)) {
                assertFalse(f.rejectedByFilter(packet));
            }
        });

        reader1.start();
        reader1.join();
    }

    @Test
    public void testDatesNotInFuture() throws IOException, InterruptedException {

        final FutureFilter f = new FutureFilter();
        final PastFilter p = new PastFilter();

        assertFalse(f.rejectedByFilter(AisTestPackets.p1()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p2()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p3()));
        assertFalse(f.rejectedByFilter(AisTestPackets.p4()));

        assertTrue(p.rejectedByFilter(AisTestPackets.p1()));
        assertTrue(p.rejectedByFilter(AisTestPackets.p2()));
        assertTrue(p.rejectedByFilter(AisTestPackets.p3()));
        assertTrue(p.rejectedByFilter(AisTestPackets.p4()));

        AisDirectoryReader directoryReader = AisReaders.createDirectoryReader(
                "src/test", "stream_example.txt", true);

        directoryReader.registerPacketHandler(packet -> assertFalse(f
                .rejectedByFilter(packet)));
        directoryReader.registerPacketHandler(packet -> assertTrue(p
                .rejectedByFilter(packet)));

        directoryReader.start();
        directoryReader.join();

    }

    @Ignore
    @Test
    public void testDatesInFutureRejected() throws SentenceException {
        final FutureFilter f = new FutureFilter();

        // String ais =
        // "$PGHP,1,2014,12,28,18,56,24,510,207,,,1,23*21\r\n!AIVDM,1,1,,A,39t6QiEP001w@7hHf0h2KwvWP000,0*23";
        String ais = "$PGHP,1,2014,12,28,19,10,22,760,207,,,1,39*2A\r\n!ABVDM,1,1,,B,16vdSv00001ukHBHCo;MOh0R0<0i,0*39";

        AisPacket p = AisPacket.readFromString(ais);

        System.out.println("Date");
        System.out.println(new Date(p.getBestTimestamp()));

        assertTrue(f.rejectedByFilter(p));

    }

    /**
     * TODO: revisit, relax the requirements?
     */
    @Ignore
    @Test
    public void testPastBoundaryFiltering() throws InterruptedException {
        AisPacket packet = AisTestPackets.p1();
        final long packetTime = packet.getBestTimestamp();

        for (int i = 0; i < 10000; i++) {
            long offset = System.currentTimeMillis() - packetTime - 1;
            assertTrue(new PastFilter(offset).rejectedByFilter(packet));
        }

        for (int i = 0; i < 10000; i++) {
            long offset = System.currentTimeMillis() - packetTime + 1;
            assertFalse(new PastFilter(offset).rejectedByFilter(packet));
        }
    }
}
