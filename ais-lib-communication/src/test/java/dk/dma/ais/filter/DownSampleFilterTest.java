package dk.dma.ais.filter;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;
import org.junit.Assert;

import java.util.function.Consumer;

public class DownSampleFilterTest {

    //@Test
    public void testRejectedByFilter() throws Exception {
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
