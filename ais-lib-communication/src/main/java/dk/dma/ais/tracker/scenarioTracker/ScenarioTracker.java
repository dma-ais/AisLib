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
package dk.dma.ais.tracker.scenarioTracker;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Tracker;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class can process a finite stream of AisPackets, and build a scenario
 * consisting of all received targets and a history of their movements.
 *
 * @author Thomas Borg Salling
 */
@NotThreadSafe
public class ScenarioTracker implements Tracker {

    /**
     * Get the Date of the first update in this scenario.
     * @return
     */
    public Date scenarioBegin() {
        Date scenarioBegin = null;
        Set<Map.Entry<Integer, Target>> entries = targets.entrySet();
        Iterator<Map.Entry<Integer, Target>> i = entries.iterator();
        while (i.hasNext()) {
            Target target = i.next().getValue();
            try {
                Date targetFirstUpdate = target.positionReports.firstKey();
                if (scenarioBegin == null || targetFirstUpdate.before(scenarioBegin)) {
                    scenarioBegin = targetFirstUpdate;
                }
            } catch (NoSuchElementException e) {
            }
        }
        return scenarioBegin;
    }

    /**
     * Get the Date of the last update in this scenario.
     * @return
     */
    public Date scenarioEnd() {
        Date scenarioEnd = null;
        Set<Map.Entry<Integer, Target>> entries = targets.entrySet();
        Iterator<Map.Entry<Integer, Target>> i = entries.iterator();
        while (i.hasNext()) {
            Target target = i.next().getValue();
            try {
                Date targetFirstUpdate = target.positionReports.lastKey();
                if (scenarioEnd == null || targetFirstUpdate.after(scenarioEnd)) {
                    scenarioEnd = targetFirstUpdate;
                }
            } catch (NoSuchElementException e) {
            }
        }
        return scenarioEnd;
    }

    /**
     * Get bounding box containing all movements in this scenario.
     * @return
     */
    public BoundingBox boundingBox() {
        return boundingBox;
    }

    /**
     * Return all targets involved in this scenario.
     * @return
     */
    public ImmutableSet<Target> getTargets() {
        return ImmutableSet.copyOf(targets.values());
    }

    @Override
    public dk.dma.ais.tracker.Target get(int mmsi) {
        return targets.get(mmsi);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return targets.size();
    }

    /**
     * Return all targets involved in this scenario and with a known location (ie. located inside of the bounding box).
     * @return
     */
    public Set<Target> getTargetsHavingPositionUpdates() {
        return Sets.filter(getTargets(), new com.google.common.base.Predicate<Target>() {
            @Override
            public boolean apply(@Nullable Target target) {
                return target.hasPosition();
            }
        });
    }

    public void update(AisPacket p) {
        AisMessage message;
        try {
            message = p.getAisMessage();
            int mmsi = message.getUserId();
            Target target;
            if (! targets.containsKey(mmsi)) {
                target = new Target(mmsi);
                targets.put(mmsi, target);
            } else {
                target = targets.get(mmsi);
            }
            if (message instanceof IVesselPositionMessage) {
                updateBoundingBox((IVesselPositionMessage) message);
            }
            target.update(p);
        } catch (AisMessageException | SixbitException e) {
            // fail silently on unparsable packets 
            //e.printStackTrace();
        }
    }

    public void tagTarget(int mmsi, Object tag) {
        targets.get(mmsi).setTag(tag);
    }

    private final Map<Integer, Target> targets = new TreeMap<>();

    private BoundingBox boundingBox;

    private void updateBoundingBox(IVesselPositionMessage positionMessage) {
        if (positionMessage.isPositionValid()) {
            Position position = positionMessage.getValidPosition();
            if (position != null) {
                if (boundingBox == null) {
                    boundingBox = BoundingBox.create(position, position, CoordinateSystem.CARTESIAN);
                } else {
                    boundingBox = boundingBox.include(BoundingBox.create(position, position, CoordinateSystem.CARTESIAN));
                }
            }
        }
    }

    private static String aisStringToJavaString(String aisString) {
        return aisString.replace('@',' ').trim();
    }

    @NotThreadSafe
    public final class Target extends dk.dma.ais.tracker.Target implements Cloneable {

        public Target(int mmsi) {
            super(mmsi);
        }

        public String getName() {
            return StringUtils.isBlank(name) ? String.valueOf(getMmsi()) : name;
        }

        public int getImo() {
            return imo;
        }

        public String getDestination() {
            return destination == null ? "" : destination;
        }

        public ShipTypeCargo getShipTypeCargo() { return shipTypeCargo; }

        public String getCargoTypeAsString() {
            return shipTypeCargo == null ? null : shipTypeCargo.prettyCargo();
        }

        public String getShipTypeAsString() {
            return shipTypeCargo == null ? null : shipTypeCargo.prettyType();
        }

        public int getToBow() {
            return toBow;
        }

        public int getToStern() {
            return toStern;
        }

        public int getToPort() {
            return toPort;
        }

        public int getToStarboard() {
            return toStarboard;
        }

        private void setTag(Object tag) {
            tags.add(tag);
        }

        public boolean isTagged(Object tag) {
            return tags.contains(tag);
        }

        public boolean hasPosition() {
            return positionReports.size() > 0;
        }

        public Set<PositionReport> getPositionReports() {
            return ImmutableSet.copyOf(positionReports.values());
        }

        public Date timeOfFirstPositionReport() {
            return positionReports.firstKey();
        }

        public Date timeOfLastPositionReport() {
            return positionReports.lastKey();
        }

        /**
         *  Return position at at time atTime. If a real position report which is not older than 'maxAge' seconds compared to
         *  atTime exists, then that real AIS-based position report will be returned. Otherwise a position report will
         *  be inter- or extrapolated to provide an estimated position report.
         *
         * @param atTime The time at which to return a position report.
         * @param maxAge The max. no. of seconds to look back in history for a position report before estimating one.
         * @return An AIS-based or estimated position report.
         */
        public PositionReport getPositionReportAt(Date atTime, int maxAge) {
            PositionReport positionReport = getPositionReportNear(atTime, maxAge);
            if (positionReport == null) {
                /* no position report at desired time - will estimate using interpolation or dead reckoning */
                Map.Entry<Date, PositionReport> entry1 = positionReports.lowerEntry(atTime);
                if (entry1 != null) {
                    PositionReport pr1 = entry1.getValue();
                    PositionReport pr2;
                    Map.Entry<Date, PositionReport> higherEntry = positionReports.higherEntry(atTime);
                    if (higherEntry != null) {
                        pr2 = higherEntry.getValue();
                        positionReport = new PositionReport(PositionTime.createInterpolated(pr1.getPositionTime(), pr2.getPositionTime(), atTime.getTime()), pr1.getCog(), pr1.getSog(), pr1.getHeading(), pr1.getNavigationalStatus(), true);
                    } else {
                        positionReport = new PositionReport(PositionTime.createExtrapolated(pr1.getPositionTime(), pr1.getCog(), pr1.getSog(), atTime.getTime()), pr1.getCog(), pr1.getSog(), pr1.getHeading(), pr1.getNavigationalStatus(), true);
                    }
                }
            }
            return positionReport;
        }

        /**
         * Get or estimate a position report. If a real position report exists somewhere in the range
         * (atTime - deltaSeconds; atTime] then return this report. Otherwise return null.
         *
         * @param atTime the time to which to look for a position report.
         * @param deltaSeconds the maximum number of seconds to go back to find a matching position report.
         * @return a matching position report or null.
         */
        PositionReport getPositionReportNear(Date atTime, int deltaSeconds) {
            Map.Entry<Date, PositionReport> positionReportEntry = positionReports.floorEntry(atTime);
            if (positionReportEntry != null) {
                if (positionReportEntry.getKey().getTime() < atTime.getTime() - deltaSeconds*1000) {
                    positionReportEntry = null;
                }
            }
            return positionReportEntry == null ? null : positionReportEntry.getValue();
        }

        private void update(AisPacket p) {
            AisMessage message = p.tryGetAisMessage();
            checkMmsi(message);
            if (message instanceof AisPositionMessage) {
                AisPositionMessage positionMessage = (AisPositionMessage) message;
                if (positionMessage.isPositionValid()) {
                    final float lat = (float) positionMessage.getPos().getLatitudeDouble();
                    final float lon = (float) positionMessage.getPos().getLongitudeDouble();
                    final int hdg = positionMessage.getTrueHeading();
                    final float cog = positionMessage.getCog() / 10.0f;
                    final float sog = positionMessage.getSog() / 10.0f;
                    final int nav = positionMessage.getNavStatus();
                    final long timestamp = p.getBestTimestamp();
                    positionReports.put(new Date(timestamp), new PositionReport(timestamp, lat,lon, cog, sog, hdg, NavigationalStatus.get(nav), false));
                }
            } else if (message instanceof AisMessage5) {
                AisMessage5 message5 = (AisMessage5) message;
                name = aisStringToJavaString(message5.getName());
                shipTypeCargo = new ShipTypeCargo(message5.getShipType());
                destination = aisStringToJavaString(message5.getDest());
                imo = (int) message5.getImo();
                toBow = message5.getDimBow();
                toStern = message5.getDimStern();
                toPort = message5.getDimPort();
                toStarboard = message5.getDimStarboard();
            }
        }

        private void checkMmsi(AisMessage message) {
            final int msgMmsi = message.getUserId();
            if (getMmsi() != msgMmsi) {
                throw new IllegalArgumentException("Message from mmsi " + msgMmsi + " cannot update target with mmsi " + getMmsi());
            }
        }

        private String name, destination;
        private int imo=-1, toBow=-1, toStern=-1, toPort=-1, toStarboard=-1;
        private ShipTypeCargo shipTypeCargo;

        private final Set<Object> tags = new HashSet<>();
        private final TreeMap<Date, PositionReport> positionReports = new TreeMap<>();

        @Immutable
        public final class PositionReport {
            private PositionReport(PositionTime pt, float cog, float sog, int heading, NavigationalStatus navstat, boolean estimated) {
                this.positionTime = pt;
                this.cog = cog;
                this.sog = sog;
                this.heading = heading;
                this.navstat = navstat;
                this.estimated = estimated;
            }

            private PositionReport(long timestamp, float latitude, float longitude, float cog, float sog, int heading, NavigationalStatus navstat, boolean estimated) {
                this.positionTime = PositionTime.create(latitude, longitude, timestamp);
                this.cog = cog;
                this.sog = sog;
                this.heading = heading;
                this.navstat = navstat;
                this.estimated = estimated;
            }

            public PositionTime getPositionTime() {
                return positionTime;
            }

            public long getTimestamp() {
                return positionTime.getTime();
            }

            public double getLatitude() {
                return positionTime.getLatitude();
            }

            public double getLongitude() {
                return positionTime.getLongitude();
            }

            public float getCog() {
                return cog;
            }

            public float getSog() {
                return sog;
            }

            public int getHeading() {
                return heading;
            }

            public NavigationalStatus getNavigationalStatus() { return navstat; }

            public boolean isEstimated() {
                return estimated;
            }

            @Override
            public String toString() {
                final StringBuffer sb = new StringBuffer("PositionReport{");
                sb.append("positionTime=").append(positionTime);
                sb.append(", cog=").append(cog);
                sb.append(", sog=").append(sog);
                sb.append(", heading=").append(heading);
                sb.append(", navstat=").append(navstat);
                sb.append(", shipTypeCargo=").append(shipTypeCargo);
                sb.append(", estimated=").append(estimated);
                sb.append('}');
                return sb.toString();
            }

            private final PositionTime positionTime;
            private final float cog;
            private final float sog;
            private final int heading;
            private final NavigationalStatus navstat;

            /** true of position is inter- or extrapolated. false if position is received from AIS */
            private final boolean estimated;
        }
    }
}
