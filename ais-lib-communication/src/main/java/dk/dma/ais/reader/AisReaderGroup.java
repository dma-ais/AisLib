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
import dk.dma.ais.packet.AisPacketStream.Subscription;
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
    final ConcurrentHashMapV8<String, AisReader> readers = new ConcurrentHashMapV8<>();

    /** All current subscriptions. */
    final ConcurrentHashMapV8<AisReader, AisPacketStream.Subscription> subscriptions = new ConcurrentHashMapV8<>();

    void add(AisReader reader) {
        lock.lock();
        try {
            if (readers.containsKey(reader.getSourceId())) {

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

    public void addReader(String url) {

    }

    public Service asService() {
        return new AbstractIdleService() {
            @Override
            protected void shutDown() throws Exception {
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
            }

            @Override
            protected void startUp() throws Exception {
                for (AisReader r : readers.values()) {
                    r.start();
                }
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<AisReader> iterator() {
        return Collections.unmodifiableCollection(readers.values()).iterator();
    }

    void remove(String name) {
        AisReader reader = readers.get(name);
        if (reader != null) {
            Subscription s = subscriptions.remove(reader);
            s.cancel();
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
