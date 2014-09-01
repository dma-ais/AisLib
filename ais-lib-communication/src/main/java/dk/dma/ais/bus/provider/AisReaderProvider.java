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
package dk.dma.ais.bus.provider;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import java.util.function.Consumer;

/**
 * Provider class that uses an AisReader to provide AIS packets
 */
@ThreadSafe
public class AisReaderProvider extends AisBusProvider implements Consumer<AisPacket> {
    
    /**
     * The AIS reader to provide packets
     */
    private AtomicReference<AisReader> aisReader = new AtomicReference<>();
    
    public AisReaderProvider(boolean blocking) {
        super(blocking);
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
