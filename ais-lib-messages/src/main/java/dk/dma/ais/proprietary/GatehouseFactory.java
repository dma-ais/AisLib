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
package dk.dma.ais.proprietary;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.SentenceLine;
import dk.dma.enav.model.Country;

/**
 *
 * @author Kasper Nielsen
 */
public class GatehouseFactory extends ProprietaryFactory {

    public GatehouseFactory() {
        super("GHP");
    }

    private static final Logger LOG = LoggerFactory.getLogger(GatehouseFactory.class);

    /** {@inheritDoc} */
    @Override
    public IProprietaryTag getTag(SentenceLine sl) {
        // Check checksum
        if (!sl.isChecksumMatch()) {
            LOG.error("Error in Gatehouse proprietary tag: \"" + sl.getLine() + "\": Wrong checksum field: " + sl.getChecksumField() + ": Should be: " + sl.getChecksum());
            return null;
        }

        List<String> fields = sl.getFields();

        if (fields == null || fields.size() < 2) {
            LOG.error("Error in Gatehouse proprietary tag: no fields in line: " + sl.getLine());
            return null;
        }

        // Only handle source tag
        Integer type = Integer.parseInt(fields.get(1));
        if (type == null || type.intValue() != 1) {
            return null;
        }

        if (fields.size() < 14) {
            LOG.error("Error in Gatehouse proprietary tag: wrong number of fields " + fields.size() + " in line: " + sl.getLine());
            return null;
        }
        Integer baseMmsi = null;
        if (fields.get(11).length() > 0) {
            try {
                baseMmsi = Integer.parseInt(fields.get(11));
            } catch (NumberFormatException e) {
                LOG.error("Error in Gatehouse proprietary tag: wrong base mmsi: " + fields.get(11) + " line: " + sl.getLine());
                return null;
            }
        }
        String country = fields.get(9);
        String region = fields.get(10);
        int[] dateParts = new int[7];
        for (int i = 2; i < 9; i++) {
            dateParts[i - 2] = Integer.parseInt(fields.get(i));
        }
        ZonedDateTime zdt = ZonedDateTime.of(dateParts[0], dateParts[1], dateParts[2], dateParts[3], dateParts[4], dateParts[5],
                dateParts[6] * 1000 * 1000, ZoneId.of("UTC"));
        Date d = new Date(zdt.toInstant().toEpochMilli());

        Country midCountry = null;
        if (country.length() > 0 && !country.equals("0")) {
            midCountry = Country.getByMid(Integer.parseInt(country));
            if (midCountry == null) {
                LOG.warn("Unkown MID " + country);
            }
        }

        return new GatehouseSourceTag(baseMmsi, midCountry, region, d, sl.getLine());
    }

}
