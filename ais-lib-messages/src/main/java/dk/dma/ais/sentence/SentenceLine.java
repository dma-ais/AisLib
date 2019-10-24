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
package dk.dma.ais.sentence;

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.proprietary.ProprietaryFactory;

/**
 * Class representing a single sentence line
 */
public class SentenceLine {

    /** Cache of all ASCII strings with length 1 */
    private static final String[] S1 = new String[256];

    /** Cache of all ASCII strings with length 2 */
    private static final String[] S2 = new String[256 * 256];

    private String line;
    private String talker;
    private String formatter;
    private Character delimiter;
    private String prefix;
    private String sentence;
    private ArrayList<String> fields = new ArrayList<>();
    private int checksum;
    private int checksumField = -1;

    static {
        for (int i = 0; i < 256; i++) {
            S1[i] = Character.toString((char) i);
        }
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                S2[i * 256 + j] = new String(new char[] { (char) i, (char) j });
            }
        }
    }

    /**
     * Instantiates a new Sentence line.
     */
    public SentenceLine() {}

    /**
     * Instantiates a new Sentence line.
     *
     * @param line the line
     */
    public SentenceLine(String line) {
        parse(line);
    }

    /**
     * Clear.
     */
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
    }

    /**
     * Parse.
     *
     * @param line the line
     */
    public void parse(String line) {
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
            ptr++;
        }
        this.prefix = convertString(line, 0, ptr);

        if (this.delimiter == null) {
            return;
        }

        int ptrStart = ptr;
        int ps = ptr;
        // Parse into fields
        while (ptr < len) {
            char ch = line.charAt(ptr);
            if (ch == '*') {
                fields.add(convertString(line, ps, ptr));
                this.checksumField = fields.size();
                ps = ptr + 1;
            } else if (ch == ',') {
                fields.add(convertString(line, ps, ptr));
                ps = ptr + 1;
            }
            if (ptr > checksumStart && this.checksumField < 0) {
                checksum ^= ch;
            }
            ptr++;
        }
        if (ps < len) {
            fields.add(convertString(line, ps, len));
        }
        
        // Make sure that a checksum field was actually added
        if (this.checksumField >= fields.size()) {
            this.checksumField = -1;
        }
        
        sentence = convertString(line, ptrStart, len);
        // Parse talker and formatter
        String f = fields.get(0);
        if (f.length() == 6) {
            talker = convertString(f, 1, 3);
            formatter = convertString(f, 3, 6);
        }
    }

    private static String convertString(String f, int start, int end) {
        switch (end - start) {
        case 0:
            return "";
        case 1:
            return S1[f.charAt(start)];
        case 2:
            return S2[(f.charAt(start) << 8) + f.charAt(start + 1)];
        default:
            return f.substring(start, end);
        }
    }

    /**
     * Is formatter boolean.
     *
     * @param formatters the formatters
     * @return the boolean
     */
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

    /**
     * Has sentence boolean.
     *
     * @return the boolean
     */
    public boolean hasSentence() {
        return sentence != null;
    }

    /**
     * Gets postfix start.
     *
     * @return the postfix start
     */
    public int getPostfixStart() {
        if (checksumField < 0) {
            return -1;
        }
        return checksumField + 1;
    }

    /**
     * Is checksum match boolean.
     *
     * @return the boolean
     */
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

    /**
     * Gets sentence head.
     *
     * @return the sentence head
     */
    public String getSentenceHead() {
        return delimiter == null || fields.size() == 0 ? null : fields.get(0);
    }

    /**
     * Is proprietary boolean.
     *
     * @return the boolean
     */
    public boolean isProprietary() {
        return ProprietaryFactory.isProprietaryTag(getSentenceHead());
    }

    /**
     * Gets checksum.
     *
     * @return the checksum
     */
    public int getChecksum() {
        return checksum;
    }

    /**
     * Gets checksum string.
     *
     * @return the checksum string
     */
    public String getChecksumString() {
        String strChecksum = Integer.toString(checksum, 16).toUpperCase();
        if (strChecksum.length() < 2) {
            strChecksum = "0" + strChecksum;
        }
        return strChecksum;
    }

    /**
     * Gets checksum field.
     *
     * @return the checksum field
     */
    public String getChecksumField() {
        return this.checksumField >= 0 ? fields.get(this.checksumField) : null;
    }

    /**
     * Gets line.
     *
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * Sets line.
     *
     * @param line the line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Gets talker.
     *
     * @return the talker
     */
    public String getTalker() {
        return talker;
    }

    /**
     * Sets talker.
     *
     * @param talker the talker
     */
    public void setTalker(String talker) {
        this.talker = talker;
    }

    /**
     * Gets formatter.
     *
     * @return the formatter
     */
    public String getFormatter() {
        return formatter;
    }

    /**
     * Sets formatter.
     *
     * @param formatter the formatter
     */
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    /**
     * Gets delimiter.
     *
     * @return the delimiter
     */
    public Character getDelimiter() {
        return delimiter;
    }

    /**
     * Sets delimiter.
     *
     * @param delimiter the delimiter
     */
    public void setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Gets prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets prefix.
     *
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gets sentence.
     *
     * @return the sentence
     */
    public String getSentence() {
        return sentence;
    }

    /**
     * Sets sentence.
     *
     * @param sentence the sentence
     */
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    /**
     * Gets fields.
     *
     * @return the fields
     */
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
