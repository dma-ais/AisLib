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
package dk.dma.ais.reader;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * AIS reader that iterates over every file from a given base directory. If a file matches a given pattern it will be read.
 * 
 * If recursive used all sub directories will also be scanned using depth first.
 * 
 * Default ordering is by full path names. A Comparator<Path> can be provided for alternative ordering. 
 * 
 */
public class AisDirectoryReader extends AisReader {

    private static final Logger LOG = LoggerFactory.getLogger(AisDirectoryReader.class);

    private volatile boolean done;
    private final String pattern;
    private final Path dir;
    private final boolean recursive;
    private final Comparator<Path> comparator;

    private Long totalNumberOfPacketsToRead;
    
    AisDirectoryReader(String dir, String pattern, boolean recursive) throws IOException {
        this(dir, pattern, recursive, null);
    }

    AisDirectoryReader(String dir, String pattern, boolean recursive, Comparator<Path> comparator) throws IOException {
        requireNonNull(dir);
        requireNonNull(pattern);
        this.pattern = pattern;
        this.dir = Paths.get(dir);
        this.recursive = recursive;
        this.comparator = comparator;
        if (!Files.exists(this.dir) || !Files.isDirectory(this.dir)) {
            throw new IOException("No such directory: " + dir);
        }
        this.totalNumberOfPacketsToRead = -1L;
    }    

    @Override
    public void run() {
        new MatchingFileIterator(comparator) {
            @Override
            protected void doWithMatchingFile(Path file) throws IOException {
                try (InputStream in = AisReaders.createFileInputStream(file.toString())) {
                    LOG.debug("Reading packets from file " + file.getFileName().toString());
                    readLoop(in);
                    LOG.debug("Completed reading packets from file " + file.getFileName().toString());
                }
            }
        }.iterate();

        LOG.debug("No more files to read.");
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
     * Compute an estimate of the fraction of AIS packets which have been read, out of the total no. of AIS packets to read in the
     * matching files. The first call to this method may be long-running as it will scan all the matching files to count AIS
     * packets.
     * 
     * @return Estimated fraction of AIS packets read as a floating point number between 0 and 1.
     */
    public float getEstimatedFractionOfPacketsRead() {
        final long packetsRead = getNumberOfLinesRead();
        final long packetsToRead = getTotalNumberOfPacketsToRead();
        final float fractionRead = ((float) packetsRead) / ((float) packetsToRead);
        return Math.max(0.0f, Math.min(fractionRead, 1.0f));
    }

    private long getTotalNumberOfPacketsToRead() {
        if (totalNumberOfPacketsToRead < 0L) {
            synchronized (this) {
                if (totalNumberOfPacketsToRead < 0L) {
                    LOG.debug("Scanning all input files to count packets.");
                    totalNumberOfPacketsToRead = 0L;
                    new MatchingFileIterator(comparator) {
                        @Override
                        protected void doWithMatchingFile(Path path) {
                            LOG.debug("Estimating no. of AIS packets in file " + path.getFileName().toString());
                            File file = path.toFile();
                            if (file.exists() && file.isFile() && file.canRead()) {
                                long fileSize = file.length();
                                boolean compressed = file.getName().endsWith(".gz");

                                long estimatedNumberOfPacketsInFile;
                                if (compressed) {
                                    estimatedNumberOfPacketsInFile = (long) ((double) fileSize * 0.030);
                                } else {
                                    estimatedNumberOfPacketsInFile = (long) ((double) fileSize * 0.009);
                                }

                                LOG.debug("estimatedNumberOfPacketsInFile " + estimatedNumberOfPacketsInFile);
                                totalNumberOfPacketsToRead += estimatedNumberOfPacketsInFile;
                            }
                        }
                    }.iterate();
                }
                LOG.info("Estimated no. of AIS packets in matching files: " + totalNumberOfPacketsToRead);
            }
        }
        return this.totalNumberOfPacketsToRead;
    }

    /**
     * Walk through all matching files and hand their InputStream to the doWithInputStreamOfMatchingFile method.
     * 
     * Matching files are collected first and handled afterwards to allow different sorting schemes
     */
    private abstract class MatchingFileIterator {
        
        private final Comparator<Path> comparator;      
        
        public MatchingFileIterator(Comparator<Path> comparator) {
            this.comparator = comparator;
        }
        
        protected abstract void doWithMatchingFile(Path matchingFile) throws IOException;

        private void handleFile(Path file) {
            try {
                doWithMatchingFile(file);
            } catch (IOException e) {
                if (!isShutdown()) {
                    LOG.error("Failed to work with file: " + file.toString() + ": " + e.getMessage());
                }
            }
        }

        public void iterate() {
            final List<Path> files = new ArrayList<>();
            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            final FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
                boolean firstDir = true;

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                    if (matcher.matches(file.getFileName())) {
                        files.add(file);
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
            
            // Sort files
            if (this.comparator != null) {
                Collections.sort(files, comparator);
            } else {
                Collections.sort(files);
            }
            
            // Iterate through all files
            for (Path file : files) {                
                handleFile(file);
            }

        }
    }
    
}
