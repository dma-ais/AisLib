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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;

import java.util.function.Consumer;

public class AisReaderTest {

    @Test
    public void dirReaderTest() throws InterruptedException, IOException {
        AisReader reader = AisReaders.createDirectoryReader("src/test", "s*.txt", true);
        reader.registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket packet) {
                // System.out.println("Read packet");
            }
        });
        reader.start();
        reader.join();
        assertEquals(4062L, reader.getNumberOfLinesRead());
    }

    @Test
    public void dirReaderPercentageReadTest() throws InterruptedException, IOException {
        AisDirectoryReader directoryReader = AisReaders.createDirectoryReader("src/test", "stream_example.txt", true);

        assertEquals(0L, directoryReader.getNumberOfLinesRead());
        assertEquals(0.0, directoryReader.getEstimatedFractionOfPacketsRead(), 1e-10);

        directoryReader.start();
        directoryReader.join();

        assertEquals(4051L, directoryReader.getNumberOfLinesRead());
        assertEquals(1.0, directoryReader.getEstimatedFractionOfPacketsRead(), 1e-10);
    }

    @Ignore
    @Test
    public void udpReaderTest() throws InterruptedException, IOException {
        int portTmp = 49552;
        for (int i=49552; i<49600; i++) {
            if (available(i)) {
                portTmp = i;
                
                break;
            }
        }
        
        final int port = portTmp;
        
        final DatagramSocket sendSocket = new DatagramSocket();
        
        final AtomicReference<Integer> count = new AtomicReference<>(0);
        
        AisUdpReader reader = AisReaders.createUdpReader(port);
        reader.registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket packet) {
                count.set(count.get() + 1);
            }
        });
        
        reader.start();

        System.out.println("Sending sentences on UDP port " + port);
        // Send UDP packets
        URL url = ClassLoader.getSystemResource("replay_dump.txt");
        Assert.assertNotNull(url);
        
        final InetAddress addr = InetAddress.getLocalHost();
        Assert.assertNotNull(addr);
        
        
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Assert.assertNotNull(in);
            
            in.lines().forEachOrdered(new java.util.function.Consumer<String>() {

                @Override
                public void accept(String line) {
                    line = line.trim() + "\r\n";
                    try {
                        sendSocket.send(new DatagramPacket(line.getBytes(StandardCharsets.US_ASCII), 0, line.length(), addr, port));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
            });
            
        }
        System.out.println("Done sending on UDP port " + port);
        Thread.sleep(3000); // slow cloudbees?
        reader.stopReader();
        reader.join();
        sendSocket.close();
        Assert.assertEquals(50, count.get().intValue());        
    }
    
    private static boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}
