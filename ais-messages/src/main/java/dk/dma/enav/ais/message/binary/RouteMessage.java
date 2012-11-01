/* Copyright (c) 2011 Danish Maritime Safety Administration
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
package dk.dma.enav.ais.message.binary;

import java.util.ArrayList;
import java.util.List;

import dk.dma.enav.ais.binary.BinArray;
import dk.dma.enav.ais.binary.SixbitEncoder;
import dk.dma.enav.ais.binary.SixbitException;
import dk.dma.enav.ais.message.AisPosition;

/**
 * Abstract base class for all messages using route information DAC=1, FI=27,28 DAC=219, FI=1,2
 */
public abstract class RouteMessage extends AisApplicationMessage {

    protected int startMonth; // 4 bits
    protected int startDay; // 5 bits
    protected int startHour; // 5 bits
    protected int startMin; // 6 bits
    protected int duration; // 18 bits: Minutes until end of validity 0=cancel
                            // route
    protected int waypointCount; // 5 bits: 0 - 16
    protected List<AisPosition> waypoints; // 55 bits each longitude 28 bit,
                                           // latitude 27 bit

    public RouteMessage(int dac, int fi) {
        super(dac, fi);
        this.waypoints = new ArrayList<>();
    }

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

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWaypointCount() {
        return waypointCount;
    }

    public void setWaypointCount(int waypointCount) {
        this.waypointCount = waypointCount;
    }

    public List<AisPosition> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<AisPosition> waypoints) {
        this.waypoints = waypoints;
    }

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
