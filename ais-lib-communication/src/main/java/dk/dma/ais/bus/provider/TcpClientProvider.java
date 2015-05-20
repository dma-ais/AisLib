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
import dk.dma.ais.bus.tcp.IClientStoppedListener;
import dk.dma.ais.bus.tcp.TcpClient;
import dk.dma.ais.bus.tcp.TcpClientConf;
import dk.dma.ais.bus.tcp.TcpReadClient;
import dk.dma.ais.packet.AisPacket;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Round robin TCP client provider
 */
@ThreadSafe
public final class TcpClientProvider extends AisBusProvider implements Runnable, Consumer<AisPacket>, IClientStoppedListener {

    private static final Logger LOG = LoggerFactory.getLogger(TcpClientProvider.class);

    private TcpReadClient readClient;

    private TcpClientConf clientConf = new TcpClientConf();
    private List<String> hostsPorts = new ArrayList<>();
    private int reconnectInterval = 10;
    private int timeout = 10;

    private List<String> hostnames = new ArrayList<>();
    private List<Integer> ports = new ArrayList<>();
    private int currentHost = -1;
    private volatile Socket socket;

    public TcpClientProvider() {
        super();
    }

    @Override
    public void run() {        
        setNotConnected();
        while (true) {
            socket = new Socket();

            // Get next host and port
            selectHost();
            String host = hostnames.get(currentHost);
            int port = ports.get(currentHost);

            // Connect
            try {
                InetSocketAddress address = new InetSocketAddress(host, port);
                LOG.info("Connecting to " + host + ":" + port + " ...");
                socket.connect(address);
                socket.setKeepAlive(true);
                if (timeout > 0) {
                    socket.setSoTimeout(timeout * 1000);
                }
                
                setConnected();

                // Start client
                readClient = new TcpReadClient(this, this, socket, clientConf);
                readClient.start();
                // Wait for client to loose connection
                readClient.join();
                readClient = null;
            } catch (IOException e) {
                LOG.info(getName() + ": connection error: " + e.getMessage());
            } catch (InterruptedException e) {                
                readClient.cancel();
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
        
        setStopped();
    }

    @Override
    public void accept(AisPacket packet) {
        push(packet);
    }

    @Override
    public synchronized void start() {
        // Start self as thread
        Thread t = new Thread(this);
        setThread(t);
        t.start();
        super.start();
    }
    
    @Override
    public void cancel() {
        // Close socket
        Socket s = socket;
        if (s != null) {
            try {
                s.close();
            } catch (IOException ignore) {
            }
        }
        getThread().interrupt();   
        try {            
            getThread().join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setStopped();
    }

    @Override
    public synchronized void init() {
        for (String hostPort : hostsPorts) {
            String[] parts = StringUtils.split(hostPort, ':');
            hostnames.add(parts[0]);
            ports.add(Integer.parseInt(parts[1]));
        }
        super.init();
    }

    public List<String> getHostsPorts() {
        return hostsPorts;
    }

    public void setHostsPorts(List<String> hostsPorts) {
        this.hostsPorts = hostsPorts;
    }

    public void setClientConf(TcpClientConf clientConf) {
        this.clientConf = clientConf;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    @Override
    public void clientStopped(TcpClient client) {

    }

    private void selectHost() {
        currentHost = (currentHost + 1) % hostnames.size();
    }

}
