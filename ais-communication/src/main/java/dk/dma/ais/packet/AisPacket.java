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

import java.util.Date;

import com.google.common.hash.Hashing;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.reader.AisPacketReader;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.SentenceException;
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
	
    private final long insertTimestamp;
    private final String stringMessage;
    private final String sourceName;
    private Vdm vdm;

    public AisPacket(String stringMessage, long receiveTimestamp, String sourceName) {
    	this.stringMessage = requireNonNull(stringMessage);
        this.insertTimestamp = receiveTimestamp;
        this.sourceName = sourceName;    	
    }
    
    public AisPacket(Vdm vdm, String stringMessage, long receiveTimestamp, String sourceName) {
    	this(stringMessage, receiveTimestamp, sourceName);
    	this.vdm = vdm;
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
    
    /**
     * Get existing VDM or parse one from message string
     * @return Vdm
     */
    public Vdm getVdm() {
    	if (vdm == null) {
    		AisPacket packet;
			try {
				packet = AisPacketReader.from(stringMessage);
				if (packet != null) {
	        		vdm = packet.getVdm();
	        	}
			} catch (SentenceException e) {
				return null;
			}        	
    	}
    	return vdm;
	}
    
    /**
     * Try to get AIS message from packet
     * @return
     * @throws SixbitException 
     * @throws AisMessageException 
     */
    public AisMessage getAisMessage() throws AisMessageException, SixbitException {
    	if (getVdm() == null) return null;
    	return AisMessage.getInstance(getVdm());
    }
    
    /**
     * Try to get timestamp for packet. 
     * @return
     */
    public Date getTimestamp() {
    	if (getVdm() == null) return null;
    	// Try comment block first
		CommentBlock cb = vdm.getCommentBlock();
		if (cb != null) {
			Long ts = cb.getTimestamp();
			if (ts != null) {
				return new Date(ts * 1000);
			}
		}
		// Try from proprietary source tags
		if (vdm.getProprietaryTags() != null) {
			for (IProprietaryTag tag : vdm.getProprietaryTags()) {
				if (tag instanceof IProprietarySourceTag) {
					Date t = ((IProprietarySourceTag) tag).getTimestamp();
					if (t != null) {
						return t;
					}
				}
			}
		}
		return null;
    }
    
    public static AisPacket from(String stringMessage, long receiveTimestamp, String sourceName) {
        return new AisPacket(stringMessage, receiveTimestamp, sourceName);
    }
    
}
