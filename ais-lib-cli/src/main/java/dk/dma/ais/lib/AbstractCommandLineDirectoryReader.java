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

package dk.dma.ais.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.commons.app.AbstractCommandLineTool;
import java.util.function.Consumer;

/**
 * @author Jens Tuxen
 *
 */
public abstract class AbstractCommandLineDirectoryReader extends AbstractCommandLineTool implements Consumer<AisPacket>{
    
    /** The logger. */
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommandLineDirectoryReader.class);

    
    @Parameter(names = "-directory", description = "The directory to read")
    protected String directory = "."; 
    
    @Parameter(names = "-pattern", description = "filepattern to look for")
    protected String pattern = "*";

    private AisReader aisDirectoryReader;

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


}
