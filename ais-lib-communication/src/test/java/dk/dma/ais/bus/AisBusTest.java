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

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.configuration.bus.AisBusConfiguration;
import dk.dma.ais.configuration.bus.consumer.DistributerConsumerConfiguration;
import dk.dma.ais.configuration.bus.consumer.StdoutConsumerConfiguration;
import dk.dma.ais.configuration.bus.consumer.TcpServerConsumerConfiguration;
import dk.dma.ais.configuration.bus.consumer.TcpWriterConsumerConfiguration;
import dk.dma.ais.configuration.bus.provider.FileReaderProviderConfiguration;
import dk.dma.ais.configuration.bus.provider.TcpClientProviderConfiguration;
import dk.dma.ais.configuration.bus.provider.TcpServerProviderConfiguration;
import dk.dma.ais.configuration.filter.DownSampleFilterConfiguration;
import dk.dma.ais.configuration.filter.DuplicateFilterConfiguration;
import dk.dma.ais.configuration.filter.FilterConfiguration;
import dk.dma.ais.configuration.filter.GatehouseSourceFilterConfiguration;
import dk.dma.ais.configuration.filter.LocationFilterConfiguration;
import dk.dma.ais.configuration.filter.PacketFilterCollectionConfiguration;
import dk.dma.ais.configuration.filter.TaggingFilterConfiguration;
import dk.dma.ais.configuration.filter.TargetCountryFilterConfiguration;
import dk.dma.ais.configuration.filter.geometry.CircleGeometryConfiguration;
import dk.dma.ais.configuration.transform.AnonymousTransfomerConfiguration;
import dk.dma.ais.configuration.transform.CropVdmTransformerConfiguration;
import dk.dma.ais.configuration.transform.PacketTaggingConfiguration;
import dk.dma.ais.configuration.transform.ReplayTransformConfiguration;
import dk.dma.ais.configuration.transform.SourceTypeSatTransformerConfiguration;
import dk.dma.ais.configuration.transform.TaggingTransformerConfiguration;
import dk.dma.ais.filter.PacketFilterCollection;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

public class AisBusTest {

    @Test
    public void confTest() throws JAXBException, FileNotFoundException {
        AisBusConfiguration conf = new AisBusConfiguration();
        // Bus Filters
        conf.getFilters().add(new DownSampleFilterConfiguration());
        conf.getFilters().add(new DuplicateFilterConfiguration());

        SourceTypeSatTransformerConfiguration satSourceConf = new SourceTypeSatTransformerConfiguration();
        satSourceConf.getSatGhRegions().add("802");
        satSourceConf.getSatGhRegions().add("804");
        satSourceConf.getSatSources().add("ORBCOMM999");
        conf.getTransformers().add(satSourceConf);

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
        spConf.setServerConf(spServerConf);
        TcpClientConf spClientConf = new TcpClientConf();
        spClientConf.setGzipCompress(true);
        spConf.setClientConf(spClientConf);
        conf.getProviders().add(spConf);

        // Provider
        FileReaderProviderConfiguration fileConf = new FileReaderProviderConfiguration();
        fileConf.setFilename("/tmp/aisdump.txt");
        ReplayTransformConfiguration replayConf = new ReplayTransformConfiguration();
        fileConf.getTransformers().add(replayConf);
        GatehouseSourceFilterConfiguration ghFilter = new GatehouseSourceFilterConfiguration();
        ghFilter.getFilterEntries().put("region", "8");
        fileConf.getFilters().add(ghFilter);
        TargetCountryFilterConfiguration tgtFilter = new TargetCountryFilterConfiguration();
        tgtFilter.getAllowedCountries().add("DNK");
        tgtFilter.getAllowedCountries().add("SWE");
        CircleGeometryConfiguration circleConf = new CircleGeometryConfiguration();
        circleConf.setLat(55.7);
        circleConf.setLon(12.6);
        circleConf.setRadius(20000);
        LocationFilterConfiguration locFilter = new LocationFilterConfiguration();
        locFilter.getGeometries().add(circleConf);
        fileConf.getFilters().add(locFilter);
        fileConf.getFilters().add(tgtFilter);
        fileConf.getTransformers().add(new AnonymousTransfomerConfiguration());

        PacketTaggingConfiguration tagging = new PacketTaggingConfiguration();
        tagging.setSourceBs(2190048);
        TaggingFilterConfiguration tagFilter = new TaggingFilterConfiguration();
        tagFilter.setFilterTagging(tagging);
        fileConf.getFilters().add(tagFilter);
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
        PacketFilterCollectionConfiguration filterCollection = new PacketFilterCollectionConfiguration();
        LocationFilterConfiguration locFilter1 = new LocationFilterConfiguration();
        circleConf = new CircleGeometryConfiguration();
        circleConf.setLat(36.3);
        circleConf.setLon(127.3);
        circleConf.setRadius(370400);
        locFilter1.getGeometries().add(circleConf);
        filterCollection.addFilterConfiguration(locFilter1);
        LocationFilterConfiguration locFilter2 = new LocationFilterConfiguration();
        circleConf = new CircleGeometryConfiguration();
        circleConf.setLat(55.3);
        circleConf.setLon(11.5);
        circleConf.setRadius(125000);
        locFilter2.getGeometries().add(circleConf);
        filterCollection.addFilterConfiguration(locFilter2);
        filterCollection.setFilterType(PacketFilterCollection.TYPE_OR);
        tcpWriter.getFilters().add(filterCollection);

        // Consumer
        TcpServerConsumerConfiguration tcpServer = new TcpServerConsumerConfiguration();
        TcpClientConf clc2 = new TcpClientConf();
        clc2.setGzipCompress(true);
        TcpServerConf serverConf = new TcpServerConf();
        serverConf.setPort(9999);
        tcpServer.setClientConf(clc2);
        tcpServer.setServerConf(serverConf);
        conf.getConsumers().add(tcpServer);

        // Consumer
        DistributerConsumerConfiguration distributerConf = new DistributerConsumerConfiguration();
        distributerConf.setName("DISTRIBUTER");
        conf.getConsumers().add(distributerConf);

        // Save
        AisBusConfiguration.save("aisbus.xml", conf);

        // Load
        conf = AisBusConfiguration.load("aisbus.xml");
        Assert.assertEquals(conf.getBusQueueSize(), 10000);

    }

    @Test
    public void confLoadTest() throws JAXBException, FileNotFoundException {
        AisBusConfiguration conf = AisBusConfiguration.load("src/main/resources/aisbus-test.xml");
        Assert.assertEquals(conf.getBusQueueSize(), 10000);
        List<FilterConfiguration> filters = conf.getFilters();
        for (FilterConfiguration filter : filters) {
            if (filter instanceof DownSampleFilterConfiguration) {
                Assert.assertEquals(((DownSampleFilterConfiguration) filter).getSamplingRate(), 60);
            } else if (filter instanceof DuplicateFilterConfiguration) {
                Assert.assertEquals(((DuplicateFilterConfiguration) filter).getWindowSize(), 10000);
            } else {
                Assert.fail();
            }
        }
        // StdoutConsumerConfiguration consumerConf = (StdoutConsumerConfiguration)conf.getConsumers().get(1);
        // Assert.assertEquals(consumerConf.getConsumerPullMaxElements(), 1);
    }

    @Test
    public void aisBusTest() throws JAXBException, FileNotFoundException {
        AisBus aisBus = AisBusFactory.get("src/main/resources/aisbus-test.xml");

        int threadCount = Thread.activeCount();

        aisBus.start();
        aisBus.startConsumers();
        aisBus.startProviders();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Threads: " + Thread.activeCount());
        System.out.println("AisBus: " + aisBus.getStatus());
        for (AisBusProvider provider : aisBus.getProviders()) {
            System.out.println("Provider: " + provider.toString());
        }
        for (AisBusConsumer consumer : aisBus.getConsumers()) {
            System.out.println("Consumer: " + consumer.toString());
        }

        System.out.println("Stopping AisBus");
        aisBus.cancel();
        System.out.println("Done stopping AisBus");

        Assert.assertEquals(threadCount, Thread.activeCount());

    }

    // @Test
    public void aisBusTest2() throws JAXBException, FileNotFoundException {
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
