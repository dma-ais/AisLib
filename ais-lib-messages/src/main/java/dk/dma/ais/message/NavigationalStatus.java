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

/**
 * Navigational status as used in AisMessages of type 1, 2, and 3 and
 * defined in Rec. ITU-R M.1371-4, table 45.
 *
 *
 *     0 = under way using engine,
 *     1 = at anchor,
 *     2 = not under command,
 *     3 = restricted manoeuvrability
 *     4 = constrained by her draught,
 *     5 = moore
 *     6 = aground
 *     7 = engaged in fishing
 *     8 = under way sailing
 *     9 = reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C, high speed craft (HSC),
 *     10 = reserved for future amendment of navigational status for ships carrying dangerous goods (DG), harmful substances (HS) or marine pollutants (MP), or IMO hazard or pollutant category A, wing in grand (WIG);
 *     11-13 = reserved for future use,
 *     14 = AIS-SART (active),
 *     15 = not defined = default (also used by AIS-SART under test)
 */
public enum NavigationalStatus {

    UNDER_WAY_USING_ENGINE(0),
    AT_ANCHOR(1),
    NOT_UNDER_COMMAND(2),
    RESTRICTED_MANOEUVRABILITY(3),
    CONSTRAINED_BY_HER_DRAUGHT(4),
    MOORED(5),
    AGROUND(6),
    ENGAGED_IN_FISHING(7),
    UNDER_WAY(8),
    UNDEFINED(15),
    AIS_SART(14)
    {
        public String prettyStatus() {
            return name().replace("_", "-");
        }
    };

    private final int code;

    NavigationalStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NavigationalStatus get(int intNavigationalStatus) {
        for (NavigationalStatus navigationalStatus : NavigationalStatus.values()) {
            if (intNavigationalStatus == navigationalStatus.code) {
                return navigationalStatus;
            }
        }
        return UNDEFINED;
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
