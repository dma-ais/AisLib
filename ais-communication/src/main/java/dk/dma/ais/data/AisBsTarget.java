/* Copyright (c) 2012 Danish Maritime Authority
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
import dk.dma.ais.message.AisMessage4;

/**
 * Class to represent an AIS base station target
 */
public class AisBsTarget extends AisTarget {

    private static final long serialVersionUID = 1L;

    // TODO

    public AisBsTarget() {

    }

    @Override
    public void update(AisMessage aisMessage) {
        // Throw error if message is from other type of target
        if (AisClassATarget.isClassAPosOrStatic(aisMessage) || AisClassBTarget.isClassBPosOrStatic(aisMessage)
                || AisAtonTarget.isAtonReport(aisMessage)) {
            throw new IllegalArgumentException("Trying to update BS target with report of other target type");
        }
        // Ignore everything but BS reports
        if (!isBsReport(aisMessage)) {
            return;
        }
        super.update(aisMessage);
    }

    /**
     * Determine if message is BS report
     * 
     * @param aisMessage
     * @return
     */
    public static boolean isBsReport(AisMessage aisMessage) {
        return (aisMessage instanceof AisMessage4);
    }

}
