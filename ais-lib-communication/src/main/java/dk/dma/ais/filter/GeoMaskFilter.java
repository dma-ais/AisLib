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

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketFilters;
import dk.dma.enav.model.geometry.BoundingBox;
import java.util.function.Predicate;

import java.util.List;

/**
 * The GeoMaskFilter is instantiated with a number of BoundingBoxes. It is a stateless filter, which ensures,
 * that if an AisPacket passed to the filter is positively known to origin from inside one of these bounding
 * boxes, then it is rejected.
 *
 * @author Thomas Borg Salling <tbsalling@tbsalling.dk>
 */
public class GeoMaskFilter implements IPacketFilter {

    final Predicate<AisPacket> blocked;
    final List<BoundingBox> suppressedBoundingBoxes;

    public GeoMaskFilter(List<BoundingBox> suppressedBoundingBoxes) {
        this.suppressedBoundingBoxes = suppressedBoundingBoxes;

        Predicate<AisPacket> oredPredicates = null;

        for (BoundingBox bbox : suppressedBoundingBoxes) {
            if (oredPredicates == null) {
                oredPredicates = AisPacketFilters.filterOnMessagePositionWithin(bbox);
            } else {
                oredPredicates = oredPredicates.or(AisPacketFilters.filterOnMessagePositionWithin(bbox));
            }
        }

        this.blocked = oredPredicates;
    }

    public List<BoundingBox> getSuppressedBoundingBoxes() {
        return suppressedBoundingBoxes;
    }

    @Override
    public boolean rejectedByFilter(AisPacket aisPacket) {
        return blocked.test(aisPacket);
    }
}
