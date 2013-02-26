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
import dk.dma.ais.bus.configuration.filter.DownSampleFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.DuplicateFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.FilterConfiguration;
import dk.dma.ais.bus.configuration.provider.AisBusProviderConfiguration;
import dk.dma.ais.bus.configuration.provider.RoundRobinTcpClientConfiguration;
import dk.dma.ais.bus.consumer.StdoutConsumer;
import dk.dma.ais.bus.provider.RoundRobinTcpClient;
import dk.dma.ais.filter.DownSampleFilter;
import dk.dma.ais.filter.DuplicateFilter;
import dk.dma.ais.filter.IPacketFilter;

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
        aisBus.setConsumerPullMaxElements(conf.getConsumerPullMaxElements());
        aisBus.setConsumerQueueSize(conf.getConsumerQueueSize());
        // Configure component parts of AisBus
        componentConfigure(aisBus, conf);

        // Start aisbus
        aisBus.start();

        // Make and configure consumers
        for (AisBusConsumerConfiguration consumerConf : conf.getConsumers()) {
            AisBusConsumer consumer;
            // Specific configurations
            if (consumerConf instanceof StdoutConsumerConfiguration) {
                consumer = new StdoutConsumer();
            } else {
                throw new IllegalArgumentException("Unknown consumer: " + consumerConf.getClass());
            }
            // Do component configuration
            componentConfigure(consumer, consumerConf);
            // Do socket configuration
            socketConfigure(consumer, consumerConf);
            // Add to ais bus
            aisBus.registerConsumer(consumer);
        }

        // Make and configure providers
        for (AisBusProviderConfiguration providerConf : conf.getProviders()) {
            AisBusProvider provider;
            // Specific configurations
            if (providerConf instanceof RoundRobinTcpClientConfiguration) {
                RoundRobinTcpClientConfiguration rrConf = (RoundRobinTcpClientConfiguration)providerConf;
                RoundRobinTcpClient rrClient = new RoundRobinTcpClient(rrConf.getHostsPorts(),
                        rrConf.getInterval(), rrConf.getTimeout());
                provider = rrClient;
            } else {
                throw new IllegalArgumentException("Unknown provider: " + providerConf.getClass());
            }
            // Do component configuration
            componentConfigure(provider, providerConf);
            // Do socket configuration
            socketConfigure(provider, providerConf);
            // Add to ais bus
            aisBus.registerProvider(provider);
        }

        
        return aisBus;
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
                DownSampleFilterConfiguration dsFilterConf = (DownSampleFilterConfiguration)filterConf;
                filter = new DownSampleFilter(dsFilterConf.getSamplingRate());                
            } else if (filterConf instanceof DuplicateFilterConfiguration) {
                DuplicateFilterConfiguration dupFilterConf = (DuplicateFilterConfiguration)filterConf;
                filter = new DuplicateFilter(dupFilterConf.getWindowSize());
            } else {
                throw new IllegalArgumentException("Unknown filter: " + filterConf.getClass());
            }
            // Add to filters
            comp.getFilters().addFilter(filter);
        }
    }

}
