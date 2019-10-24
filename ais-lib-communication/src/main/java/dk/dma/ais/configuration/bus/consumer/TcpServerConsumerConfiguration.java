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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.consumer.TcpServerConsumer;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServerConf;

/**
 * The type Tcp server consumer configuration.
 */
@XmlRootElement
public class TcpServerConsumerConfiguration extends AisBusConsumerConfiguration {

    private TcpClientConf clientConf = new TcpClientConf();
    private TcpServerConf serverConf = new TcpServerConf();

    /**
     * Instantiates a new Tcp server consumer configuration.
     */
    public TcpServerConsumerConfiguration() {

    }

    /**
     * Gets client conf.
     *
     * @return the client conf
     */
    public TcpClientConf getClientConf() {
        return clientConf;
    }

    /**
     * Sets client conf.
     *
     * @param clientConf the client conf
     */
    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    /**
     * Gets server conf.
     *
     * @return the server conf
     */
    public TcpServerConf getServerConf() {
        return serverConf;
    }

    /**
     * Sets server conf.
     *
     * @param serverConf the server conf
     */
    public void setServerConf(TcpServerConf serverConf) {
        this.serverConf = serverConf;
    }
    
    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        TcpServerConsumer server = new TcpServerConsumer();
        server.setClientConf(clientConf);
        server.setServerConf(serverConf);
        return super.configure(server);
    }

}
