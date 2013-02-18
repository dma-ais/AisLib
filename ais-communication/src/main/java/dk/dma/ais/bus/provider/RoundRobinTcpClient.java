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
package dk.dma.ais.bus.provider;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBus;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.RoundRobinAisTcpReader;

/**
 * Round robin TCP client provider
 */
@ThreadSafe
public class RoundRobinTcpClient extends AisReaderProvider {
    
    private RoundRobinTcpClient(AisBus aisBus, AisReader aisReader) {
        super(aisBus, aisReader);
    }
    
    // TODO config should come from configuration class
    public static RoundRobinTcpClient create(AisBus aisBus, String hostPorts, int interval, int timeout) {
        RoundRobinAisTcpReader rrReader = new RoundRobinAisTcpReader();
        rrReader.setCommaseparatedHostPort(hostPorts);
        rrReader.setTimeout(timeout);
        rrReader.setReconnectInterval(interval);             
        return new RoundRobinTcpClient(aisBus, rrReader);
    }

}
