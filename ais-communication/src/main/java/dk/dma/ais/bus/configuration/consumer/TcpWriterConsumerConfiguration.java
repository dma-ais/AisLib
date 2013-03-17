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
package dk.dma.ais.bus.configuration.consumer;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.consumer.TcpWriterConsumer;
import dk.dma.ais.bus.tcp.TcpClientConf;

@XmlRootElement
public class TcpWriterConsumerConfiguration extends AisBusConsumerConfiguration {

    private String host;
    private int port;
    private int reconnectInterval = 10;
    private TcpClientConf clientConf;

    public TcpWriterConsumerConfiguration() {

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public TcpClientConf getClientConf() {
        return clientConf;
    }

    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }
    
    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        TcpWriterConsumer tcpw = new TcpWriterConsumer();
        tcpw.setPort(port);
        tcpw.setHost(host);
        tcpw.setClientConf((clientConf == null) ? new TcpClientConf() : clientConf);
        tcpw.setReconnectInterval(reconnectInterval);
        return super.configure(tcpw);
    }

}
