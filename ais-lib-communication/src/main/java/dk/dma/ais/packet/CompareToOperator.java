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
package dk.dma.ais.packet;

public enum CompareToOperator {
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">=");

    private final String operator;

    CompareToOperator(String operator) {
        this.operator = operator;
    }

    static CompareToOperator fromString(String operator) {
        if ("=".equals(operator)) {
            return EQUALS;
        } else if ("!=".equals(operator)) {
            return NOT_EQUALS;
        } else if (">".equals(operator)) {
            return GREATER_THAN;
        } else if (">=".equals(operator)) {
            return GREATER_THAN_OR_EQUALS;
        } else if ("<".equals(operator)) {
            return LESS_THAN;
        } else if ("<=".equals(operator)) {
            return LESS_THAN_OR_EQUALS;
        } else {
            throw new IllegalArgumentException("Operator " + operator + " not implemented.");
        }
    }
}
