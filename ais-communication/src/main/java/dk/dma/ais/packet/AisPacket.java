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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.google.common.hash.Hashing;

import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.Vdm;

/**
 * Encapsulation of the VDM lines containing a single AIS message
 * including leading proprietary tags and comment/tag blocks.
 * 
 * List of lines rather than string?
 * Parsed proprietary tags?
 * Parsed comment blocks?
 * Parsed VDM object?
 * Parsed AIS message?
 * 
 * Or parse later?
 * 
 * @author Kasper Nielsen
 */
public class AisPacket {
    private final Vdm vdm;
    private final List<IProprietaryTag> proprietaryTags; 
    private final long insertTimestamp;
    private final String stringMessage;
    private final String sourceName;

    public AisPacket(Vdm vdm, List<IProprietaryTag> proprietaryTags, String stringMessage, long receiveTimestamp, String sourceName) {
    	this.vdm = vdm;
    	this.proprietaryTags = proprietaryTags;
        this.stringMessage = requireNonNull(stringMessage);
        this.insertTimestamp = receiveTimestamp;
        this.sourceName = sourceName;        
    }

    /**
     * Calculates a 128 bit hash on the received package.
     * 
     * @return a 128 hash on the received package
     */
    public byte[] calculateHash128() {
        return Hashing.murmur3_128().hashString(stringMessage + ((sourceName != null) ? sourceName : "")).asBytes();
    }

    public long getReceiveTimestamp() {
        return insertTimestamp;
    }

    public String getStringMessage() {
        return stringMessage;
    }
    
    public Vdm getVdm() {
		return vdm;
	}
    
    public List<IProprietaryTag> getProprietaryTags() {
		return proprietaryTags;
	}
    
    public static AisPacket from(Vdm vdm, List<IProprietaryTag> proprietaryTags, String stringMessage, long receiveTimestamp, String sourceName) {
        return new AisPacket(vdm, proprietaryTags, stringMessage, receiveTimestamp, sourceName);
    }
    
}
