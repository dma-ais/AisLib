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
package dk.dma.ais.proprietary;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
            LOG.error("Error in Gatehouse proprietary tag wrong checksum: " + sl.getChecksum());
            return null;
        }
        
        List<String> fields = sl.getFields();
        
        if (fields == null || fields.size() < 14) {
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
        DateTime datetime = new DateTime(dateParts[0], dateParts[1], dateParts[2], dateParts[3], dateParts[4], dateParts[5],
                dateParts[6], DateTimeZone.UTC);

        Country midCountry = null;
        if (country.length() > 0) {
            midCountry = Country.getByMid(Integer.parseInt(country));
            if (midCountry == null) {
                LOG.warn("Unkown MID " + country);
            }
        }

        return new GatehouseSourceTag(baseMmsi, midCountry, region, datetime.toDate(), sl.getLine());
    }

}
