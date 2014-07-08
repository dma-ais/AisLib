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
package dk.dma.ais.bus.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.zip.GZIPOutputStream;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writing TCP client.
 */
@ThreadSafe
public class TcpWriteClient extends TcpClient {

    private static final Logger LOG = LoggerFactory.getLogger(TcpWriteClient.class);    

    private final BlockingQueue<String> buffer;

    public TcpWriteClient(IClientStoppedListener stopListener, Socket socket, TcpClientConf conf) {
        super(stopListener, socket, conf);
        this.buffer = new ArrayBlockingQueue<>(conf.getBufferSize());
    }

    /**
     * Send message
     * 
     * @param sentenceStr
     */
    public boolean send(String msg) {
        status.receive();
        if (!buffer.offer(msg)) {
            status.overflow();            
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        status.setConnected();
        try {
            // Open output stream
            OutputStream outputStream;
            if (conf.isGzipCompress()) {
                outputStream = new GZIPOutputStream(socket.getOutputStream(), conf.getGzipBufferSize());
            } else {
                outputStream = socket.getOutputStream();
            }
            PrintWriter writer = new PrintWriter(outputStream);
            List<String> list = new ArrayList<>();

            // Pull-write loop
            while (true) {
                // Pull from queue
                list.clear();
                list.add(buffer.take());
                buffer.drainTo(list);
                // Write to client
                for (String str : list) {
                    writer.println(str);
                }
                if (writer.checkError()) {
                    throw new IOException("Connection to client lost");
                }
            }
        } catch (IOException e) {
            if (!isInterrupted()) {
                LOG.info(e.getMessage());
            }
        } catch (InterruptedException e) {

        }
        try {
            socket.close();
        } catch (IOException e) {
        }
        
        stopping();
        
        LOG.info("Stopped");
    }

}
