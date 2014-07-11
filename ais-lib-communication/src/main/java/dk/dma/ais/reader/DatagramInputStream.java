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
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramInputStream extends InputStream {
    
    private static final int BUFFER_SIZE = 256;

    private final DatagramSocket socket;
    private DatagramPacket packet;

    private byte[] buf = new byte[BUFFER_SIZE];
    private int packetSize;
    private int packetIndex;

    int value;

    public DatagramInputStream(InetSocketAddress addr) throws UnknownHostException, SocketException {
        socket = new DatagramSocket(addr);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public int available() {
        return packetSize - packetIndex;
    }

    @Override
    public int read() throws IOException {
        if (packetIndex == packetSize) {
            receive();
        }
        value = buf[packetIndex] & 0xff;
        packetIndex++;
        return value;
    }

    @Override
    public int read(byte[] buff) throws IOException {
        return read(buff, 0, buff.length);
    }

    @Override
    public int read(byte[] buff, int off, int len) throws IOException {
        if (packetIndex == packetSize) {
            receive();
        }

        int lenRemaining = len;

        while (available() < lenRemaining) {
            System.arraycopy(buf, packetIndex, buff, off + (len - lenRemaining), available());
            lenRemaining -= available();
            receive();
        }

        System.arraycopy(buf, packetIndex, buff, off + (len - lenRemaining), lenRemaining);
        packetIndex += lenRemaining;
        //System.out.println("Passing along " + new String(buff, 0, len, StandardCharsets.US_ASCII));
        return len;
    }

    @Override
    public long skip(long len) throws IOException {
        if (packetIndex == packetSize) {
            receive();
        }

        long lenRemaining = len;

        while (available() < lenRemaining) {
            lenRemaining -= available();
            receive();
        }

        packetIndex += (int) lenRemaining;
        return len;
    }

    private void receive() throws IOException {
        packet = new DatagramPacket(buf, BUFFER_SIZE);
        socket.receive(packet);
        packetIndex = 0;
        packetSize = packet.getLength();
        //System.out.println("Received " + new String(buf, 0, packet.getLength(), StandardCharsets.US_ASCII));
    }

}
