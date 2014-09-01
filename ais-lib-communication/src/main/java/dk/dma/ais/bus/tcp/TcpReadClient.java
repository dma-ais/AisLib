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
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.reader.AisReader;
import java.util.function.Consumer;

/**
 * A reading TCP client
 */
public class TcpReadClient extends TcpClient implements Consumer<AisPacket> {

    private static final Logger LOG = LoggerFactory.getLogger(TcpReadClient.class);

    private final Consumer<AisPacket> packetConsumer;
    private final AtomicReference<AisReader> reader = new AtomicReference<>();

    public TcpReadClient(Consumer<AisPacket> packetConsumer, IClientStoppedListener stopListener, Socket socket,
            TcpClientConf conf) {
        super(stopListener, socket, conf);
        this.packetConsumer = packetConsumer;
    }

    @Override
    public void run() {
        status.setConnected();
        // Read from socket
        try {
            InputStream inputStream;
            if (conf.isGzipCompress()) {
                inputStream = new GZIPInputStream(socket.getInputStream(), conf.getGzipBufferSize());
            } else {
                inputStream = socket.getInputStream();
            }
            try (AisPacketReader r = new AisPacketReader(inputStream)) {
                r.forEachRemaining(this);
            }
        } catch (IOException e) {
            if (!isInterrupted()) {
                LOG.info(e.getMessage());
            }
        }

        try {
            socket.close();
        } catch (IOException e) {}

        stopping();

        LOG.info("Stopped");
    }

    @Override
    public void cancel() {
        AisReader r = reader.get();
        if (r != null) {
            r.stopReader();
        }
        super.cancel();
    }

    @Override
    public void accept(AisPacket packet) {
        status.receive();
        packetConsumer.accept(packet);
    }

}
