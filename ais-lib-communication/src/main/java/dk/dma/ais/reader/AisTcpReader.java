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
package dk.dma.ais.reader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import dk.dma.enav.util.function.Consumer;

/**
 * Thread class for reading AIS messages from a TCP stream.
 * 
 * Connection handling is fail tolerant. Connection error is handled by waiting a specified amount of time before doing
 * a re-connect (default 5 sec).
 * 
 * Handlers for the parsed AIS messages must be registered with registerHanlder() method.
 * 
 * Example of use:
 * 
 * IAisHandler handler = new SomeHandler();
 * 
 * AisTcpReader aisReader = new AisTcpReader("localhost", 4001); aisReader.registerHandler(handler);
 * aisReader.addProprietaryFactory(new GatehouseFactory()); aisReader.start(); aisReader.join();
 * 
 */
public class AisTcpReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisTcpReader.class);

    protected volatile long reconnectInterval = 5000; // Default 5 sec
    protected volatile String hostname;
    protected volatile int port;
    protected OutputStream outputStream;
    protected final AtomicReference<Socket> clientSocket = new AtomicReference<>();

    protected volatile int timeout = 10;

    public AisTcpReader() {
        clientSocket.set(new Socket());
    }

    /**
     * Constructor with hostname and port
     * 
     * @param hostname
     * @param port
     */
    public AisTcpReader(String hostname, int port) {
        this();
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Contructor where hostname port is specified in string on the form: host:port
     * 
     * @param hostPort
     */
    public AisTcpReader(String hostPort) {
        this();
        setHostPort(hostPort);
    }

    /**
     * Set hostname
     * 
     * @param hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Set port
     * 
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set host and port from string: host:port
     * 
     * @param hostPort
     */
    protected void setHostPort(String hostPort) {
        String[] parts = StringUtils.split(hostPort, ':');
        this.hostname = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    /**
     * Main read loop
     */
    @Override
    public void run() {
        while (!isShutdown()) {
            try {
                disconnect();
                connect();
                readLoop(clientSocket.get().getInputStream());
            } catch (IOException e) {
                if (isShutdown() || isInterrupted()) {
                    return;
                }
                LOG.error("Source communication failed: " + e.getMessage() + ": host:port: " + hostname + ":" + port
                        + " Retry in " + reconnectInterval / 1000 + " seconds");
                if (!isShutdown()) {
                    try {
                        // LOG.info("Starting to sleep " + getId() + " " + shutdown);
                        shutdownLatch.await(reconnectInterval, TimeUnit.MILLISECONDS);
                        // LOG.info("Wake up " + getId() + " " + shutdown);
                    } catch (InterruptedException ignored) {
                        // intE.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void stopReader() {
        super.stopReader();
        try {
            // Close socket if open
            clientSocket.get().close();
        } catch (IOException ignored) {}
    }

    protected void connect() throws IOException {
        try {
            LOG.info("Connecting to source " + hostname + ":" + port);
            clientSocket.set(new Socket());
            InetSocketAddress address = new InetSocketAddress(hostname, port);
            clientSocket.get().connect(address);
            if (timeout > 0) {
                clientSocket.get().setSoTimeout(timeout * 1000);
            }
            clientSocket.get().setKeepAlive(true);
            outputStream = clientSocket.get().getOutputStream();
            LOG.info("Connected to source " + hostname + ":" + port);
        } catch (UnknownHostException e) {
            LOG.error("Unknown host: " + hostname + ": " + e.getMessage());
            throw e;
        } catch (IOException e) {
            if (!isShutdown()) {
                LOG.error("Could not connect to: " + hostname + ": " + e.getMessage());
            }
            throw e;
        }
    }

    protected void disconnect() {
        if (getStatus() == Status.CONNECTED) {
            try {
                LOG.info("Disconnecting source " + hostname + ":" + port);
                clientSocket.get().close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * Send sendRequest to the socket output stream and get result sent to resultListener.
     * 
     * @param sendRequest
     * @param resultListener
     * @throws SendException
     */
    @Override
    public void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException {
        doSend(sendRequest, resultListener, outputStream);
    }

    public Status getStatus() {
        return clientSocket.get().isConnected() ? Status.CONNECTED : Status.DISCONNECTED;
    }

    /**
     * Get the interval in milliseconds between re-connect attempts
     * 
     * @return reconnectInterval
     */
    public long getReconnectInterval() {
        return reconnectInterval;
    }

    /**
     * Set the interval in milliseconds between re-connect attempts
     * 
     * @param reconnectInterval
     */
    public void setReconnectInterval(long reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    /**
     * Get socket read timeout
     * 
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set socket read timeout
     * 
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Get hostname
     * 
     * @return
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get port
     * 
     * @return
     */
    public int getPort() {
        return port;
    }

    public String toString() {
        return "TcpReader [sourceIf = " + getSourceId() + ", host=" + getHostname() + ":" + getPort() + "]";
    }
}
