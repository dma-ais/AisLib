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
package dk.dma.ais.configuration.bus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.bus.AisBus;
import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.configuration.bus.consumer.AisBusConsumerConfiguration;
import dk.dma.ais.configuration.bus.provider.AisBusProviderConfiguration;

@XmlRootElement
public class AisBusConfiguration extends AisBusComponentConfiguration {

    private int busPullMaxElements = 1000;
    private int busQueueSize = 10000;

    private List<AisBusProviderConfiguration> providers = new ArrayList<>();
    private List<AisBusConsumerConfiguration> consumers = new ArrayList<>();

    public AisBusConfiguration() {

    }

    public int getBusPullMaxElements() {
        return busPullMaxElements;
    }

    public void setBusPullMaxElements(int busPullMaxElements) {
        this.busPullMaxElements = busPullMaxElements;
    }

    public int getBusQueueSize() {
        return busQueueSize;
    }

    public void setBusQueueSize(int busQueueSize) {
        this.busQueueSize = busQueueSize;
    }

    @XmlElement(name = "provider")
    public List<AisBusProviderConfiguration> getProviders() {
        return providers;
    }

    public void setProviders(List<AisBusProviderConfiguration> providers) {
        this.providers = providers;
    }

    @XmlElement(name = "consumer")
    public List<AisBusConsumerConfiguration> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<AisBusConsumerConfiguration> consumers) {
        this.consumers = consumers;
    }

    @Override
    @XmlTransient
    public AisBus getInstance() {
        AisBus aisBus = new AisBus();
        aisBus.setBusQueueSize(busQueueSize);
        aisBus.setBusPullMaxElements(busPullMaxElements);
        configure(aisBus);
        aisBus.init();
        for (AisBusConsumerConfiguration consumerConf : consumers) {
            AisBusConsumer consumer = (AisBusConsumer) consumerConf.getInstance();
            if (consumer != null) {
                consumer.init();
                aisBus.registerConsumer(consumer);
            }
        }
        for (AisBusProviderConfiguration providerConf : providers) {
            AisBusProvider provider = (AisBusProvider) providerConf.getInstance();
            if (provider != null) {
                provider.init();
                aisBus.registerProvider(provider);
            }
        }
        return aisBus;
    }

    public static void save(OutputStream out, final AisBusConfiguration conf) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AisBusConfiguration.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.marshal(conf, out);
    }

    public static AisBusConfiguration load(InputStream in) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AisBusConfiguration.class);
        Unmarshaller um = context.createUnmarshaller();
        return (AisBusConfiguration) um.unmarshal(in);
    }
    
    public static void save(String filename, final AisBusConfiguration conf) throws JAXBException, FileNotFoundException {
        save(new FileOutputStream(new File(filename)), conf);
    }
    
    public static AisBusConfiguration load(String filename) throws FileNotFoundException, JAXBException {
        return load(new FileInputStream(new File(filename)));
    }
    
    

}
