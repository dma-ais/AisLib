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

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.tracker.Target;
import dk.dma.ais.tracker.eventEmittingTracker.events.CellChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.PositionChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.TimeEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.TrackStaleEvent;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;
import dk.dma.enav.model.geometry.grid.Cell;
import dk.dma.enav.model.geometry.grid.Grid;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * EventEmittingTracker is a tracker which receives a (potentially never-ending) series of AisPackets. It uses these
 * to maintain in-memory a complete state of the "situation" (e.g. positions, speeds, static information,
 * and historic movements) of the targets it has detected.
 *
 * The tracker will emit events to its subscribes when certain tracking related events occur. Examples of such
 * events are cell changes (a target entered a new grid cell), position changes, track goes stale (has not been
 * updated for a duration of time), heart beats (TimeEvents).
 *
 * The heart beat mechanism is based on timestamps from the data stream and therefore quite unprecise.
 * Significant slack must be expected.
 */
@ThreadSafe
public class EventEmittingTrackerImpl implements EventEmittingTracker {

    static final Logger LOG = LoggerFactory.getLogger(EventEmittingTrackerImpl.class);
    {
        LOG.info(this.getClass().getSimpleName() + " created (" + this + ").");
    }

    private final EventBus eventBus = new EventBus();

    @GuardedBy("tracksLock")
    final HashMap<Integer, Track> tracks = new HashMap<>(256);
    private final Lock tracksLock = new ReentrantLock();

    final Grid grid;

    static final long TRACK_STALE_MILLIS = Duration.of(30, MINUTES).toMillis();
    static final long TRACK_INTERPOLATION_REQUIRED_MILLIS = Duration.of(30, SECONDS).toMillis();
    static final long INTERPOLATION_TIME_STEP_MILLIS = Duration.of(10, SECONDS).toMillis();

    /**
     * A set of mmsi no.'s for which no messages are processed.
     */
    private final TreeSet<Integer> mmsiBlacklist = new TreeSet<>();

    /**
     * A set of mmsi no.'s for which to output detailed debugging/observation data.
     */
    private final Set<Integer> mmsiToObserve = new TreeSet<>();

    /**
     * A simple counter which is used to reduce CPU load by only performing more complicated
     * calculations when this markTrigger has reached certain values.
     */
    private int markTrigger;

    /**
     * The last hour-of-the-day when a timestamp message was log to the LOG.
     */
    private int markLastHourLogged = -1;

    /**
     * The time since Epoch when the most recent TimeEvent was posted to the EventBus.
     */
    private long lastTimeEventMillis = 0;

    /**
     * The approximate no. of milliseconds between each TimeEvent.
     */
    static final int TIME_EVENT_PERIOD_MILLIS = 1000;

    /**
     * Initialize the tracker.
     * @param grid
     */
    public EventEmittingTrackerImpl(Grid grid) {
        this(grid, null);
    }

    /**
     * Initialize the tracker.
     * @param grid
     */
    public EventEmittingTrackerImpl(Grid grid, int... blackListedMmsis) {
        this.grid = grid;

        if (blackListedMmsis != null) {
            for (int blackListedMmsi : blackListedMmsis) {
                this.mmsiBlacklist.add(blackListedMmsi);
            }
        }

        eventBus.register(this); // to catch dead events
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AisPacket packet) {
        performUpdate(packet.getBestTimestamp(), packet.tryGetAisMessage(), track -> track.update(packet));
    }

    @Override
    public Target get(int mmsi) {
        Track track;
        tracksLock.lock();
        try {
            track = tracks.get(mmsi);
        } finally {
            tracksLock.unlock();
        }
        return track;
    }

    void update(long timeOfCurrentUpdate, AisMessage aisMessage) {
        performUpdate(timeOfCurrentUpdate, aisMessage, track -> track.update(timeOfCurrentUpdate, aisMessage));
    }

    private void performUpdate(long timeOfCurrentUpdate, AisMessage aisMessage, Consumer<Track> trackUpdater) {
        final int mmsi = aisMessage.getUserId();

        if (LOG.isDebugEnabled()) {
            IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
            if (sourceTag != null) {
                LOG.debug("Received " + sourceTag.getTimestamp() + " " + aisMessage);
            } else {
                LOG.debug("Received " + aisMessage);
            }
        }

        if (! isOnBlackList(mmsi)) {
            if (isOnObservationList(mmsi)) {
                outputMessageSummary(aisMessage);
            }

            final AisTargetType targetType = aisMessage.getTargetType();
            if (targetType == AisTargetType.A || targetType == AisTargetType.B) {
                Track track = getOrCreateTrack(mmsi);

                long timeOfLastUpdate = track.getTimeOfLastUpdate();
                long timeOfLastPositionUpdate = track.getTimeOfLastPositionReport();

                // Rebirth track if stale
                if (isTrackStale(timeOfLastUpdate, timeOfLastPositionUpdate, timeOfCurrentUpdate)) {
                    removeTrack(mmsi);
                    track = getOrCreateTrack(mmsi);
                    timeOfLastUpdate = 0L;
                    timeOfLastPositionUpdate = 0L;
                }

                if (aisMessage instanceof IVesselPositionMessage) {
                    IVesselPositionMessage positionMessage = (IVesselPositionMessage) aisMessage;

                    if (positionMessage.getPos().getGeoLocation() != null) {
                        if (isInterpolationRequired(timeOfLastPositionUpdate, timeOfCurrentUpdate)) {
                            interpolateTrackUpToNewMessage(track, timeOfCurrentUpdate, aisMessage);
                        }

                        TrackingReport oldTrackingReport = track.getNewestTrackingReport();
                        trackUpdater.accept(track);
                        track.setProperty(Track.CELL_ID, grid.getCell(positionMessage.getPos().getGeoLocation()).getCellId());
                        TrackingReport newTrackingReport = track.getNewestTrackingReport();
                        firePositionRelatedEvents(track, oldTrackingReport, newTrackingReport);
                    }
                } else {
                    trackUpdater.accept(track);
                }
            }
        }

        mark(timeOfCurrentUpdate);
        tryFireTimeEvent(timeOfCurrentUpdate);
    }

    private void firePositionRelatedEvents(Track track, TrackingReport oldTrackingReport, TrackingReport newTrackingReport) {
        Position oldPosition = null;
        Cell oldCell = null;
        if (oldTrackingReport != null) {
            oldPosition = oldTrackingReport.getPosition();
            oldCell = grid.getCell(oldPosition);
        }

        Position newPosition = newTrackingReport.getPosition();
        Cell newCell = grid.getCell(newPosition);

        if (hasChanged(oldPosition, newPosition)) {
            eventBus.post(new PositionChangedEvent(track, oldPosition));
        }
        if (hasChanged(oldCell, newCell)) {
            eventBus.post(new CellChangedEvent(track, oldCell == null ? null : oldCell.getCellId()));
        }
    }

    private static <T> boolean hasChanged(T oldValue, T newValue) {
        boolean hasChanged;
        if (oldValue == null) {
            hasChanged = newValue != null;
        } else {
            hasChanged = !oldValue.equals(newValue);
        }
        return hasChanged;
    }

    private static void outputMessageSummary(AisMessage aisMessage) {
        if (aisMessage instanceof IVesselPositionMessage) {
            IVesselPositionMessage positionMessage = (IVesselPositionMessage) aisMessage;
            System.out.println(/*p.getBestTimestamp() + ": " + */ aisMessage.getUserId() + ": " + positionMessage.getSog() / 10.0 + " kts");
        }
    }

    private void interpolateTrackUpToNewMessage(Track track, long timestamp, AisMessage message) {
        if (! (message instanceof IVesselPositionMessage)) {
            throw new IllegalArgumentException();
        }
        IVesselPositionMessage posMessage = (IVesselPositionMessage) message;

        Position p1 = track.getPosition();
        long t1 = track.getNewestTrackingReport().getTimestamp();
        Position p2 = posMessage.getPos().getGeoLocation();
        long t2 = timestamp;

        Map<Long, Position> interpolatedPositions = calculateInterpolatedPositions(PositionTime.create(p1, t1), PositionTime.create(p2, t2));

        interpolatedPositions.forEach((t, p) -> {
            Position oldPosition = track.getPosition();
            track.update(t, p, (float) (posMessage.getCog() / 10.0), (float) (posMessage.getSog() / 10.0), posMessage.getTrueHeading());
            eventBus.post(new PositionChangedEvent(track, oldPosition));
        });
    }

    /**
     * Calculate a map of <timestamp, Position>-pairs which are interpolated positions at regular, fixed time-intervals
     * between the two positions p1 and p2 known at time t1 and t2 respectively.
     *
     * The set of interpolated positions contains positions up to - but not including - t2/p2.
     *
     * @param pt1 The first of the two positions to interpolate between.
     * @param pt2 The last of the two positions to interpolate between.
     * @return a Map of interpolated positions
     */
    static final Map<Long, Position> calculateInterpolatedPositions(PositionTime pt1, PositionTime pt2) {
        TreeMap<Long, Position> interpolatedPositions = new TreeMap<>();

        if (pt2.getTime() < pt1.getTime()) {
            LOG.error("Cannot interpolate backwards: " + pt1.getTime() + " " + pt2.getTime());
            return interpolatedPositions;
        }

        final long t1 = pt1.getTime();
        final long t2 = pt2.getTime();

        for (long t = t1 + INTERPOLATION_TIME_STEP_MILLIS; t < t2; t += INTERPOLATION_TIME_STEP_MILLIS) {
            interpolatedPositions.put(t, PositionTime.createInterpolated(pt1, pt2, t));
        }

        return interpolatedPositions;
    }

    static boolean isTrackStale(long lastAnyUpdate, long lastPositionUpdate, long currentUpdate) {
        long lastUpdate = Math.max(lastAnyUpdate, lastPositionUpdate);
        boolean trackStale = lastUpdate > 0L && currentUpdate - lastUpdate >= TRACK_STALE_MILLIS;
        if (trackStale) {
            LOG.debug("Track is stale (" + currentUpdate + ", " + lastUpdate + ")");
        }
        return trackStale;
    }

    static boolean isInterpolationRequired(long lastPositionUpdate, long currentPositionUpdate) {
        return lastPositionUpdate > 0L && currentPositionUpdate - lastPositionUpdate >= TRACK_INTERPOLATION_REQUIRED_MILLIS;
    }

    private void removeTrack(int mmsi) {
        tracksLock.lock();
        try {
            Track track = tracks.get(mmsi);
            tracks.remove(mmsi);
            eventBus.post(new TrackStaleEvent(track));
        } finally {
            tracksLock.unlock();
        }
    }

    private Track getOrCreateTrack(int mmsi) {
        Track track;
        tracksLock.lock();
        try {
            track = tracks.get(mmsi);
            if (track == null) {
                track = new Track(mmsi);
                tracks.put(mmsi, track);
            }
        } finally {
            tracksLock.unlock();
        }
        return track;
    }

    /**
     * {@inheritDoc}
     */
    @Subscribe
    @SuppressWarnings("unused")
    public void listen(DeadEvent event) {
        LOG.trace("No subscribers were interested in this event: " + event.getEvent());
    }

    /**
     * {@inheritDoc}
     */
    public void registerSubscriber(Object subscriber) {
        eventBus.register(subscriber);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Track> getTracks() {
        tracksLock.lock();
        Collection<Track> trackCollection = null;
        try {
            trackCollection = tracks.values();
        } finally {
            tracksLock.unlock();
        }
        return trackCollection;
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        int n;

        tracksLock.lock();
        try {
            n = tracks.size();
        } finally {
            tracksLock.unlock();
        }

        return n;
    }

    /**
     * {@inheritDoc}
     * @deprecated Use #size() instead
     */
    public int getNumberOfTracks() {
        return size();
    }

    /**
     * Check if this MMSI no. is on the list of vessels to be closely observed (for programming and debugging purposes).
     * @param mmsi
     * @return true if the MMSO is on the observation list; false if not.
     */
    private final boolean isOnObservationList(int mmsi) {
        return mmsiToObserve.contains(mmsi);
    }

    /**
     * Check if this MMSI no. corresponds to a black listed vessel.
     * @param mmsi
     * @return true if the MMSI is black listed; false otherwise.
     */
    private final boolean isOnBlackList(int mmsi) {
        return mmsiBlacklist.contains(mmsi);
    }

    /**
     * Occasionally write a mark to show how far we have come in the data stream.
     *
     * @param timestampMillis current time.
     */
    private void mark(long timestampMillis) {
        if ((markTrigger & 0xffff) == 0) {
            Instant instant = Instant.ofEpochMilli(timestampMillis);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            int t = dateTime.get(ChronoField.HOUR_OF_DAY);
            if (t != markLastHourLogged) {
                markLastHourLogged = t;
                LOG.info("Now processing stream at time " + dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
            }
        }
        markTrigger++;
    }

    /**
     * Fire a TimeEvent event to the EventBus if at least TIME_EVENT_PERIOD_MILLIS msecs have
     * passed since the last TimeEvent was emitted.
     *
     * @param timestampMillis current time.
     */
    private void tryFireTimeEvent(long timestampMillis) {
        long millisSinceLastTimeEvent = timestampMillis - lastTimeEventMillis;

        if (millisSinceLastTimeEvent >= TIME_EVENT_PERIOD_MILLIS) {
            TimeEvent timeEvent = new TimeEvent(Instant.ofEpochMilli(timestampMillis), lastTimeEventMillis == 0 ? null : Duration.ofMillis(millisSinceLastTimeEvent));
            eventBus.post(timeEvent);
            lastTimeEventMillis = timestampMillis;
            if (LOG.isDebugEnabled()) {
                LOG.debug("TimeEvent emitted at time " + timeEvent.getTimestamp() + " msecs (" + timeEvent.getMillisSinceLastMark() + " msecs since last).");
            }
        }
    }
}
