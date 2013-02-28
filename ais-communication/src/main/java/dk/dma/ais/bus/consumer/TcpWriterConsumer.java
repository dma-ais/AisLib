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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusElement;

/**
 * TCP client that connects to host/port and sends data. Will reconnect on connection error.
 */
@ThreadSafe
public class TcpWriterConsumer extends AisBusConsumer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TcpWriterConsumer.class);

    @GuardedBy("this")
    private int port;
    @GuardedBy("this")
    private String host;
    @GuardedBy("this")
    private boolean gzipCompress = false;
    @GuardedBy("this")
    private int gzipBufferSize = 2048;
    @GuardedBy("this")
    private int reconnectInterval = 20000;
    @GuardedBy("this")
    private PrintWriter writer;

    public TcpWriterConsumer() {
        // TODO TcpConsumer ??
    }

    @Override
    public void receiveFiltered(AisBusElement queueElement) {
        PrintWriter out = getWriter();
        if (out == null) {
            return;
        }
        out.print(queueElement.getPacket().getStringMessage());
    }

    /**
     * Connect and reconnect
     */
    @Override
    public void run() {
        while (true) {
            Socket socket = new Socket();
            try {
                InetSocketAddress address = new InetSocketAddress(host, port);
                LOG.info("Connecting to " + host + ":" + port + " ...");
                socket.connect(address);
                socket.setKeepAlive(true);

                OutputStream outputStream;
                if (isGzipCompress()) {
                    outputStream = new GZIPOutputStream(socket.getOutputStream(), gzipBufferSize);
                } else {
                    outputStream = socket.getOutputStream();
                }
                setWriter(new PrintWriter(outputStream));
                LOG.info("Connected.");

                // Block with reading
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (in.readLine() != null) {
                    
                }
                socket.close();                

            } catch (IOException e) {
                LOG.info("TcpWriterConsumer lost connection");                
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
            
            setWriter(null);

            try {
                Thread.sleep(reconnectInterval);
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

    private synchronized PrintWriter getWriter() {
        return writer;
    }

    public synchronized void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized String getHost() {
        return host;
    }

    public synchronized void setHost(String host) {
        this.host = host;
    }

    public synchronized boolean isGzipCompress() {
        return gzipCompress;
    }

    public synchronized void setGzipCompress(boolean gzipCompress) {
        this.gzipCompress = gzipCompress;
    }

    public synchronized int getGzipBufferSize() {
        return gzipBufferSize;
    }

    public synchronized void setGzipBufferSize(int gzipBufferSize) {
        this.gzipBufferSize = gzipBufferSize;
    }

    public synchronized int getReconnectInterval() {
        return reconnectInterval;
    }

    public synchronized void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

}
