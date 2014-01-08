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

package dk.dma.ais.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.commons.app.AbstractCommandLineTool;
import dk.dma.enav.util.function.Consumer;

/**
 * @author Jens Tuxen
 *
 */
public abstract class AbstractCommandLineDirectoryReader extends AbstractCommandLineTool implements Consumer<AisPacket> {
    
    /** The logger. */
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommandLineDirectoryReader.class);

    
    @Parameter(names = "-directory", description = "The directory to read")
    protected String directory = "."; 
    
    @Parameter(names = "-pattern", description = "filepattern to look for")
    protected String pattern = "*";

    protected AisReader aisDirectoryReader;

    /* (non-Javadoc)
     * @see dk.dma.commons.app.AbstractDmaApplication#run(com.google.inject.Injector)
     */
    @Override
    protected void run(Injector injector) throws Exception {
        aisDirectoryReader = AisReaders.createDirectoryReader(directory, pattern, true);
        aisDirectoryReader.registerPacketHandler(this);
        aisDirectoryReader.start();
        aisDirectoryReader.join();
    }
    
    /* (non-Javadoc)
     * @see dk.dma.enav.util.function.Consumer#accept(java.lang.Object)
     */
    @Override
    public void accept(AisPacket t) {
        this.process(t);
    }

    /**
     * whatever processing needs to be done
     * @param t
     */
    public abstract void process(AisPacket t);

}
