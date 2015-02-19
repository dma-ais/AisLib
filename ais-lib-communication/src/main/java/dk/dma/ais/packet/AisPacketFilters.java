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

import dk.dma.ais.filter.ReplayDownSampleFilter;
import dk.dma.ais.filter.ReplayDuplicateFilter;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * @author Kasper Nielsen
 */
public class AisPacketFilters extends AisPacketFiltersBase {

    public static <T> Predicate<AisPacket> filterOnMessageType(final Class<T> messageType, final Predicate<T> predicate) {
        requireNonNull(messageType);
        requireNonNull(predicate);
        return new AbstractMessagePredicate() {
            /**
             * If AisMessage m is of the given messageType, then evaluate its predicate. Otherwise ignore it and return true.
             *
             * @param m
             * @return
             */
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

    public static Predicate<AisPacket> filterOnSourceBasestation(Integer... ids) {
        final Integer[] copy = ids.clone();
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
    public static Predicate<AisPacket> filterOnSourceBasestation(String... ids) {
        final Integer[] bss = new Integer[ids.length];
        for (int i = 0; i < bss.length; i++) {
            bss[i] = Integer.valueOf(ids[i]);
        }
        return filterOnSourceBasestation(bss);
    }

    /**
     * Returns a predicate that will filter packets based on the country of the source tag.
     *
     * @param countries
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
                for (int i = 0; i < sourceTypes.length; i++) {
                    // return sourceType == p.getTags().getSourceType();
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

    /**
     * Filter on message to have known position inside given area.
     *
     * @param area
     *            The area that the position must reside inside.
     * @return
     */

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

    /**
     * Block position messages outside of given area; let all other messages pass.
     *
     * @param area
     *            The area that the position messages must reside inside.
     * @return
     */

    public static Predicate<AisPacket> filterRelaxedOnMessagePositionWithin(final Area area) {
        requireNonNull(area);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket packet) {
                Position position = null;
                AisMessage message = packet.tryGetAisMessage();
                if (message instanceof IPositionMessage) {
                    position = ((IPositionMessage) message).getPos().getGeoLocation();
                }
                return position == null ? true : area.contains(position);
            }

            public String toString() {
                return "position within = " + area;
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceBasestation(final CompareToOperator operator, final Integer bs) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return compare(p.getTags().getSourceBs(), bs, operator);
            }

            public String toString() {
                return "bs = " + bs;
            }
        };
    }

    // ---

    /**
     * Filter on a comparison to a value of the indicated calendarField (see java.util.Calendar).
     *
     * @param operator
     * @param calendarField
     * @param value
     * @return
     */

    protected static Predicate<AisPacket> filterOnMessageReceiveTime(final CompareToOperator operator, final int calendarField,
            final int value) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                Date msgTimestamp = p.getTimestamp();
                if (msgTimestamp != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(msgTimestamp);
                    pass = compare(getCalendarValue(calendar, calendarField), value, operator);
                }
                return pass;
            }

            public String toString() {
                return "cal#" + calendarField + " = " + value;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeMonth(final CompareToOperator operator, Integer rhs) {
        return filterOnMessageReceiveTime(operator, Calendar.MONTH, rhs);
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfWeek(final CompareToOperator operator, Integer rhs) {
        return filterOnMessageReceiveTime(operator, Calendar.DAY_OF_WEEK, rhs);
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeYear(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.YEAR);
            }

            public String toString() {
                return "year = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeYear(Integer... years) {
        final Integer[] copy = years.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.YEAR, copy);
            }

            public String toString() {
                return "year = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeMonth(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.MONTH);
            }

            public String toString() {
                return "month = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeMonth(Integer... months) {
        final Integer[] copy = months.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.MONTH, copy);
            }

            public String toString() {
                return "months = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfMonth(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.DAY_OF_MONTH);
            }

            public String toString() {
                return "dom = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfMonth(Integer... days) {
        final Integer[] copy = days.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.DAY_OF_MONTH, copy);
            }

            public String toString() {
                return "dom = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfWeek(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.DAY_OF_WEEK);
            }

            public String toString() {
                return "dom = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfWeek(Integer... days) {
        final Integer[] copy = days.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.DAY_OF_WEEK, copy);
            }

            public String toString() {
                return "dow = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeHour(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.HOUR_OF_DAY);
            }

            public String toString() {
                return "hour = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeHour(Integer... hours) {
        final Integer[] copy = hours.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.HOUR_OF_DAY, copy);
            }

            public String toString() {
                return "hour = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeMinute(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInRange(min, max, p.getTimestamp(), Calendar.MINUTE);
            }

            public String toString() {
                return "minute = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageReceiveTimeMinute(Integer... minutes) {
        final Integer[] copy = minutes.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return timestampInSortedArray(p.getTimestamp(), Calendar.MINUTE, copy);
            }

            public String toString() {
                return "minute = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    private static boolean timestampInSortedArray(Date timestamp, int calendarField, Integer[] sortedArray) {
        boolean pass = false;
        if (timestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            int value = getCalendarValue(calendar, calendarField);
            pass = Arrays.binarySearch(sortedArray, value) >= 0;
        }
        return pass;
    }

    private static boolean timestampInRange(int min, int max, Date timestamp, int calendarField) {
        boolean pass = false;
        if (timestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            int value = getCalendarValue(calendar, calendarField);
            pass = inRange(min, max, value);
        }
        return pass;
    }

    /**
     * Extract values from Calendar, by adjusting month numbers to January = 1 and weekdays having Monday = 1
     *
     * @param calendarField
     * @param calendar
     * @return
     */
    private static int getCalendarValue(Calendar calendar, int calendarField) {
        int value = calendar.get(calendarField);
        if (calendarField == Calendar.MONTH) {
            value = value + 1;
        } else if (calendarField == Calendar.DAY_OF_WEEK) {
            value = (value - 1 + 8) % 8; // 1 = man, 7 = sun
        }
        return value;
    }

    // ---

    public static Predicate<AisPacket> filterOnMessageId(final CompareToOperator operator, final Integer id) {
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

    public static Predicate<AisPacket> filterOnMessageMmsi(final CompareToOperator operator, final Integer mmsi) {
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

    public static Predicate<AisPacket> filterOnMessageImo(final CompareToOperator operator, final Integer imo) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageShiptype(final CompareToOperator operator, final Integer shiptype) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    pass = compare(((AisMessage5) aisMessage).getShipType(), shiptype, operator);
                }
                return pass;
            }

            public String toString() {
                return "shiptype = " + shiptype;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageNavigationalStatus(final CompareToOperator operator, final Integer navstatus) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisPositionMessage) {
                    pass = compare(((AisPositionMessage) aisMessage).getNavStatus(), navstatus, operator);
                }
                return pass;
            }

            public String toString() {
                return "navstatus = " + navstatus;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageName(final CompareToOperator operator, final Float name) {
        return filterOnMessageName(operator, name.toString());
    }

    public static Predicate<AisPacket> filterOnMessageName(final CompareToOperator operator, final Integer name) {
        return filterOnMessageName(operator, name.toString());
    }

    public static Predicate<AisPacket> filterOnMessageName(final CompareToOperator operator, String name) {
        final String n = preprocessExpressionString(name);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageNameMatch(String pattern) {
        final String glob = preprocessExpressionString(pattern);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    pass = matchesGlob(preprocessAisString(((AisStaticCommon) aisMessage).getName()), glob);
                }
                return pass;
            }

            public String toString() {
                return "name = " + glob;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageCallsign(final CompareToOperator operator, String callsign) {
        final String n = preprocessExpressionString(callsign);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageCallsignMatch(String pattern) {
        final String glob = preprocessExpressionString(pattern);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisStaticCommon) {
                    pass = matchesGlob(preprocessAisString(((AisStaticCommon) aisMessage).getCallsign()), glob);
                }
                return pass;
            }

            public String toString() {
                return "name = " + glob;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageSpeedOverGround(final CompareToOperator operator, final Float sog) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageCourseOverGround(final CompareToOperator operator, final Float cog) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageTrueHeading(final CompareToOperator operator, final Integer hdg) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = compare((float) ((IVesselPositionMessage) aisMessage).getTrueHeading(), hdg, operator);
                }
                return pass;
            }

            public String toString() {
                return "hdg = " + hdg;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageLongitude(final CompareToOperator operator, final Float lon) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IPositionMessage) {
                    pass = compare((float) ((IPositionMessage) aisMessage).getPos().getLongitudeDouble(), lon, operator);
                }
                return pass;
            }

            public String toString() {
                return "lon = " + lon;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageLatitude(final CompareToOperator operator, final Float lat) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IPositionMessage) {
                    pass = compare((float) ((IPositionMessage) aisMessage).getPos().getLatitudeDouble(), lat, operator);
                }
                return pass;
            }

            public String toString() {
                return "lat = " + lat;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageDraught(final CompareToOperator operator, final Float draught) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageId(Integer... ids) {
        final Integer[] m = ids.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageMmsi(Integer... mmsis) {
        final Integer[] m = mmsis.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageImo(Integer... imos) {
        final Integer[] m = imos.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageShiptype(Integer... shiptypes) {
        final Integer[] m = shiptypes.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    int shiptype = ((AisMessage5) aisMessage).getShipType();
                    pass = Arrays.binarySearch(m, shiptype) >= 0;
                }
                return pass;
            }

            public String toString() {
                return "shiptypes = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageNavigationalStatus(Integer... navstats) {
        final Integer[] m = navstats.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisPositionMessage) {
                    int navstat = ((AisPositionMessage) aisMessage).getNavStatus();
                    pass = Arrays.binarySearch(m, navstat) >= 0;
                }
                return pass;
            }

            public String toString() {
                return "navstat = " + skipBrackets(Arrays.toString(m));
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageName(String... names) {
        final String[] m = preprocessExpressionStrings(names);
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageCallsign(String... callsigns) {
        final String[] m = preprocessExpressionStrings(callsigns);
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageTrueHeading(Integer... hdgs) {
        final Integer[] m = hdgs.clone();
        Arrays.sort(m);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
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

    public static Predicate<AisPacket> filterOnMessageId(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    pass = inRange(min, max, aisMessage.getMsgId());
                }
                return pass;
            }

            public String toString() {
                return "msgid = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageMmsi(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage != null) {
                    pass = inRange(min, max, aisMessage.getUserId());
                }
                return pass;
            }

            public String toString() {
                return "mmsi = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageImo(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    pass = inRange(min, max, (int) ((AisMessage5) aisMessage).getImo());
                }
                return pass;
            }

            public String toString() {
                return "imo = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageShiptype(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisMessage5) {
                    pass = inRange(min, max, ((AisMessage5) aisMessage).getShipType());
                }
                return pass;
            }

            public String toString() {
                return "shiptype = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageNavigationalStatus(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof AisPositionMessage) {
                    pass = inRange(min, max, ((AisPositionMessage) aisMessage).getNavStatus());
                }
                return pass;
            }

            public String toString() {
                return "navstat = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageTrueHeading(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = inRange(min, max, ((IVesselPositionMessage) aisMessage).getTrueHeading());
                }
                return pass;
            }

            public String toString() {
                return "hdg = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageLatitude(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = inRange(min, max, (float) ((IVesselPositionMessage) aisMessage).getPos().getLatitudeDouble());
                }
                return pass;
            }

            public String toString() {
                return "lat = " + min + ".." + max;
            }
        };
    }

    public static Predicate<AisPacket> filterOnMessageLongitude(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                boolean pass = false;
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage instanceof IVesselPositionMessage) {
                    pass = inRange(min, max, (float) ((IVesselPositionMessage) aisMessage).getPos().getLongitudeDouble());
                }
                return pass;
            }

            public String toString() {
                return "lon = " + min + ".." + max;
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
    
    public static Predicate<AisPacket> filterOnMessageCountry(final Country... countries) {
        return filterOnTargetCountry(countries);
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

    /**
     * Replaced by Predicate<AisPacket> parseExpressionFilter(String filter)
     *
     * @deprecated
     * @param filter
     * @return
     */
    @Deprecated
    public static Predicate<AisPacket> parseSourceFilter(String filter) {
        return AisPacketFiltersExpressionFilterParser.parseExpressionFilter(filter);
    }

    public static Predicate<AisPacket> parseExpressionFilter(String filter) {
        return AisPacketFiltersExpressionFilterParser.parseExpressionFilter(filter);
    }

    // ---

    abstract static class AbstractMessagePredicate implements Predicate<AisPacket> {

        abstract boolean test(AisMessage message);

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean test(AisPacket element) {
            AisMessage m = element.tryGetAisMessage();
            return m != null && test(m);
        }
    }

    /**
     * A non thread-safe predicate (statefull) that can be used sample duration/distance.
     */
    static class SamplerFilter implements Predicate<AisPacket> {

        /**
         * The latest received position that was accepted, or null if no position has been received.
         */
        Position latestPosition;

        /**
         * The latest time stamp that was accepted, or null if no packets has been received.
         */
        Long latestTimestamp;

        /**
         * If non-null the minimum distance traveled in meters between updates.
         */
        final Integer minDistanceInMeters;

        /**
         * If non-null the minimum duration in milliseconds between updates.
         */
        final Long minDurationInMS;

        SamplerFilter(Integer minDistanceInMeters, Long minDurationInMS) {
            if (minDistanceInMeters == null && minDurationInMS == null) {
                throw new IllegalArgumentException("minDistanceInMeters and minDurationInMS cannot both be null");
            }
            this.minDistanceInMeters = minDistanceInMeters;
            this.minDurationInMS = minDurationInMS;
        }

        /**
         * {@inheritDoc}
         */
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


    /**
     * Similar to the {@code SamplingFilter} except that it applies the sampling per MMSI target
     * @param minDistance the minimum distance between two packets per target
     * @param minDuration the minimum time in ms between two packets per target
     * @return the target sampling filter predicate
     */
    public static Predicate<AisPacket> targetSamplingFilter(final Integer minDistance, final Long minDuration) {
        return new Predicate<AisPacket>() {

            Map<Integer, Position> latestPositions = new ConcurrentHashMap<>();
            Map<Integer, Long> latestTimestamps = new ConcurrentHashMap<>();
            final Integer minDistanceInMeters = minDistance;
            final Long minDurationInMS = minDuration;

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean test(AisPacket p) {
                // Get hold of the packet MMSI
                AisMessage aisMessage = p.tryGetAisMessage();
                if (aisMessage == null) {
                    return false;
                }
                int mmsi = aisMessage.getUserId();

                int type = aisMessage.getMsgId();

                // Include all relevant static data reports
                if (type == 5 || type == 24) {
                    return true;
                }

                // Check that we only include relevant position reports
                if (type != 1 && type != 2 && type != 3 && type != 18) {
                    return false;
                }

                // Get hold of the position time
                PositionTime pos = p.tryGetPositionTime();
                if (pos == null) {
                    return false;
                }
                Position latestPosition = latestPositions.get(mmsi);
                Long latestTimestamp = latestTimestamps.get(mmsi);

                // Check if the current position/time incurs an update
                boolean updateDistance = this.minDistanceInMeters != null && (latestPosition == null || latestPosition.rhumbLineDistanceTo(pos) >= (double) this.minDistanceInMeters);
                boolean updateDuration = this.minDurationInMS != null && (latestTimestamp == null || pos.getTime() - latestTimestamp >= this.minDurationInMS);
                if (!updateDistance && !updateDuration) {
                    return false;
                } else {
                    latestPositions.put(mmsi, pos);
                    latestTimestamps.put(mmsi, pos.getTime());
                    return true;
                }

            }
        };
    }

    /**
     * Removes duplicates within the given time window
     * @param windowSize the sampling rate in ms
     * @return the target sampling filter predicate
     */
    public static Predicate<AisPacket> duplicateFilter(final long windowSize) {
        return new Predicate<AisPacket>() {
            ReplayDuplicateFilter filter = new ReplayDuplicateFilter(windowSize);

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean test(AisPacket p) {
                return !filter.rejectedByFilter(p);
            }
        };
    }
}
