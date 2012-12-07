/* Copyright (c) 2012 Danish Maritime Authority
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
package dk.dma.ais.reader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.proprietary.ProprietaryFactory;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Sentence;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

/**
 * Class to parse lines in a stream containing VDM sentences. 
 * The class will deliver packets containing complete VDM and associated
 * comment blocks and proprietary tags.
 */
public class AisPacketReader {
	
	/**
	 * The name of the source
	 */
	private String sourceName;
	
	/**
	 * A received VDO/VDM
	 */
	private Vdm vdm = new Vdm();

	/**
	 * List of the raw lines of the AIS packet
	 */
	private List<String> packetLines = new ArrayList<>();

	/**
	 * Possible proprietary tags for current VDM
	 */
	private Deque<IProprietaryTag> tags = new ArrayDeque<>();

	/**
	 * Constructor
	 */
	public AisPacketReader() {

	}

	/**
	 * Handle a single line. If a complete packet is assembled the package will be returned. Otherwise null is returned.
	 * 
	 * @param line
	 * @return
	 * @throws SentenceException 
	 * @throws SixbitException 
	 */
	public AisPacket readLine(String line) throws SentenceException {

		// Ignore everything else than sentences
		if (!Sentence.hasSentence(line)) {
			// Special case is a single comment without sentence
			if (CommentBlock.hasCommentBlock(line)) {
				packetLines.add(line);
				try { 
					vdm.addSingleCommentBlock(line);
				} catch (SentenceException e) {
					newVdm();
					throw e;
				}
				return null;
			} else {
				// Non sentence line
				newVdm();
				throw new SentenceException("Non sentence line in stream: " + line);				
			}
		}

		// Add line to raw packet
		packetLines.add(line);

		// Check if proprietary line
		if (ProprietaryFactory.isProprietaryTag(line)) {
			// Try to parse with one the registered factories in META-INF/services/dk.dma.ais.proprietary.ProprietaryFactory
			IProprietaryTag tag = ProprietaryFactory.parseTag(line);
			if (tag != null) {
				tags.add(tag);
			}
			return null;
		}

		// Check if VDM. If not the possible current VDM is broken.
		if (!Vdm.isVdm(line)) {
			newVdm();
			return null;
		}
		
		// Parse VDM
		int result;
		try {
			result = vdm.parse(line);
		} catch (SentenceException e) {
			newVdm();
			throw e;		
		}
		
		// If not complete package wait for more
		if (result != 0) {
			return null;
		}
		
		// Complete package have been read
    	
		// Put proprietary tags on vdm
    	if (tags.size() > 0) {
    		vdm.setTags(new LinkedList<>(tags));
    	}
    	
    	AisPacket packet = new AisPacket(vdm, StringUtils.join(packetLines, "\r\n"), System.currentTimeMillis(), sourceName);
		
    	newVdm();

		return packet;
	}

	public void newVdm() {
		vdm = new Vdm();
		tags.clear();
		packetLines.clear();
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * Construct AisPacket from raw packet string
	 * @param messageString
	 * @param optional factory
	 * @return
	 * @throws SentenceException
	 */
	public static AisPacket from(String messageString) throws SentenceException {
		AisPacket packet = null;
		AisPacketReader packetReader = new AisPacketReader();
		String[] lines = StringUtils.split(messageString, "\n");		
		for (String line : lines) {
			packet = packetReader.readLine(line);
			if (packet != null) {
				return packet;
			}
		}
		return null;
	}
	
	

}
