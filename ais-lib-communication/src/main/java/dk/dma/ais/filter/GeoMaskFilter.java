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

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketFilters;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.util.function.Predicate;

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

        Predicate oredPredicates = null;

        for (BoundingBox bbox : suppressedBoundingBoxes) {
            if (oredPredicates == null) {
                oredPredicates = AisPacketFilters.filterOnMessagePositionWithin(bbox);
            } else {
                oredPredicates = oredPredicates.or(AisPacketFilters.filterOnMessagePositionWithin(bbox));
            }
        }

        this.blocked = oredPredicates;
    }

    @SuppressWarnings("Unused")
    public List<BoundingBox> getSuppressedBoundingBoxes() {
        return suppressedBoundingBoxes;
    }

    @Override
    public boolean rejectedByFilter(AisPacket aisPacket) {
        return blocked.test(aisPacket);
    }
}
