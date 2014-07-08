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
