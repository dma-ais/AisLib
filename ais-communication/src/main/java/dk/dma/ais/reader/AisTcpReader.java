/* Copyright (c) 2011 Danish Maritime Safety Administration
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected long reconnectInterval = 5000; // Default 5 sec
    protected String hostname;
    protected int port;

    protected OutputStream outputStream;
    protected Socket clientSocket = new Socket();

    protected int timeout = 10;

    public AisTcpReader() {}

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
        while (true) {
            try {
                disconnect();
                connect();
                readLoop(clientSocket.getInputStream());
            } catch (IOException e) {
                LOG.error("Source communication failed: " + e.getMessage() + " retry in " + reconnectInterval / 1000
                        + " seconds");
                try {
                    Thread.sleep(reconnectInterval);
                } catch (InterruptedException intE) {
                    LOG.info("Stopping reader");
                    return;
                }
            }
        }
    }

    @Override
    public void stopReader() {
        try {
            // Close socket if open
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            LOG.info("Could not close client socket");
        }
        // Interrupt this thread
        this.interrupt();
    }

    protected void connect() throws IOException {
        try {
            LOG.info("Connecting to source " + hostname + ":" + port);
            clientSocket = new Socket();
            InetSocketAddress address = new InetSocketAddress(hostname, port);
            clientSocket.connect(address);
            if (timeout > 0) {
                clientSocket.setSoTimeout(timeout * 1000);
            }
            clientSocket.setKeepAlive(true);
            outputStream = clientSocket.getOutputStream();
            LOG.info("Connected to source " + hostname + ":" + port);
        } catch (UnknownHostException e) {
            LOG.error("Unknown host: " + hostname + ": " + e.getMessage());
            throw e;
        } catch (IOException e) {
            LOG.error("Could not connect to: " + hostname + ": " + e.getMessage());
            throw e;
        }
    }

    protected void disconnect() {
        if (clientSocket != null && getStatus() == Status.CONNECTED) {
            try {
                LOG.info("Disconnecting source " + hostname + ":" + port);
                clientSocket.close();
            } catch (IOException e) {}
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
    public void send(SendRequest sendRequest, ISendResultListener resultListener) throws SendException {
        doSend(sendRequest, resultListener, outputStream);
    }

    public Status getStatus() {
        synchronized (clientSocket) {
            if (clientSocket != null && clientSocket.isConnected()) {
                return Status.CONNECTED;
            }
            return Status.DISCONNECTED;
        }
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

}
