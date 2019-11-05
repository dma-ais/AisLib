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

//import java.math.BigInteger;

/**
 * Abstract base class for area notice ASM's
 */
public abstract class AreaNotice extends AisApplicationMessage {

    /**
     * The enum Sub area type.
     */
    public enum SubAreaType {
        /**
         * Circle or point sub area type.
         */
        CIRCLE_OR_POINT(0),
        /**
         * Rectangle sub area type.
         */
        RECTANGLE(1),
        /**
         * Sector sub area type.
         */
        SECTOR(2),
        /**
         * Polyline sub area type.
         */
        POLYLINE(3),
        /**
         * Polygon sub area type.
         */
        POLYGON(4),
        /**
         * Text sub area type.
         */
        TEXT(5),
        /**
         * Reserved sub area type.
         */
        RESERVED(6),
        /**
         * Reserved 2 sub area type.
         */
        RESERVED2(7);
        private int astype;

        private SubAreaType(int astype) {
            this.astype = astype;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
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

    /**
     * Instantiates a new Area notice.
     *
     * @param fi the fi
     */
    public AreaNotice(int fi) {
        super(1, fi);
        this.subareas = new ArrayList<>();
    }

    /**
     * Instantiates a new Area notice.
     *
     * @param fi       the fi
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
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

    /**
     * Gets msg link id.
     *
     * @return the msg link id
     */
    public int getMsgLinkId() {
        return msgLinkId;
    }

    /**
     * Sets msg link id.
     *
     * @param msgLinkId the msg link id
     */
    public void setMsgLinkId(int msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    /**
     * Gets notice.
     *
     * @return the notice
     */
    public int getNotice() {
        return notice;
    }

    /**
     * Sets notice.
     *
     * @param notice the notice
     */
    public void setNotice(int notice) {
        this.notice = notice;
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
     * Sets sub areas.
     *
     * @param subareas the subareas
     */
    public void setSubAreas(List<SubArea> subareas) {
        this.subareas = subareas;
    }

    /**
     * Add sub area.
     *
     * @param subarea the subarea
     */
    public void addSubArea(SubArea subarea) {
        subareas.add(subarea);
        subareasCount = subareas.size();
    }

    /**
     * Gets sub areas.
     *
     * @return the sub areas
     */
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
