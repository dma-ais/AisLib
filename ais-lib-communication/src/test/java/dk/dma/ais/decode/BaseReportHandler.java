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
package dk.dma.ais.decode;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import java.util.function.Consumer;

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
