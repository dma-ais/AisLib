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

import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold a comment block
 */
public class CommentBlock {

    private Map<String, String> parameterMap = new HashMap<>();

    private int totalLines = -1;
    private int lastLine = -1;
    private String lastGroupId;

    /**
     * Add line containing comment block
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
                if (cbLine.getTotalLines() != totalLines || cbLine.getLineNumber() != lastLine + 1 || !cbLine.getGroupId().equals(lastGroupId)) {
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
     * Get string value for parameter code
     * @param parameter
     * @return
     */
    public String getString(String parameter) {
        return parameterMap.get(parameter);
    }

    /**
     * Get integer value of parameter code
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
     * @return
     */
    public Long getTimestamp() {
        return getLong("c");
    }

    /**
     * Determine if a line contains comment block
     * @param line
     * @return
     */
    public static boolean hasCommentBlock(String line) {
        return line.length() > 0 && line.charAt(0) == '\\';
    }

    /**
     * Determine if comment block is completed. That is no unfinished groups.
     * @return
     */
    public boolean isFinished() {
        return lastGroupId == null;
    }

    /**
     * Get id of last group
     * @return
     */
    public String getLastGroupId() {
        return lastGroupId;
    }

    /**
     * Get total number of lines in last group
     * @return
     */
    public int getTotalLines() {
        return totalLines;
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
