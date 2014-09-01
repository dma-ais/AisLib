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
package dk.dma.ais.reader;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

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
    protected OutputStream outputStream;
    protected final AtomicReference<Socket> clientSocket = new AtomicReference<>(new Socket());

    protected volatile int timeout = 10;

    List<HostAndPort> hosts = new ArrayList<>();
    int currentHostIndex = -1;

    // /**
    // * Constructor with hostname and port
    // *
    // * @param hostname
    // * @param port
    // */
    // public AisTcpReader(String hostname, int port) {
    // hostAndPort = HostAndPort.fromParts(hostname, port);
    // }

    /**
     * Add another host/port on the form host:port
     * 
     * @param hostPort
     */
    void addHostPort(HostAndPort hostAndPort) {
        requireNonNull(hostAndPort);
        hosts.add(hostAndPort);
        currentHostIndex++;
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
                LOG.error("Source communication failed: " + e.getMessage() + ": host:port: " + currentHost()
                        + " Retry in " + reconnectInterval / 1000 + " seconds");
                if (!isShutdown()) {
                    try {
                        // LOG.info("Starting to sleep " + getId() + " " + shutdown);
                        shutdownLatch.await(reconnectInterval, TimeUnit.MILLISECONDS);
                        // LOG.info("Wake up " + getId() + " " + shutdown);
                    } catch (InterruptedException ignored) {
                        LOG.info("Stopping reader");
                        return;
                    }
                }
                currentHostIndex = (currentHostIndex + 1) % hosts.size();
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
            LOG.info("Connecting to source " + currentHost());
            clientSocket.set(new Socket());
            InetSocketAddress address = new InetSocketAddress(currentHost().getHostText(), currentHost().getPort());
            clientSocket.get().connect(address);
            if (timeout > 0) {
                clientSocket.get().setSoTimeout(timeout * 1000);
            }
            clientSocket.get().setKeepAlive(true);
            outputStream = clientSocket.get().getOutputStream();
            LOG.info("Connected to source " + currentHost());
        } catch (UnknownHostException e) {
            LOG.error("Unknown host: " + currentHost().getHostText() + ": " + e.getMessage());
            throw e;
        } catch (IOException e) {
            if (!isShutdown()) {
                LOG.error("Could not connect to: " + currentHost() + ": " + e.getMessage());
            }
            throw e;
        }
    }

    protected void disconnect() {
        if (getStatus() == Status.CONNECTED) {
            try {
                LOG.info("Disconnecting source " + currentHost());
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
     * Get the number of hosts
     * 
     * @return
     */
    public int getHostCount() {
        return hosts.size();
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
     * Returns the current hostname
     * 
     * @return
     */
    public String getHostname() {
        return currentHost().getHostText();
    }

    HostAndPort currentHost() {
        return hosts.get(currentHostIndex);
    }

    /**
     * Get port
     * 
     * @return
     */
    public int getPort() {
        return currentHost().getPort();
    }

    public String toString() {
        return "AisTcpReader [sourceId = " + getSourceId() + ", current host=" + currentHost() + "]";
    }
}
