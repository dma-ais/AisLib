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
package dk.dma.ais.bus.configuration.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.bus.configuration.tcp.ClientConfiguration;

@XmlRootElement
public class TcpClientProviderConfiguration extends AisBusProviderConfiguration {

    private List<String> hostPort = new ArrayList<>();
    private ClientConfiguration clientConf = new ClientConfiguration();
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

    public ClientConfiguration getClientConf() {
        return clientConf;
    }

    public void setClientConf(ClientConfiguration clientConf) {
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

}
