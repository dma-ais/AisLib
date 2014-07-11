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
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import dk.dma.enav.util.function.Consumer;

/**
 * Thread class for reading AIS messages from UDP
 * 
 */
public class AisUdpReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisUdpReader.class);

    private final InetSocketAddress addr;
    private volatile DatagramInputStream stream;

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
            stream = new DatagramInputStream(addr);
            readLoop(stream);
        } catch (IOException e) {
            if (isShutdown() || isInterrupted()) {
                return;
            }
            LOG.error("Failed to listen for datagrams", e);
        }
        
        
//        byte[] receiveData = new byte[256];
//
//        try {
//            socket.set(new DatagramSocket(addr));
//            while (!isShutdown()) {
//                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                socket.get().receive(receivePacket);
//                String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), Charsets.US_ASCII);
//                
//                
//            }
//            
//
//        } catch (IOException e) {
//            if (isShutdown() || isInterrupted()) {
//                return;
//            }
//            LOG.error("Failed to listen for UDP: " + e);
//        }
    }

    @Override
    public void stopReader() {
        super.stopReader();
        try {
            stream.close();
        } catch (IOException e) {
        }
        stream = null;
    }

    @Override
    public void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException {
        throw new UnsupportedOperationException();
    }

    public Status getStatus() {
        return (stream != null) ? Status.CONNECTED : Status.DISCONNECTED;
    }

    public String toString() {
        return "AisUdpReader [sourceId = " + getSourceId() + "]";
    }
    
}
