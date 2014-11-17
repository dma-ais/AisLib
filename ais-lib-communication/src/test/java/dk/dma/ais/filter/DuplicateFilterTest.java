package dk.dma.ais.filter;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DuplicateFilterTest {

    MessageHandlerFilter doubletFilter;

    @Before
    public void setUp() throws Exception {
        doubletFilter = new MessageHandlerFilter(new DuplicateFilter());
    }

    // @Test
    public void doubletFilterTest() throws InterruptedException {
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

    @Test
    public void testRejectedByFilter() throws Exception {
        String msg = "\\si:AISW*2C\\\r\n"
                   + "$PGHP,1,2014,11,1,13,18,12,315,219,,2190049,1,48*2B\r\n"
                   + "!BSVDM,1,1,,B,155C1v0vif0r:s`OjGpM0b@F0<6R,0*48\r\n";

        AisPacket packet1 = AisPacket.readFromString(msg);
        AisPacket packet2 = AisPacket.readFromString(msg);

        DuplicateFilter filter = new DuplicateFilter();
        assertFalse(filter.rejectedByFilter(packet1));
        assertTrue(filter.rejectedByFilter(packet2));
    }

}
