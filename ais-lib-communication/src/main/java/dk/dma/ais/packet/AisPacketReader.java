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
package dk.dma.ais.packet;

import com.google.common.collect.AbstractIterator;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.commons.util.io.CountingInputStream;
import dk.dma.commons.util.io.OutputStreamSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;

/**
 * Class for reading AIS packet streams.
 *
 * @author Kasper Nielsen
 */
public class AisPacketReader implements AutoCloseable, Iterable<AisPacket> {

    /** The logger */
    static final Logger LOG = LoggerFactory.getLogger(AisPacketReader.class);

    /** The number of bytes read by this instance. */
    private final AtomicLong bytesRead = new AtomicLong();

    /** Whether or not this reader has been closed. */
    volatile boolean closed;

    /** The number of lines read by this instance. */
    private final AtomicLong linesRead = new AtomicLong();

    /** Reader to parse lines and deliver complete AIS packets. */
    protected final AisPacketParser packetReader = new AisPacketParser();

    /** The number of packets read by this instance. */
    private final AtomicLong packetsRead = new AtomicLong();

    /** The wrapped reader. */
    final BufferedReader reader;

    /** The wrapped input stream. */
    final InputStream stream;

    /**
     * Sometimes we expect input to only contain valid messages. So we want to throw an exception in case we meet some
     * unknown content. This was added when trying to find a bug, where we where trying to read a gzipped stream.
     */
    final boolean throwExceptions;

    /**
     * Create
     *
     * @param stream
     *            the input stream to read data from
     */
    public AisPacketReader(InputStream stream) {
        this(stream, false);
    }

    AisPacketReader(InputStream stream, boolean errorFree) {
        this.stream = requireNonNull(stream);
        this.reader = new BufferedReader(new InputStreamReader(new CountingInputStream(stream, bytesRead),
                StandardCharsets.US_ASCII));
        this.throwExceptions = errorFree;
    }

    public void close() throws IOException {
        stream.close();
        closed = true;
    }

    /**
     * Returns the number of bytes read by this reader.
     *
     * @return the number of bytes read by this reader
     */
    public long getNumberOfBytesRead() {
        return bytesRead.get();
    }

    /**
     * Returns the number of lines read by this reader.
     *
     * @return the number of lines read by this reader
     */
    public long getNumberOfLinesRead() {
        return linesRead.get();
    }

    /**
     * Returns the number of packets read by this reader.
     *
     * @return the number of packets read by this reader
     */
    public long getNumberOfPacketsRead() {
        return packetsRead.get();
    }

    /**
     * Override this method to handle {@link Abk} sentences.
     *
     * @param abk
     *            the sentence to handle
     */
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received ABK: " + line);
            }
            Abk abk = new Abk();
            try {
                abk.parse(new SentenceLine(line));
                handleAbk(abk);
            } catch (Exception e) {
                if (throwExceptions) {
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
            if (throwExceptions) {
                throw new IOException(se);
            }
            LOG.error("Sentence error: " + line + " (possible related proptag: " + se.getPossibleProprietaryTag() + ")");
            LOG.debug("Sentence trace: " + se.getMessage());
            return null;
        } catch (Exception e) {
            if (throwExceptions) {
                throw new IOException(e);
            }
            LOG.error("Sentence line error: " + line);
            LOG.debug("Sentence line error: " + e.getMessage());
            return null;
        }
    }

    @SafeVarargs
    public final void forEachRemaining(Consumer<? super AisPacket>... consumers) throws IOException {
        requireNonNull(consumers);
        AisPacket p;
        while ((p = readPacket()) != null) {
            for (Consumer<? super AisPacket> c : consumers) {
                c.accept(p);
            }
        }
    }

    @SafeVarargs
    public final void forEachRemainingMessage(Consumer<? super AisMessage>... consumers) throws IOException {
        requireNonNull(consumers);
        AisPacket p;
        while ((p = readPacket()) != null) {
            AisMessage m = p.tryGetAisMessage();
            if (m != null) {
                for (Consumer<? super AisMessage> c : consumers) {
                    c.accept(m);
                }
            }
        }
    }

    /**
     * @return the next packet or null if the end of the stream has been reached
     * @throws IOException
     *             if an exception occurred while reading the packet
     */
    public AisPacket readPacket() throws IOException {
        return readPacket0();
    }

    /**
     * Reads the next AisPacket
     */
    AisPacket readPacket0() throws IOException {
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (closed) {
                return null;
            }
            AisPacket p = handleLine(line);
            if (p != null) {
                packetsRead.incrementAndGet();
                return p;
            }
        }
        return null;
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
        final AisPacketStream s = AisPacketStream.newStream();
        e.execute(new Runnable() {
            public void run() {
                try {
                    for (AisPacket p = readPacket(); p != null; p = readPacket()) {
                        s.add(p);
                    }
                } catch (IOException e) {
                    LOG.error("Failed to read packet: ", e);
                }
            }
        });
        return s.immutableStream();
    }

    public static AisPacketReader createFromSystemResource(String resourceName, boolean throwExceptions)
            throws IOException {
        URL url = ClassLoader.getSystemResource(resourceName);
        if (url == null) {
            throw new IllegalArgumentException("Could not find system resource: " + resourceName);
        }
        InputStream inputStream = url.openStream();
        return new AisPacketReader(inputStream, throwExceptions);

    }

    /**
     * Creates a new AIS packet reader from the specified file. If the specified file has a '.zip' suffix. The file is
     * automatically treated as a zip file.
     *
     * @param p
     *            the path of the file
     * @param throwExceptions
     *            whether to throw exceptions or just log them
     * @return a new reader
     * @throws IOException
     *             if the reader failed to be constructed, for example, if the specified file does not exist
     */
    public static AisPacketReader createFromFile(Path p, boolean throwExceptions) throws IOException {
        InputStream is = Files.newInputStream(p);
        BufferedInputStream bis = new BufferedInputStream(is);
        if (!p.getFileName().toString().endsWith(".zip")) {
            return new AisPacketReader(bis, throwExceptions); // not a zip file
        }
        final ZipInputStream zis = new ZipInputStream(bis);

        return new AisPacketReader(zis, throwExceptions) {
            ZipEntry e;

            boolean isFirst = true;

            /** {@inheritDoc} */
            @Override
            AisPacket readPacket0() throws IOException {
                if (!isFirst && e == null) {
                    return null; // already empty
                } else if (isFirst) {
                    e = zis.getNextEntry();
                    isFirst = false;
                }
                AisPacket p = super.readPacket0();
                // while the packet is null and we still have more zip entries
                while (p == null && e != null) {
                    e = zis.getNextEntry();
                    p = super.readPacket0();
                }
                return p;
            }
        };
    }

    /**
     * Writes the reminder of packets to the output stream using the specified sink
     *
     * @param os
     *            the output stream to write to
     * @param sink
     *            the sink to write to
     * @throws IOException
     *             if the a packet could not be written
     */
    public void writeTo(OutputStream os, OutputStreamSink<AisPacket> sink) throws IOException {
        sink.header(os);
        long count = 0;
        AisPacket p = readPacket();
        while (p != null) {
            count++;
            sink.process(os, p, count);
            p = readPacket();
        }
        sink.footer(os, count);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<AisPacket> iterator() {
        return new IteratorImpl();
    }

    class IteratorImpl extends AbstractIterator<AisPacket> {

        /** {@inheritDoc} */
        @Override
        protected AisPacket computeNext() {
            try {
                AisPacket p = readPacket();
                if (p == null) {
                    super.endOfData();
                }
                return p;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
