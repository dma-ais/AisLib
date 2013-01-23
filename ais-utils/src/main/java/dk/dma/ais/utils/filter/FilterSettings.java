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
package dk.dma.ais.utils.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FilterSettings {

    private final Set<Long> baseStations = new HashSet<>();
    private final Set<String> countries = new HashSet<>();
    private final Set<String> regions = new HashSet<>();
    private Date startDate;
    private Date endDate;

    public FilterSettings() {

    }

    public void addBaseStation(long baseMmsi) {
        baseStations.add(baseMmsi);
    }

    public void addCountry(String country) {
        countries.add(country);
    }

    public Set<Long> getBaseStations() {
        return baseStations;
    }

    public Set<String> getCountries() {
        return countries;
    }

    public void addRegion(String region) {
        regions.add(region);
    }

    public Set<String> getRegions() {
        return regions;
    }

    public void parseBaseStations(String str) {
        if (str == null) {
            return;
        }
        String[] stationsAr = str.split(",");
        for (String station : stationsAr) {
            baseStations.add(Long.parseLong(station));

        }
    }

    public void parseCountries(String str) {
        if (str == null) {
            return;
        }
        String[] countriesArr = str.split(",");
        for (String country : countriesArr) {
            countries.add(country);

        }
    }

    public void parseRegions(String r) {
        if (r == null) {
            return;
        }
        String[] regionsArr = r.split(",");
        for (String region : regionsArr) {
            regions.add(region);
        }
    }

    public void parseStartAndEnd(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        if (start != null) {
            try {
                startDate = dateFormat.parse(start);
            } catch (ParseException e) {
                System.err.println("Failed to parse date: " + start);
            }
        }
        if (end != null) {
            try {
                endDate = dateFormat.parse(end);
            } catch (ParseException e) {
                System.err.println("Failed to parse date: " + end);
            }
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
