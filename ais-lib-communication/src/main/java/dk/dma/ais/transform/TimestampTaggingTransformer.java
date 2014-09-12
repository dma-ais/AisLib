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

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;

public class TimestampTaggingTransformer implements IAisPacketTransformer {
    @Override
    public AisPacket transform(AisPacket packet) {
        if (packet.getBestTimestamp() > -1) {
            return packet;
        }
        
        AisPacketTags tags = new AisPacketTags();
        tags.setTimestamp(new Date());
        AisPacketTaggingTransformer tagTransformer = new AisPacketTaggingTransformer(Policy.fromString("MERGE_PRESERVE"),tags);
        
        return tagTransformer.transform(packet);
    }
}
