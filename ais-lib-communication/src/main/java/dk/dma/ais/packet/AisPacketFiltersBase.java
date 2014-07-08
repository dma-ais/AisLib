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

package dk.dma.ais.packet;

import java.util.Arrays;

/**
 * This class provides shared behaviour for different types of AisPacketFilters.
 */
public abstract class AisPacketFiltersBase implements FilterPredicateFactory {

    @SafeVarargs
    static <T> T[] check(T... elements) {
        T[] s = elements.clone();
        Arrays.sort(s);
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                throw new NullPointerException("Array is null at position " + i);
            }
        }
        // Check for nulls
        return s;
    }

    static String skipBrackets(String s) {
        return s.length() < 2 ? "" : s.substring(1, s.length() - 1);
    }

    /**
     * Convert an AIS string to a Java string. I.e. convert '@' to space and remove leading and trailing spaces.
     * @param aisString the AIS string
     * @return the Java string
     */
    protected static final String preprocessAisString(String aisString) {
        return aisString != null ? aisString.replace('@', ' ').trim() : null;
    }

    /**
     * Remove an optional pair of apostrophes around the string.
     * @param string
     * @return
     */
    protected static final String preprocessExpressionString(String string) {
        String preprocessedString = string;
        if (preprocessedString.startsWith("'") && preprocessedString.endsWith("'") && preprocessedString.length() > 2) {
            preprocessedString = preprocessedString.substring(1, preprocessedString.length() - 1);
        }
        return preprocessedString;
    }

    /**
     * Remove an optional pair of apostrophes around each string in an array of strings.
     * @param strings
     * @return
     */
    protected static final String[] preprocessExpressionStrings(String[] strings) {
        String[] preprocessedStrings = new String[strings.length];
        for (int i = 0; i < preprocessedStrings.length; i++) {
            preprocessedStrings[i] = preprocessExpressionString(strings[i]);
        }
        return preprocessedStrings;
    }

    /**
     * Lexically compare the left-hand side value to the right-hand side value using the specified operator.
     * @param lhs the value of the left-hand side.
     * @param rhs the value of the right-hand side.
     * @param operator the binary operator to apply for the comparison.
     * @return true if the left-hand side compares true to the right-hand side. False otherwise.
     */
    protected static final boolean compare(String lhs, String rhs, CompareToOperator operator) {
        lhs = preprocessAisString(lhs);
        rhs = preprocessAisString(rhs);

        switch (operator) {
            case EQUALS:
                return lhs.equalsIgnoreCase(rhs);
            case NOT_EQUALS:
                return !lhs.equalsIgnoreCase(rhs);
            case GREATER_THAN:
                return lhs.compareToIgnoreCase(rhs) > 0;
            case GREATER_THAN_OR_EQUALS:
                return lhs.compareToIgnoreCase(rhs) >= 0;
            case LESS_THAN:
                return lhs.compareToIgnoreCase(rhs) < 0;
            case LESS_THAN_OR_EQUALS:
                return lhs.compareToIgnoreCase(rhs) <= 0;
            default:
                throw new IllegalArgumentException("CompareToOperator " + operator + " not implemented.");
        }
    }

    /**
     * Compare the left-hand side value to the right-hand side value using the specified operator.
     * @param lhs the value of the left-hand side.
     * @param rhs the value of the right-hand side.
     * @param operator the binary operator to apply for the comparison.
     * @return true if the left-hand side compares true to the right-hand side. False otherwise.
     */
    protected static final boolean compare(int lhs, int rhs, CompareToOperator operator) {
        switch (operator) {
            case EQUALS:
                return lhs == rhs;
            case NOT_EQUALS:
                return lhs != rhs;
            case GREATER_THAN:
                return lhs > rhs;
            case GREATER_THAN_OR_EQUALS:
                return lhs >= rhs;
            case LESS_THAN:
                return lhs < rhs;
            case LESS_THAN_OR_EQUALS:
                return lhs <= rhs;
            default:
                throw new IllegalArgumentException("CompareToOperator " + operator + " not implemented.");
        }
    }

    /**
     * Compare the left-hand side value to the right-hand side value using the specified operator.
     * @param lhs the value of the left-hand side.
     * @param rhs the value of the right-hand side.
     * @param operator the binary operator to apply for the comparison.
     * @return true if the left-hand side compares true to the right-hand side. False otherwise.
     */
    protected static final boolean compare(float lhs, float rhs, CompareToOperator operator) {
        switch (operator) {
            case EQUALS:
                return lhs == rhs;
            case NOT_EQUALS:
                return lhs != rhs;
            case GREATER_THAN:
                return lhs > rhs;
            case GREATER_THAN_OR_EQUALS:
                return lhs >= rhs;
            case LESS_THAN:
                return lhs < rhs;
            case LESS_THAN_OR_EQUALS:
                return lhs <= rhs;
            default:
                throw new IllegalArgumentException("CompareToOperator " + operator + " not implemented.");
        }
    }

    /**
     * Test if the integer value is in the closed range between min and max.
     * @param min
     * @param max
     * @param value
     * @return
     */
    protected static final boolean inRange(int min, int max, int value) {
        return value >= min && value <= max;
    }

    /**
     * Test if the floating point value is in the closed range between min and max.
     * @param min
     * @param max
     * @param value
     * @return
     */
    protected static final boolean inRange(float min, float max, float value) {
        return value >= min && value <= max;
    }

    /**
     * Test of the string representation of the supplied value matches the given glob expression.
     * @see <a href="https://en.wikipedia.org/wiki/Glob_(programming)">Wikipedia on Glob expressions.</a>
     * @param value
     * @param glob
     * @param <T>
     * @return true if the value matches the glob.
     */
    protected static final <T> boolean matchesGlob(T value, String glob) {
        return value.toString().matches(convertGlobToRegex(glob));
    }

    /**
     * Converts a standard POSIX Shell globbing pattern into a regular expression
     * pattern. The result can be used with the standard {@link java.util.regex} API to
     * recognize strings which matchesGlob the glob pattern.
     * <p/>
     * See also, the POSIX Shell language:
     * http://pubs.opengroup.org/onlinepubs/009695399/utilities/xcu_chap02.html#tag_02_13_01
     *
     * Thanks go to
     * http://stackoverflow.com/questions/1247772/is-there-an-equivalent-of-java-util-regex-for-glob-type-patterns
     *
     * @param pattern A glob pattern.
     * @return A regex pattern to recognize the given glob pattern.
     */
    private static String convertGlobToRegex(String pattern) {
        StringBuilder sb = new StringBuilder(pattern.length());
        int inGroup = 0;
        int inClass = 0;
        int firstIndexInClass = -1;
        char[] arr = pattern.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            switch (ch) {
                case '\\':
                    if (++i >= arr.length) {
                        sb.append('\\');
                    } else {
                        char next = arr[i];
                        switch (next) {
                            case ',':
                                // escape not needed
                                break;
                            case 'Q':
                            case 'E':
                                // extra escape needed
                                sb.append('\\');
                            default:
                                sb.append('\\');
                        }
                        sb.append(next);
                    }
                    break;
                case '*':
                    if (inClass == 0) {
                        sb.append(".*");
                    } else {
                        sb.append('*');
                    }
                    break;

                case '?':
                    if (inClass == 0) {
                        sb.append('.');
                    } else {
                        sb.append('?');
                    }
                    break;
                case '[':
                    inClass++;
                    firstIndexInClass = i + 1;
                    sb.append('[');
                    break;
                case ']':
                    inClass--;
                    sb.append(']');
                    break;
                case '.':
                case '(':
                case ')':
                case '+':
                case '|':
                case '^':
                case '$':
                case '@':
                case '%':
                    if (inClass == 0 || (firstIndexInClass == i && ch == '^')) {
                        sb.append('\\');
                    }
                    sb.append(ch);
                    break;
                case '!':
                    if (firstIndexInClass == i) {
                        sb.append('^');
                    } else {
                        sb.append('!');
                    }
                    break;
                case '{':
                    inGroup++;
                    sb.append('(');
                    break;
                case '}':
                    inGroup--;
                    sb.append(')');
                    break;
                case ',':
                    if (inGroup > 0) {
                        sb.append('|');
                    } else {
                        sb.append(',');
                    }
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }
}
