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
import java.util.List;

import dk.dma.ais.proprietary.ProprietaryFactory;

/**
 * Class representing a single sentence line
 */
public class SentenceLine {

    private String line;
    private String talker;
    private String formatter;
    private Character delimiter;
    private String prefix;
    private String sentence;
    private ArrayList<String> fields = new ArrayList<>();
    private int checksum;
    private int checksumField = -1;

    private StringBuilder tmpStr = new StringBuilder(60);
    private StringBuilder sentenceStr = new StringBuilder(80);

    public SentenceLine() {}

    public SentenceLine(String line) {
        parse(line);
    }

    public void clear() {
        line = null;
        talker = null;
        formatter = null;
        delimiter = null;
        prefix = null;
        sentence = null;
        fields.clear();
        checksum = 0;
        checksumField = -1;
        tmpStr.setLength(0);
        sentenceStr.setLength(0);
    }

    public void parse(String line) {
        clear();
        this.line = line;
        int len = line.length();
        int checksumStart = 0;
        int ptr = 0;

        // Find len without CR LF
        char[] cha = line.toCharArray();
        while (len > 0 && (cha[len - 1] == '\r' || cha[len - 1] == '\n')) {
            len--;
        }

        // Find prefix and start of sentence
        while (ptr < len) {
            char ch = cha[ptr];
            if (ch == '!' || ch == '$') {
                delimiter = ch;
                checksumStart = ptr;
                break;
            }
            tmpStr.append(ch);
            ptr++;
        }
        this.prefix = tmpStr.toString();
        tmpStr.setLength(0);

        if (this.delimiter == null) {
            return;
        }

        // Parse into fields
        while (ptr < len) {
            char ch = cha[ptr];
            sentenceStr.append(ch);
            if (ch == '*') {
                fields.add(tmpStr.toString());
                tmpStr.setLength(0);
                this.checksumField = fields.size();
            } else if (ch == ',') {
                fields.add(tmpStr.toString());
                tmpStr.setLength(0);
            } else {
                tmpStr.append(ch);
            }
            if (ptr > checksumStart && this.checksumField < 0) {
                checksum ^= ch;
            }
            ptr++;
        }
        fields.add(tmpStr.toString());
        sentence = sentenceStr.toString();

        // Parse talker and formatter
        if (fields.get(0).length() == 6) {
            tmpStr.setLength(0);
            talker = fields.get(0).substring(1, 3);
            formatter = fields.get(0).substring(3, 6);
        }
    }

    public void parse2(String line) {
        clear();
        this.line = line;
        int len = line.length();
        int checksumStart = 0;
        int ptr = 0;

        // Find len without CR LF
        while (len > 0 && (line.charAt(len - 1) == '\r' || line.charAt(len - 1) == '\n')) {
            len--;
        }

        // Find prefix and start of sentence
        while (ptr < len) {
            char ch = line.charAt(ptr);
            if (ch == '!' || ch == '$') {
                delimiter = ch;
                checksumStart = ptr;
                break;
            }
            tmpStr.append(ch);
            ptr++;
        }
        this.prefix = tmpStr.toString();
        tmpStr.setLength(0);

        if (this.delimiter == null) {
            return;
        }

        // Parse into fields
        while (ptr < len) {
            char ch = line.charAt(ptr);
            sentenceStr.append(ch);
            if (ch == '*') {
                fields.add(tmpStr.toString());
                tmpStr.setLength(0);
                this.checksumField = fields.size();
            } else if (ch == ',') {
                fields.add(tmpStr.toString());
                tmpStr.setLength(0);
            } else {
                tmpStr.append(ch);
            }
            if (ptr > checksumStart && this.checksumField < 0) {
                checksum ^= ch;
            }
            ptr++;
        }
        fields.add(tmpStr.toString());
        sentence = sentenceStr.toString();

        // Parse talker and formatter
        if (fields.get(0).length() == 6) {
            tmpStr.setLength(0);
            talker = fields.get(0).substring(1, 3);
            formatter = fields.get(0).substring(3, 6);
        }
    }

    public boolean isFormatter(String... formatters) {
        if (formatter == null) {
            return false;
        }
        for (String f : formatters) {
            if (formatter.equals(f)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSentence() {
        return sentence != null;
    }

    public int getPostfixStart() {
        if (checksumField < 0) {
            return -1;
        }
        return checksumField + 1;
    }

    public boolean isChecksumMatch() {
        String strChecksum = getChecksumField();
        if (strChecksum == null) {
            return false;
        }
        try {
            if (Integer.parseInt(strChecksum, 16) != this.checksum) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String getSentenceHead() {
        return delimiter == null || fields.size() == 0 ? null : fields.get(0);
    }

    public boolean isProprietary() {
        return ProprietaryFactory.isProprietaryTag(getSentenceHead());
    }

    public int getChecksum() {
        return checksum;
    }

    public String getChecksumString() {
        String strChecksum = Integer.toString(checksum, 16).toUpperCase();
        if (strChecksum.length() < 2) {
            strChecksum = "0" + strChecksum;
        }
        return strChecksum;
    }

    public String getChecksumField() {
        return this.checksumField >= 0 ? fields.get(this.checksumField) : null;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public Character getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<String> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SentenceLine [line=");
        builder.append(line);
        builder.append(", talker=");
        builder.append(talker);
        builder.append(", formatter=");
        builder.append(formatter);
        builder.append(", delimiter=");
        builder.append(delimiter);
        builder.append(", prefix=");
        builder.append(prefix);
        builder.append(", sentence=");
        builder.append(sentence);
        builder.append(", fields=");
        builder.append(fields);
        builder.append("]");
        return builder.toString();
    }

}
