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
package dk.dma.ais.bus.configuration.provider;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RoundRobinTcpClientConfiguration extends AisBusProviderConfiguration {

    private String hostsPorts;
    private int interval = 10;
    private int timeout = 10;

    public RoundRobinTcpClientConfiguration() {

    }

    public String getHostsPorts() {
        return hostsPorts;
    }
    
    public void setHostsPorts(String hostsPorts) {
        this.hostsPorts = hostsPorts;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
