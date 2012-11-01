package dk.dma.enav.ais.filter;

import org.junit.*;

import dk.dma.enav.ais.filter.MessageDoubletFilter;
import dk.dma.enav.ais.reader.RoundRobinAisTcpReader;

public class DoubletFilterTest {

    @Ignore
    @Test
    public void doubletFilterTest() throws InterruptedException {
        MessageDoubletFilter doubletFilter = new MessageDoubletFilter();

        // Connect to unfiltered sources
        RoundRobinAisTcpReader reader1 = new RoundRobinAisTcpReader();
        reader1.setCommaseparatedHostPort("ais163.sealan.dk:65262");
        RoundRobinAisTcpReader reader2 = new RoundRobinAisTcpReader();
        reader2.setCommaseparatedHostPort("10.10.5.144:65261");

        reader1.registerHandler(doubletFilter);
        reader2.registerHandler(doubletFilter);

        reader1.start();
        reader2.start();

        reader2.join();
    }

}
