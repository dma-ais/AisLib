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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a single comment block line The parsing is somewhat relaxed
 */
public class CommentBlockLine {

    private Map<String, String> parameterMap;
    private Integer totalLines;
    private Integer lineNumber;
    private String groupId;
    private int checksum;

    /**
     * Parse.
     *
     * @param line the line
     * @throws CommentBlockException the comment block exception
     */
    public void parse(String line) throws CommentBlockException {
        parameterMap = new HashMap<>();
        int start = -1;
        int end = -1;
        checksum = 0;
        // Find start, end, checksum and fields
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '*') {
                end = i;
                break;
            }
            if (start >= 0) {
                checksum ^= c;
            }
            if (c == '\\') {
                if (start < 0) {
                    start = i;
                }
            }
        }
        if (start < 0) {
            throw new CommentBlockException("No comment block found");
        }
        if (end < 0) {
            throw new CommentBlockException("Malformed comment block");
        }

        // Check checksum
        try {
            String given = line.substring(end + 1, end + 3);
            String calculated = Integer.toString(checksum, 16).toUpperCase();
            if (checksum != Integer.parseInt(given, 16)) {
                throw new CommentBlockException("Wrong checksum " + given + " calculated " + calculated);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CommentBlockException("Missing checksum in comment block");
        } catch (NumberFormatException e) {
            throw new CommentBlockException("Invalid checksum");
        }

        // Split into fields
        StringBuilder tmpStr = new StringBuilder(16);
        List<String> fields = new ArrayList<>();
        for (int i = start + 1; i < end; i++) {
            if (line.charAt(i) == ',' || line.charAt(i) == ':') {
                fields.add(tmpStr.toString());
                tmpStr.setLength(0);
            } else {
                tmpStr.append(line.charAt(i));
            }
        }
        if (start + 1 < end) {
            fields.add(tmpStr.toString());
        }

        if (fields.size() % 2 != 0) {
            throw new CommentBlockException("Malformed comment block");
        }

        for (int i = 0; i < fields.size(); i += 2) {
            String parameterCode = fields.get(i);
            String value = fields.get(i + 1);

            // Check for grouping parameter code
            int groupCharIndex;
            if ((groupCharIndex = parameterCode.indexOf('G')) >= 0) {
                try {
                    lineNumber = Integer.parseInt(parameterCode.substring(0, groupCharIndex));
                    totalLines = Integer.parseInt(parameterCode.substring(groupCharIndex + 1, parameterCode.length()));
                } catch (NumberFormatException e) {
                    throw new CommentBlockException("Invalid group tag: " + parameterCode);
                }
                groupId = value;
            }
            // Check for tag block group
            if (parameterCode.equals("g")) {
                String[] tagGroupParts = StringUtils.splitPreserveAllTokens(value, '-');
                if (tagGroupParts.length != 3) {
                    throw new CommentBlockException("Invalid TAG block g parameter: " + value);
                }
                try {
                    lineNumber = Integer.parseInt(tagGroupParts[0]);
                    totalLines = Integer.parseInt(tagGroupParts[1]);
                } catch (NumberFormatException e) {
                    throw new CommentBlockException("Invalid TAG block g parameter: " + value);
                }
                groupId = tagGroupParts[2];
            }
            parameterMap.put(parameterCode, value);
        }
    }

    /**
     * Gets total lines.
     *
     * @return the total lines
     */
    public Integer getTotalLines() {
        return totalLines;
    }

    /**
     * Gets line number.
     *
     * @return the line number
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Gets parameter map.
     *
     * @return the parameter map
     */
    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

}
