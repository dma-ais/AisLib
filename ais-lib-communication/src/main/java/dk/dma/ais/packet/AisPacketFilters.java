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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketFilters {

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

    public static Predicate<AisPacket> filterOnSourceBaseStation(final int... ids) {
        final int[] copy = ids.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                Integer sourceBs = p.getTags().getSourceBs();
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
    public static Predicate<AisPacket> filterOnSourceBaseStation(final String... ids) {
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
    public static Predicate<AisPacket> filterOnSourceCountry(final Country... countries) {
        final Country[] c = check(countries);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                Country country = p.getTags().getSourceCountry();
                return country != null && Arrays.binarySearch(c, country) >= 0;
            }

            public String toString() {
                return "sourceCountry = " + skipBrackets(Arrays.toString(c));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceId(final String... ids) {
        final String[] s = check(ids);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                String sourceId = p.getTags().getSourceId();
                return sourceId != null && Arrays.binarySearch(s, sourceId) >= 0;
            }

            public String toString() {
                return "sourceId = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceRegion(final String... regions) {
        final String[] s = check(regions);
        requireNonNull(regions, "regions is null");
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                IProprietarySourceTag sourceTag = p.getVdm().getSourceTag();
                String region = sourceTag == null ? null : sourceTag.getRegion();
                return region != null && Arrays.binarySearch(s, region) >= 0;
            }

            public String toString() {
                return "sourceRegion = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceType(final SourceType sourceType) {
        requireNonNull(sourceType, "sourceType is null");
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return sourceType == p.getTags().getSourceType();
            }

            public String toString() {
                return "sourceType = " + sourceType;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessagePositionWithin(final Area area) {
        requireNonNull(area);
        return filterOnMessageType(IPositionMessage.class, new Predicate<IPositionMessage>() {
            public boolean test(IPositionMessage element) {
                AisPosition pos = element.getPos();
                if (pos != null) {
                    Position p = pos.getGeoLocation();
                    return p != null && area.contains(p);
                }
                return false;
            }

            public String toString() {
                return "position within = " + area;
            }
        });
    }

    public static <T> Predicate<AisPacket> filterOnMessageType(final Class<T> messageType, final Predicate<T> predicate) {
        requireNonNull(messageType);
        requireNonNull(predicate);
        return new AbstractMessagePredicate() {
            @SuppressWarnings("unchecked")
            public boolean test(AisMessage m) {
                if (messageType.isAssignableFrom(m.getClass())) {
                    return predicate.test((T) m);
                }
                return false;
            }

            public String toString() {
                return predicate.toString();
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageType(final int... types) {
        final int[] t = types.clone();
        Arrays.sort(t);
        for (int i : t) {
            if (!AisMessage.VALID_MESSAGE_TYPES.contains(i)) {
                throw new IllegalArgumentException(i + " is not a valid message type");
            }
        }
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                Vdm vdm = p.getVdm();
                return vdm != null && Arrays.binarySearch(t, vdm.getMsgId()) >= 0;
            }

            public String toString() {
                return "messageType = " + skipBrackets(Arrays.toString(t));
            }
        };
    }

    public static Predicate<AisPacket> filterOnTargetCountry(final Country... countries) {
        final Country[] c = AisPacketFilters.check(countries);
        return new AbstractMessagePredicate() {
            public boolean test(AisMessage m) {
                Country country = Country.getCountryForMmsi(m.getUserId());
                return country != null && Arrays.binarySearch(c, country) >= 0;
            }

            public String toString() {
                return "targetCountry = " + skipBrackets(Arrays.toString(c));
            }
        };
    }

    public static Predicate<AisPacket> parseSourceFilter(String filter) {
        return AisPacketFiltersParseHelper.parseSourceFilter(filter);
    }

    static String skipBrackets(String s) {
        return s.length() < 2 ? "" : s.substring(1, s.length() - 1);
    }

    abstract static class AbstractMessagePredicate extends Predicate<AisPacket> {

        abstract boolean test(AisMessage message);

        /** {@inheritDoc} */
        @Override
        public boolean test(AisPacket element) {
            AisMessage m = element.tryGetAisMessage();
            return m != null && test(m);
        }
    }

}
