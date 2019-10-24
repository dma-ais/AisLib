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
 * The type Ais packet filters.
 *
 * @author Kasper Nielsen
 */
public class AisPacketFilters extends AisPacketFiltersBase {

    /**
     * Filter on message type predicate.
     *
     * @param <T>         the type parameter
     * @param messageType the message type
     * @param predicate   the predicate
     * @return the predicate
     */
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

    /**
     * Filter on message type predicate.
     *
     * @param <T>         the type parameter
     * @param messageType the message type
     * @return the predicate
     */
    public static <T> Predicate<AisPacket> filterOnMessageType(final Class<T> messageType) {
        requireNonNull(messageType);
        return new AbstractMessagePredicate() {
            public boolean test(AisMessage m) {
                return messageType.isAssignableFrom(m.getClass());
            }
        };
    }

    /**
     * Filter on source basestation predicate.
     *
     * @param ids the ids
     * @return the predicate
     */
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
     * @param ids the id of the base stations for which packets should be accepted
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
     * @param countries the countries for which packets should be accepted
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

    /**
     * Filter on source id predicate.
     *
     * @param ids the ids
     * @return the predicate
     */
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

    /**
     * Filter on source region predicate.
     *
     * @param regions the regions
     * @return the predicate
     */
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

    /**
     * Filter on source type predicate.
     *
     * @param sourceType the source type
     * @return the predicate
     */
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
     * @param area The area that the position must reside inside.
     * @return predicate predicate
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
     * @param area The area that the position messages must reside inside.
     * @return predicate predicate
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

    /**
     * Filter on source basestation predicate.
     *
     * @param operator the operator
     * @param bs       the bs
     * @return the predicate
     */
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
     * @param operator      the operator
     * @param calendarField the calendar field
     * @param value         the value
     * @return predicate predicate
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

    /**
     * Filter on message receive time month predicate.
     *
     * @param operator the operator
     * @param rhs      the rhs
     * @return the predicate
     */
    public static Predicate<AisPacket> filterOnMessageReceiveTimeMonth(final CompareToOperator operator, Integer rhs) {
        return filterOnMessageReceiveTime(operator, Calendar.MONTH, rhs);
    }

    /**
     * Filter on message receive time day of week predicate.
     *
     * @param operator the operator
     * @param rhs      the rhs
     * @return the predicate
     */
    public static Predicate<AisPacket> filterOnMessageReceiveTimeDayOfWeek(final CompareToOperator operator, Integer rhs) {
        return filterOnMessageReceiveTime(operator, Calendar.DAY_OF_WEEK, rhs);
    }

    /**
     * Filter on message receive time year predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time year predicate.
     *
     * @param years the years
     * @return the predicate
     */
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

    /**
     * Filter on message receive time month predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time month predicate.
     *
     * @param months the months
     * @return the predicate
     */
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

    /**
     * Filter on message receive time day of month predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time day of month predicate.
     *
     * @param days the days
     * @return the predicate
     */
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

    /**
     * Filter on message receive time day of week predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time day of week predicate.
     *
     * @param days the days
     * @return the predicate
     */
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

    /**
     * Filter on message receive time hour predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time hour predicate.
     *
     * @param hours the hours
     * @return the predicate
     */
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

    /**
     * Filter on message receive time minute predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message receive time minute predicate.
     *
     * @param minutes the minutes
     * @return the predicate
     */
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

    /**
     * Filter on message id predicate.
     *
     * @param operator the operator
     * @param id       the id
     * @return the predicate
     */
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

    /**
     * Filter on message mmsi predicate.
     *
     * @param operator the operator
     * @param mmsi     the mmsi
     * @return the predicate
     */
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

    /**
     * Filter on message imo predicate.
     *
     * @param operator the operator
     * @param imo      the imo
     * @return the predicate
     */
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

    /**
     * Filter on message shiptype predicate.
     *
     * @param operator the operator
     * @param shiptype the shiptype
     * @return the predicate
     */
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

    /**
     * Filter on message navigational status predicate.
     *
     * @param operator  the operator
     * @param navstatus the navstatus
     * @return the predicate
     */
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

    /**
     * Filter on message name predicate.
     *
     * @param operator the operator
     * @param name     the name
     * @return the predicate
     */
    public static Predicate<AisPacket> filterOnMessageName(final CompareToOperator operator, final Float name) {
        return filterOnMessageName(operator, name.toString());
    }

    /**
     * Filter on message name predicate.
     *
     * @param operator the operator
     * @param name     the name
     * @return the predicate
     */
    public static Predicate<AisPacket> filterOnMessageName(final CompareToOperator operator, final Integer name) {
        return filterOnMessageName(operator, name.toString());
    }

    /**
     * Filter on message name predicate.
     *
     * @param operator the operator
     * @param name     the name
     * @return the predicate
     */
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

    /**
     * Filter on message name match predicate.
     *
     * @param pattern the pattern
     * @return the predicate
     */
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

    /**
     * Filter on message callsign predicate.
     *
     * @param operator the operator
     * @param callsign the callsign
     * @return the predicate
     */
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

    /**
     * Filter on message callsign match predicate.
     *
     * @param pattern the pattern
     * @return the predicate
     */
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

    /**
     * Filter on message speed over ground predicate.
     *
     * @param operator the operator
     * @param sog      the sog
     * @return the predicate
     */
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

    /**
     * Filter on message course over ground predicate.
     *
     * @param operator the operator
     * @param cog      the cog
     * @return the predicate
     */
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

    /**
     * Filter on message true heading predicate.
     *
     * @param operator the operator
     * @param hdg      the hdg
     * @return the predicate
     */
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

    /**
     * Filter on message longitude predicate.
     *
     * @param operator the operator
     * @param lon      the lon
     * @return the predicate
     */
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

    /**
     * Filter on message latitude predicate.
     *
     * @param operator the operator
     * @param lat      the lat
     * @return the predicate
     */
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

    /**
     * Filter on message draught predicate.
     *
     * @param operator the operator
     * @param draught  the draught
     * @return the predicate
     */
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

    /**
     * Filter on message id predicate.
     *
     * @param ids the ids
     * @return the predicate
     */
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

    /**
     * Filter on message mmsi predicate.
     *
     * @param mmsis the mmsis
     * @return the predicate
     */
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

    /**
     * Filter on message imo predicate.
     *
     * @param imos the imos
     * @return the predicate
     */
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

    /**
     * Filter on message shiptype predicate.
     *
     * @param shiptypes the shiptypes
     * @return the predicate
     */
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

    /**
     * Filter on message navigational status predicate.
     *
     * @param navstats the navstats
     * @return the predicate
     */
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

    /**
     * Filter on message name predicate.
     *
     * @param names the names
     * @return the predicate
     */
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

    /**
     * Filter on message callsign predicate.
     *
     * @param callsigns the callsigns
     * @return the predicate
     */
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

    /**
     * Filter on message true heading predicate.
     *
     * @param hdgs the hdgs
     * @return the predicate
     */
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

    /**
     * Filter on message id predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message mmsi predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message imo predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message shiptype predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message navigational status predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message true heading predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message latitude predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message longitude predicate.
     *
     * @param min the min
     * @param max the max
     * @return the predicate
     */
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

    /**
     * Filter on message type predicate.
     *
     * @param types the types
     * @return the predicate
     */
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

    /**
     * Filter on message country predicate.
     *
     * @param countries the countries
     * @return the predicate
     */
    public static Predicate<AisPacket> filterOnMessageCountry(final Country... countries) {
        return filterOnTargetCountry(countries);
    }

    /**
     * Filter on target country predicate.
     *
     * @param countries the countries
     * @return the predicate
     */
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

    /**
     * Sampling filter predicate.
     *
     * @param minDistanceInMeters the min distance in meters
     * @param minDurationInMS     the min duration in ms
     * @return the predicate
     */
    public static Predicate<AisPacket> samplingFilter(Integer minDistanceInMeters, Long minDurationInMS) {
        return new SamplerFilter(minDistanceInMeters, minDurationInMS);
    }

    /**
     * Replaced by Predicate&lt;AisPacket&gt; parseExpressionFilter(String filter)
     *
     * @param filter the filter
     * @return predicate predicate
     * @deprecated
     */
    @Deprecated
    public static Predicate<AisPacket> parseSourceFilter(String filter) {
        return AisPacketFiltersExpressionFilterParser.parseExpressionFilter(filter);
    }

    /**
     * Parse expression filter predicate.
     *
     * @param filter the filter
     * @return the predicate
     */
    public static Predicate<AisPacket> parseExpressionFilter(String filter) {
        return AisPacketFiltersExpressionFilterParser.parseExpressionFilter(filter);
    }

    // ---

    /**
     * The type Abstract message predicate.
     */
    abstract static class AbstractMessagePredicate implements Predicate<AisPacket> {

        /**
         * Test boolean.
         *
         * @param message the message
         * @return the boolean
         */
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

        /**
         * Instantiates a new Sampler filter.
         *
         * @param minDistanceInMeters the min distance in meters
         * @param minDurationInMS     the min duration in ms
         */
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
     *
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
     *
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
