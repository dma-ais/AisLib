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

import java.io.Serializable;

public class NavigationalStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum NavStatus {
        UNDEFINED, UNDER_WAY_USING_ENGINE, AT_ANCHOR, NOT_UNDER_COMMAND, RESTRICTED_MANOEUVRABILITY, CONSTRAINED_BY_HER_DRAUGHT, MOORED, AGROUND, ENGAGED_IN_FISHING, UNDER_WAY, SAILING, AIS_SART
    }

    private NavStatus navStatus;

    public NavigationalStatus(int intNavStatus) {
        navStatus = NavStatus.UNDEFINED;

        switch (intNavStatus) {
        case 0:
            navStatus = NavStatus.UNDER_WAY_USING_ENGINE;
            break;
        case 1:
            navStatus = NavStatus.AT_ANCHOR;
            break;
        case 2:
            navStatus = NavStatus.NOT_UNDER_COMMAND;
            break;
        case 3:
            navStatus = NavStatus.RESTRICTED_MANOEUVRABILITY;
            break;
        case 4:
            navStatus = NavStatus.CONSTRAINED_BY_HER_DRAUGHT;
            break;
        case 5:
            navStatus = NavStatus.MOORED;
            break;
        case 6:
            navStatus = NavStatus.AGROUND;
            break;
        case 7:
            navStatus = NavStatus.ENGAGED_IN_FISHING;
            break;
        case 8:
            navStatus = NavStatus.UNDER_WAY;
            break;
        case 14:
            navStatus = NavStatus.AIS_SART;
            break;
        case 15:
            navStatus = NavStatus.UNDEFINED;
            break;
        }

    }

    public NavStatus getNavType() {
        return this.navStatus;
    }

    public String prettyStatus() {
        NavigationalStatus navStatus = this;
        String result;

        if (navStatus.getNavType() == NavStatus.AIS_SART) {
            result = navStatus.getNavType().toString().replace("_", "-");
        } else {

            String navStat = navStatus.getNavType().toString().replace("_", " ");
            result = navStat.substring(0, 1) + navStat.substring(1).toLowerCase();
        }

        return result;
    }

    @Override
    public String toString() {
        // only capitalize the first letter - need to remove underscore - maybe
        NavigationalStatus navStatus = this;

        return navStatus.prettyStatus();

    }

}
