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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Class representing a single comment block line The parsing is somewhat relaxed
 */
public class CommentBlockLine {

    private Map<String, String> parameterMap;
    private Integer totalLines;
    private Integer lineNumber;
    private String groupId;
    private int checksum;

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
                String[] tagGroupParts = StringUtils.split(value, '-');
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
            if (groupId != null && groupId.length() == 0) {
                throw new CommentBlockException("Invalid group id in TAG block: " + value);
            }
            parameterMap.put(parameterCode, value);
        }
    }

    public Integer getTotalLines() {
        return totalLines;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

}
