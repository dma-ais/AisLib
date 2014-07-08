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
package dk.dma.ais.transform;

import java.util.Date;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;

/**
 * Special kind of transformer that imposes a delay to replay an AIS stream given speedup, and the timing in the stream.
 */
@ThreadSafe
public class ReplayTransformer implements IAisPacketTransformer {

    @GuardedBy("this")
    private double speedup = 1;

    @GuardedBy("this")
    private Long epochStream;
    @GuardedBy("this")
    private Long epochReplay;

    public ReplayTransformer() {

    }

    public ReplayTransformer(double speedup) {
        this.speedup = speedup;
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        long diff;
        long sleepTime;
        synchronized (this) {
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
            long elapsedReplay = (long) ((now - epochReplay) * speedup);
            diff = elapsedStream - elapsedReplay;
            sleepTime = (long) (diff / speedup);
        }

        // Sleep if diff is becoming too large
        if (diff > 200) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {

            }
        }
        return packet;
    }

    /**
     * Method to reset the replay values. Can be used
     * if the stream of packets are being repeated.
     */
    public synchronized void reset() {
        epochReplay = null;
        epochStream = null;
    }

    /**
     * Fractional speedup to use
     * 
     * @param speedup
     */
    public synchronized void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

}
