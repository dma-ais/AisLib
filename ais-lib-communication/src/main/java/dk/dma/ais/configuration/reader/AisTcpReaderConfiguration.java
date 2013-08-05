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
