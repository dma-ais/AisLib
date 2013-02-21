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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An extended AisTcpReader with added possibility of doing round robin connect attempt to list of AIS TCP sources.
 */
public class RoundRobinAisTcpReader extends AisTcpReader {

    private static final Logger LOG = LoggerFactory.getLogger(RoundRobinAisTcpReader.class);

    private List<String> hostnames = new ArrayList<>();
    private List<Integer> ports = new ArrayList<>();
    private int currentHost = -1;

    public RoundRobinAisTcpReader() {
        super();
    }

    /**
     * Set hosts and ports from a list of hostname:ports
     * 
     * @param commaHostPort
     */
    public RoundRobinAisTcpReader(List<String> hostPorts) {
        for (String hp : hostPorts) {
            addHostPort(hp);
        }
    }

    /**
     * Set hosts and ports from a comma separated list on the form: host1:port1,...,hostN:portN
     * 
     * @param commaHostPort
     */
    public RoundRobinAisTcpReader setCommaseparatedHostPort(String commaHostPort) {
        String[] hostPorts = StringUtils.split(commaHostPort, ",");
        for (String hp : hostPorts) {
            addHostPort(hp);
        }
        return this;
    }

    /**
     * Add another host/port on the form host:port
     * 
     * @param hostPort
     */
    public void addHostPort(String hostPort) {
        setHostPort(hostPort);
        hostnames.add(hostname);
        ports.add(port);
        currentHost = 0;
        setHost();
    }

    /**
     * Get the number of hosts
     * 
     * @return
     */
    public int getHostCount() {
        return hostnames.size();
    }

    @Override
    public void run() {
        while (true) {
            try {
                disconnect();
                setHost();
                connect();
                readLoop(clientSocket.getInputStream());
            } catch (IOException e) {
                LOG.error("Source communication failed: " + e.getMessage() + " retry in " + reconnectInterval / 1000
                        + " seconds");
                try {
                    Thread.sleep(reconnectInterval);
                } catch (InterruptedException intE) {}
                selectHost();
            }
        }
    }

    private void setHost() {
        hostname = hostnames.get(currentHost);
        port = ports.get(currentHost);
    }

    private void selectHost() {
        currentHost = (currentHost + 1) % hostnames.size();
    }

}
