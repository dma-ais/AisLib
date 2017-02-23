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

package dk.dma.ais.tracker.eventEmittingTracker;

import com.google.common.collect.ImmutableList;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Target;
import dk.dma.commons.util.DateTimeUtil;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Comparator.comparingLong;

/**
 * The Track class contains the consolidated information known about a given target - likely as the result
 * of several received AIS messages.
 *
 * Track supports dynamic storage of track properties via the getProperty()/setProperty() methods, but
 * also contains some business logic and knowledge of what it stores; e.g. via the getMmsi(), and get/setPosition()
 * methods.
 *
 * Information which is available directly in AisPackets is stored and retrieved from these. Other information,
 * such as cell-id's, event booking-keeping, etc. is set dynamically as custom properties.
 *
 */
@ThreadSafe
public final class Track extends Target implements Cloneable {

    public static final String CELL_ID = "cellId";
    public static final String SAFETY_ZONE = "safetyZone";
    public static final String EXTENT = "extent";

    private Lock trackLock = new ReentrantLock();

    private static final Comparator<TrackingReport> byTimestamp = comparingLong(TrackingReport::getTimestamp);
    private static final Supplier<TreeSet<TrackingReport>> treeSetSupplier = () -> new TreeSet<>(byTimestamp);
    private boolean positionReportPurgeEnable = true;

    public final static int MAX_AGE_POSITION_REPORTS_MINUTES = 20;

    @GuardedBy("trackLock")
    private TreeSet<TrackingReport> trackingReports = treeSetSupplier.get();

    @GuardedBy("trackLock")
    private final Map<String, Object> properties = new HashMap<>(3);

    /** The last received AIS packet of type 5 */
    @GuardedBy("trackLock")
    private AisPacket lastStaticReport;

    /** Cached value of ship type from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer shipType;

    /** Cached value of ship name from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private String shipName;

    /** Cached value of ship callsign from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private String callsign;

    /** Cached value of IMO no. lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer imo;

    /** Cached value of dimension A from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer shipDimensionBow;

    /** Cached value of dimension B from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer shipDimensionStern;

    /** Cached value of dimension C from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer shipDimensionPort;

    /** Cached value of dimension D from lastStaticReport - for faster reads */
    @GuardedBy("trackLock")
    private Integer shipDimensionStarboard;

    @GuardedBy("trackLock")
    private long timeOfLastUpdate = 0L;

    @GuardedBy("trackLock")
    private long timeOfLastPositionReport = 0L;

    /**
     * Create a new track with the given MMSI no.
     * @param mmsi the MMSI no. assigned to this track.
     */
    public Track(int mmsi) {
        super(mmsi);
    }

    /**
     * Deep copy/clone a track
     */
    @Override
    public Track clone() throws CloneNotSupportedException {
        Track clone = (Track) super.clone();

        // Do not share locks
        clone.trackLock = new ReentrantLock();

        // Deep copy collection
        TreeSet<TrackingReport> trackingReportsClone = treeSetSupplier.get();
        clone.trackingReports.forEach(tr -> trackingReportsClone.add(tr));
        clone.trackingReports = trackingReportsClone;

        return clone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Track{");
        sb.append("positionReportPurgeEnable=").append(positionReportPurgeEnable);
        sb.append(", trackingReports=").append(trackingReports);
        sb.append(", properties=").append(properties);
        sb.append(", lastStaticReport=").append(lastStaticReport);
        sb.append(", shipType=").append(shipType);
        sb.append(", shipName='").append(shipName).append('\'');
        sb.append(", callsign='").append(callsign).append('\'');
        sb.append(", imo=").append(imo);
        sb.append(", shipDimensionBow=").append(shipDimensionBow);
        sb.append(", shipDimensionStern=").append(shipDimensionStern);
        sb.append(", shipDimensionPort=").append(shipDimensionPort);
        sb.append(", shipDimensionStarboard=").append(shipDimensionStarboard);
        sb.append(", timeOfLastUpdate=").append(timeOfLastUpdate);
        sb.append(", timeOfLastPositionReport=").append(timeOfLastPositionReport);
        sb.append(", mmsi=").append(getMmsi());
        sb.append(", timeOfLastAisTrackingReport=").append(getTimeOfLastAisTrackingReport());
        sb.append(", oldestTrackingReport=").append(getOldestTrackingReport());
        sb.append(", newestTrackingReport=").append(getNewestTrackingReport());
        sb.append(", IMO=").append(getIMO());
        sb.append(", vesselLength=").append(getVesselLength());
        sb.append(", vesselBeam=").append(getVesselBeam());
        sb.append(", position=").append(getPosition());
        sb.append(", speedOverGround=").append(getSpeedOverGround());
        sb.append(", courseOverGround=").append(getCourseOverGround());
        sb.append(", trueHeading=").append(getTrueHeading());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = (positionReportPurgeEnable ? 1 : 0);
        result = 31*result + (trackingReports != null ? trackingReports.hashCode() : 0);
        result = 31*result + (properties != null ? properties.hashCode() : 0);
        result = 31*result + (lastStaticReport != null ? lastStaticReport.hashCode() : 0);
        result = 31*result + (shipType != null ? shipType.hashCode() : 0);
        result = 31*result + (shipName != null ? shipName.hashCode() : 0);
        result = 31*result + (callsign != null ? callsign.hashCode() : 0);
        result = 31*result + (imo != null ? imo.hashCode() : 0);
        result = 31*result + (shipDimensionBow != null ? shipDimensionBow.hashCode() : 0);
        result = 31*result + (shipDimensionStern != null ? shipDimensionStern.hashCode() : 0);
        result = 31*result + (shipDimensionPort != null ? shipDimensionPort.hashCode() : 0);
        result = 31*result + (shipDimensionStarboard != null ? shipDimensionStarboard.hashCode() : 0);
        result = 31*result + (int) (timeOfLastUpdate ^ (timeOfLastUpdate >>> 32));
        result = 31*result + (int) (timeOfLastPositionReport ^ (timeOfLastPositionReport >>> 32));
        return result;
    }

    /*
     * Update this track with a new AisPacket. The MMSI no. inside the packet must match this Track's MMSI no.
     * and in order to maintain low insertion-cost only packets newer than the previously received are accepted.
     *
     * This update will be treated as an update received from a real AIS source and the packet will be stored for
     * a period of time to support replay.
     *
     * @param p
     */
    public void update(AisPacket p) {
        checkAisPacket(p);
        AisMessage msg = p.tryGetAisMessage();
        if (msg instanceof AisStaticCommon) {
            addStaticReport(p);
        }
        if (msg instanceof IVesselPositionMessage) {
            addTrackingReport(new AisTrackingReport(p));
        }
    }

    /**
     * Update this track with a new AisPacket. The MMSI no. inside the packet must match this Track's MMSI no.
     * and in order to maintain low insertion-cost only packets newer than the previously received are accepted.
     *
     * This update is treated as an interpolated (artifical, non-real) update.
     *
     * @param m
     */
    public void update(long timestamp, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            IVesselPositionMessage pm = (IVesselPositionMessage) m;
            update(timestamp, pm.getValidPosition(), (float) (pm.getCog() / 10.0), (float) (pm.getSog() / 10.0), pm.getTrueHeading());
        }
    }

    /**
     * Update this track with interpolated or predicted information (as opposed to information
     * received from an AIS receiver or basestation).
     */
    public void update(long timestamp, Position position, float cog, float sog, float hdg) {
        InterpolatedTrackingReport trackingReport = new InterpolatedTrackingReport(timestamp, position, cog, sog, hdg);
        addTrackingReport(trackingReport);
    }

    /**
     * Get a custom property previously set on this track.
     * @param propertyName
     * @return
     */
    public Object getProperty(String propertyName) {
        return threadSafeGetStaticData(() -> properties.get(propertyName));
    }

    /**
     * Attach/set a custom property on this track.
     *
     * @param propertyName
     * @param propertyValue
     */
    public void setProperty(String propertyName, Object propertyValue) {
        try {
            trackLock.lock();
            properties.put(propertyName, propertyValue);
        } finally {
            trackLock.unlock();
        }
    }

    /**
     * Remove a custom property previously set on this track.
     *
     * @param propertyName
     */
    public void removeProperty(String propertyName) {
        try {
            trackLock.lock();
            properties.remove(propertyName);
        } finally {
            trackLock.unlock();
        }
    }

    /** Get the timestamp of the last update of any type. */
    public long getTimeOfLastUpdate() {
        return threadSafeGetStaticData(() -> timeOfLastUpdate);
    }

    /** Get the timestamp of the last update of any type. */
    public LocalDateTime getTimeOfLastUpdateTyped() {
        return DateTimeUtil.MILLIS_TO_LOCALDATETIME_UTC.apply(getTimeOfLastUpdate());
    }

    /** Get the timestamp of the last position report. */
    public long getTimeOfLastPositionReport() {
        return threadSafeGetStaticData(() -> timeOfLastPositionReport);
    }

    /** Get the timestamp of the last position report. */
    public LocalDateTime getTimeOfLastPositionReportTyped() {
        return DateTimeUtil.MILLIS_TO_LOCALDATETIME_UTC.apply(getTimeOfLastPositionReport());
    }

    /** Get time timestamp of the last non-predicted position report */
    public long getTimeOfLastAisTrackingReport() {
        try {
            trackLock.lock();
            return trackingReports
                .stream()
                .filter(tr -> tr instanceof AisTrackingReport)
                .max(Comparator.comparing(tr -> tr.getTimestamp()))
                .get()
                .getTimestamp();
        } catch(NoSuchElementException e) {
            return -1;
        } finally {
            trackLock.unlock();
        }
    }

    /** Get time timestamp of the last non-predicted position report */
    public LocalDateTime getTimeOfLastAisTrackingReportTyped() {
        return DateTimeUtil.MILLIS_TO_LOCALDATETIME_UTC.apply(getTimeOfLastAisTrackingReport());
    }

    /** Return the last received static report (if any) */
    public AisPacket getLastStaticReport() {
        return threadSafeGetStaticData(() -> lastStaticReport);
    }

    /**
     * Enable or disable purging of old position reports from the track.
     * @param positionReportPurgeEnable
     */
    public void setPositionReportPurgeEnable(boolean positionReportPurgeEnable) {
        this.positionReportPurgeEnable = positionReportPurgeEnable;
    }

    /** Update the track with a new static report */
    private void addStaticReport(AisPacket p) {
        AisStaticCommon msg = (AisStaticCommon) p.tryGetAisMessage();
        try {
            trackLock.lock();

            lastStaticReport = p;
            timeOfLastUpdate = p.getBestTimestamp();
            callsign = msg.getCallsign();
            shipType = msg.getShipType();
            shipName = msg.getName();
            shipDimensionBow = msg.getDimBow();
            shipDimensionStern = msg.getDimStern();
            shipDimensionPort = msg.getDimPort();
            shipDimensionStarboard = msg.getDimStarboard();
            if (msg instanceof AisMessage5) {
                imo = (int) ((AisMessage5) msg).getImo();
            }
        } finally {
          trackLock.unlock();
        }
    }

    /** Update the track with a new trackingReport */
    private void addTrackingReport(TrackingReport trackingReport) {
        try {
            trackLock.lock();

            trackingReports.add(trackingReport);
            timeOfLastUpdate = trackingReport.getTimestamp();
            timeOfLastPositionReport = trackingReports.last().getTimestamp();
        } finally {
            trackLock.unlock();
        }

        purgeTrackingReports(MAX_AGE_POSITION_REPORTS_MINUTES);
    }

    /** Get the oldest reported position report kept. */
    public TrackingReport getOldestTrackingReport() {
        TrackingReport oldestTrackingReport = null;
        try {
            trackLock.lock();
            oldestTrackingReport = trackingReports.first();
        } catch(NoSuchElementException e)  {
        } finally {
            trackLock.unlock();
        }
        return oldestTrackingReport;
    }

    /** Get the most recently reported position report. */
    public TrackingReport getNewestTrackingReport() {
        TrackingReport mostRecentTrackingReport = null;
        try {
            trackLock.lock();
            mostRecentTrackingReport = trackingReports.last();
        } catch(NoSuchElementException e)  {
        } finally {
            trackLock.unlock();
        }
        return mostRecentTrackingReport;
    }

    /** Get the the position report at time t. */
    public TrackingReport getTrackingReportAt(long t) {
        TrackingReport trackingReportAtT = null;
        try {
            trackLock.lock();
            Iterator<TrackingReport> iterator = trackingReports.iterator();
            while(iterator.hasNext()) {
                trackingReportAtT = iterator.next();
                if (trackingReportAtT.getTimestamp() == t) {
                    break;
                }
                trackingReportAtT = null;
            }
        } finally {
            trackLock.unlock();
        }
        return trackingReportAtT;
    }

    /**
     * Get a trail of historic position reports. The oldest will be a maximum of MAX_AGE_POSITION_REPORTS_MINUTES
     * minutes older than the newest (if position report purging is enabled; otherwise there is no limit).
     * @return
     */
    public List<TrackingReport> getTrackingReports() {
        purgeTrackingReports(MAX_AGE_POSITION_REPORTS_MINUTES);
        ImmutableList<TrackingReport> trackingReports1 = null;
        try {
            trackLock.lock();
            trackingReports1 = ImmutableList.copyOf(trackingReports);
        } finally {
            trackLock.unlock();
        }

        return trackingReports1;
    }

    /**
     * Purge position reports older than the given amount of minutes, compared to the most
     * recent stored position report.
     * @return
     */
    private void purgeTrackingReports(int maxAgeMinutes) {
        if (positionReportPurgeEnable) {
            TrackingReport newestTrackingReport = getNewestTrackingReport();
            if (newestTrackingReport != null) {
                long now = newestTrackingReport.getTimestamp();
                long oldestKept = now - maxAgeMinutes * 60 * 1000;

                trackLock.lock();
                try {
                    trackingReports.removeIf(tr -> tr.getTimestamp() < oldestKept);
                } finally {
                    trackLock.unlock();
                }
            }
        }
    }

    /**
     * Predict this track's position forward to time atTime.
     * @param atTime timestamp in milliseconds since the Epoch
     */
    public void predict(long atTime) {
        long now = getTimeOfLastPositionReport();

        if (atTime > now) {
            TrackingReport trackingReport = getNewestTrackingReport();
            if (trackingReport != null) {
                Position currentPosition = trackingReport.getPosition();
                float cog = trackingReport.getCourseOverGround();
                float sog = trackingReport.getSpeedOverGround();
                float hdg = trackingReport.getTrueHeading();

                long deltaMillis = atTime - now;
                float deltaSeconds = deltaMillis / 1000;
                float deltaMinutes = deltaSeconds / 60;
                float deltaHours = deltaMinutes / 60;
                float distanceNauticalMiles = sog * deltaHours;
                float distanceMeters = distanceNauticalMiles * 1852;
                Position predictedPosition = CoordinateSystem.CARTESIAN.pointOnBearing(currentPosition, distanceMeters, cog);

                addTrackingReport(new InterpolatedTrackingReport(atTime, predictedPosition, cog, sog, hdg /* TODO should be PredictedTrackingReport */));
            } else {
                throw new IllegalStateException("No enough data to predict future position.");
            }
        } else {
            throw new IllegalArgumentException("atTime (" + atTime + ") should be ahead of track's last position update (" + now + ").");
        }
    }

    private void checkAisPacket(AisPacket p) {
        AisMessage msg = p.tryGetAisMessage();
        if (msg == null) {
            throw new IllegalArgumentException("Cannot extract AisMessage from AisPacket: " + p.toString());
        }
        if (msg.getUserId() != getMmsi()) {
            throw new IllegalArgumentException("This track only accepts updates for MMSI " + getMmsi() + ", not " + msg.getUserId() + ".");
        }
    }

    /** Convenience method to return track's last reported ship type or null */
    public Integer getShipType() {
        return threadSafeGetStaticData(() -> shipType);
    }

    /** Convenience method to return track's last reported ship name or null */
    public String getShipName() {
        return threadSafeGetStaticData(() -> shipName);
    }

    /** Convenience method to return track's last reported callsign or null */
    public String getCallsign() {
        return threadSafeGetStaticData(() -> callsign);
    }

    /** Convenience method to return track's last reported IMO no. or null */
    public Integer getIMO() {
        return threadSafeGetStaticData(() -> imo);
    }

    /** Convenience method to return track's last reported vessel length or null */
    public Integer getVesselLength() {
        return shipDimensionBow == null || shipDimensionStern == null ? null : shipDimensionBow + shipDimensionStern;
    }

    /** Convenience method to return track's last reported vessel beam or null */
    public Integer getVesselBeam() {
        return shipDimensionPort == null || shipDimensionStarboard == null ? null : shipDimensionPort + shipDimensionStarboard;
    }

    public Integer getShipDimensionBow() {
        return threadSafeGetStaticData(() -> shipDimensionBow);
    }

    public Integer getShipDimensionStern() {
        return threadSafeGetStaticData(() -> shipDimensionStern);
    }

    public Integer getShipDimensionPort() {
        return threadSafeGetStaticData(() -> shipDimensionPort);
    }

    public Integer getShipDimensionStarboard() {
        return threadSafeGetStaticData(() -> shipDimensionStarboard);
    }

    /** Convenience method to return track's last reported position or null */
    public Position getPosition() {
        return nullAndThreadSafeGetFromNewestTrackingReport(tp -> tp.getPosition());
    }

    /** Convenience method to return track's last reported SOG or null */
    public Float getSpeedOverGround() {
        return nullAndThreadSafeGetFromNewestTrackingReport(tp -> tp.getSpeedOverGround());
    }

    /** Convenience method to return track's last reported COG or null */
    public Float getCourseOverGround() {
        return nullAndThreadSafeGetFromNewestTrackingReport(tp -> tp.getCourseOverGround());
    }

    /** Convenience method to return track's last reported heading or null */
    public Float getTrueHeading() {
        return nullAndThreadSafeGetFromNewestTrackingReport(tp -> tp.getTrueHeading());
    }

    private <T> T threadSafeGetStaticData(Supplier<T> getter) {
        T value = null;
        try {
            trackLock.lock();
            value = getter.get();
        } finally {
            trackLock.unlock();
        }
        return value;
    }

    private <T> T nullAndThreadSafeGetFromNewestTrackingReport(Function<? super TrackingReport, T> getter) {
        T value = null;
        try {
            trackLock.lock();
            TrackingReport tp = getNewestTrackingReport();
            if (tp != null) {
                try {
                    value = getter.apply(tp);
                } catch (NullPointerException e) {
                }
            }
        } finally {
            trackLock.unlock();
        }
        return value;
    }

}
