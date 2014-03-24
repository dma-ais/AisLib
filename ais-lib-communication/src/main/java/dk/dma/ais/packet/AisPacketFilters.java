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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;
import dk.dma.enav.util.function.Predicate;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

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

    public static Predicate<AisPacket> filterOnSourceType(final SourceType... sourceType) {
        final SourceType[] sourceTypes = sourceType.clone();
        requireNonNull(sourceType, "sourceType is null");
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean test = false;
                for (int i = 0; i<sourceTypes.length; i++) {
                    //return sourceType == p.getTags().getSourceType();
                    if (sourceTypes[i].equals(p.getTags().getSourceType())) {
                        test = true;
                    }
                }
                return test;
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

    public static <T> Predicate<AisPacket> filterOnMessageType(final Class<T> messageType) {
        requireNonNull(messageType);
        return new AbstractMessagePredicate() {
            public boolean test(AisMessage m) {
                return messageType.isAssignableFrom(m.getClass());
            }
        };
    }

    public enum Operator {EQUALS, NOT_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS};

    private static boolean compare(String lhs, String rhs, Operator operator) {
        lhs = lhs.replace('@',' ').trim();
        rhs = rhs.replace('@',' ').trim();

        switch (operator) {
            case EQUALS:
                return lhs.equalsIgnoreCase(rhs);
            case NOT_EQUALS:
                return ! lhs.equalsIgnoreCase(rhs);
            case GREATER_THAN:
                return lhs.compareToIgnoreCase(rhs) > 0;
            case GREATER_THAN_OR_EQUALS:
                return lhs.compareToIgnoreCase(rhs) >= 0;
            case LESS_THAN:
                return lhs.compareToIgnoreCase(rhs) < 0;
            case LESS_THAN_OR_EQUALS:
                return lhs.compareToIgnoreCase(rhs) <= 0;
            default:
                throw new IllegalArgumentException("Operator " + operator + " not implemented.");
        }
    }

    private static boolean compare(int lhs, int rhs, Operator operator) {
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
                throw new IllegalArgumentException("Operator " + operator + " not implemented.");
        }
    }

    private static boolean compare(float lhs, float rhs, Operator operator) {
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
                throw new IllegalArgumentException("Operator " + operator + " not implemented.");
        }
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageId(final Operator operator, final Integer id) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                AisMessage aisMessage = p.tryGetAisMessage();
                return aisMessage == null ? false : compare(aisMessage.getMsgId(), id, operator);
            }
            public String toString() {
                return "id = " + id;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageMmsi(final Operator operator, final Integer mmsi) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                AisMessage aisMessage = p.tryGetAisMessage();
                return aisMessage == null ? false : compare(aisMessage.getUserId(), mmsi, operator);
            }
            public String toString() {
                return "mmsi = " + mmsi;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageImo(final Operator operator, final Integer imo) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    pass = compare((int) ((AisMessage5) aisMessage).getImo(), imo, operator);
                }
                return pass;
            }
            public String toString() {
                return "imo = " + imo;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageName(final Operator operator, final Float name) {
        return filterOnMessageName(operator, name.toString());
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageName(final Operator operator, final Integer name) {
        return filterOnMessageName(operator, name.toString());
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageName(final Operator operator, String name) {
        final String n = preprocessExpressionString(name);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    pass = compare(((AisStaticCommon) aisMessage).getName(), n, operator);
                }
                return pass;
            }
            public String toString() {
                return "name = " + n;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageCallsign(final Operator operator, String callsign) {
        final String n = preprocessExpressionString(callsign);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    pass = compare(((AisStaticCommon) aisMessage).getCallsign(), n, operator);
                }
                return pass;
            }
            public String toString() {
                return "callsign = " + n;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageSpeedOverGround(final Operator operator, final Float sog) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = compare((float) (((IVesselPositionMessage) aisMessage).getSog() / 10.0), sog, operator);
                }
                return pass;
            }
            public String toString() {
                return "sog = " + sog;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageCourseOverGround(final Operator operator, final Float cog) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = compare((float) (((IVesselPositionMessage) aisMessage).getCog() / 10.0), cog, operator);
                }
                return pass;
            }
            public String toString() {
                return "cog = " + cog;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageTrueHeading(final Operator operator, final Integer hdg) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = compare((float) (((IVesselPositionMessage) aisMessage).getTrueHeading()), hdg, operator);
                }
                return pass;
            }
            public String toString() {
                return "hdg = " + hdg;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageLongitude(final Operator operator, final Float lon) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IPositionMessage) {
                    pass = compare((float) (((IPositionMessage) aisMessage).getPos().getLongitudeDouble()), lon, operator);
                }
                return pass;
            }
            public String toString() {
                return "lon = " + lon;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageLatitude(final Operator operator, final Float lat) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IPositionMessage) {
                    pass = compare((float) (((IPositionMessage) aisMessage).getPos().getLatitudeDouble()), lat, operator);
                }
                return pass;
            }
            public String toString() {
                return "lat = " + lat;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageDraught(final Operator operator, final Float draught) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    pass = compare((float) (((AisMessage5) aisMessage).getDraught() / 10.0), draught, operator);
                }
                return pass;
            }
            public String toString() {
                return "draught = " + draught;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageIdInList(Integer... ids) {
        final Integer[] m = ids.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    pass = Arrays.binarySearch(m, aisMessage.getMsgId()) >= 0;
                }
                return pass;
            }
            public String toString() {
                return "msgid = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageMmsiInList(Integer... mmsis) {
        final Integer[] m = mmsis.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    pass = Arrays.binarySearch(m, aisMessage.getUserId()) >= 0;
                }
                return pass;
            }
            public String toString() {
                return "mmsi = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageImoInList(Integer... imos) {
        final Integer[] m = imos.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    int imo = (int) ((AisMessage5) aisMessage).getImo();
                    pass = Arrays.binarySearch(m, imo) >= 0;
                }
                return pass;
            }
            public String toString() {
                return "imo = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageNameInList(String... names) {
        final String[] m = preprocessExpressionStrings(names);
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    String name = preprocessAisString(((AisStaticCommon) aisMessage).getName());
                    pass = Arrays.binarySearch(m, name) >= 0;
                }
                return pass;
            }

            public String toString() {
                return "name = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageCallsignInList(String... callsigns) {
        final String[] m = preprocessExpressionStrings(callsigns);
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    String name = preprocessAisString(((AisStaticCommon) aisMessage).getCallsign());
                    pass = Arrays.binarySearch(m, name) >= 0;
                }
                return pass;
            }

            public String toString() {
                return "name = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageTrueHeadingInList(Integer... hdgs) {
        final Integer[] m = hdgs.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    int hdg = ((IVesselPositionMessage) aisMessage).getTrueHeading();
                    pass = Arrays.binarySearch(m, hdg) >= 0;
                }
                return pass;
            }
            public String toString() {
                return "hdg = " + skipBrackets(Arrays.toString(m));
            }
        };
    }
    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageIdInRange(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    int id = aisMessage.getMsgId();
                    pass = id >= min && id <= max;
                }
                return pass;
            }
            public String toString() {
                return "msgid = " + min + ".." + max;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageMmsiInRange(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    int mmsi = aisMessage.getUserId();
                    pass = mmsi >= min && mmsi <= max;
                }
                return pass;
            }
            public String toString() {
                return "mmsi = " + min + ".." + max;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageImoInRange(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    int imo = (int) ((AisMessage5) aisMessage).getImo();
                    pass = imo >= min && imo <= max;
                }
                return pass;
            }
            public String toString() {
                return "imo = " + min + ".." + max;
            }
        };
    }

    @SuppressWarnings("unused")
    public static Predicate<AisPacket> filterOnMessageTrueHeadingInRange(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = true;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    int hdg = ((IVesselPositionMessage) aisMessage).getTrueHeading();
                    pass = hdg >= min && hdg <= max;
                }
                return pass;
            }
            public String toString() {
                return "hdg = " + min + ".." + max;
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

    public static Predicate<AisPacket> samplingFilter(Integer minDistanceInMeters, Long minDurationInMS) {
        return new SamplerFilter(minDistanceInMeters, minDurationInMS);
    }

    public static Predicate<AisPacket> parseSourceFilter(String filter) {
        return AisPacketFiltersSourceFilterParser.parseSourceFilter(filter);
    }

    static String skipBrackets(String s) {
        return s.length() < 2 ? "" : s.substring(1, s.length() - 1);
    }

    private final static String preprocessAisString(String name) {
        return name != null ? name.replace('@',' ').trim() : null;
    }

    private final static String[] preprocessExpressionStrings(String[] exprStrings) {
        String[] preprocessedStrings = new String[exprStrings.length];
        for (int i=0; i<preprocessedStrings.length; i++) {
            preprocessedStrings[i] = preprocessExpressionString(exprStrings[i]);
        }
        return preprocessedStrings;
    }

    private final static String preprocessExpressionString(String exprString) {
        String preprocessedString = exprString;
        if (preprocessedString.startsWith("'") && preprocessedString.endsWith("'") && preprocessedString.length()>2) {
            preprocessedString = preprocessedString.substring(1, preprocessedString.length()-1);
        }
        return preprocessedString;
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

    /**
     * A non thread-safe predicate (statefull) that can be used sample duration/distance.
     */
    static class SamplerFilter extends Predicate<AisPacket> {

        /** The latest received position that was accepted, or null if no position has been received. */
        Position latestPosition;

        /** The latest time stamp that was accepted, or null if no packets has been received. */
        Long latestTimestamp;

        /** If non-null the minimum distance traveled in meters between updates. */
        final Integer minDistanceInMeters;

        /** If non-null the minimum duration in milliseconds between updates. */
        final Long minDurationInMS;

        SamplerFilter(Integer minDistanceInMeters, Long minDurationInMS) {
            if (minDistanceInMeters == null && minDurationInMS == null) {
                throw new IllegalArgumentException("minDistanceInMeters and minDurationInMS cannot both be null");
            }
            this.minDistanceInMeters = minDistanceInMeters;
            this.minDurationInMS = minDurationInMS;
        }

        /** {@inheritDoc} */
        @Override
        public boolean test(AisPacket p) {
            PositionTime pos = p.tryGetPositionTime();
            if (pos == null) {
                return false;
            }
            boolean updateDistance = minDistanceInMeters != null
                    && (latestPosition == null || latestPosition.rhumbLineDistanceTo(pos) >= minDistanceInMeters);
            boolean updateDuration = minDurationInMS != null
                    && (latestTimestamp == null || pos.getTime() - latestTimestamp >= minDurationInMS);
            if (!updateDistance && !updateDuration) {
                return false;
            }
            // update latest positions
            latestPosition = pos;
            latestTimestamp = pos.getTime();
            return true;
        }
    }
}
