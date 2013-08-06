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
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.reader.AisReader;
import dk.dma.enav.util.function.Consumer;

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
