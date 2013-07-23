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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.transform.AisPacketTaggingTransformer;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;
import dk.dma.commons.util.io.CountingInputStream;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketInputStream implements AutoCloseable {
    static final Logger LOG = LoggerFactory.getLogger(AisPacketInputStream.class);

    /** The number of bytes read by this reader. */
    private final AtomicLong bytesRead = new AtomicLong();

    volatile boolean closed;

    /** The number of lines read by this reader. */
    private final AtomicLong linesRead = new AtomicLong();

    /** Reader to parse lines and deliver complete AIS packets. */
    protected final AisPacketReader packetReader = new AisPacketReader();

    final BufferedReader reader;

    final InputStream stream;

    /**
     * Sometimes we expect input to only contain valid messages. So we want to throw an exception in case we meet some
     * unknown content. This was added when trying to find a bug, where we where trying to read a gzipped stream.
     */
    final boolean errorFree;

    public AisPacketInputStream(InputStream stream) {
        this(stream, false);
    }

    public AisPacketInputStream(InputStream stream, boolean errorFree) {
        this.stream = requireNonNull(stream);
        reader = new BufferedReader(new InputStreamReader(new CountingInputStream(stream, bytesRead)));
        this.errorFree = errorFree;
    }

    public void close() throws IOException {
        stream.close();
        closed = true;
    }

    protected void handleAbk(Abk abk) {}

    /**
     * Handle a received line
     * 
     * @param line
     * @return
     */
    private AisPacket handleLine(String line) throws IOException {
        linesRead.incrementAndGet();
        // Check for ABK
        if (Abk.isAbk(line)) {
            LOG.debug("Received ABK: " + line);
            Abk abk = new Abk();
            try {
                abk.parse(line);
                handleAbk(abk);
            } catch (Exception e) {
                if (errorFree) {
                    throw new IOException(e);
                }
                LOG.error("Failed to parse ABK: " + line + ": " + e.getMessage());
            }
            packetReader.newVdm();
            return null;
        }

        try {
            return packetReader.readLine(line);
        } catch (SentenceException se) {
            if (errorFree) {
                throw new IOException(se);
            }
            LOG.info("Sentence error: " + se.getMessage() + " line: " + line);
            return null;
        }
    }

    /**
     * Reads the next AisPacket.
     */
    public AisPacket readPacket() throws IOException {
        return readPacket(null);
    }

    /**
     * Returns a AIS packet stream running in a new thread.
     * 
     * @return a AIS packet stream running in a new thread
     */
    public AisPacketStream stream() {
        return stream(Executors.newSingleThreadExecutor());
    }

    /**
     * Returns a AIS packet stream using the specified executor.
     * 
     * @return a AIS packet stream using the specified executor
     */
    public AisPacketStream stream(Executor e) {
        final AisPacketStream s = AisPacketStreams.newStream();
        e.execute(new Runnable() {
            public void run() {
                try {
                    for (AisPacket p = readPacket(); p != null; readPacket()) {
                        s.add(readPacket());
                    }
                } catch (IOException e) {
                    LOG.error("Failed to read packet: ", e);
                }
            }
        });
        return AisPacketStreams.immutableStream(s);
    }

    public AisPacket readPacket(String source) throws IOException {
        return readPacket0(source);
    }

    /**
     * Reads the next AisPacket using the specified source id.
     */
    AisPacket readPacket0(String source) throws IOException {
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (closed) {
                return null;
            }
            AisPacket p = handleLine(line);
            if (p != null) {
                if (source != null) {
                    AisPacketTags tagging = new AisPacketTags();
                    tagging.setSourceId(source);
                    AisPacketTaggingTransformer tranformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING,
                            tagging);
                    return tranformer.transform(p);
                }
                return p;
            }
        }
        return null;
    }

    public static AisPacketInputStream createFromFile(Path p, boolean errorOnRead) throws IOException {
        InputStream is = Files.newInputStream(p);
        BufferedInputStream bis = new BufferedInputStream(is);
        if (!p.getFileName().toString().endsWith(".zip")) {
            return new AisPacketInputStream(bis, errorOnRead); // not a zip file
        }
        final ZipInputStream zis = new ZipInputStream(bis);

        return new AisPacketInputStream(zis, errorOnRead) {
            boolean isFirst = true;
            ZipEntry e;

            /** {@inheritDoc} */
            @Override
            AisPacket readPacket0(String source) throws IOException {
                if (!isFirst && e == null) {
                    return null; // already empty
                } else if (isFirst) {
                    e = zis.getNextEntry();
                    isFirst = false;
                }
                AisPacket p = super.readPacket0(source);
                // while the packet is null and we still have more zip entries
                while (p == null && e != null) {
                    e = zis.getNextEntry();
                    p = super.readPacket0(source);
                }
                return p;
            }
        };
    }
}
