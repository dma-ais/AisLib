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
package dk.dma.ais.data;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage21;

/**
 * Class to represent AIS AtoN target
 */
public class AisAtonTarget extends AisTarget {

    private static final long serialVersionUID = 1L;

    @Override
    public void update(AisMessage aisMessage) {
        // Throw error if message is from other type of target
        if (AisClassATarget.isClassAPosOrStatic(aisMessage) || AisClassBTarget.isClassBPosOrStatic(aisMessage)
                || AisBsTarget.isBsReport(aisMessage)) {
            throw new IllegalArgumentException("Trying to update AtoN target with report of other target type");
        }
        // Ignore everything but BS reports
        if (!isAtonReport(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    /**
     * Determine if message is AtoN report
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isAtonReport(AisMessage aisMessage) {
        return aisMessage instanceof AisMessage21;
    }
}
