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
package dk.dma.ais.utils.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FilterSettings {

    private final Set<Integer> baseStations = new HashSet<>();
    private final Set<String> countries = new HashSet<>();
    private final Set<String> regions = new HashSet<>();
    private Date startDate;
    private Date endDate;

    public FilterSettings() {

    }

    public void addBaseStation(int baseMmsi) {
        baseStations.add(baseMmsi);
    }

    public void addCountry(String country) {
        countries.add(country);
    }

    public Set<Integer> getBaseStations() {
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
            baseStations.add(Integer.parseInt(station));

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
