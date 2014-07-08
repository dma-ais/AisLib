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
