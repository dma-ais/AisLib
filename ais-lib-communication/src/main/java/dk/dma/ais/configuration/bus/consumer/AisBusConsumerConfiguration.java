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
package dk.dma.ais.configuration.bus.consumer;

import javax.xml.bind.annotation.XmlSeeAlso;

import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.configuration.bus.AisBusSocketConfiguration;

/**
 * The type Ais bus consumer configuration.
 */
@XmlSeeAlso({ StdoutConsumerConfiguration.class, TcpWriterConsumerConfiguration.class, TcpServerConsumerConfiguration.class,
        DistributerConsumerConfiguration.class })
public abstract class AisBusConsumerConfiguration extends AisBusSocketConfiguration {

    private int consumerPullMaxElements = 1000;
    private int consumerQueueSize = 10000;

    /**
     * Instantiates a new Ais bus consumer configuration.
     */
    public AisBusConsumerConfiguration() {

    }

    /**
     * Gets consumer queue size.
     *
     * @return the consumer queue size
     */
    public int getConsumerQueueSize() {
        return consumerQueueSize;
    }

    /**
     * Sets consumer queue size.
     *
     * @param consumerQueueSize the consumer queue size
     */
    public void setConsumerQueueSize(int consumerQueueSize) {
        this.consumerQueueSize = consumerQueueSize;
    }

    /**
     * Gets consumer pull max elements.
     *
     * @return the consumer pull max elements
     */
    public int getConsumerPullMaxElements() {
        return consumerPullMaxElements;
    }

    /**
     * Sets consumer pull max elements.
     *
     * @param consumerPullMaxElements the consumer pull max elements
     */
    public void setConsumerPullMaxElements(int consumerPullMaxElements) {
        this.consumerPullMaxElements = consumerPullMaxElements;
    }

    /**
     * Configure ais bus consumer.
     *
     * @param consumer the consumer
     * @return the ais bus consumer
     */
    protected AisBusConsumer configure(AisBusConsumer consumer) {
        consumer.setConsumerPullMaxElements(consumerPullMaxElements);
        consumer.setConsumerQueueSize(consumerQueueSize);
        super.configure(consumer);
        return consumer;
    }

}
