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
package dk.dma.ais.message;

/**
 * Navigational status as used in AisMessages of type 1, 2, and 3 and
 * defined in Rec. ITU-R M.1371-4, table 45.
 * <pre>{@code
 * <p>
 * <p>
 * }
 * </pre>
 * 0 = under way using engine,
 * 1 = at anchor,
 * 2 = not under command,
 * 3 = restricted manoeuvrability
 * 4 = constrained by her draught,
 * 5 = moore
 * 6 = aground
 * 7 = engaged in fishing
 * 8 = under way sailing
 * 9 = reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C, high speed craft (HSC),
 * 10 = reserved for future amendment of navigational status for ships carrying dangerous goods (DG), harmful substances (HS) or marine pollutants (MP), or IMO hazard or pollutant category A, wing in grand (WIG);
 * 11-13 = reserved for future use,
 * 14 = AIS-SART (active),
 * 15 = not defined = default (also used by AIS-SART under test)
 */
public enum NavigationalStatus {

    /**
     * Under way using engine navigational status.
     */
    UNDER_WAY_USING_ENGINE(0),
    /**
     * At anchor navigational status.
     */
    AT_ANCHOR(1),
    /**
     * Not under command navigational status.
     */
    NOT_UNDER_COMMAND(2),
    /**
     * Restricted manoeuvrability navigational status.
     */
    RESTRICTED_MANOEUVRABILITY(3),
    /**
     * Constrained by her draught navigational status.
     */
    CONSTRAINED_BY_HER_DRAUGHT(4),
    /**
     * Moored navigational status.
     */
    MOORED(5),
    /**
     * Aground navigational status.
     */
    AGROUND(6),
    /**
     * Engaged in fishing navigational status.
     */
    ENGAGED_IN_FISHING(7),
    /**
     * Under way navigational status.
     */
    UNDER_WAY(8),
    /**
     * Undefined navigational status.
     */
    UNDEFINED(15),
    /**
     * The Ais sart.
     */
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

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Get navigational status.
     *
     * @param intNavigationalStatus the int navigational status
     * @return the navigational status
     */
    public static NavigationalStatus get(int intNavigationalStatus) {
        for (NavigationalStatus navigationalStatus : NavigationalStatus.values()) {
            if (intNavigationalStatus == navigationalStatus.code) {
                return navigationalStatus;
            }
        }
        return UNDEFINED;
    }

    /**
     * Pretty status string.
     *
     * @return the string
     */
    public String prettyStatus() {
        String navStat = name().replace("_", " ");
        return navStat.substring(0, 1) + navStat.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return prettyStatus();
    }
}
