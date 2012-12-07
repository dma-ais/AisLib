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
package dk.dma.ais.sentence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;

/**
 * Abstract base class for representing IEC sentences
 */
public abstract class Sentence {

    protected String talker;
    protected String formatter;
    protected String delimiter;
    protected int checksum;
    protected String msgChecksum;
    protected String[] fields;
    protected String msg;
    protected String prefix;
    protected List<String> orgLines = new ArrayList<>();
    protected LinkedList<String> encodedFields;
    protected CommentBlock commentBlock;
    protected LinkedList<IProprietaryTag> tags = null; // Possible proprietary source tags for the message

    public Sentence() {
        talker = "AI";
        delimiter = "!";
    }

    /**
     * Abstract method that all sentence classes must implement
     * 
     * The method handles assembly and extraction of the 6-bit data from sentences. The sentence is expected to be in
     * order.
     * 
     * It will return an error if received line is out of order or from a new sequence before the previous one is
     * finished.
     * 
     * @param line
     * @return 0 Complete packet - 1 Incomplete packet
     */
    public abstract int parse(String line) throws SentenceException, SixbitException;

    /**
     * Get an encoded sentence
     * 
     * @return
     */
    public abstract String getEncoded();

    /**
     * Basic parse of line into sentence parts
     * 
     * @param line
     * @throws SentenceException
     */
    protected void baseParse(String line) throws SentenceException {
        this.orgLines.add(line);

        // Split into prefix and sentence
        splitLine(line);
        
        // Check for comment block
        if (prefix.length() > 0 && CommentBlock.hasCommentBlock(prefix)) {
        	addCommentBlock(prefix);
        }
        
        // Calculate checksum
        calculateChecksum();
        
        // Check checksum
        checkChecksum();

        // Split into fields
        fields = msg.split(",|\\*");
        if (fields.length < 2) {
            throw new SentenceException("Invalid sentence, less than two fields");
        }

        // Get talker/formatter
        if (fields[0].length() != 6) {
            throw new SentenceException("Invalid sentence, wrong talker/formatter: " + fields[0]);
        }
        talker = fields[0].substring(1, 3);
        formatter = fields[0].substring(3, 6);
    }
    
	public void addSingleCommentBlock(String line) throws SentenceException {
		this.orgLines.add(line);
		addCommentBlock(line);		
	}
    
	private void addCommentBlock(String line) throws SentenceException {
		if (commentBlock == null) {
			commentBlock = new CommentBlock();
		}
		try {
			commentBlock.addLine(line);
		} catch (CommentBlockException e) {
			throw new SentenceException("CommentBlockException: " + e.getMessage());
		}
	}

    /**
     * The top most encode method
     */
    protected void encode() {
        encodedFields = new LinkedList<>();
        encodedFields.add(delimiter + talker + formatter);
    }

    /**
     * Method to finalize the encoding and return sentence
     * 
     * @return final sentence
     */
    protected String finalEncode() {
        // Join fields
        String encoded = StringUtils.join(encodedFields.iterator(), ',');

        this.msg = encoded;
        try {
            calculateChecksum();
        } catch (SentenceException e) {
            e.printStackTrace();
        }
        msgChecksum = Integer.toString(checksum, 16).toUpperCase();
        if (msgChecksum.length() < 2) {
            msgChecksum = "0" + msgChecksum;
        }
        encoded += "*" + msgChecksum;

        return encoded;
    }

    /**
     * Split line into prefix and message part. Message will start at first ! or $ character
     * 
     * @param line
     * @throws SentenceException
     */
    private void splitLine(String line) throws SentenceException {
        char[] lineArray = line.toCharArray();
        int i = 0;
        for (char x : lineArray) {
            if (x == '!' || x == '$') {
                this.prefix = line.substring(0, i);
                this.msg = line.substring(i);
                return;
            }
            i++;
        }
        throw new SentenceException("NMEA Start Not Found");
    }

    /**
     * Calculate checksum of this sentence
     * 
     * @throws SentenceException
     */
    private void calculateChecksum() throws SentenceException {
        this.checksum = getChecksum(msg);
    }

    /**
     * Calculate checksum of sentence
     * 
     * @param sentence
     * @return
     * @throws SentenceException
     */
    public static int getChecksum(String sentence) throws SentenceException {
        int checksum = 0;
        for (int i = 1; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (c == '!' || c == '$') {
                throw new SentenceException("Start Character Found before Checksum");
            }
            if (c == '*') {
                break;
            }
            checksum ^= c;
        }
        return checksum;
    }

    /**
     * Check calculated checksum with checksum indicated in sentence line
     * 
     * @throws SentenceException
     */
    private void checkChecksum() throws SentenceException {
        int ptr = this.msg.indexOf('*');
        if (ptr < 0) {
            throw new SentenceException("Invalid sentence, no checksum");
        }
        try {
            this.msgChecksum = this.msg.substring(ptr + 1, ptr + 3);
        } catch (IndexOutOfBoundsException e) {
            throw new SentenceException("Invalid sentence, invalid checksum not two bytes");
        }
        try {
            if (Integer.parseInt(this.msgChecksum, 16) != this.checksum) {
                throw new SentenceException("Checksum failed, should have been: " + Integer.toString(this.checksum, 16));
            }
        } catch (NumberFormatException e) {
            throw new SentenceException("Invalid message checksum: " + this.msgChecksum);
        }
    }

    public static int parseInt(String str) throws SentenceException {
        if (str == null || str.length() == 0) {
            throw new SentenceException("Invalid integer field: " + str);
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new SentenceException("Invalid integer field: " + str);
        }
    }

    /**
     * Determine if a line seems to contain a sentence There should be a ! or $
     * 
     * @param line
     * @return
     */
    public static boolean hasSentence(String line) {
        return line.indexOf("!") >= 0 || line.indexOf("$") >= 0;
    }

    /**
     * Determine if a line seems to contain a proprietary sentence
     * 
     * @param line
     * @return
     */
    public static boolean hasProprietarySentence(String line) {
        return line.indexOf("$P") >= 0;
    }

    /**
     * Get original lines for this sentence
     * 
     * @return
     */
    public List<String> getOrgLines() {
        return orgLines;
    }

    /**
     * Get original lines joined by carrige return line feed
     * 
     * @return
     */
    public String getOrgLinesJoined() {
        return StringUtils.join(orgLines.iterator(), "\r\n");
    }

    /**
     * Set talker
     * 
     * @param talker
     */
    public void setTalker(String talker) {
        this.talker = talker;
    }

    /**
     * Set delimiter
     * 
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Set formatter
     * 
     * @param formatter
     */
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }
    
    /**
     * Get possible comment block
     * @return
     */
    public CommentBlock getCommentBlock() {
		return commentBlock;
	}
    
    /**
     * Get all tags
     * 
     * @return
     */
    public LinkedList<IProprietaryTag> getTags() {
        return tags;
    }
    
    public void setTags(LinkedList<IProprietaryTag> tags) {
        this.tags = tags;
    }
    
    /**
     * Return the LAST source tag (closest to AIS sentence)
     * 
     * @return
     */
    public IProprietarySourceTag getSourceTag() {
        if (tags == null) {
            return null;
        }
        // Iterate backwards
        for (Iterator<IProprietaryTag> iterator = tags.descendingIterator(); iterator.hasNext();) {
            IProprietaryTag tag = iterator.next();
            if (tag instanceof IProprietarySourceTag) {
                return (IProprietarySourceTag) tag;
            }
        }
        return null;
    }

    /**
     * Add tag (to front)
     * 
     * @param sourceTag
     */
    public void setTag(IProprietaryTag tag) {
        if (this.tags == null) {
            this.tags = new LinkedList<>();
        }
        this.tags.addFirst(tag);
    }

    /**
     * Convert any sentence to new sentence with !<talker><formatter>,....,
     * 
     * @param sentence
     * @param talker
     * @param formatter
     * @return
     * @throws SentenceException
     */
    public static String convert(String sentence, String talker, String formatter) throws SentenceException {
        String newSentence = sentence.trim();
        newSentence = newSentence.substring(6, newSentence.length() - 3);
        newSentence = "!" + talker + formatter + newSentence;
        String checksum = Integer.toString(getChecksum(newSentence), 16).toUpperCase();
        if (checksum.length() < 2) {
            checksum = "0" + checksum;
        }
        newSentence += "*" + checksum;
        return newSentence;
    }

}
