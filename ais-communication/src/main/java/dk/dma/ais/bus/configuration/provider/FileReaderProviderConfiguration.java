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

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

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
