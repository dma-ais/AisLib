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
package dk.dma.ais.configuration.transform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import dk.dma.ais.transform.IAisPacketTransformer;
import dk.dma.ais.transform.PacketTransformerCollection;

public class PacketTransformerCollectionConfiguration extends
        TransformerConfiguration {

    //@XmlElement(name="transformerCollection")
    private List<TransformerConfiguration> collection = new ArrayList<>();
    
    public List<TransformerConfiguration> getCollection() {
        return collection;
    }

    public void setCollection(List<TransformerConfiguration> collection) {
        this.collection = collection;
    }

    @Override
    @XmlTransient
    public IAisPacketTransformer getInstance() {
        PacketTransformerCollection packetTransformerCollection = new PacketTransformerCollection();
        
        for (TransformerConfiguration tc: collection) {
            packetTransformerCollection.addTransformer(tc.getInstance());
        }
        
        return packetTransformerCollection;
    }
    
    public void addTransformerConfiguration(TransformerConfiguration transformerConfiguration) {
        this.collection.add(transformerConfiguration);
    }

}
