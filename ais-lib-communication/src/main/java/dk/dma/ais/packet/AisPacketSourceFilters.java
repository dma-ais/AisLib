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

import com.google.common.collect.ImmutableSet;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * @author Kasper Nielsen
 */
public class AisPacketSourceFilters implements FilterPredicateFactory {

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

    @SuppressWarnings("unused")
    public static Predicate<AisPacketSource> filterOnSourceBasestation(final CompareToOperator operator, final Integer bs) {
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                return compare(p.getSourceBaseStation(), bs, operator);
            }
            public String toString() {
                return "bs = " + bs;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacketSource> filterOnSourceBasestation(Integer... ids) {
        final Integer[] copy = ids.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                int sourceBs = p.getSourceBaseStation();
                return Arrays.binarySearch(copy, sourceBs) >= 0;
            }
            public String toString() {
                return "sourceBaseStation = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    /**
     * Returns a predicate that will filter packets based on the country of the source tag.
     * 
     * @param countries
     *            the countries for which packets should be accepted
     * @return the predicate
     */
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public static Predicate<AisPacketSource> filterOnSourceType(final SourceType... sourceType) {
        requireNonNull(sourceType, "sourceType is null");
        final ImmutableSet<SourceType> sourceTypes = ImmutableSet.copyOf(sourceType);
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                return sourceTypes.contains(p.getSourceType()); // sourceType == p.getSourceType();
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

    private static boolean compare(int lhs, int rhs, CompareToOperator operator) {
        switch (operator) {
            case EQUALS:
                return lhs == rhs;
            case NOT_EQUALS:
                return lhs != rhs;
            case GREATER_THAN:
                return lhs > rhs;
            case GREATER_THAN_OR_EQUALS:
                return lhs >= rhs;
            case LESS_THAN:
                return lhs < rhs;
            case LESS_THAN_OR_EQUALS:
                return lhs <= rhs;
            default:
                throw new IllegalArgumentException("CompareToOperator " + operator + " not implemented.");
        }
    }
}
