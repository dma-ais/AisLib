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
package dk.dma.ais.message.binary;

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;

//import java.math.BigInteger;

/**
 * Abstract base class for area notice ASM's
 */
public abstract class AreaNotice extends AisApplicationMessage {

    public enum SubAreaType {
        CIRCLE_OR_POINT(0), RECTANGLE(1), SECTOR(2), POLYLINE(3), POLYGON(4), TEXT(5), RESERVED(6), RESERVED2(7);
        private int astype;

        private SubAreaType(int astype) {
            this.astype = astype;
        }

        public int getType() {
            return astype;
        }
    }

    private int msgLinkId; // 10 bits: Source specific running number linking
    // birary messages
    private int notice; // 7 bits: Notice description
    private int startMonth; // 4 bits
    private int startDay; // 5 bits
    private int startHour; // 5 bits
    private int startMin; // 6 bits
    private int duration; // 18 bits: Minutes until end of validity 0=cancel
    // route

    // /---------------
    private int subareasCount;
    private List<SubArea> subareas; // = null; // 55 bits each longitude 28 bit,

    // latitude 27 bit

    // TODO the rest from SN Circ 289

    public AreaNotice(int fi) {
        super(1, fi);
        this.subareas = new ArrayList<>();
    }

    public AreaNotice(int fi, BinArray binArray) throws SixbitException {
        super(1, fi, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgLinkId, 10);
        encoder.addVal(notice, 7);
        encoder.addVal(startMonth, 4);
        encoder.addVal(startDay, 5);
        encoder.addVal(startHour, 5);
        encoder.addVal(startMin, 6);
        encoder.addVal(duration, 18);

        for (SubArea subarea : subareas) {
            encoder.addVal(subarea.getRawAreaShape(), 3);
            switch (subarea.getRawAreaShape()) {
            case 0:
                encoder.addVal(subarea.getRawScaleFactor(), 2);
                encoder.addVal(subarea.getRawLongitude(), 25);
                encoder.addVal(subarea.getRawLatitude(), 24);
                encoder.addVal(subarea.getRawPrecision(), 3);
                encoder.addVal(subarea.getRawRadius(), 12);
                encoder.addVal(subarea.getSpare(), 18);
                break;
            case 1:
                encoder.addVal(subarea.getRawScaleFactor(), 2);
                encoder.addVal(subarea.getRawLongitude(), 25);
                encoder.addVal(subarea.getRawLatitude(), 24);
                encoder.addVal(subarea.getRawPrecision(), 3);
                encoder.addVal(subarea.getRawEDim(), 8);
                encoder.addVal(subarea.getRawNDim(), 8);
                encoder.addVal(subarea.getRawOrient(), 9);
                encoder.addVal(subarea.getSpare(), 5);
                break;
            case 2:
                encoder.addVal(subarea.getRawScaleFactor(), 2);
                encoder.addVal(subarea.getRawLongitude(), 25);
                encoder.addVal(subarea.getRawLatitude(), 24);
                encoder.addVal(subarea.getRawPrecision(), 3);
                encoder.addVal(subarea.getRawRadius(), 12);
                encoder.addVal(subarea.getRawLeftBound(), 9);
                encoder.addVal(subarea.getRawRightBound(), 9);
                encoder.addVal(subarea.getSpare(), 0);
                break;
            case 3:
                encoder.addVal(subarea.getRawScaleFactor(), 2);
                encoder.addVal(subarea.getRawP1Angle(), 10);
                encoder.addVal(subarea.getRawP1Dist(), 10);
                encoder.addVal(subarea.getRawP2Angle(), 10);
                encoder.addVal(subarea.getRawP2Dist(), 10);
                encoder.addVal(subarea.getRawP3Angle(), 10);
                encoder.addVal(subarea.getRawP3Dist(), 10);
                encoder.addVal(subarea.getRawP4Angle(), 10);
                encoder.addVal(subarea.getRawP4Dist(), 10);
                encoder.addVal(subarea.getSpare(), 2);
                break;
            case 4:
                encoder.addVal(subarea.getRawScaleFactor(), 2);
                encoder.addVal(subarea.getRawP1Angle(), 10);
                encoder.addVal(subarea.getRawP1Dist(), 10);
                encoder.addVal(subarea.getRawP2Angle(), 10);
                encoder.addVal(subarea.getRawP2Dist(), 10);
                encoder.addVal(subarea.getRawP3Angle(), 10);
                encoder.addVal(subarea.getRawP3Dist(), 10);
                encoder.addVal(subarea.getRawP4Angle(), 10);
                encoder.addVal(subarea.getRawP4Dist(), 10);
                encoder.addVal(subarea.getSpare(), 2);
                break;
            case 5:
                encoder.addVal(subarea.getText(), 32);
                encoder.addVal(subarea.getText1(), 32);
                encoder.addVal(subarea.getText2(), 20);
                break;
            default: // reserved

                break;

            }
        }

        // TODO rest of the fields (as in RouteInformation)

        return encoder;
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.subareas = new ArrayList<>();
        this.msgLinkId = (int) binArray.getVal(10);
        this.notice = (int) binArray.getVal(7);
        this.startMonth = (int) binArray.getVal(4);
        this.startDay = (int) binArray.getVal(5);
        this.startHour = (int) binArray.getVal(5);
        this.startMin = (int) binArray.getVal(6);
        this.duration = (int) binArray.getVal(18);
        subareasCount = (binArray.getLength() - 111) / 87;

        for (int i = 0; i < subareasCount; i++) {
            SubArea subarea = new SubArea();
            subarea.setRawAreaShape((int) binArray.getVal(3));
            switch (subarea.getRawAreaShape()) {
            case 0:
                subarea.setRawScaleFactor((int) binArray.getVal(2));
                subarea.setRawLongitude((int) binArray.getVal(25));
                subarea.setRawLatitude((int) binArray.getVal(24));
                subarea.setRawPrecision((int) binArray.getVal(3));
                subarea.setRawRadius((int) binArray.getVal(12));
                subarea.setSpare((int) binArray.getVal(18));
                break;
            case 1:
                subarea.setRawScaleFactor((int) binArray.getVal(2));
                subarea.setRawLongitude((int) binArray.getVal(25));
                subarea.setRawLatitude((int) binArray.getVal(24));
                subarea.setRawPrecision((int) binArray.getVal(3));
                subarea.setRawEDim((int) binArray.getVal(8));
                subarea.setRawNDim((int) binArray.getVal(8));
                subarea.setRawOrient((int) binArray.getVal(9));
                subarea.setSpare((int) binArray.getVal(5));
                break;
            case 2:
                subarea.setRawScaleFactor((int) binArray.getVal(2));
                subarea.setRawLongitude((int) binArray.getVal(25));
                subarea.setRawLatitude((int) binArray.getVal(24));
                subarea.setRawPrecision((int) binArray.getVal(3));
                subarea.setRawRadius((int) binArray.getVal(12));
                subarea.setRawLeftBound((int) binArray.getVal(9));
                subarea.setRawRightBound((int) binArray.getVal(9));
                subarea.setSpare((int) binArray.getVal(0));
                break;
            case 3:
                subarea.setRawScaleFactor((int) binArray.getVal(2));
                subarea.setRawP1Angle((int) binArray.getVal(10));
                subarea.setRawP1Dist((int) binArray.getVal(10));
                subarea.setRawP2Angle((int) binArray.getVal(10));
                subarea.setRawP2Dist((int) binArray.getVal(10));
                subarea.setRawP3Angle((int) binArray.getVal(10));
                subarea.setRawP3Dist((int) binArray.getVal(10));
                subarea.setRawP4Angle((int) binArray.getVal(10));
                subarea.setRawP4Dist((int) binArray.getVal(10));
                subarea.setSpare((int) binArray.getVal(2));
                break;
            case 4:
                subarea.setRawScaleFactor((int) binArray.getVal(2));
                subarea.setRawP1Angle((int) binArray.getVal(10));
                subarea.setRawP1Dist((int) binArray.getVal(10));
                subarea.setRawP2Angle((int) binArray.getVal(10));
                subarea.setRawP2Dist((int) binArray.getVal(10));
                subarea.setRawP3Angle((int) binArray.getVal(10));
                subarea.setRawP3Dist((int) binArray.getVal(10));
                subarea.setRawP4Angle((int) binArray.getVal(10));
                subarea.setRawP4Dist((int) binArray.getVal(10));
                subarea.setSpare((int) binArray.getVal(2));
                break;
            case 5:
                subarea.setText((int) binArray.getVal(32));
                subarea.setText1((int) binArray.getVal(32));
                subarea.setText2((int) binArray.getVal(20));
                break;
            default:

                // reserved
                break;

            }
            this.subareas.add(subarea);
            // addSubArea(subarea);
        }

        // TODO rest of the fields (as in RouteInformation)

    }

    public int getMsgLinkId() {
        return msgLinkId;
    }

    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
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

    public void setSubAreas(List<SubArea> subareas) {
        this.subareas = subareas;
    }

    public void addSubArea(SubArea subarea) {
        subareas.add(subarea);
        subareasCount = subareas.size();
    }

    public List<SubArea> getSubAreas() {
        return subareas;
    }

    // TODO rest of getters and setters

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", msgLinkId=");
        builder.append(msgLinkId);
        builder.append(", notice=");
        builder.append(notice);
        builder.append(", startDay=");
        builder.append(startDay);
        builder.append(", startHour=");
        builder.append(startHour);
        builder.append(", startMin=");
        builder.append(startMin);
        builder.append(", startMonth=");
        builder.append(startMonth);
        builder.append(", duration=");
        builder.append(duration);
        builder.append(", subareas=");
        builder.append(subareas);
        // builder.append(this.getLength());

        // TODO add rest of the fields

        builder.append("]");
        return builder.toString();
    }

}
