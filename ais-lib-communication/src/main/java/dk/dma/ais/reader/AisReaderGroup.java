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

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketStream;

/**
 * A reader group organizes a group of readers.
 * 
 * @author Kasper Nielsen
 */
public class AisReaderGroup implements Iterable<AisReader> {

    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(AisReaderGroup.class);

    /** A stream of all incoming packets. */
    final AisPacketStream stream = AisPacketStream.newStream();

    final ReentrantLock lock = new ReentrantLock();

    /** All readers configured for this group. */
    final ConcurrentHashMap<String, AisTcpReader> readers = new ConcurrentHashMap<>();

    /** All current subscriptions. */
    final ConcurrentHashMap<AisReader, AisPacketStream.Subscription> subscriptions = new ConcurrentHashMap<>();

    /** The name of the group. */
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
     * Returns a stream of incoming packets for all the readers this group is managing.
     * 
     * @return a stream of incoming packets for all the readers this group is managing
     */
    public AisPacketStream stream() {
        return stream.immutableStream();
    }
}
