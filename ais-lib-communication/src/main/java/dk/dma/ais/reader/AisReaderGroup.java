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

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import jsr166e.ConcurrentHashMapV8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketStream;
import dk.dma.ais.packet.AisPacketStreams;
import dk.dma.enav.util.function.Consumer;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisReaderGroup implements Iterable<AisReader> {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(AisReaderGroup.class);

    /** A stream of all incoming packets. */
    final AisPacketStream stream = AisPacketStreams.newStream();

    final ReentrantLock lock = new ReentrantLock();

    /** All readers configured for this group. */
    final ConcurrentHashMapV8<String, AisTcpReader> readers = new ConcurrentHashMapV8<>();

    /** All current subscriptions. */
    final ConcurrentHashMapV8<AisReader, AisPacketStream.Subscription> subscriptions = new ConcurrentHashMapV8<>();

    final String name;

    AisReaderGroup(String name) {
        this.name = requireNonNull(name);
    }

    void add(AisTcpReader reader) {
        lock.lock();
        try {
            if (readers.containsKey(reader.getSourceId())) {
                throw new IllegalArgumentException("A reader with the specified source has already been added, id = "
                        + reader.getSourceId());
            }
            readers.put(reader.getSourceId(), reader);
            subscriptions.put(reader, reader.stream().subscribe(new Consumer<AisPacket>() {
                public void accept(AisPacket p) {
                    stream.add(p);
                }
            }));
        } finally {
            lock.unlock();
        }
    }

    public Service asService() {
        return new AbstractIdleService() {
            @Override
            protected void shutDown() throws Exception {
                lock.lock();
                try {
                    for (AisReader r : readers.values()) {
                        LOG.info("Trying to stop reader " + r);
                        r.stopReader();
                    }
                    for (AisReader r : readers.values()) {
                        try {
                            LOG.info("Trying to join reader thread" + r);
                            r.join();
                        } catch (InterruptedException e) {
                            LOG.error("Interrupted while waiting for shutdown", e);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }

            @Override
            protected void startUp() throws Exception {
                lock.lock();
                try {
                    for (AisReader r : readers.values()) {
                        r.start();
                    }
                } finally {
                    lock.unlock();
                }
            }
        };
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Iterator<AisReader> iterator() {
        return (Iterator) Collections.unmodifiableCollection(readers.values()).iterator();
    }

    boolean remove(String name) {
        lock.lock();
        try {
            AisReader reader = readers.get(name);
            if (reader != null) {
                subscriptions.remove(reader).cancel();
            }
            reader.stopReader();
            return reader != null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns a stream of all incoming packets.
     * 
     * @return a stream of all incoming packets
     */
    public AisPacketStream stream() {
        return AisPacketStreams.immutableStream(stream);
    }
}
