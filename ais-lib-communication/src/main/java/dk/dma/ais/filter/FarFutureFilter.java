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

package dk.dma.ais.filter;

import java.util.Date;
import dk.dma.ais.packet.AisPacket;

/**
 * @author Jens Tuxen
 *
 * Reject packets with timestamp far into the future according to device clock
 * 
 * @deprecated please use FutureFilter with argument new FutureFilter(86400000)
 */
@Deprecated
public class FarFutureFilter implements IPacketFilter {
    
    /** One day in milliseconds is 24*60*60*1000 */
    private static final long ONE_DAY = 86400000;
    @Override
    public final boolean rejectedByFilter(AisPacket packet) {
        return packet.getBestTimestamp() > (new Date().getTime()+ONE_DAY);
    }

}
