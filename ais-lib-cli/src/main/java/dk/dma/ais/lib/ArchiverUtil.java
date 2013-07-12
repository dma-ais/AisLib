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
package dk.dma.ais.lib;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.enav.util.function.Consumer;

/**
 * Just some wrappers from AisReaders.
 * 
 * @author Kasper Nielsen
 */
class ArchiverUtil {
    static final Logger LOG = LoggerFactory.getLogger(ArchiverUtil.class);

    static Service wrapAisReader(final AisReader reader, final Consumer<AisPacket> handler, final CountDownLatch latch) {
        requireNonNull(reader);
        requireNonNull(handler);
        requireNonNull(latch);
        reader.registerPacketHandler(handler);
        return new AbstractExecutionThreadService() {
            @Override
            protected void triggerShutdown() {
                LOG.info("Trying to stop reader " + reader.getSourceId());
                reader.stopReader();
                LOG.info("Trying to join reader thread" + reader.getSourceId());
                try {
                    reader.join();
                } catch (InterruptedException e) {
                    LOG.error("Interrupted while waiting for shutdown", e);
                }
            }

            @Override
            protected void run() throws Exception {
                try {
                    reader.run();
                } finally {
                    latch.countDown();
                }
            }
        };
    }

    static Service wrapAisReader(final AisReader reader, final Consumer<AisPacket> handler) {
        requireNonNull(reader);
        requireNonNull(handler);
        reader.registerPacketHandler(handler);
        return new AbstractExecutionThreadService() {
            @Override
            protected void triggerShutdown() {
                LOG.info("Trying to stop reader " + reader);
                reader.stopReader();
                LOG.info("Trying to join reader thread" + reader);
                try {
                    reader.join();
                } catch (InterruptedException e) {
                    LOG.error("Interrupted while waiting for shutdown", e);
                }
            }

            @Override
            protected void run() throws Exception {
                reader.run();
            }
        };
    }
}
