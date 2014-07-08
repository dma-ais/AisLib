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

    public TcpClientConf() {

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
