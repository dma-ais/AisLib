/* Copyright (c) 2011 Danish Maritime Safety Administration
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
package dk.dma.enav.ais.proprietary;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.ais.binary.SixbitException;
import dk.dma.enav.ais.sentence.Sentence;
import dk.dma.enav.ais.sentence.SentenceException;

/**
 * Class representing a DMA source tag
 * 
 * <source_name> : Name of source system <timestamp> : Time when this sentence was generated
 * 
 * $PDMA,<source_name>,<timestamp>*hh
 * 
 */
public class DmaSourceTag extends Sentence implements IProprietaryTag {

    private static final Logger LOG = LoggerFactory.getLogger(DmaSourceTag.class);

    protected String sourceName;
    protected Date timestamp = new Date(0);
    private String sentence = null;

    public DmaSourceTag() {
        talker = "P";
        formatter = "DMA";
        delimiter = "$";
    }

    @Override
    public String getSentence() {
        if (sentence == null) {
            sentence = getEncoded();
        }
        return sentence;
    }

    @Override
    public int parse(String line) throws SentenceException, SixbitException {
        int start = line.indexOf("$PDMA,");
        if (start < 0) {
            throw new SentenceException("Error in DMA proprietary sentence: " + line);
        }
        line = line.substring(start);
        setSentence(line);

        // Remove checksum
        String dataLine = line.substring(0, line.indexOf("*"));

        String[] elems = StringUtils.splitByWholeSeparatorPreserveAllTokens(dataLine, ",");
        if (elems.length < 3) {
            LOG.error("Error in DMA proprietary message: wrong number of fields " + elems.length + " in line: " + line);
            throw new SentenceException("Error in DMA proprietary sentence: " + line);
        }
        setSourceName(elems[1]);
        try {
            setTimestamp(new Date(Long.parseLong(elems[2])));
        } catch (NumberFormatException e) {
            LOG.error("Error in DMA propritary tag. Wrong timestamp: " + elems[2]);
            throw new SentenceException("Error in DMA proprietary sentence: " + line);
        }

        return 0;
    }

    @Override
    public String getEncoded() {
        super.encode();
        encodedFields.add(sourceName);
        encodedFields.add(Long.toString(timestamp.getTime()));
        return finalEncode();
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DmaSourceTag [sourceName=");
        builder.append(sourceName);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append("]");
        return builder.toString();
    }

}
