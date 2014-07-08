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

/**
 * An MXBean interface to {@link AisReaderGroup}.
 * 
 * @author Kasper Nielsen
 */
public interface AisReaderGroupMXBean {

    /**
     * Adds the specified reader to the group.
     * 
     * @param sourceId
     *            the source id of the reader
     * @param oneOrMorHosts
     *            one or more hosts
     */
    void addReader(String sourceId, String oneOrMorHosts);

    /**
     * Returns the number of bytes received.
     * 
     * @return the number of bytes received
     */
    long getBytesReceived();

    /**
     * Returns the number of bytes received.
     * 
     * @return the number of bytes received
     */
    long getPacketsReceived();

    /**
     * Returns the number of readers.
     * 
     * @return the number of readers
     */
    int getReaderCount();

    /**
     * Removes a reader from the group.
     * 
     * @param sourceId
     *            the source id of the group
     * @return true if a reader with the specified id was removed, false otherwise
     */
    boolean removeReader(String sourceId);
}

/** The default implementation */
class AisReaderGroupMXBeanImpl implements AisReaderGroupMXBean {

    final AisReaderGroup group;

    AisReaderGroupMXBeanImpl(AisReaderGroup group) {
        this.group = requireNonNull(group);
    }

    /** {@inheritDoc} */
    @Override
    public void addReader(String sourceId, String oneOrMorHosts) {
        AisTcpReader reader = AisReaders.createReader(oneOrMorHosts);
        reader.setSourceId(sourceId);
        group.add(reader);
        reader.start();
    }

    /** {@inheritDoc} */
    @Override
    public int getReaderCount() {
        return group.readers.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeReader(String sourceId) {
        return group.remove(sourceId);
    }

    /** {@inheritDoc} */
    @Override
    public long getBytesReceived() {
        long sum = 0;
        for (AisTcpReader r : group.readers.values()) {
            sum += r.getNumberOfBytesRead();
        }
        return sum;
    }

    /** {@inheritDoc} */
    @Override
    public long getPacketsReceived() {
        long sum = 0;
        for (AisTcpReader r : group.readers.values()) {
            sum += r.getNumberOfLinesRead();
        }
        return sum;
    }
}
