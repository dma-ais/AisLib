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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
class AisPacketSourceFilters {

    @SafeVarargs
    static <T> T[] check(T... elements) {
        T[] s = elements.clone();
        Arrays.sort(s);
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                throw new NullPointerException("Array is null at position " + i);
            }
        }
        // Check for nulls
        return s;
    }

    public static Predicate<AisPacketSource> filterOnSourceBaseStation(final int... ids) {
        final int[] copy = ids.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                Integer sourceBs = p.getSourceBaseStation();
                return sourceBs != null && Arrays.binarySearch(copy, sourceBs) >= 0;
            }

            public String toString() {
                return "sourceBaseStation = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    /**
     * Returns a predicate that will filter packets based on the base station source tag.
     * 
     * @param ids
     *            the id of the base stations for which packets should be accepted
     * @return the predicate
     */
    public static Predicate<AisPacketSource> filterOnSourceBaseStation(final String... ids) {
        final int[] bss = new int[ids.length];
        for (int i = 0; i < bss.length; i++) {
            bss[i] = Integer.valueOf(ids[i]);
        }
        return filterOnSourceBaseStation(bss);
    }

    /**
     * Returns a predicate that will filter packets based on the country of the source tag.
     * 
     * @param ids
     *            the countries for which packets should be accepted
     * @return the predicate
     */
    public static Predicate<AisPacketSource> filterOnSourceCountry(final Country... countries) {
        final Country[] c = check(countries);
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                Country country = p.getSourceCountry();
                return country != null && Arrays.binarySearch(c, country) >= 0;
            }

            public String toString() {
                return "sourceCountry = " + skipBrackets(Arrays.toString(c));
            }
        };
    }

    public static Predicate<AisPacketSource> filterOnSourceId(final String... ids) {
        final String[] s = check(ids);
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                String sourceId = p.getSourceId();
                return sourceId != null && Arrays.binarySearch(s, sourceId) >= 0;
            }

            public String toString() {
                return "sourceId = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    public static Predicate<AisPacketSource> filterOnSourceRegion(final String... regions) {
        final String[] s = check(regions);
        requireNonNull(regions, "regions is null");
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                String region = p.getSourceRegion();
                return region != null && Arrays.binarySearch(s, region) >= 0;
            }

            public String toString() {
                return "sourceRegion = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    public static Predicate<AisPacketSource> filterOnSourceType(final SourceType sourceType) {
        requireNonNull(sourceType, "sourceType is null");
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                return sourceType == p.getSourceType();
            }

            public String toString() {
                return "sourceType = " + sourceType;
            }
        };
    }

    public static Predicate<AisPacketSource> parseSourceFilter(String filter) {
        return AisPacketSourceFiltersParser.parseSourceFilter(filter);
    }

    static String skipBrackets(String s) {
        return s.length() < 2 ? "" : s.substring(1, s.length() - 1);
    }

}
