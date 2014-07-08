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
    }

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
