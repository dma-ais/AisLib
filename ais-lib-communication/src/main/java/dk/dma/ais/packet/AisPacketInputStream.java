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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.reader.AisReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.transform.AisPacketTaggingTransformer;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;
import dk.dma.commons.util.io.CountingInputStream;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketInputStream {
    static final Logger LOG = LoggerFactory.getLogger(AisReader.class);

    final InputStream stream;

    volatile boolean closed;

    public static void main(String[] args) throws IOException {
        AisPacketInputStream s = new AisPacketInputStream(new FileInputStream("null"));
        AisPacket p;
        while ((p = s.readPacket()) != null) {
            System.out.println(p);
        }
    }

    /** The number of bytes read by this reader. */
    private final AtomicLong bytesRead = new AtomicLong();

    /** The number of lines read by this reader. */
    private final AtomicLong linesRead = new AtomicLong();

    /** Reader to parse lines and deliver complete AIS packets. */
    protected final AisPacketReader packetReader = new AisPacketReader();

    final BufferedReader reader;

    public AisPacketInputStream(InputStream stream) {
        this.stream = requireNonNull(stream);
        reader = new BufferedReader(new InputStreamReader(new CountingInputStream(stream, bytesRead)));
    }

    protected void handleAbk(Abk abk) {}

    public void close() throws IOException {
        stream.close();
        closed = true;
    }

    /**
     * Handle a received line
     * 
     * @param line
     * @return
     */
    protected AisPacket handleLine(String line) {
        linesRead.incrementAndGet();
        // Check for ABK
        if (Abk.isAbk(line)) {
            LOG.debug("Received ABK: " + line);
            Abk abk = new Abk();
            try {
                abk.parse(line);
                handleAbk(abk);
            } catch (Exception e) {
                LOG.error("Failed to parse ABK: " + line + ": " + e.getMessage());
            }
            packetReader.newVdm();
            return null;
        }

        try {
            return packetReader.readLine(line);
        } catch (SentenceException se) {
            LOG.info("Sentence error: " + se.getMessage() + " line: " + line);
            return null;
        }

    }

    /**
     * Reads the next AisPacket
     * 
     * @return
     * @throws IOException
     */
    public AisPacket readPacket() throws IOException {
        return readPacket(null);
    }

    public AisPacket readPacket(String source) throws IOException {
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

    /**
     * Construct AisPacket from raw packet string
     * 
     * @param messageString
     * @param optional
     *            factory
     * @return
     * @throws SentenceException
     */
    public static AisPacket readPacketFromString(String messageString) throws SentenceException {
        AisPacket packet = null;
        AisPacketReader packetReader = new AisPacketReader();
        String[] lines = messageString.split("\\r?\\n");
        for (String line : lines) {
            packet = packetReader.readLine(line);
            if (packet != null) {
                return packet;
            }
        }
        return null;
    }
}
