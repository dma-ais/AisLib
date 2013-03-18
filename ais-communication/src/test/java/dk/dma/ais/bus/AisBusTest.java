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

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.bus.configuration.AisBusConfiguration;
import dk.dma.ais.bus.configuration.consumer.StdoutConsumerConfiguration;
import dk.dma.ais.bus.configuration.consumer.TcpServerConsumerConfiguration;
import dk.dma.ais.bus.configuration.consumer.TcpWriterConsumerConfiguration;
import dk.dma.ais.bus.configuration.filter.DownSampleFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.DuplicateFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.FilterConfiguration;
import dk.dma.ais.bus.configuration.provider.FileReaderProviderConfiguration;
import dk.dma.ais.bus.configuration.provider.TcpClientProviderConfiguration;
import dk.dma.ais.bus.configuration.provider.TcpServerProviderConfiguration;
import dk.dma.ais.bus.configuration.transform.CropVdmTransformerConfiguration;
import dk.dma.ais.bus.configuration.transform.TaggingTransformerConfiguration;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

public class AisBusTest {

    @Test
    public void confTest() throws JAXBException {
        AisBusConfiguration conf = new AisBusConfiguration();
        // Bus Filters
        conf.getFilters().add(new DownSampleFilterConfiguration());
        conf.getFilters().add(new DuplicateFilterConfiguration());
        
        // Provider
        TcpClientProviderConfiguration rrReader = new TcpClientProviderConfiguration();
        rrReader.getHostPort().add("ais163.sealan.dk:65262");
        rrReader.getFilters().add(new DownSampleFilterConfiguration(300));
        TcpClientConf rrReaderConf = new TcpClientConf();
        rrReader.setClientConf(rrReaderConf);
        TaggingTransformerConfiguration tagConf = new TaggingTransformerConfiguration();
        tagConf.setTagPolicy(Policy.PREPEND_MISSING);        
        tagConf.getTagging().setSourceId("AISD");
        tagConf.getTagging().setSourceCountry("DNK");
        tagConf.getTagging().setSourceType("SAT");
        tagConf.getExtraTags().put("somekey", "someval");        
        rrReader.getTransformers().add(tagConf);
        conf.getProviders().add(rrReader);
        
        // Provider
        TcpServerProviderConfiguration spConf = new TcpServerProviderConfiguration();
        TcpServerConf spServerConf = new TcpServerConf();
        spServerConf.setPort(9998);
        spConf.setServerConf(spServerConf) ;
        TcpClientConf spClientConf = new TcpClientConf();
        spClientConf.setGzipCompress(true);
        spConf.setClientConf(spClientConf);
        conf.getProviders().add(spConf);
        
        // Provider
        FileReaderProviderConfiguration fileConf = new FileReaderProviderConfiguration();
        fileConf.setFilename("/tmp/aisdump.txt");
        conf.getProviders().add(fileConf);
        
        // Consumer
        StdoutConsumerConfiguration stdoutConsumer = new StdoutConsumerConfiguration();
        stdoutConsumer.getFilters().add(new DownSampleFilterConfiguration(600));
        stdoutConsumer.getTransformers().add(new CropVdmTransformerConfiguration());
        stdoutConsumer.setConsumerPullMaxElements(1);
        conf.getConsumers().add(stdoutConsumer);
        
        // Consumer
        TcpWriterConsumerConfiguration tcpWriter = new TcpWriterConsumerConfiguration();
        TcpClientConf clc1 = new TcpClientConf();
        clc1.setBufferSize(1);
        tcpWriter.setClientConf(clc1);
        tcpWriter.setPort(8089);
        tcpWriter.setHost("localhost");
        conf.getConsumers().add(tcpWriter);
        
        // Consumer
        TcpServerConsumerConfiguration tcpServer = new TcpServerConsumerConfiguration();
        TcpClientConf clc2 = new TcpClientConf();
        clc2.setGzipCompress(true);
        TcpServerConf serverConf = new TcpServerConf();
        serverConf.setPort(9999);
        tcpServer.setClientConf(clc2);
        tcpServer.setServerConf(serverConf);
        conf.getConsumers().add(tcpServer);
        
        
        // Save
        AisBusConfiguration.save("aisbus.xml", conf);
        
        // Load
        conf = AisBusConfiguration.load("aisbus.xml");      
        Assert.assertEquals(conf.getBusQueueSize(), 10000);        
        
    }
    
    @Test
    public void confLoadTest() throws JAXBException {
        AisBusConfiguration conf = AisBusConfiguration.load("src/main/resources/aisbus-example.xml");
        Assert.assertEquals(conf.getBusQueueSize(), 10000);
        List<FilterConfiguration> filters = conf.getFilters();
        for (FilterConfiguration filter : filters) {
            if (filter instanceof DownSampleFilterConfiguration) {
                Assert.assertEquals(((DownSampleFilterConfiguration)filter).getSamplingRate(), 60);
            }
            else if (filter instanceof DuplicateFilterConfiguration) {
                Assert.assertEquals(((DuplicateFilterConfiguration)filter).getWindowSize(), 10000);
            }
            else {
                Assert.fail();
            }
        }
//        StdoutConsumerConfiguration consumerConf = (StdoutConsumerConfiguration)conf.getConsumers().get(1);
//        Assert.assertEquals(consumerConf.getConsumerPullMaxElements(), 1);
    }
    
    @Test
    public void factoryTest() throws JAXBException, InterruptedException {
        AisBus aisBus = AisBusFactory.get("src/main/resources/aisbus-example.xml");
        aisBus.start();
        aisBus.startConsumers();
        aisBus.startProviders();
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
    }


}
