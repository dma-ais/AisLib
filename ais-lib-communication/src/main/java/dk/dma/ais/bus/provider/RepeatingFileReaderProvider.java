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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.ReplayTransformer;
import java.util.function.Consumer;

/**
 * Provider that reads repeatedly from the same file
 */
@ThreadSafe
public class RepeatingFileReaderProvider extends AisBusProvider implements Consumer<AisPacket>, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RepeatingFileReaderProvider.class);

    /**
     * The AIS reader to provide packets
     */
    private AtomicReference<AisReader> aisReader = new AtomicReference<>();
    private final String filename;
    private final boolean gzip;

    public RepeatingFileReaderProvider(String filename, boolean gzip) {
        this.filename = filename;
        this.gzip = gzip;
    }

    @Override
    public void start() {
        super.start();
        Thread t = new Thread(this);
        setThread(t);
        t.start();
    };

    @Override
    public void cancel() {
        getThread().interrupt();
        AisReader reader = aisReader.get();
        if (reader != null) {
            reader.stopReader();
            try {
                reader.join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            getThread().join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        InputStream stream;
        while (true) {
            try {
                stream = new FileInputStream(filename);
                if (gzip) {
                    stream = new GZIPInputStream(stream);
                }
            } catch (IOException e) {
                if (!getThread().isInterrupted()) {
                    LOG.error("Failed to open stream: " + e.getMessage());
                }
                return;
            }
            try {
                try (AisPacketReader r = new AisPacketReader(stream)) {
                    r.forEachRemaining(this);
                }
            } catch (IOException e) {}

            try {
                stream.close();
            } catch (IOException e) {}

            // Special handling for possible replay transformers that needs to be reset
            for (IAisPacketTransformer transformer : getPacketTransformers()) {
                if (transformer instanceof ReplayTransformer) {
                    ((ReplayTransformer) transformer).reset();
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

        }

    }

    /**
     * Receive packet from reader
     */
    @Override
    public void accept(AisPacket packet) {
        // Push to bus
        push(packet);
    }

}
