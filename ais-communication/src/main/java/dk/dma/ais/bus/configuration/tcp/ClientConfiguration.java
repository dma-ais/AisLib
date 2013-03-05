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
package dk.dma.ais.bus.configuration.tcp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientConfiguration {

    private boolean gzipCompress;
    private int gzipBufferSize = 2048;
    private int bufferSize = 2048;

    public ClientConfiguration() {

    }

    public boolean isGzipCompress() {
        return gzipCompress;
    }

    public void setGzipCompress(boolean gzipCompress) {
        this.gzipCompress = gzipCompress;
    }

    public int getGzipBufferSize() {
        return gzipBufferSize;
    }

    public void setGzipBufferSize(int gzipBufferSize) {
        this.gzipBufferSize = gzipBufferSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
