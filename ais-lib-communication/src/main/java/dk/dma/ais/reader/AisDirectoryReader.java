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

    AisDirectoryReader(String dir, String pattern, boolean recursive) throws IOException {
        requireNonNull(dir);
        requireNonNull(pattern);
        this.pattern = pattern;
        this.dir = Paths.get(dir);
        this.recursive = recursive;
        if (!Files.exists(this.dir) || !Files.isDirectory(this.dir)) {
            throw new IOException("No such directory: " + dir);
        }
    }

    @Override
    public void run() {
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        final FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
            boolean firstDir = true;
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                if (matcher.matches(file.getFileName())) {
                    try {
                        InputStream in = AisReaders.createFileInputStream(file.toString());
                        readLoop(in);
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
            Files.walkFileTree(this.dir, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
        } catch (IOException e) {
            LOG.error("Failed to read directory: " + e.getMessage());
        }
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

}
