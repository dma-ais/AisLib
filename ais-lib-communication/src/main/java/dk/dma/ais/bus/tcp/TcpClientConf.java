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
package dk.dma.ais.bus.tcp;

/**
 * Configuration for a TCP client
 */
public class TcpClientConf {

    private boolean gzipCompress;
    private int gzipBufferSize = 2048;
    private int bufferSize = 8192;

    /**
     * Instantiates a new Tcp client conf.
     */
    public TcpClientConf() {

    }

    /**
     * Is gzip compress boolean.
     *
     * @return the boolean
     */
    public boolean isGzipCompress() {
        return gzipCompress;
    }

    /**
     * Sets gzip compress.
     *
     * @param gzipCompress the gzip compress
     */
    public void setGzipCompress(boolean gzipCompress) {
        this.gzipCompress = gzipCompress;
    }

    /**
     * Gets gzip buffer size.
     *
     * @return the gzip buffer size
     */
    public int getGzipBufferSize() {
        return gzipBufferSize;
    }

    /**
     * Sets gzip buffer size.
     *
     * @param gzipBufferSize the gzip buffer size
     */
    public void setGzipBufferSize(int gzipBufferSize) {
        this.gzipBufferSize = gzipBufferSize;
    }

    /**
     * Gets buffer size.
     *
     * @return the buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Sets buffer size.
     *
     * @param bufferSize the buffer size
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
