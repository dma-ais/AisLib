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

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;

/**
 * Filter based on a given tagging. Non null fields in the tagging much
 * be matched by the packet tagging.
 */
@ThreadSafe
public class TaggingFilter implements IPacketFilter {

    private final AisPacketTags filterTagging;

    public TaggingFilter(AisPacketTags filterTagging) {
        this.filterTagging = filterTagging;
    }

    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        return !filterTagging.filterMatch(packet.getTags());
    }

}
