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
