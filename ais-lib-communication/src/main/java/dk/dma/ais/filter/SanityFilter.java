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
package dk.dma.ais.filter;

import java.util.concurrent.TimeUnit;

import net.jcip.annotations.ThreadSafe;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.enav.model.Country;

/**
 * Filter performing simple sanity filter on received messages
 */
@ThreadSafe
public class SanityFilter extends MessageFilterBase {

    private static final int STATIC_CACHE_SIZE = 200000;

    private volatile long count;
    private volatile long rejectCount;

    /**
     * Cache of vessels for which we have received static information
     */
    private final Cache<Integer, Boolean> hasStaticMap;

    public SanityFilter() {
        hasStaticMap = CacheBuilder.newBuilder().maximumSize(STATIC_CACHE_SIZE).expireAfterWrite(24, TimeUnit.HOURS).build();
    }

    @Override
    public boolean rejectedByFilter(AisMessage message) {
        count++;
        boolean rejected = isNotSane(message);
        if (rejected) {
            rejectCount++;
        }

        // if (count % 5000 == 0) {
        // System.out.println("Rejected percentage: " + ((double) rejectCount / (double) count) * 100.0);
        // System.out.println("Static cache size: " + hasStaticMap.size());
        // }

        return rejected;
    }

    private boolean isNotSane(AisMessage message) {
        IVesselPositionMessage vesselPosMessage = null;
        if (message instanceof IVesselPositionMessage) {
            vesselPosMessage = (IVesselPositionMessage) message;
        }
        AisStaticCommon vesselStaticCommon = null;
        if (message instanceof AisStaticCommon) {
            vesselStaticCommon = (AisStaticCommon) message;
        }
        boolean isVessel = vesselPosMessage != null || vesselStaticCommon != null;

        // Update statics cache
        if (vesselStaticCommon != null) {
            hasStaticMap.put(message.getUserId(), true);
        }

        // Determine if has static
        boolean noStatic = isVessel && hasStaticMap.getIfPresent(message.getUserId()) == null;

        // Check if vessel MMSI number is valid
        if (noStatic && isVessel && (message.getUserId() < 100000000 || message.getUserId() > 999999999)) {
            // System.out.println("Invalid vessel MMSI number");
            return true;
        }

        // Reject unknown country for vessel targets
        if (noStatic && isVessel && Country.getCountryForMmsi(message.getUserId()) == null) {
            // System.out.println("Unknown vessel target country: " + message.getUserId());
            return true;
        }

        // Sanity check on speed (not sane if above 30 knots and no static report)
        if (vesselPosMessage != null) {
            if (noStatic && (vesselPosMessage.getSog() > 300 && vesselPosMessage.getSog() < 1023)) {
                // System.out.println("High speed for vessel with no static report: " + (vesselPosMessage.getSog() / 10) + " mmsi: "
                // + message.getUserId());
                return true;
            }
        }

        return false;
    }

    public long getCount() {
        return count;
    }

    public long getRejectCount() {
        return rejectCount;
    }

}
