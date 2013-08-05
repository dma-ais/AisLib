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
}
