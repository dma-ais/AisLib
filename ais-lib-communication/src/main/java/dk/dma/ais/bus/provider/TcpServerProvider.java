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
package dk.dma.ais.bus.provider;

import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpReadServer;
import dk.dma.ais.bus.tcp.TcpServer;
import dk.dma.ais.bus.tcp.TcpServerConf;
import dk.dma.ais.packet.AisPacket;
import java.util.function.Consumer;

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
    
    @Override
    public void cancel() {
        server.cancel();
        try {
            server.join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setStopped();
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
