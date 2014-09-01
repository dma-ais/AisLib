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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketOutputSinks;
import dk.dma.ais.reader.AisReaderGroup;
import dk.dma.ais.reader.AisReaders;
import dk.dma.commons.app.AbstractDaemon;
import dk.dma.commons.management.ManagedAttribute;
import dk.dma.commons.management.ManagedOperation;
import dk.dma.commons.management.ManagedResource;
import dk.dma.commons.service.AbstractBatchedStage;
import dk.dma.commons.service.io.MessageToFileService;
import dk.dma.commons.util.io.IoUtil;
import java.util.function.Consumer;

/**
 * Writes AisData to a directory.
 * 
 * @author Kasper Nielsen
 */
@ManagedResource
public class FileDump extends AbstractDaemon {

    /** The logger. */
    static final Logger LOGGER = LoggerFactory.getLogger(FileDump.class);

    @Parameter(names = "-directory", description = "The backup directory")
    File backup = new File("ais-dump");

    @Parameter(names = "-fileformat", description = "The backup Format")
    String backupFormat = "yyyy/MM-dd/'aisarchive' yyyy-MM-dd HHmm'.txt.zip'";

    @Parameter(description = "filestore [A list of AIS sources (sourceName=host:port,host:port sourceName=host:port ...]")
    List<String> sources;

    @ManagedAttribute
    public String getDirectory() {
        return backup.getAbsolutePath();
    }

    @ManagedOperation
    public long getStoreSize() throws IOException {
        return IoUtil.recursiveSizeOf(backup.toPath());
    }

    /** {@inheritDoc} */
    @Override
    protected void runDaemon(Injector injector) throws Exception {
        LOGGER.info("Starting file archiver with sources = " + sources);
        LOGGER.info("Archived files are written to " + backup.toPath().toAbsolutePath());
        // setup a reader group
        AisReaderGroup g = AisReaders.createGroup("filedump", sources);

        // Starts the backup service that will write files to disk if disconnected
        final AbstractBatchedStage<AisPacket> fileWriter = start(MessageToFileService.dateTimeService(backup.toPath(),
                backupFormat, AisPacketOutputSinks.OUTPUT_TO_TEXT));

        start(g.asService());// connects to all sources
        g.stream().subscribe(new Consumer<AisPacket>() {
            public void accept(AisPacket p) {
                // We use offer because we do not want to block receiving thread
                if (!fileWriter.getInputQueue().offer(p)) {
                    LOGGER.error("Could not persist packet, dropping it");
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        // args = new String[] { "src1=ais163.sealan.dk:65262,ais167.sealan.dk:65261",
        // "src2=iala63.sealan.dk:4712,iala68.sealan.dk:4712", "src3=10.10.5.144:65061" };
        // args = AisReaders.getDefaultSources();
        if (args.length == 0) {
            System.err.println("Must specify at least 1 source (sourceName=host:port,host:port sourceName=host:port)");
            System.exit(1);
        }
        new FileDump().execute(args);
    }
}
