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
package dk.dma.ais.reader;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.net.HostAndPort;

/**
 * 
 * @author Kasper Nielsen
 */
public interface AisReaderMXBean {

    String getHosts();

    void addHost(String hostname, int port);

    long getBytesRead();

    long getPacketsRead();

    String getSource();
}

class AisReaderMXBeanImpl implements AisReaderMXBean {
    final AisTcpReader reader;

    AisReaderMXBeanImpl(AisTcpReader reader) {
        this.reader = requireNonNull(reader);
    }

    /**
     * @return the bytesRead
     */
    public long getBytesRead() {
        return reader.getNumberOfBytesRead();
    }

    /**
     * @return the packetsRead
     */
    public long getPacketsRead() {
        return 0;// reader.g;
    }

    public String getHosts() {
        List<String> l = new ArrayList<>();
        for (HostAndPort hap : reader.hosts) {
            l.add(hap.getHostText());
        }
        return Joiner.on(',').join(l);
    }

    /**
     * @return the source
     */
    public String getSource() {
        return reader.getSourceId();
    }

    /** {@inheritDoc} */
    @Override
    public void addHost(String hostName, int port) {
        HostAndPort hap = HostAndPort.fromParts(hostName, port);
        reader.addHostPort(hap);
    }
}
