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

import javax.xml.bind.JAXBException;

import dk.dma.ais.bus.configuration.AisBusComponentConfiguration;
import dk.dma.ais.bus.configuration.AisBusConfiguration;
import dk.dma.ais.bus.configuration.AisBusSocketConfiguration;
import dk.dma.ais.bus.configuration.consumer.AisBusConsumerConfiguration;
import dk.dma.ais.bus.configuration.consumer.StdoutConsumerConfiguration;
import dk.dma.ais.bus.configuration.consumer.TcpServerConsumerConfiguration;
import dk.dma.ais.bus.configuration.consumer.TcpWriterConsumerConfiguration;
import dk.dma.ais.bus.configuration.filter.DownSampleFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.DuplicateFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.FilterConfiguration;
import dk.dma.ais.bus.configuration.provider.AisBusProviderConfiguration;
import dk.dma.ais.bus.configuration.provider.TcpClientProviderConfiguration;
import dk.dma.ais.bus.configuration.provider.TcpServerProviderConfiguration;
import dk.dma.ais.bus.configuration.tcp.ClientConfiguration;
import dk.dma.ais.bus.configuration.tcp.ServerConfiguration;
import dk.dma.ais.bus.configuration.transform.CropVdmTransformerConfiguration;
import dk.dma.ais.bus.configuration.transform.TransformerConfiguration;
import dk.dma.ais.bus.consumer.StdoutConsumer;
import dk.dma.ais.bus.consumer.TcpServerConsumer;
import dk.dma.ais.bus.consumer.TcpWriterConsumer;
import dk.dma.ais.bus.provider.TcpClientProvider;
import dk.dma.ais.bus.provider.TcpServerProvider;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.filter.DownSampleFilter;
import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.transform.CropVdmTransformer;
import dk.dma.ais.transform.IAisPacketTransformer;

public class AisBusFactory {

    public static AisBus get(String filename) throws JAXBException {
        AisBusConfiguration conf = AisBusConfiguration.load(filename);
        return get(conf);
    }

    public static AisBus get(AisBusConfiguration conf) {
        AisBus aisBus = new AisBus();

        // Configure AisBus
        aisBus.setBusQueueSize(conf.getBusQueueSize());
        aisBus.setBusPullMaxElements(conf.getBusPullMaxElements());
        // Configure component parts of AisBus
        componentConfigure(aisBus, conf);
        // Initialize
        aisBus.init();

        // Make and configure consumers
        for (AisBusConsumerConfiguration consumerConf : conf.getConsumers()) {
            AisBusConsumer consumer;
            // Specific configurations
            if (consumerConf instanceof StdoutConsumerConfiguration) {
                consumer = new StdoutConsumer();
            } else if (consumerConf instanceof TcpServerConsumerConfiguration) {
                TcpServerConsumerConfiguration serverConf = (TcpServerConsumerConfiguration) consumerConf;
                TcpServerConsumer server = new TcpServerConsumer();
                server.setClientConf(getClientConf(serverConf.getClientConf()));
                server.setServerConf(getServerConf(serverConf.getServerConf()));
                consumer = server;
            } else if (consumerConf instanceof TcpWriterConsumerConfiguration) {
                TcpWriterConsumerConfiguration tcpwConf = (TcpWriterConsumerConfiguration) consumerConf;
                TcpWriterConsumer tcpw = new TcpWriterConsumer();
                tcpw.setPort(tcpwConf.getPort());
                tcpw.setHost(tcpwConf.getHost());
                tcpw.setClientConf(getClientConf(tcpwConf.getClientConf()));
                tcpw.setReconnectInterval(tcpwConf.getReconnectInterval());
                consumer = tcpw;
            } else {
                throw new IllegalArgumentException("Unknown consumer: " + consumerConf.getClass());
            }
            // Do consumer configuration
            consumer.setConsumerPullMaxElements(consumerConf.getConsumerPullMaxElements());
            consumer.setConsumerQueueSize(consumerConf.getConsumerQueueSize());
            // Do component configuration
            componentConfigure(consumer, consumerConf);
            // Do socket configuration
            socketConfigure(consumer, consumerConf);
            // Initialize
            consumer.init();
            // Add to ais bus
            aisBus.registerConsumer(consumer);
        }

        // Make and configure providers
        for (AisBusProviderConfiguration providerConf : conf.getProviders()) {
            AisBusProvider provider;
            // Specific configurations
            if (providerConf instanceof TcpClientProviderConfiguration) {
                TcpClientProviderConfiguration cpConf = (TcpClientProviderConfiguration) providerConf;
                TcpClientProvider clientProvider = new TcpClientProvider();
                clientProvider.setHostsPorts(cpConf.getHostPort());
                clientProvider.setTimeout(cpConf.getTimeout());
                clientProvider.setReconnectInterval(cpConf.getReconnectInterval());
                clientProvider.setClientConf(getClientConf(cpConf.getClientConf()));                
                provider = clientProvider;
            } else if (providerConf instanceof TcpServerProviderConfiguration) {
                TcpServerProviderConfiguration serverConf = (TcpServerProviderConfiguration)providerConf;
                TcpServerProvider server = new TcpServerProvider();
                server.setClientConf(getClientConf(serverConf.getClientConf()));
                server.setServerConf(getServerConf(serverConf.getServerConf()));
                provider = server;
            } else {
                throw new IllegalArgumentException("Unknown provider: " + providerConf.getClass());
            }
            // Do component configuration
            componentConfigure(provider, providerConf);
            // Do socket configuration
            socketConfigure(provider, providerConf);
            // Initialize
            provider.init();
            // Add to ais bus
            aisBus.registerProvider(provider);
        }

        return aisBus;
    }

    private static TcpServerConf getServerConf(ServerConfiguration conf) {
        TcpServerConf serverConf = new TcpServerConf();
        if (conf != null) {
            serverConf.setPort(conf.getPort());
            serverConf.setMaxClients(conf.getMaxClients());
        }
        return serverConf;
    }

    private static TcpClientConf getClientConf(ClientConfiguration conf) {
        TcpClientConf clientConf = new TcpClientConf();
        if (conf != null) {
            clientConf.setBufferSize(conf.getBufferSize());
            clientConf.setGzipBufferSize(conf.getGzipBufferSize());
            clientConf.setGzipCompress(conf.isGzipCompress());
        }
        return clientConf;
    }

    private static void socketConfigure(AisBusSocket socket, AisBusSocketConfiguration conf) {
        socket.setName(conf.getName());
        socket.setDescription(conf.getDescription());
    }

    private static void componentConfigure(AisBusComponent comp, AisBusComponentConfiguration conf) {
        // Add filters
        for (FilterConfiguration filterConf : conf.getFilters()) {
            IPacketFilter filter;
            if (filterConf instanceof DownSampleFilterConfiguration) {
                DownSampleFilterConfiguration dsFilterConf = (DownSampleFilterConfiguration) filterConf;
                filter = new DownSampleFilter(dsFilterConf.getSamplingRate());
            } else if (filterConf instanceof DuplicateFilterConfiguration) {
                DuplicateFilterConfiguration dupFilterConf = (DuplicateFilterConfiguration) filterConf;
                filter = new DuplicateFilter(dupFilterConf.getWindowSize());
            } else {
                throw new IllegalArgumentException("Unknown filter: " + filterConf.getClass());
            }
            // Add to filters
            comp.getFilters().addFilter(filter);
        }
        // Add transformers
        for (TransformerConfiguration transConf : conf.getTransformers()) {
            IAisPacketTransformer transformer;
            if (transConf instanceof CropVdmTransformerConfiguration) {
                transformer = new CropVdmTransformer();
            } else {
                throw new IllegalArgumentException("Unknown packet transformer: " + transConf.getClass());
            }
            // Add to transformers
            comp.getPacketTransformers().add(transformer);
        }
    }

}
