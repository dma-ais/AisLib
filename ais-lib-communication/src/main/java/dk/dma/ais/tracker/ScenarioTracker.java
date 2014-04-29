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
package dk.dma.ais.tracker;

import com.google.common.collect.ImmutableSet;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.packet.AisPacketStream;
import dk.dma.ais.packet.AisPacketStream.Subscription;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Consumer;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
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
public class ScenarioTracker implements Tracker {

    @Override
    public Subscription readFromStream(AisPacketStream stream) {
        return stream.subscribe(new Consumer<AisPacket>() {
            public void accept(AisPacket p) {
                update(p);
            }
        });
    }

    public void readFromPacketReader(AisPacketReader packetReader) throws IOException {
        packetReader.forEachRemaining(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket p) {
                update(p);
            }
        });
    }

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

    public void update(AisPacket p) {
        AisMessage message;
        try {
            message = p.getAisMessage();
            int mmsi = message.getUserId();
            Target target;
            if (! targets.containsKey(mmsi)) {
                target = new Target();
                targets.put(mmsi, target);
            } else {
                target = targets.get(mmsi);
            }
            if (message instanceof IPositionMessage) {
                updateBoundingBox((IPositionMessage) message);
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

    private void updateBoundingBox(IPositionMessage positionMessage) {
        Position position = positionMessage.getPos().getGeoLocation();
        if (boundingBox == null) {
            boundingBox = BoundingBox.create(position, position, CoordinateSystem.CARTESIAN);
        } else {
            boundingBox = boundingBox.include(BoundingBox.create(position, position, CoordinateSystem.CARTESIAN));
        }
    }

    private static String aisStringToJavaString(String aisString) {
        return aisString.replace('@',' ').trim();
    }

    public final class Target implements Cloneable {

        public Target() {
        }

        public String getName() {
            return StringUtils.isBlank(name) ? getMmsi() : name;
        }

        public String getMmsi() {
            return String.valueOf(mmsi);
        }

        public Set<PositionReport> getPositionReports() {
            return ImmutableSet.copyOf(positionReports.values());
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

        private void update(AisPacket p) {
            AisMessage message = p.tryGetAisMessage();
            checkOrSetMmsi(message);
            if (message instanceof AisPositionMessage) {
                AisPositionMessage positionMessage = (AisPositionMessage) message;
                float lat = (float) positionMessage.getPos().getLatitudeDouble();
                float lon = (float) positionMessage.getPos().getLongitudeDouble();
                int hdg = positionMessage.getTrueHeading();
                long timestamp = p.getBestTimestamp();
                positionReports.put(new Date(timestamp), new PositionReport(timestamp, lat,lon, hdg));
            } else if (message instanceof AisMessage5) {
                AisMessage5 message5 = (AisMessage5) message;
                name = aisStringToJavaString(message5.getName());
                toBow = message5.getDimBow();
                toStern = message5.getDimStern();
                toPort = message5.getDimPort();
                toStarboard = message5.getDimStarboard();
            }
        }

        private void checkOrSetMmsi(AisMessage message) {
            final int msgMmsi = message.getUserId();
            if (mmsi < 0) {
                mmsi = msgMmsi;
            } else {
                if (mmsi != msgMmsi) {
                    throw new IllegalArgumentException("Message from mmsi " + msgMmsi + " cannot update target with mmsi " + mmsi);
                }
            }
        }

        private String name;
        private int mmsi=-1, toBow=-1, toStern=-1, toPort=-1, toStarboard=-1;

        private final Set<Object> tags = new HashSet<>();
        private final TreeMap<Date, PositionReport> positionReports = new TreeMap<>();

        public final class PositionReport {
            private PositionReport(long timestamp, float latitude, float longitude, int heading) {
                this.timestamp = timestamp;
                this.latitude = latitude;
                this.longitude = longitude;
                this.heading = heading;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public float getLatitude() {
                return latitude;
            }

            public float getLongitude() {
                return longitude;
            }

            public int getHeading() {
                return heading;
            }

            private final long timestamp;
            private final float latitude;
            private final float longitude;
            private final int heading;
        }
    }
}
