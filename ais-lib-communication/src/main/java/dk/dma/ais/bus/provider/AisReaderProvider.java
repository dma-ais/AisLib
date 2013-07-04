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
package dk.dma.ais.bus.provider;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.enav.util.function.Consumer;

/**
 * Provider class that uses an AisReader to provide AIS packets
 */
@ThreadSafe
public class AisReaderProvider extends AisBusProvider implements Consumer<AisPacket> {
    
    /**
     * The AIS reader to provide packets
     */
    private AtomicReference<AisReader> aisReader = new AtomicReference<>();
    
    public AisReaderProvider() {
        super();
    }
    
    public AisReaderProvider(AisReader aisReader) {
        setAisReader(aisReader);
    }
    
    /**
     * Receive packet from reader
     */
    @Override
    public void accept(AisPacket packet) {
        // Push to bus
        push(packet);        
    }

    
    @Override
    public void start() {
        // Start the reader
        aisReader.get().start();
        setThread(aisReader.get());
    }
    
    @Override
    public void cancel() {
        aisReader.get().stopReader();
        try {
            aisReader.get().join(THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void setAisReader(AisReader aisReader) {
        if (this.aisReader.get() != null) {
            throw new IllegalStateException("AisReader already defined");
        }
        this.aisReader.set(aisReader);
        // Register self as handler of packets
        this.aisReader.get().registerPacketHandler(this);
    }

}
