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

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

/**
 * Thread class for reading AIS messages from UDP
 * 
 */
public class AisUdpReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisUdpReader.class);

    private final InetSocketAddress addr;
    private volatile DatagramSocket socket;
    private volatile Thread reader;

    AisUdpReader(int port) {
        this(null, port);
    }

    AisUdpReader(String address, int port) {
        addr = (address == null) ? new InetSocketAddress(port) : new InetSocketAddress(address, port);
    }

    /**
     * Main read loop
     */
    @Override
    public void run() {
        try {
            final UdpSentenceInputStream stream = new UdpSentenceInputStream();
            socket = new DatagramSocket(addr);
            reader = new Thread() {
                @Override
                public void run() {
                    final byte[] receiveData = new byte[256];
                    while (true) {
                        try {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            socket.receive(receivePacket);
                            String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), Charsets.US_ASCII);
                            stream.push(sentence.getBytes(StandardCharsets.US_ASCII));
                        } catch (IOException | InterruptedException e) {
                            if (isShutdown() || isInterrupted()) {
                                return;
                            }
                            LOG.error("Failed to read datagrams", e);
                            return;
                        }
                    }
                }
            };
            reader.start();
            readLoop(stream);
        } catch (IOException e) {
            if (isShutdown() || isInterrupted()) {
                return;
            }
            LOG.error("Failed to listen for datagrams", e);
        }
    }

    @Override
    public void stopReader() {
        super.stopReader();
        socket.close();
        reader.interrupt();
    }

    @Override
    public void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException {
        throw new UnsupportedOperationException();
    }

    public Status getStatus() {
        return (socket != null) ? Status.CONNECTED : Status.DISCONNECTED;
    }

    public String toString() {
        return "AisUdpReader [sourceId = " + getSourceId() + "]";
    }
    
    private class UdpSentenceInputStream extends InputStream {

        private final ArrayBlockingQueue<byte[]> queue;

        public UdpSentenceInputStream() {
            this(1024);
        }

        public UdpSentenceInputStream(int maxLines) {
            queue = new ArrayBlockingQueue<>(maxLines);
        }
        
        public void push(byte[] packet) throws InterruptedException {
            queue.put(packet);
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            byte[] packet;
            try {
                packet = queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return 0;
            }
            if (len < packet.length) {
                throw new IOException("Read with too small buffer");
            }

            System.arraycopy(packet, 0, b, off, packet.length);
            return packet.length;
        }

        @Override
        public int read() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read(byte[] b) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long skip(long n) throws IOException {
            throw new UnsupportedOperationException();
        }

    }


}
