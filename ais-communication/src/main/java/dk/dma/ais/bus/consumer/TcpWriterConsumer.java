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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;
import dk.dma.ais.bus.tcp.IClientStoppedListener;
import dk.dma.ais.bus.tcp.TcpClient;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpWriteClient;

/**
 * TCP client that connects to host/port and sends data. Will reconnect on connection error.
 */
@ThreadSafe
public class TcpWriterConsumer extends AisBusConsumer implements Runnable, IClientStoppedListener {

    private static final Logger LOG = LoggerFactory.getLogger(TcpWriterConsumer.class);

    @GuardedBy("this")
    private TcpWriteClient writeClient;
    
    private TcpClientConf clientConf = new TcpClientConf();
    private int reconnectInterval = 10;
    private String host;
    private int port;

    public TcpWriterConsumer() {

    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        synchronized (this) {
            if (writeClient != null) {
                writeClient.send(queueElement.getPacket().getStringMessage());
            }
        }

    }

    /**
     * Connect and reconnect
     */
    @Override
    public void run() {
        while (true) {
            Socket socket = new Socket();
            try {
                // Connect
                InetSocketAddress address = new InetSocketAddress(host, port);
                LOG.info("Connecting to " + host + ":" + port + " ...");
                socket.connect(address);
                socket.setKeepAlive(true);

                // Start client
                synchronized (this) {
                    writeClient = new TcpWriteClient(this, socket, clientConf);
                }
                writeClient.start();

                // Wait for client to loose connection
                writeClient.join();

                synchronized (this) {
                    writeClient = null;
                }

            } catch (IOException e) {
                LOG.info("Connection error");
            } catch (InterruptedException e) {
                // TODO handle
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }

            try {
                LOG.info("Waiting to reconnect");
                Thread.sleep(reconnectInterval * 1000);
            } catch (InterruptedException e) {
                // TODO handle interuption
                e.printStackTrace();
            }
        }

    }

    @Override
    public void start() {
        Thread t = new Thread(this);
        setThread(t);
        t.start();
        super.start();
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
    public void clientStopped(TcpClient client) {

    }

}
