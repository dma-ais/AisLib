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
package dk.dma.ais.transform;

import java.util.Date;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;

/**
 * Special kind of transformer that imposes a delay to replay an AIS stream given speedup, 
 * and the timing in the stream.   
 */
@ThreadSafe
public class ReplayTransformer implements IAisPacketTransformer {
    
    private volatile double speedup = 1;
    
    private Long epochStream;
    private Long epochReplay;
    
    public ReplayTransformer() {
        
    }
    
    public ReplayTransformer(double speedup) {
        this.speedup = speedup;
    }    

    @Override
    public AisPacket transform(AisPacket packet) {
        // Get timestamp from stream
        Date timestamp = packet.getTimestamp();
        if (timestamp == null) {
            return packet;                    
        }
        long now = System.currentTimeMillis();
        // Set epochs if not already set
        if (epochReplay == null) {
            epochReplay = now;
        }
        if (epochStream == null) {
            epochStream = timestamp.getTime();
        }
        
        // How long has elapsed in the stream and in replay
        long elapsedStream = timestamp.getTime() - epochStream;
        long elapsedReplay = (long)((double)(now - epochReplay) * speedup);
                
        long diff = elapsedStream - elapsedReplay;
        
        // Sleep if diff is becoming too large
        if (diff > 200) {
            try {
                long sleepTime = (long)((double)diff / speedup);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {                
                e.printStackTrace();
            }
        }
        return packet;
    }
    
    /**
     * Fractional speedup to use
     * @param speedup
     */
    public synchronized void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

}
