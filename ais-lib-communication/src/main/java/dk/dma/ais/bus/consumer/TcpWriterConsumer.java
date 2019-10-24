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

    /**
     * Instantiates a new Tcp writer consumer.
     */
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

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets reconnect interval.
     *
     * @return the reconnect interval
     */
    public int getReconnectInterval() {
        return reconnectInterval;
    }

    /**
     * Sets reconnect interval.
     *
     * @param reconnectInterval the reconnect interval
     */
    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    /**
     * Gets client conf.
     *
     * @return the client conf
     */
    public TcpClientConf getClientConf() {
        return clientConf;
    }

    /**
     * Sets client conf.
     *
     * @param clientConf the client conf
     */
    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    @Override
    public void clientStopped(TcpClient client) {
        setNotConnected();
    }

}
