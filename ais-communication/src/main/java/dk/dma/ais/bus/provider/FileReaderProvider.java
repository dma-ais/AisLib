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
        super();
        InputStream stream = new FileInputStream(filename);
        if (gzip) {
            stream = new GZIPInputStream(stream);
        }
        setStream(stream);
    }

}
