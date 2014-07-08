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
package dk.dma.ais.configuration.reader;

import java.io.FileNotFoundException;

import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.reader.AisTcpReader;

public class AisTcpReaderConfiguration extends AisReaderConfiguration {

    private long reconnectInterval = 5000; // Default 5 sec
    private String hostname;
    private int port;
    private int timeout = 10;

    @Override
    public AisReader getInstance() throws FileNotFoundException {
        AisTcpReader aisTcpReader = AisReaders.createReader(hostname, port);
        aisTcpReader.setReconnectInterval(reconnectInterval);
        aisTcpReader.setTimeout(timeout);

        return aisTcpReader;
    }

}
