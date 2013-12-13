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
package dk.dma.ais.reader;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.enav.util.function.Consumer;

/**
 * AIS reader that iterates over every file from a given base directory. If a file matches a given pattern it will be read.
 * 
 * If recursive used all sub directories will also be scanned using depth first. No guarantees no ordering of directories and files.
 */
public class AisDirectoryReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisDirectoryReader.class);

    private volatile boolean done;
    private final String pattern;
    private final Path dir;
    private final boolean recursive;

    private Long totalNumberOfPacketsToRead;

    AisDirectoryReader(String dir, String pattern, boolean recursive) throws IOException {
        requireNonNull(dir);
        requireNonNull(pattern);
        this.pattern = pattern;
        this.dir = Paths.get(dir);
        this.recursive = recursive;
        if (!Files.exists(this.dir) || !Files.isDirectory(this.dir)) {
            throw new IOException("No such directory: " + dir);
        }
        this.totalNumberOfPacketsToRead = -1L;
    }

    @Override
    public void run() {
        new MatchingFileIterator() {
            @Override
            protected void doWithInputStreamOfMatchingFile(InputStream in) throws IOException {
                readLoop(in);
            }
        }.iterate();
        done = true;
    }

    @Override
    public Status getStatus() {
        return done ? Status.DISCONNECTED : Status.CONNECTED;
    }

    @Override
    public void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException {
        // Cannot send
        resultListener.accept(null);
    }

    /**
     * Compute an estimate of the fraction of AIS packets which have been read, out of the total no. of AIS packets
     * to read in the matching files. The first call to this method may be long-running as it will scan all the
     * matching files to count AIS packets.
     *
     * @return Estimated fraction of AIS packets read as a floating point number between 0 and 1.
     */
    public float getEstimatedFractionOfPacketsRead() {
        final long packetsRead = getNumberOfLinesRead();
        final long packetsToRead = getTotalNumberOfPacketsToRead();
        final float fractionRead = ((float) packetsRead) / ((float) packetsToRead);
        return Math.min(fractionRead, 1.0f);
    }

    private long getTotalNumberOfPacketsToRead() {
        if (totalNumberOfPacketsToRead < 0L) {
            synchronized (totalNumberOfPacketsToRead) {
                if (totalNumberOfPacketsToRead < 0L) {
                    LOG.debug("Scanning all input files to count packets.");
                    totalNumberOfPacketsToRead = 0L;
                    new MatchingFileIterator() {
                        @Override
                        protected void doWithInputStreamOfMatchingFile(InputStream in) {
                            try (AisPacketReader s = new AisPacketReader(in)) {
                                while ((s.readPacket(getSourceId())) != null) {
                                    totalNumberOfPacketsToRead++;
                                    if (totalNumberOfPacketsToRead % 1e6 == 0) {
                                        if (totalNumberOfPacketsToRead % 1e7 == 0) {
                                            LOG.info("Packets counted: " + totalNumberOfPacketsToRead);
                                        } else {
                                            LOG.debug("Packets counted: " + totalNumberOfPacketsToRead);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                LOG.error("Failed to read packets: " + e.getMessage(), e);
                            }
                        }
                    }.iterate();
                }
                LOG.info("AIS packets counted in matching files: " + totalNumberOfPacketsToRead);
            }
        }
        return this.totalNumberOfPacketsToRead;
    }

    /**
     * Walk through all matching files and hand their InputStream to the
     * doWithInputStreamOfMatchingFile method.
     */
    private abstract class MatchingFileIterator {
        protected abstract void doWithInputStreamOfMatchingFile(InputStream in) throws IOException;

        public void iterate() {
            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            final FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
                boolean firstDir = true;

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                    if (matcher.matches(file.getFileName())) {
                        try {
                            LOG.debug("Reading packets from file " + file.getFileName().toString());
                            InputStream in = AisReaders.createFileInputStream(file.toString());
                            doWithInputStreamOfMatchingFile(in);
                            in.close();
                        } catch (IOException e) {
                            if (!isShutdown()) {
                                LOG.error("Failed to read file: " + file.toString() + ": " + e.getMessage());
                            }
                            return FileVisitResult.TERMINATE;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (recursive || firstDir) {
                        firstDir = false;
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.SKIP_SUBTREE;
                }
            };
            try {
                Files.walkFileTree(dir, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
            } catch (IOException e) {
                LOG.error("Failed to read directory: " + e.getMessage());
            }
        }
    }
}
