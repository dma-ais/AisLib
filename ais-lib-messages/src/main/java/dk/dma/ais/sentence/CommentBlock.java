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

import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to hold a comment block
 */
@NotThreadSafe
public class CommentBlock {

    private Map<String, String> parameterMap = new HashMap<>();

    private int totalLines = -1;
    private int lastLine = -1;
    private String lastGroupId;

    /**
     * Add line containing comment block
     * 
     * @param line
     */
    public void addLine(String line) throws CommentBlockException {
        // Make line object and parse
        CommentBlockLine cbLine = new CommentBlockLine();
        cbLine.parse(line);

        if (cbLine.getGroupId() != null) {
            // Line part of group
            if (lastGroupId == null) {
                // New group
                lastGroupId = cbLine.getGroupId();
                totalLines = cbLine.getTotalLines();
                lastLine = cbLine.getLineNumber();
            } else {
                // Part of existing group
                if (cbLine.getTotalLines() != totalLines || cbLine.getLineNumber() != lastLine + 1
                        || !cbLine.getGroupId().equals(lastGroupId)) {
                    throw new CommentBlockException("Invalid comment block grouping");
                }
                lastLine = cbLine.getLineNumber();
            }
            // Maybe finish group
            if (totalLines == lastLine) {
                totalLines = -1;
                lastLine = -1;
                lastGroupId = null;
            }
        } else {
            // Single line
            if (lastGroupId != null) {
                throw new CommentBlockException("Single line comment block in unfinished group");
            }
        }

        // Merge values
        parameterMap.putAll(cbLine.getParameterMap());
    }

    /**
     * Get number of entries in comment block
     * @return
     */
    public int getSize() {
        return parameterMap.size();
    }
    
    /**
     * Get string value for parameter code
     * 
     * @param parameter
     * @return
     */
    public String getString(String parameter) {
        return parameterMap.get(parameter);
    }

    /**
     * Add key value pair with string value
     * 
     * @param parameter
     * @param value
     */
    public void addString(String parameter, String value) {
        parameterMap.put(parameter, value);
    }

    /**
     * Add integer value
     * 
     * @param parameter
     * @param value
     */
    public void addInt(String parameter, int value) {
        parameterMap.put(parameter, Integer.toString(value));
    }

    /**
     * Add timestamp
     * 
     * @param timestamp
     */
    public void addTimestamp(Date timestamp) {
        parameterMap.put("c", Long.toString(timestamp.getTime() / 1000));
    }

    /**
     * Get integer value of parameter code
     * 
     * @param parameter
     * @return
     */
    public Integer getInt(String parameter) {
        String val = parameterMap.get(parameter);
        try {
            return val == null ? null : Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get long value of parameter code
     * 
     * @param parameter
     * @return
     */
    public Long getLong(String parameter) {
        String val = parameterMap.get(parameter);
        try {
            return val == null ? null : Long.parseLong(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Get timestamp in tag
     * 
     * @return
     */
    public Long getTimestamp() {
        return getLong("c");
    }

    /**
     * Determine if parameter exists in comment block
     * 
     * @param parameter
     * @return
     */
    public boolean contains(String parameter) {
        return parameterMap.containsKey(parameter);
    }

    /**
     * Determine if a line contains comment block
     * 
     * @param line
     * @return
     */
    public static boolean hasCommentBlock(String line) {
        return line.length() > 0 && line.charAt(0) == '\\';
    }

    /**
     * Determine if comment block is completed. That is no unfinished groups.
     * 
     * @return
     */
    public boolean isFinished() {
        return lastGroupId == null;
    }

    /**
     * Get id of last group
     * 
     * @return
     */
    public String getLastGroupId() {
        return lastGroupId;
    }

    /**
     * Get total number of lines in last group
     * 
     * @return
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * Determine if comment block contains any key value pairs
     * 
     * @return
     */
    public boolean isEmpty() {
        return parameterMap.size() == 0;
    }

    /**
     * Encode comment block in 80 character lines
     * 
     * @return
     */
    public String encode() {
        return encode(80);
    }

    /**
     * Encode comment block in a number of lines
     * 
     * @param maxLen
     *            Maximum line length
     * @return
     */
    public String encode(int maxLen) {
        // Get all pairs
        List<String> pairs = new ArrayList<>();
        for (Entry<String, String> pair : parameterMap.entrySet()) {
            // Skip grouping tags
            if (!pair.getKey().equals("g") && !pair.getKey().matches("\\d+G\\d+")) {
                pairs.add(pair.getKey() + ":" + pair.getValue());
            }
        }

        // List of lines
        List<List<String>> lines = new ArrayList<>();

        // Max len when accommodating for wrapping
        int actualMaxLen = maxLen - 5;

        List<String> currentLine = new ArrayList<>();
        lines.add(currentLine);
        int currentRemaining = actualMaxLen;

        // Less than optimal way to separate into lines
        for (String pair : pairs) {
            if (pair.length() > actualMaxLen) {
                throw new IllegalArgumentException("maxLen to small to accomodate pair: " + pair);
            }
            if (pair.length() > currentRemaining) {
                currentLine = new ArrayList<>();
                lines.add(currentLine);
                currentLine.add(pair);
                currentRemaining = actualMaxLen - pair.length();
            } else {
                currentLine.add(pair);
                currentRemaining -= pair.length();
            }
        }

        return wrapLines(lines);
    }

    private String wrapLines(List<List<String>> lines) {
        List<String> strLines = new ArrayList<>();
        for (List<String> line : lines) {
            String lineStr = StringUtils.join(line, ",");
            int checksum = 0;
            for (int i = 0; i < lineStr.length(); i++) {
                checksum ^= lineStr.charAt(i);
            }
            strLines.add("\\" + lineStr + "*" + Sentence.getStringChecksum(checksum) + "\\");
        }
        return StringUtils.join(strLines, "\r\n");
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[CommentBlock:");
        for (String parameterCode : parameterMap.keySet()) {
            buf.append(" " + parameterCode + ":" + parameterMap.get(parameterCode));
        }
        buf.append("]");
        return buf.toString();
    }

}
