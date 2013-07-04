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
package dk.dma.ais.configuration.bus.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.provider.TcpClientProvider;
import dk.dma.ais.bus.tcp.TcpClientConf;

@XmlRootElement
public class TcpClientProviderConfiguration extends AisBusProviderConfiguration {

    private List<String> hostPort = new ArrayList<>();
    private TcpClientConf clientConf = new TcpClientConf();
    private int reconnectInterval = 10;
    private int timeout = 10;

    public TcpClientProviderConfiguration() {

    }

    public List<String> getHostPort() {
        return hostPort;
    }

    public void setHostPort(List<String> hostPort) {
        this.hostPort = hostPort;
    }


    public TcpClientConf getClientConf() {
        return clientConf;
    }

    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        TcpClientProvider clientProvider = new TcpClientProvider();
        clientProvider.setHostsPorts(hostPort);
        clientProvider.setTimeout(timeout);
        clientProvider.setReconnectInterval(reconnectInterval);
        clientProvider.setClientConf(clientConf);
        return super.configure(clientProvider);
    }

}
