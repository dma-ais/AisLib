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
package dk.dma.ais.message.binary;

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisPosition;

/**
 * Abstract base class for all messages using route information DAC=1, FI=27,28 DAC=219, FI=1,2
 */
public abstract class RouteMessage extends AisApplicationMessage {

    /**
     * The Start month.
     */
    protected int startMonth; // 4 bits
    /**
     * The Start day.
     */
    protected int startDay; // 5 bits
    /**
     * The Start hour.
     */
    protected int startHour; // 5 bits
    /**
     * The Start min.
     */
    protected int startMin; // 6 bits
    /**
     * The Duration.
     */
    protected int duration; // 18 bits: Minutes until end of validity 0=cancel
    /**
     * The Waypoint count.
     */
// route
    protected int waypointCount; // 5 bits: 0 - 16
    /**
     * The Waypoints.
     */
    protected List<AisPosition> waypoints; // 55 bits each longitude 28 bit,
                                           // latitude 27 bit

    /**
     * Instantiates a new Route message.
     *
     * @param dac the dac
     * @param fi  the fi
     */
    public RouteMessage(int dac, int fi) {
        super(dac, fi);
        this.waypoints = new ArrayList<>();
    }

    /**
     * Instantiates a new Route message.
     *
     * @param dac      the dac
     * @param fi       the fi
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public RouteMessage(int dac, int fi, BinArray binArray) throws SixbitException {
        super(dac, fi, binArray);
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.waypoints = new ArrayList<>();
        this.startMonth = (int) binArray.getVal(4);
        this.startDay = (int) binArray.getVal(5);
        this.startHour = (int) binArray.getVal(5);
        this.startMin = (int) binArray.getVal(6);
        this.duration = (int) binArray.getVal(18);
        this.waypointCount = (int) binArray.getVal(5);
        for (int i = 0; i < this.waypointCount; i++) {
            AisPosition waypoint = new AisPosition();
            waypoint.setRawLongitude(binArray.getVal(28));
            waypoint.setRawLatitude(binArray.getVal(27));
            this.waypoints.add(waypoint);
        }
    }

    /**
     * Encode.
     *
     * @param encoder the encoder
     */
    public void encode(SixbitEncoder encoder) {
        encoder.addVal(startMonth, 4);
        encoder.addVal(startDay, 5);
        encoder.addVal(startHour, 5);
        encoder.addVal(startMin, 6);
        encoder.addVal(duration, 18);
        encoder.addVal(waypointCount, 5);
        for (AisPosition waypoint : waypoints) {
            encoder.addVal(waypoint.getRawLongitude(), 28);
            encoder.addVal(waypoint.getRawLatitude(), 27);
        }
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encode(encoder);
        return encoder;
    }

    /**
     * Gets start month.
     *
     * @return the start month
     */
    public int getStartMonth() {
        return startMonth;
    }

    /**
     * Sets start month.
     *
     * @param startMonth the start month
     */
    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * Gets start day.
     *
     * @return the start day
     */
    public int getStartDay() {
        return startDay;
    }

    /**
     * Sets start day.
     *
     * @param startDay the start day
     */
    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    /**
     * Gets start hour.
     *
     * @return the start hour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * Sets start hour.
     *
     * @param startHour the start hour
     */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    /**
     * Gets start min.
     *
     * @return the start min
     */
    public int getStartMin() {
        return startMin;
    }

    /**
     * Sets start min.
     *
     * @param startMin the start min
     */
    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets waypoint count.
     *
     * @return the waypoint count
     */
    public int getWaypointCount() {
        return waypointCount;
    }

    /**
     * Sets waypoint count.
     *
     * @param waypointCount the waypoint count
     */
    public void setWaypointCount(int waypointCount) {
        this.waypointCount = waypointCount;
    }

    /**
     * Gets waypoints.
     *
     * @return the waypoints
     */
    public List<AisPosition> getWaypoints() {
        return waypoints;
    }

    /**
     * Sets waypoints.
     *
     * @param waypoints the waypoints
     */
    public void setWaypoints(List<AisPosition> waypoints) {
        this.waypoints = waypoints;
    }

    /**
     * Add waypoint.
     *
     * @param waypoint the waypoint
     */
    public void addWaypoint(AisPosition waypoint) {
        waypoints.add(waypoint);
        waypointCount = waypoints.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", startMonth=");
        builder.append(startMonth);
        builder.append(", startDay=");
        builder.append(startDay);
        builder.append(", startHour=");
        builder.append(startHour);
        builder.append(", startMin=");
        builder.append(startMin);
        builder.append(", duration=");
        builder.append(duration);
        builder.append(", waypointCount=");
        builder.append(waypointCount);
        builder.append(", waypoints=");
        builder.append(waypoints);
        builder.append("]");
        return builder.toString();
    }

}
