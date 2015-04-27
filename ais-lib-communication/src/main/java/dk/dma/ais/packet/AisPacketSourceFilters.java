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
package dk.dma.ais.packet;

import com.google.common.collect.ImmutableSet;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;

import java.util.Arrays;
import java.util.function.Predicate;

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

    public static Predicate<AisPacketSource> filterOnSourceBasestation(final CompareToOperator operator, final Integer bs) {
        return new Predicate<AisPacketSource>() {
            public boolean test(AisPacketSource p) {
                return p.getSourceBaseStation() != null && compare(p.getSourceBaseStation(), bs, operator);
            }

            public String toString() {
                return "bs = " + bs;
            }
        };
    }

    public static Predicate<AisPacketSource> filterOnSourceBasestation(Integer... ids) {
        final Integer[] copy = ids.clone();
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
     * Returns a predicate that will filter packets based on the country of the source tag.
     * 
     * @param countries
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

    private final static boolean compare(int lhs, int rhs, CompareToOperator operator) {
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
