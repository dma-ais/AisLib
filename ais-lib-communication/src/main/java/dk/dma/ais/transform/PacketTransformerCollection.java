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

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.packet.AisPacket;

public class PacketTransformerCollection implements IAisPacketTransformer {

    private List<IAisPacketTransformer> collection = new ArrayList<>();
    
    public List<IAisPacketTransformer> getCollection() {
        return collection;
    }

    public void setCollection(List<IAisPacketTransformer> collection) {
        this.collection = collection;
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        AisPacket p = packet;
        for (IAisPacketTransformer packetTransformer: getCollection()) {
            p = packetTransformer.transform(p);
        }
        return p;
    }
    
    public void addTransformer(IAisPacketTransformer packetTransformer) {
        this.collection.add(packetTransformer);
    }

}
