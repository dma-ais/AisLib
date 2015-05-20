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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.enav.model.geometry.Area;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * This class provides factory methods to create Predicate<AisPacket> functions which can be used to filter AisPackets.
 *
 * As opposed to the AisPacketFilters class, this class is designed to create predicates which "remember" some state of AisPackets
 * that have previously been served. This implies, that it is possible to establish more advanced filtering, than just inspecting
 * the current AisPacket.
 *
 * Instead, with this stateful filter, it is possible to generate Predicates which can e.g. filter away AisStaticCommon packages for
 * targets whose position is outside some defined area.
 *
 * @author Thomas Borg Salling <tbsalling@tbsalling.dk>
 * @since v2.1
 */
public class AisPacketFiltersStateful extends AisPacketFiltersBase {

    private AisPacketStream aisPacketStream = new AisPacketStreamImpl();
    private TargetTracker targetTracker = new TargetTracker();

    public AisPacketFiltersStateful() {
        targetTracker.subscribeToPacketStream(aisPacketStream);
    }

    // --- IMO

    /**
     * Return false if this message is known to be related to a target with an IMO no. different to 'imo'.
     * 
     * @param operator
     * @param rhsImo
     * @return
     */

    public Predicate<AisPacket> filterOnTargetImo(final CompareToOperator operator, Integer rhsImo) {
        final int imo = rhsImo;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int lhsImo = getImo(mmsi); // Extract IMO no. - if we know it
                return lhsImo < 0 ? false : compare(lhsImo, imo, operator);
            }

            public String toString() {
                return "imo = " + imo;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with an IMO outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetImo(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int imo = getImo(mmsi); // Extract IMO no. - if we know it
                return imo < 0 ? false : inRange(min, max, imo);
            }

            public String toString() {
                return "imo in " + min + ".." + max;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with an IMO outside the given list.
     */

    public Predicate<AisPacket> filterOnTargetImo(Integer[] imos) {
        final int[] list = ArrayUtils.toPrimitive(imos);
        Arrays.sort(list);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int imo = getImo(mmsi); // Extract IMO no. - if we know it
                return imo < 0 ? false : Arrays.binarySearch(list, imo) >= 0;
            }

            public String toString() {
                return "imo in " + skipBrackets(Arrays.toString(list));
            }
        };
    }

    // --- ship type

    /**
     * Return false if this message is known to be related to a target with an IMO no. different to 'imo'.
     * 
     * @param operator
     * @param rhsShiptype
     * @return
     */

    public Predicate<AisPacket> filterOnTargetShiptype(final CompareToOperator operator, Integer rhsShiptype) {
        final int shiptype = rhsShiptype;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int lhsShiptype = getShiptype(mmsi); // Extract shiptype - if we know it
                return lhsShiptype < 0 ? false : compare(lhsShiptype, shiptype, operator);
            }

            public String toString() {
                return "shiptype = " + shiptype;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with an shiptype outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetShiptype(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int shiptype = getShiptype(mmsi); // Extract IMO no. - if we know it
                return shiptype < 0 ? false : inRange(min, max, shiptype);
            }

            public String toString() {
                return "imo in " + min + ".." + max;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a ship type outside the given list.
     */

    public Predicate<AisPacket> filterOnTargetShiptype(Integer[] shiptypes) {
        final int[] list = ArrayUtils.toPrimitive(shiptypes);
        Arrays.sort(list);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int shiptype = getShiptype(mmsi); // Extract shiptype no. - if we know it
                return shiptype < 0 ? false : Arrays.binarySearch(list, shiptype) >= 0;
            }

            public String toString() {
                return "shiptype in " + skipBrackets(Arrays.toString(list));
            }
        };
    }

    // --- navstat

    /**
     * Return false if this message is known to be related to a target with an navstat different to the rhs parameter.
     * 
     * @param operator
     * @param rhsNavstat
     * @return
     */

    public Predicate<AisPacket> filterOnTargetNavigationalStatus(final CompareToOperator operator, Integer rhsNavstat) {
        final int navstat = rhsNavstat;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int lhsNavstat = getNavstat(mmsi); // Extract navstat - if we know it
                return lhsNavstat < 0 ? false : compare(lhsNavstat, navstat, operator);
            }

            public String toString() {
                return "navstat = " + navstat;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a navstat outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetNavigationalStatus(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int navstat = getNavstat(mmsi); // Extract IMO no. - if we know it
                return navstat < 0 ? false : inRange(min, max, navstat);
            }

            public String toString() {
                return "imo in " + min + ".." + max;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a navstat outside the given list.
     */

    public Predicate<AisPacket> filterOnTargetNavigationalStatus(Integer[] navstats) {
        final int[] list = ArrayUtils.toPrimitive(navstats);
        Arrays.sort(list);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int navstat = getNavstat(mmsi); // Extract navstat no. - if we know it
                return navstat < 0 ? false : Arrays.binarySearch(list, navstat) >= 0;
            }

            public String toString() {
                return "navstat in " + skipBrackets(Arrays.toString(list));
            }
        };
    }

    // --- sog

    /**
     * Return false if this message is known to be related to a target with a SOG comparing false to 'sog'.
     * 
     * @param operator
     * @param sog
     * @return
     */

    public Predicate<AisPacket> filterOnTargetSpeedOverGround(final CompareToOperator operator, Float sog) {
        final float rhsSog = sog;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsSog = getSog(mmsi); // Extract sog - if we know it
                return lhsSog != lhsSog /* NaN */? false : compare(lhsSog, rhsSog, operator);
            }

            public String toString() {
                return "sog " + operator + " " + rhsSog;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a SOG outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetSpeedOverGround(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsSog = getSog(mmsi); // Extract sog - if we know it
                return lhsSog != lhsSog /* NaN */? false : inRange(min, max, lhsSog);
            }

            public String toString() {
                return "sog in " + min + ".." + max;
            }
        };
    }

    // --- cog

    /**
     * Return false if this message is known to be related to a target with a COG comparing false to 'cog'.
     * 
     * @param operator
     * @param cog
     * @return
     */

    public Predicate<AisPacket> filterOnTargetCourseOverGround(final CompareToOperator operator, Float cog) {
        final float rhsCog = cog;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsCog = getCog(mmsi); // Extract cog - if we know it
                return lhsCog != lhsCog /* NaN */? false : compare(lhsCog, rhsCog, operator);
            }

            public String toString() {
                return "cog " + operator + " " + rhsCog;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a COG outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetCourseOverGround(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsCog = getCog(mmsi); // Extract cog - if we know it
                return lhsCog != lhsCog /* NaN */? false : inRange(min, max, lhsCog);
            }

            public String toString() {
                return "cog in " + min + ".." + max;
            }
        };
    }

    // --- hdg

    /**
     * Return false if this message is known to be related to a target with a COG comparing false to 'hdg'.
     * 
     * @param operator
     * @param hdg
     * @return
     */

    public Predicate<AisPacket> filterOnTargetTrueHeading(final CompareToOperator operator, Integer hdg) {
        final int rhsHdg = hdg;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int lhsHdg = getHdg(mmsi); // Extract hdg - if we know it
                return lhsHdg < 0 ? false : compare(lhsHdg, rhsHdg, operator);
            }

            public String toString() {
                return "hdg " + operator + " " + rhsHdg;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a COG outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetTrueHeading(final int min, final int max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final int lhsHdg = getHdg(mmsi); // Extract cog - if we know it
                return lhsHdg < 0 ? false : inRange(min, max, lhsHdg);
            }

            public String toString() {
                return "hdg in " + min + ".." + max;
            }
        };
    }

    // --- draught

    /**
     * Return false if this message is known to be related to a target with a draught comparing false to 'draught'.
     * 
     * @param operator
     * @param draught
     * @return
     */

    public Predicate<AisPacket> filterOnTargetDraught(final CompareToOperator operator, Float draught) {
        final float rhsDraught = draught;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsDraught = getDraught(mmsi); // Extract draught - if we know it
                return lhsDraught != lhsDraught /* NaN */? false : compare(lhsDraught, rhsDraught, operator);
            }

            public String toString() {
                return "draught " + operator + " " + rhsDraught;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a draught outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetDraught(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsDraught = getDraught(mmsi); // Extract cog - if we know it
                return lhsDraught != lhsDraught /* NaN */? false : inRange(min, max, lhsDraught);
            }

            public String toString() {
                return "draught in " + min + ".." + max;
            }
        };
    }

    // --- lat

    /**
     * Return false if this message is known to be related to a target with a latitude comparing false to 'lat'.
     * 
     * @param operator
     * @param lat
     * @return
     */

    public Predicate<AisPacket> filterOnTargetLatitude(final CompareToOperator operator, Float lat) {
        final float rhsLat = lat;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsLat = getLatitude(mmsi); // Extract lat - if we know it
                return lhsLat != lhsLat /* NaN */? false : compare(lhsLat, rhsLat, operator);
            }

            public String toString() {
                return "lat " + operator + " " + rhsLat;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a latitude outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetLatitude(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsLat = getLatitude(mmsi); // Extract - if we know it
                return lhsLat != lhsLat /* NaN */? false : inRange(min, max, lhsLat);
            }

            public String toString() {
                return "lat in " + min + ".." + max;
            }
        };
    }

    // --- lon

    /**
     * Return false if this message is known to be related to a target with a longitude comparing false to 'lon'.
     * 
     * @param operator
     * @param lon
     * @return
     */

    public Predicate<AisPacket> filterOnTargetLongitude(final CompareToOperator operator, Float lon) {
        final float rhsLon = lon;
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsLon = getLongitude(mmsi); // Extract lon - if we know it
                return lhsLon != lhsLon /* NaN */? false : compare(lhsLon, rhsLon, operator);
            }

            public String toString() {
                return "lon " + operator + " " + rhsLon;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a longitude outside the given range.
     */

    public Predicate<AisPacket> filterOnTargetLongitude(final float min, final float max) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final float lhsLon = getLongitude(mmsi); // Extract cog - if we know it
                return lhsLon != lhsLon /* NaN */? false : inRange(min, max, lhsLon);
            }

            public String toString() {
                return "lon in " + min + ".." + max;
            }
        };
    }

    // ---

    /**
     * Return false if this message is known to be related to a target with a name not comparing to rhs parameter.
     * 
     * @return
     */

    public Predicate<AisPacket> filterOnTargetName(final CompareToOperator operator, final String rhsName) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final String lhsName = getName(mmsi); // Extract - if we know it
                return lhsName == null ? false : compare(lhsName, rhsName, operator);
            }

            public String toString() {
                return "name " + operator + " " + rhsName;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a name not comparing to rhs parameter.
     * 
     * @return
     */

    public Predicate<AisPacket> filterOnTargetNameMatch(final String pattern) {
        final String glob = preprocessExpressionString(pattern);
        return p -> {
            aisPacketStream.add(p); // Update state
            final int mmsi = getMmsi(p); // Get MMSI in question
            final String lhsName = getName(mmsi); // Extract - if we know it
            return lhsName != null && matchesGlob(preprocessAisString(lhsName), glob);
        };
    }

    // ---

    /**
     * Return false if this message is known to be related to a target with a callsign not comparing to rhs parameter.
     * 
     * @return
     */
    public Predicate<AisPacket> filterOnTargetCallsign(final CompareToOperator operator, final String rhsCallsign) {
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final String lhsCallsign = getCallsign(mmsi); // Extract - if we know it
                return lhsCallsign == null ? false : compare(lhsCallsign, rhsCallsign, operator);
            }

            public String toString() {
                return "cs " + operator + " " + rhsCallsign;
            }
        };
    }

    /**
     * Return false if this message is known to be related to a target with a callsign not comparing to rhs parameter.
     * 
     * @return
     */
    public Predicate<AisPacket> filterOnTargetCallsignMatch(final String pattern) {
        final String glob = preprocessExpressionString(pattern);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final String callsign = getCallsign(mmsi); // Extract - if we know it
                return callsign == null ? false : matchesGlob(preprocessAisString(callsign), glob);
            }

            public String toString() {
                return "cs ~ " + glob;
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
    public Predicate<AisPacket> filterOnTargetPositionWithin(final Area area) {
        requireNonNull(area);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                aisPacketStream.add(p); // Update state
                final int mmsi = getMmsi(p); // Get MMSI in question
                final AisPosition pos = getPosition(mmsi); // Extract - if we know it
                return (pos == null || pos.getGeoLocation() == null) ? false : area.contains(pos.getGeoLocation());
            }

            public String toString() {
                return "pos within " + area;
            }
        };
    }

    // ---

    /**
     * Extract mmsi no. from AisPacket
     * 
     * @param p
     * @return
     */
    private static int getMmsi(AisPacket p) {
        final AisMessage aisMessage = p.tryGetAisMessage();
        return aisMessage == null ? -1 : aisMessage.getUserId();
    }

    /**
     * Use target tracker to extract IMO no. based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private int getImo(int mmsi) {
        int imo = -1;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket[] staticPackets = target.getStaticPackets();
            if (staticPackets.length > 0 && staticPackets[0].tryGetAisMessage() instanceof AisMessage5) {
                imo = (int) ((AisMessage5) staticPackets[0].tryGetAisMessage()).getImo();
            }
        }
        return imo;
    }

    /**
     * Use target tracker to extract speed over ground based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private float getSog(int mmsi) {
        float sog = Float.NaN;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                sog = (float) (((AisPositionMessage) positionPacket.tryGetAisMessage()).getSog() / 10.0);
            }
        }
        return sog;
    }

    /**
     * Use target tracker to extract course over ground based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private float getCog(int mmsi) {
        float cog = Float.NaN;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                cog = (float) (((AisPositionMessage) positionPacket.tryGetAisMessage()).getCog() / 10.0);
            }
        }
        return cog;
    }

    /**
     * Use target tracker to extract true heading based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private int getHdg(int mmsi) {
        int hdg = -1;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                hdg = ((AisPositionMessage) positionPacket.tryGetAisMessage()).getTrueHeading();
            }
        }
        return hdg;
    }

    /**
     * Use target tracker to extract draught based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private float getDraught(int mmsi) {
        float draught = Float.NaN;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket[] staticPackets = target.getStaticPackets();
            if (staticPackets.length > 0 && staticPackets[0].tryGetAisMessage() instanceof AisMessage5) {
                draught = (float) (((AisMessage5) staticPackets[0].tryGetAisMessage()).getDraught() / 10.0);
            }
        }
        return draught;
    }

    /**
     * Use target tracker to extract latitude based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private float getLatitude(int mmsi) {
        float lat = Float.NaN;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                lat = (float) ((AisPositionMessage) positionPacket.tryGetAisMessage()).getPos().getLatitudeDouble();
            }
        }
        return lat;
    }

    /**
     * Use target tracker to extract latitude based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private float getLongitude(int mmsi) {
        float lon = Float.NaN;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                lon = (float) ((AisPositionMessage) positionPacket.tryGetAisMessage()).getPos().getLongitudeDouble();
            }
        }
        return lon;
    }

    /**
     * Use target tracker to extract position based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private AisPosition getPosition(int mmsi) {
        AisPosition pos = null;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                pos = ((AisPositionMessage) positionPacket.tryGetAisMessage()).getPos();
            }
        }
        return pos;
    }

    /**
     * Use target tracker to extract shiptype based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private int getShiptype(int mmsi) {
        int shiptype = -1;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket[] staticPackets = target.getStaticPackets();
            if (staticPackets.length > 0 && staticPackets[0].tryGetAisMessage() instanceof AisMessage5) {
                shiptype = ((AisMessage5) staticPackets[0].tryGetAisMessage()).getShipType();
            }
        }
        return shiptype;
    }

    /**
     * Use target tracker to extract shiptype based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private int getNavstat(int mmsi) {
        int navstat = -1;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket positionPacket = target.getPositionPacket();
            if (positionPacket != null && positionPacket.tryGetAisMessage() instanceof AisPositionMessage) {
                navstat = ((AisPositionMessage) positionPacket.tryGetAisMessage()).getNavStatus();
            }
        }
        return navstat;
    }

    /**
     * Use target tracker to extract ship name based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private String getName(int mmsi) {
        String name = null;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket[] staticPackets = target.getStaticPackets();
            if (staticPackets.length > 0 && staticPackets[0].tryGetAisMessage() instanceof AisMessage5) {
                name = ((AisMessage5) staticPackets[0].tryGetAisMessage()).getName();
            }
        }
        return name;
    }

    /**
     * Use target tracker to extract ship's callsign based on mmsi lookup.
     * 
     * @param mmsi
     * @return
     */
    private String getCallsign(int mmsi) {
        String cs = null;
        TargetInfo target = targetTracker.get(mmsi);
        if (target != null) {
            AisPacket[] staticPackets = target.getStaticPackets();
            if (staticPackets.length > 0 && staticPackets[0].tryGetAisMessage() instanceof AisMessage5) {
                cs = ((AisMessage5) staticPackets[0].tryGetAisMessage()).getCallsign();
            }
        }
        return cs;
    }
}
