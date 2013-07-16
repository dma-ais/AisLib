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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import jsr166e.ConcurrentHashMapV8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
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

    final ConcurrentHashMapV8<String, AisReader> readers = new ConcurrentHashMapV8<>();

    final ConcurrentHashMapV8<AisReader, AisPacketStream.Subscription> subscriptions = new ConcurrentHashMapV8<>();

    final AisPacketStream stream = AisPacketStreams.newStream();

    final AisPacketStream immutableStream = AisPacketStreams.immutableStream(stream);

    final ReentrantLock lock = new ReentrantLock();

    void add(AisReader reader) {
        lock.lock();
        try {
            readers.put(reader.getSourceId(), reader);
            subscriptions.put(reader, reader.stream().subscribePackets(new Consumer<AisPacket>() {
                public void accept(AisPacket p) {
                    stream.add(p);
                }
            }));
        } finally {
            lock.unlock();
        }
    }

    void remove(String name) {
        AisReader reader = readers.get(name);
        if (reader != null) {
            Subscription s = subscriptions.remove(reader);
            s.cancel();
        }
    }

    public AisPacketStream stream() {
        return immutableStream;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<AisReader> iterator() {
        return Collections.unmodifiableCollection(readers.values()).iterator();
    }

    public Service asService() {
        return new AbstractIdleService() {

            @Override
            protected void startUp() throws Exception {
                for (AisReader r : readers.values()) {
                    r.start();
                }
            }

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
        };
    }

    /**
     * Parses the string and returns either an {@link AisTcpReader} if only one hostname is found or a
     * {@link RoundRobinAisTcpReader} if more than 1 host name is found. Example
     * "mySource=ais163.sealan.dk:65262,ais167.sealan.dk:65261" will return a RoundRobinAisTcpReader alternating between
     * the two sources if one is down.
     * 
     * @param fullSource
     *            the full source
     * @return a ais reader
     */
    static AisTcpReader parseSource(String fullSource) {
        try (Scanner s = new Scanner(fullSource);) {
            s.useDelimiter("\\s*=\\s*");
            if (!s.hasNext()) {
                throw new IllegalArgumentException("Source must be of the format src=host:port,host:port, was "
                        + fullSource);
            }
            String src = s.next();
            List<String> hosts = new ArrayList<>();
            if (!s.hasNext()) {
                throw new IllegalArgumentException(
                        "A list of hostname:ports must follow the source (format src=host:port,host:port), was "
                                + fullSource);
            }
            try (Scanner s1 = new Scanner(s.next())) {
                s1.useDelimiter("\\s*,\\s*");
                if (!s1.hasNext()) {
                    throw new IllegalArgumentException(
                            "Source must have at least one host:port (format src=host:port,host:port), was "
                                    + fullSource);
                }
                while (s1.hasNext()) {
                    String hostPort = s1.next();
                    HostAndPort hap = HostAndPort.fromString(hostPort);
                    hosts.add(hap.toString());
                }
            }

            final AisTcpReader r = hosts.size() == 1 ? new AisTcpReader(hosts.get(0)) : new RoundRobinAisTcpReader(
                    hosts);
            r.setSourceId(src);
            return r;
        }
    }

    /**
     * Equivalent to {@link #parseSource(String)} except that it will parse an array of sources. Returning a map making
     * sure there all source names are unique
     */
    public static AisReaderGroup create(List<String> sources) {
        Map<String, AisTcpReader> readers = new HashMap<>();
        for (String s : sources) {
            AisTcpReader r = parseSource(s);
            if (readers.put(r.getSourceId(), r) != null) {
                // Make sure its unique
                throw new Error("More than one reader specified with the same source id (id =" + r.getSourceId()
                        + "), source string = " + sources);
            }
        }
        AisReaderGroup g = new AisReaderGroup();
        for (AisTcpReader r : readers.values()) {
            g.add(r);
        }
        return g;
    }

}
