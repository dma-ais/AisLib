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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import net.jcip.annotations.ThreadSafe;

/**
 * Provide from file
 */
@ThreadSafe
public class FileReaderProvider extends StreamReaderProvider {
    
    public FileReaderProvider(String filename) throws IOException {
        this(filename, false);
    }
    
    public FileReaderProvider(String filename, boolean gzip) throws IOException {
        super(true);
        InputStream stream = new FileInputStream(filename);
        if (gzip) {
            stream = new GZIPInputStream(stream);
        }
        setStream(stream);
    }

}
