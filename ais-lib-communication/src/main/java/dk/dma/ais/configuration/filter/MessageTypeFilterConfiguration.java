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
package dk.dma.ais.configuration.filter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.MessageTypeFilter;

@XmlRootElement
public class MessageTypeFilterConfiguration extends FilterConfiguration {

    private List<Integer> messageTypes = new ArrayList<>();
    private boolean disallowed;

    public MessageTypeFilterConfiguration() {

    }

    @XmlElement(name = "message_type")
    public List<Integer> getMessageTypes() {
        return messageTypes;
    }

    public void setMessageTypes(List<Integer> messageTypes) {
        this.messageTypes = messageTypes;
    }

    public boolean isDisallowed() {
        return disallowed;
    }

    public void setDisallowed(boolean disallowed) {
        this.disallowed = disallowed;
    }

    @Override
    public IPacketFilter getInstance() {
        MessageTypeFilter filter = new MessageTypeFilter();
        filter.setDisallowed(disallowed);
        for (Integer type : messageTypes) {
            filter.getMessageTypes().add(type);            
        }
        return filter;
    }

}
