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
package dk.dma.ais.message;

public enum NavigationalStatus {

    UNDEFINED, UNDER_WAY_USING_ENGINE, AT_ANCHOR, NOT_UNDER_COMMAND, RESTRICTED_MANOEUVRABILITY, CONSTRAINED_BY_HER_DRAUGHT, MOORED, AGROUND, ENGAGED_IN_FISHING, UNDER_WAY, SAILING, AIS_SART {
        public String prettyStatus() {
            return name().replace("_", "-");
        }}
    ;
    public static NavigationalStatus get(int intNavigationalStatus) {
        switch (intNavigationalStatus) {
        case 0:
            return UNDER_WAY_USING_ENGINE;
        case 1:
            return AT_ANCHOR;
        case 2:
            return NOT_UNDER_COMMAND;
        case 3:
            return RESTRICTED_MANOEUVRABILITY;
        case 4:
            return CONSTRAINED_BY_HER_DRAUGHT;
        case 5:
            return MOORED;
        case 6:
            return AGROUND;
        case 7:
            return ENGAGED_IN_FISHING;
        case 8:
            return UNDER_WAY;
        case 14:
            return AIS_SART;
        case 15:
            return UNDEFINED;
        default:
            return UNDEFINED;
        }
    }

    public String prettyStatus() {
        String navStat = name().replace("_", " ");
        return navStat.substring(0, 1) + navStat.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return prettyStatus();
    }
}
