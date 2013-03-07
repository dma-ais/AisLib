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
package dk.dma.ais.bus.provider;

import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpReadServer;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.util.function.Consumer;

/**
 * Server providing TCP connections for receiving date
 */
public class TcpServerProvider extends AisBusProvider implements Consumer<AisPacket> {

    private final TcpReadServer server;

    public TcpServerProvider() {
        server = new TcpReadServer(this);
    }

    /**
     * Get AisPacket from clients
     */
    @Override
    public void accept(AisPacket packet) {
        push(packet);
    }

    @Override
    public synchronized void init() {
        super.init();
    }

    @Override
    public synchronized void start() {
        setThread(server);
        server.start();
        super.start();
    }

    public void setClientConf(TcpClientConf clientConf) {
        server.setClientConf(clientConf);
    }

    public void setServerConf(TcpServerConf serverConf) {
        server.setServerConf(serverConf);
    }

}
