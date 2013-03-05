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
package dk.dma.ais.bus.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for TCP servers. Spawns and handles TCP clients.
 */
public abstract class TcpServer extends Thread implements IClientStoppedListener {

    private static final Logger LOG = LoggerFactory.getLogger(TcpServer.class);

    protected ServerSocket serverSocket;

    protected TcpServerConf serverConf = new TcpServerConf();
    protected TcpClientConf clientConf = new TcpClientConf();

    private Semaphore semaphore;
    protected final Set<TcpClient> clients = Collections.newSetFromMap(new ConcurrentHashMap<TcpClient, Boolean>());

    public TcpServer() {
    }

    /**
     * Inheriting classes must be able to provide a new client
     * 
     * @param socket
     * @return
     */
    protected abstract TcpClient newClient(Socket socket);

    /**
     * Clients notify them self when they are done
     * 
     * @param client
     */
    @Override
    public void clientStopped(TcpClient client) {
        clients.remove(client);
        semaphore.release();
    }

    @Override
    public void run() {
        // Initialize semaphore
        semaphore = new Semaphore(serverConf.getMaxClients());

        // Setup server socket
        try {
            serverSocket = new ServerSocket(serverConf.getPort());
        } catch (IOException e) {
            LOG.error("Failed to setup server socket: " + e.getMessage());
            return;
        }
        // Accept incoming connections
        while (true) {
            Socket socket = null;
            try {
                // Maybe wait if max connections is exceeded
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    // TODO
                    e.printStackTrace();
                    return;
                }

                LOG.info("Waiting for connections on port " + serverConf.getPort());
                socket = serverSocket.accept();
                socket.setKeepAlive(true);
                LOG.info("Accepting connection from " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                LOG.info("Connection failed " + e.getMessage());
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException closeE) {
                    }
                }
                semaphore.release();
                continue;
            }

            // Register and start client
            TcpClient client = newClient(socket);
            clients.add(client);
            client.start();

        }

    }

    public TcpClientConf getClientConf() {
        return clientConf;
    }

    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    public TcpServerConf getServerConf() {
        return serverConf;
    }

    public void setServerConf(TcpServerConf serverConf) {
        this.serverConf = serverConf;
    }

}
