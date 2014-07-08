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
package dk.dma.ais.configuration.bus.provider;

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.provider.FileReaderProvider;

@XmlRootElement
public class FileReaderProviderConfiguration extends AisBusProviderConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(FileReaderProvider.class);

    private String filename;
    private boolean gzip;

    public FileReaderProviderConfiguration() {

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isGzip() {
        return gzip;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }

    @Override
    @XmlTransient
    public AisBusComponent getInstance() {
        try {
            FileReaderProvider provider = new FileReaderProvider(filename, gzip);
            return super.configure(provider);
        } catch (IOException e) {
            LOG.error("Error opening file: " + filename + ": " + e.getMessage());
        }
        return null;
    }

}
