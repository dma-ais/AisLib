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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

/**
 * Thread class for reading any InputStream for AIS messages
 * 
 * For fail tolerant TCP reading use AisTcpReader
 * 
 */
public class AisStreamReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisStreamReader.class);

    private final InputStream stream;
    private volatile boolean done;

    AisStreamReader(InputStream stream) {
        this.stream = requireNonNull(stream);
    }

    @Override
    public void run() {
        try {
            readLoop(stream);
        } catch (IOException e) {
            if (!isShutdown()) {
                LOG.error("Failed to read stream: " + e.getMessage());
            }
        }
        done = true;
    }

    @Override
    public void send(SendRequest sendRequest, Consumer<Abk> resultListener) {
        // Cannot send
        resultListener.accept(null);
    }

    @Override
    public Status getStatus() {
        return done ? Status.DISCONNECTED : Status.CONNECTED;
    }

    @Override
    public void stopReader() {
        super.stopReader();
        try {
            stream.close();
        } catch (IOException ignored) {}
    }
}
