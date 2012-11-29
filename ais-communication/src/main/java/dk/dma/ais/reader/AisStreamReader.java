/* Copyright (c) 2011 Danish Maritime Safety Administration
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
package dk.dma.ais.reader;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread class for reading any InputStream for AIS messages
 * 
 * For fail tolerant TCP reading use AisTcpReader
 * 
 * Example of use:
 * 
 * IAisHandler handler = new SomeHandler(); InputStream inputStream = new SomeInputStream();
 * 
 * AisStreamReader aisReader = new AisStreamReader(inputStream); aisReader.registerHandler(handler);
 * aisReader.addProprietaryFactory(new GatehouseFactory()); aisReader.start(); aisReader.join();
 * 
 */
public class AisStreamReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisStreamReader.class);

    private InputStream stream;

    public AisStreamReader(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        try {
            readLoop(stream);
        } catch (IOException e) {
            LOG.error("Failed to read stream: " + e.getMessage());
        }
        stream = null;
    }

    @Override
    public void send(SendRequest sendRequest, ISendResultListener resultListener) {
        // Cannot send
        resultListener.sendResult(null);
    }

    @Override
    public Status getStatus() {
        return stream != null ? Status.CONNECTED : Status.DISCONNECTED;
    }

    @Override
    public void close() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {}
        }
    }
}
