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
package dk.dma.ais.bus.provider;

import java.io.InputStream;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.reader.AisReaders;

/**
 * Provider reading from stream
 */
@ThreadSafe
public class StreamReaderProvider extends AisReaderProvider {

    @GuardedBy("this")
    private InputStream stream;

    public StreamReaderProvider(boolean blocking) {
        super(blocking);
    }

    public StreamReaderProvider(InputStream stream, boolean blocking) {
        super(blocking);
        this.stream = stream;
    }
    
    public StreamReaderProvider(InputStream stream) {
        this(stream, false);
    }

    protected synchronized void setStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public synchronized void init() {
        setAisReader(AisReaders.createReaderFromInputStream(stream));
        super.init();
    }

}
