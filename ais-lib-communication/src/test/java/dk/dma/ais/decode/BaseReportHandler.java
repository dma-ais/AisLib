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
package dk.dma.ais.decode;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.enav.util.function.Consumer;

/**
 * Example AIS message handler that saves MMSI for the base stations observed
 * 
 * For debug out adjust log4j.xml level
 * 
 * @author obo
 * 
 */
public class BaseReportHandler implements Consumer<AisMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseReportHandler.class);

    private Set<Integer> baseStations = new HashSet<>();
    private Set<Integer> baseStationOrigins = new HashSet<>();

    public void accept(AisMessage aisMessage) {
        // Try to get proprietary source tag and evaluate base station origin
        IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
        if (sourceTag != null) {
            baseStationOrigins.add(sourceTag.getBaseMmsi());
            LOG.debug("Observed base station origins: " + baseStationOrigins.size());
        }

        // Only handle base station reports
        if (aisMessage.getMsgId() != 4) {
            return;
        }

        // Cast to message 4 and save user id in hash set
        AisMessage4 msg4 = (AisMessage4) aisMessage;
        baseStations.add(msg4.getUserId());
        LOG.debug("Observed base stations: " + baseStations.size());
    }

    public Set<Integer> getBaseStations() {
        return baseStations;
    }

    public Set<Integer> getBaseStationOrigins() {
        return baseStationOrigins;
    }

}
