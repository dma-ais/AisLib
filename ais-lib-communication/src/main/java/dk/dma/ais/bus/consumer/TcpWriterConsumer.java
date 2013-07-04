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

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;
import dk.dma.ais.bus.OverflowLogger;
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
    private final OverflowLogger overflowLogger = new OverflowLogger(LOG);

    private TcpWriteClient writeClient;

    private TcpClientConf clientConf = new TcpClientConf();
    private int reconnectInterval = 10;
    private String host;
    private int port;

    public TcpWriterConsumer() {

    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        if (status.isConnected()) {
            if (!writeClient.send(queueElement.getPacket().getStringMessage())) {
                status.overflow();
                overflowLogger.log("Overflow writing to client");
            }
        }
    }

    /**
     * Connect and reconnect
     */
    @Override
    public void run() {
        setNotConnected();
        while (true) {
            try(Socket socket = new Socket()) {
                // Connect
                InetSocketAddress address = new InetSocketAddress(host, port);
                LOG.info("Connecting to " + host + ":" + port + " ...");
                socket.connect(address);
                socket.setKeepAlive(true);

                // Start client
                writeClient = new TcpWriteClient(this, socket, clientConf);
                setConnected();
                writeClient.start();
                // Wait for client to loose connection
                writeClient.join();
            } catch (IOException e) {
                LOG.info(getName() + ": connection error: " + e.getMessage());
            } catch (InterruptedException e) {
                break;
            }

            setNotConnected();

            try {
                LOG.info("Waiting to reconnect");
                Thread.sleep(reconnectInterval * 1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        if (writeClient != null) {
            writeClient.cancel();
        }

        setStopped();

        LOG.info("Stopped");
    }

    @Override
    public void start() {
        Thread t = new Thread(this);
        setThread(t);
        t.start();
        super.start();
    }

    @Override
    public void cancel() {
        getThread().interrupt();
        try {
            getThread().join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.cancel();
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
        setNotConnected();
    }

}
