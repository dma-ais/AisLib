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
package dk.dma.ais.bus.consumer;

import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpServer;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.bus.tcp.TcpWriteServer;

/**
 * Server providing TCP connections sending data
 */
public class TcpServerConsumer extends AisBusConsumer {

    private final TcpWriteServer server = new TcpWriteServer();

    public TcpServerConsumer() {

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
    
    @Override
    public void cancel() {
        server.cancel();
        try {
            server.join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.cancel();
        setStopped();
    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        server.send(queueElement.getPacket().getStringMessage());
    }
    
    public void setClientConf(TcpClientConf clientConf) {
        server.setClientConf(clientConf);        
    }
    
    public void setServerConf(TcpServerConf serverConf) {
        server.setServerConf(serverConf);
    }
    
    public TcpServer getServer() {
        return server;
    }

}
