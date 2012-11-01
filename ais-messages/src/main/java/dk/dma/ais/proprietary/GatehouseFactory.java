/* Copyright (c) 2011 Danish Maritime Safety Administration
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.model.Country;

/**
 * An implementation of the IProprietaryFactory for Gatehouse proprietary sentences
 */
public class GatehouseFactory implements IProprietaryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(GatehouseFactory.class);

    @Override
    public boolean match(String line) {
        return line.indexOf("$PGHP,1") >= 0;
    }

    @Override
    public IProprietarySourceTag getTag(String line) {
        int start = line.indexOf("$PGHP,1");
        if (start < 0) {
            return null;
        }
        line = line.substring(start);
        String[] elems = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
        if (elems.length < 14) {
            LOG.error("Error in Gatehouse proprietary message: wrong number of fields " + elems.length + " in line: "
                    + line);
            return null;
        }
        Long baseMmsi = null;
        if (elems[11].length() > 0) {
            try {
                baseMmsi = Long.parseLong(elems[11]);
            } catch (NumberFormatException e) {
                LOG.error("Error in Gatehouse proprietary message: wrong base mmsi: " + elems[11] + " line: " + line);
                return null;
            }
        }
        String country = elems[9];
        String region = elems[10];
        String[] dateParts = new String[7];
        for (int i = 2; i < 9; i++) {
            dateParts[i - 2] = elems[i];
        }
        String dateStr = StringUtils.join(dateParts, ",") + " +0000";
        String dateFormat = "yyyy,M,d,H,m,s,S Z";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date timestamp;

        try {
            timestamp = format.parse(dateStr);
        } catch (ParseException e) {
            LOG.error("Error in Gatehouse proprietary message: wrong date: " + dateStr + " line: " + line);
            return null;
        }

        Country midCountry = null;
        if (country.length() > 0) {
            midCountry = Country.getByMid(Integer.parseInt(country));
            if (midCountry == null) {
                LOG.warn("Unkown MID " + country);
            }
        }

        return new GatehouseSourceTag(baseMmsi, midCountry, region, timestamp, line);
    }

}
