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
import dk.dma.ais.bus.consumer.TcpWriterConsumer;
import dk.dma.ais.bus.tcp.TcpClientConf;

/**
 * The type Tcp writer consumer configuration.
 */
@XmlRootElement
public class TcpWriterConsumerConfiguration extends AisBusConsumerConfiguration {

    private String host;
    private int port;
    private int reconnectInterval = 10;
    private TcpClientConf clientConf = new TcpClientConf();

    /**
     * Instantiates a new Tcp writer consumer configuration.
     */
    public TcpWriterConsumerConfiguration() {

    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets reconnect interval.
     *
     * @return the reconnect interval
     */
    public int getReconnectInterval() {
        return reconnectInterval;
    }

    /**
     * Sets reconnect interval.
     *
     * @param reconnectInterval the reconnect interval
     */
    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
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
    
    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        TcpWriterConsumer tcpw = new TcpWriterConsumer();
        tcpw.setPort(port);
        tcpw.setHost(host);
        tcpw.setClientConf(clientConf);
        tcpw.setReconnectInterval(reconnectInterval);
        return super.configure(tcpw);
    }

}
