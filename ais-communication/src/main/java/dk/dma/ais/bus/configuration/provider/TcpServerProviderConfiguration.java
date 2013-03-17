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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.provider.TcpServerProvider;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServerConf;

@XmlRootElement
public class TcpServerProviderConfiguration extends AisBusProviderConfiguration {

    private TcpClientConf clientConf;
    private TcpServerConf serverConf;

    public TcpServerProviderConfiguration() {

    }

    public TcpClientConf getClientConf() {
        return clientConf;
    }

    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    public TcpServerConf getServerConf() {
        return serverConf;
    }

    public void setServerConf(TcpServerConf serverConf) {
        this.serverConf = serverConf;
    }
    
    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        TcpServerProvider server = new TcpServerProvider();
        server.setClientConf((clientConf == null) ? new TcpClientConf() : clientConf);
        server.setServerConf((serverConf == null) ? new TcpServerConf() : serverConf);
        return super.configure(server);
    }

}
