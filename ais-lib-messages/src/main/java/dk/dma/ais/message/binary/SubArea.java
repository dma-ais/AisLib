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


/**
 * Subarea class for area notice
 */
public class SubArea {

    private int rawSEDim; // 8 bits for Rectangle shape
    private int rawSLatitude; // 24 bits
    private int rawSLeftBound; // 9 bits for sector
    private int rawSLongitude; // 25 bits
    private int rawSNDim; // 8 bits for Rectangle shape
    private int rawSOrient; // 9 bits for Rectangle shape
    private int rawSP1Angle; // 10 bits for wypnt polygon and polyline
    private int rawSP1Dist; // 10 bits for wypnt polygon and polyline
    private int rawSP2Angle; // 10 bits for wypnt polygon and polyline
    private int rawSP2Dist; // 10 bits for wypnt polygon and polyline
    private int rawSP3Angle; // 10 bits for wypnt polygon and polyline
    private int rawSP3Dist; // 10 bits for wypnt polygon and polyline
    private int rawSP4Angle; // 10 bits for wypnt polygon and polyline
    private int rawSP4Dist; // 10 bits for wypnt polygon and polyline
    private int rawSPrecision; // 3 bits
    private int rawSRadius; // 12 bits for circle or point and sector
    private int rawSRightBound;// 9 bits for sector
    private int rawsSAreaShape; // 3 bits
    private int rawSScaleFactor; // 2 bits
    private int rawSSpare; // varying 0 value
    private int sText; // 3x32 bits for text up to 84 (Ascii 6 bits)
    private int sText1;
    private int sText2;

    // private double resolution = 1000.0;

    /**
     * Instantiates a new Sub area.
     */
    public SubArea() {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        SubArea subarea = (SubArea) obj;
        boolean equalz = false;
        if (subarea.rawsSAreaShape == this.rawsSAreaShape)

        {
            switch (subarea.rawsSAreaShape) {
            case 0:
                if (subarea.rawSScaleFactor == this.rawSScaleFactor && subarea.rawSLongitude == this.rawSLongitude
                        && subarea.rawSLatitude == this.rawSLatitude && subarea.rawSPrecision == this.rawSPrecision
                        && subarea.rawSRadius == this.rawSRadius && subarea.rawSSpare == this.rawSSpare) {
                    equalz = true;
                } else {
                    equalz = false;
                }

                break;
            case 1:
                if (subarea.rawSScaleFactor == this.rawSScaleFactor && subarea.rawSLongitude == this.rawSLongitude
                        && subarea.rawSLatitude == this.rawSLatitude && subarea.rawSPrecision == this.rawSPrecision
                        && subarea.rawSEDim == this.rawSEDim && subarea.rawSNDim == this.rawSNDim
                        && subarea.rawSOrient == this.rawSOrient && subarea.rawSSpare == this.rawSSpare) {
                    equalz = true;
                } else {
                    equalz = false;
                }
                break;
            case 2:
                if (subarea.rawSScaleFactor == this.rawSScaleFactor && subarea.rawSLongitude == this.rawSLongitude
                        && subarea.rawSLatitude == this.rawSLatitude && subarea.rawSPrecision == this.rawSPrecision
                        && subarea.rawSRadius == this.rawSRadius && subarea.rawSLeftBound == this.rawSLeftBound
                        && subarea.rawSRightBound == this.rawSRightBound && subarea.rawSSpare == this.rawSSpare) {
                    equalz = true;
                } else {
                    equalz = false;
                }
                break;
            case 3:
                if (subarea.rawSScaleFactor == this.rawSScaleFactor && subarea.rawSP1Angle == this.rawSP1Angle
                        && subarea.rawSP1Dist == this.rawSP1Dist && subarea.rawSP2Angle == this.rawSP2Angle
                        && subarea.rawSP2Dist == this.rawSP2Dist && subarea.rawSP3Angle == this.rawSP3Angle
                        && subarea.rawSP3Dist == this.rawSP3Dist && subarea.rawSP4Angle == this.rawSP4Angle
                        && subarea.rawSP4Dist == this.rawSP4Dist && subarea.rawSSpare == this.rawSSpare) {
                    equalz = true;
                } else {
                    equalz = false;
                }
                break;
            case 4:
                if (subarea.rawSScaleFactor == this.rawSScaleFactor && subarea.rawSP1Angle == this.rawSP1Angle
                        && subarea.rawSP1Dist == this.rawSP1Dist && subarea.rawSP2Angle == this.rawSP2Angle
                        && subarea.rawSP2Dist == this.rawSP2Dist && subarea.rawSP3Angle == this.rawSP3Angle
                        && subarea.rawSP3Dist == this.rawSP3Dist && subarea.rawSP4Angle == this.rawSP4Angle
                        && subarea.rawSP4Dist == this.rawSP4Dist && subarea.rawSSpare == this.rawSSpare) {
                    equalz = true;
                } else {
                    equalz = false;
                }
                break;
            case 5:
                if (subarea.sText == this.sText && subarea.sText1 == this.sText1 && subarea.sText2 == this.sText2) {
                    equalz = true;
                } else {
                    equalz = false;
                }
                break;
            default:

                // reserved
                break;
            }
        }
        return equalz;
    }

    /**
     * Gets raw area shape.
     *
     * @return the raw area shape
     */
    public int getRawAreaShape() {
        return rawsSAreaShape;
    }

    /**
     * Gets raw e dim.
     *
     * @return the raw e dim
     */
    public int getRawEDim() {
        return rawSEDim;
    }

    /**
     * Gets raw latitude.
     *
     * @return the raw latitude
     */
    public int getRawLatitude() {
        return rawSLatitude;
    }

    /**
     * Gets raw left bound.
     *
     * @return the raw left bound
     */
    public int getRawLeftBound() {
        return rawSLeftBound;
    }

    /**
     * Gets raw longitude.
     *
     * @return the raw longitude
     */
    public int getRawLongitude() {
        return rawSLongitude;
    }

    /**
     * Gets raw n dim.
     *
     * @return the raw n dim
     */
    public int getRawNDim() {
        return rawSNDim;
    }

    /**
     * Gets raw orient.
     *
     * @return the raw orient
     */
    public int getRawOrient() {
        return rawSOrient;
    }

    /**
     * Gets raw p 1 angle.
     *
     * @return the raw p 1 angle
     */
    public int getRawP1Angle() {
        return rawSP1Angle;
    }

    /**
     * Gets raw p 1 dist.
     *
     * @return the raw p 1 dist
     */
    public int getRawP1Dist() {
        return rawSP1Dist;
    }

    /**
     * Gets raw p 2 angle.
     *
     * @return the raw p 2 angle
     */
    public int getRawP2Angle() {
        return rawSP2Angle;
    }

    /**
     * Gets raw p 2 dist.
     *
     * @return the raw p 2 dist
     */
    public int getRawP2Dist() {
        return rawSP2Dist;
    }

    /**
     * Gets raw p 3 angle.
     *
     * @return the raw p 3 angle
     */
    public int getRawP3Angle() {
        return rawSP3Angle;
    }

    /**
     * Gets raw p 3 dist.
     *
     * @return the raw p 3 dist
     */
    public int getRawP3Dist() {
        return rawSP3Dist;
    }

    /**
     * Gets raw p 4 angle.
     *
     * @return the raw p 4 angle
     */
    public int getRawP4Angle() {
        return rawSP4Angle;
    }

    /**
     * Gets raw p 4 dist.
     *
     * @return the raw p 4 dist
     */
    public int getRawP4Dist() {
        return rawSP4Dist;
    }

    /**
     * Gets raw precision.
     *
     * @return the raw precision
     */
    public int getRawPrecision() {
        return rawSPrecision;
    }

    /**
     * Gets raw radius.
     *
     * @return the raw radius
     */
    public int getRawRadius() {
        return rawSRadius;
    }

    /**
     * Gets raw right bound.
     *
     * @return the raw right bound
     */
    public int getRawRightBound() {
        return rawSRightBound;
    }

    /**
     * Gets raw scale factor.
     *
     * @return the raw scale factor
     */
    public int getRawScaleFactor() {
        return rawSScaleFactor;
    }

    /**
     * Gets spare.
     *
     * @return the spare
     */
    public int getSpare() {
        return rawSSpare;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public int getText() {
        return sText;
    }

    /**
     * Gets text 1.
     *
     * @return the text 1
     */
    public int getText1() {
        return sText2;
    }

    /**
     * Gets text 2.
     *
     * @return the text 2
     */
    public int getText2() {
        return sText2;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rawSEDim;
        result = prime * result + rawSLatitude;
        result = prime * result + rawSLeftBound;
        result = prime * result + rawSLongitude;
        result = prime * result + rawSNDim;
        result = prime * result + rawSOrient;
        result = prime * result + rawSP1Angle;
        result = prime * result + rawSP1Dist;
        result = prime * result + rawSP2Angle;
        result = prime * result + rawSP2Dist;
        result = prime * result + rawSP3Angle;
        result = prime * result + rawSP3Dist;
        result = prime * result + rawSP4Angle;
        result = prime * result + rawSP4Dist;
        result = prime * result + rawSPrecision;
        result = prime * result + rawSRadius;
        result = prime * result + rawSRightBound;
        result = prime * result + rawSScaleFactor;
        result = prime * result + rawSSpare;
        result = prime * result + rawsSAreaShape;
        result = prime * result + sText;
        result = prime * result + sText1;
        result = prime * result + sText2;
        return result;
    }

    /**
     * Sets raw area shape.
     *
     * @param rawsSAreaShape the raws s area shape
     */
    public void setRawAreaShape(int rawsSAreaShape) {
        this.rawsSAreaShape = rawsSAreaShape;
    }

    /**
     * Sets raw e dim.
     *
     * @param rawSEDim the raw se dim
     */
    public void setRawEDim(int rawSEDim) {
        this.rawSEDim = rawSEDim;
    }

    /**
     * Sets raw latitude.
     *
     * @param rawSLatitude the raw s latitude
     */
    public void setRawLatitude(int rawSLatitude) {
        this.rawSLatitude = rawSLatitude;
    }

    /**
     * Sets raw left bound.
     *
     * @param rawSLeftBound the raw s left bound
     */
    public void setRawLeftBound(int rawSLeftBound) {
        this.rawSLeftBound = rawSLeftBound;
    }

    /**
     * Sets raw longitude.
     *
     * @param rawSLongitude the raw s longitude
     */
    public void setRawLongitude(int rawSLongitude) {
        this.rawSLongitude = rawSLongitude;
    }

    /**
     * Sets raw n dim.
     *
     * @param rawSNDim the raw sn dim
     */
    public void setRawNDim(int rawSNDim) {
        this.rawSNDim = rawSNDim;
    }

    /**
     * Sets raw orient.
     *
     * @param rawSOrient the raw s orient
     */
    public void setRawOrient(int rawSOrient) {
        this.rawSOrient = rawSOrient;
    }

    /**
     * Sets raw p 1 angle.
     *
     * @param rawSP1Angle the raw sp 1 angle
     */
    public void setRawP1Angle(int rawSP1Angle) {
        this.rawSP1Angle = rawSP1Angle;
    }

    /**
     * Sets raw p 1 dist.
     *
     * @param rawSP1Dist the raw sp 1 dist
     */
    public void setRawP1Dist(int rawSP1Dist) {
        this.rawSP1Dist = rawSP1Dist;
    }

    /**
     * Sets raw p 2 angle.
     *
     * @param rawSP2Angle the raw sp 2 angle
     */
    public void setRawP2Angle(int rawSP2Angle) {
        this.rawSP2Angle = rawSP2Angle;
    }

    /**
     * Sets raw p 2 dist.
     *
     * @param rawSP2Dist the raw sp 2 dist
     */
    public void setRawP2Dist(int rawSP2Dist) {
        this.rawSP2Dist = rawSP2Dist;
    }

    /**
     * Sets raw p 3 angle.
     *
     * @param rawSP3Angle the raw sp 3 angle
     */
    public void setRawP3Angle(int rawSP3Angle) {
        this.rawSP3Angle = rawSP3Angle;
    }

    /**
     * Sets raw p 3 dist.
     *
     * @param rawSP3Dist the raw sp 3 dist
     */
    public void setRawP3Dist(int rawSP3Dist) {
        this.rawSP3Dist = rawSP3Dist;
    }

    /**
     * Sets raw p 4 angle.
     *
     * @param rawSP4Angle the raw sp 4 angle
     */
    public void setRawP4Angle(int rawSP4Angle) {
        this.rawSP4Angle = rawSP4Angle;
    }

    /**
     * Sets raw p 4 dist.
     *
     * @param rawSP4Dist the raw sp 4 dist
     */
    public void setRawP4Dist(int rawSP4Dist) {
        this.rawSP4Dist = rawSP4Dist;
    }

    /**
     * Sets raw precision.
     *
     * @param rawSPrecision the raw s precision
     */
    public void setRawPrecision(int rawSPrecision) {
        this.rawSPrecision = rawSPrecision;
    }

    /**
     * Sets raw radius.
     *
     * @param rawSRadius the raw s radius
     */
    public void setRawRadius(int rawSRadius) {
        this.rawSRadius = rawSRadius;
    }

    /**
     * Sets raw right bound.
     *
     * @param rawSRightBound the raw s right bound
     */
    public void setRawRightBound(int rawSRightBound) {
        this.rawSRightBound = rawSRightBound;
    }

    /**
     * Sets raw scale factor.
     *
     * @param rawSScaleFactor the raw s scale factor
     */
    public void setRawScaleFactor(int rawSScaleFactor) {
        this.rawSScaleFactor = rawSScaleFactor;
    }

    /**
     * Sets spare.
     *
     * @param rawSSpare the raw s spare
     */
    public void setSpare(int rawSSpare) {
        this.rawSSpare = rawSSpare;
    }

    /**
     * Sets text.
     *
     * @param sText the s text
     */
    public void setText(int sText) {
        this.sText = sText;
    }

    /**
     * Sets text 1.
     *
     * @param sText1 the s text 1
     */
    public void setText1(int sText1) {
        this.sText1 = sText1;
    }

    /**
     * Sets text 2.
     *
     * @param sText2 the s text 2
     */
    public void setText2(int sText2) {
        this.sText2 = sText2;
    }

    @Override
    public String toString() {
        String subareaMessage = null;

        switch (this.rawsSAreaShape) {
        case 0:
            subareaMessage = "( Circle, SF=" + getRawScaleFactor() + ", Pos = [" + getRawLatitude() + ","
                    + getRawLongitude() + "], Prec=" + getRawPrecision() + ", Radius=" + getRawRadius() + " )";
            break;
        case 1:
            subareaMessage = "( Rectangle, SF=" + getRawScaleFactor() + " Pos = [" + getRawLatitude() + ","
                    + getRawLongitude() + "], dim E=" + getRawEDim() + ", dim N=" + getRawNDim() + ", Orientation="
                    + getRawOrient() + " )";
            break;
        case 2:
            subareaMessage = "( Sector, SF=" + getRawScaleFactor() + " Pos = [" + getRawLatitude() + ","
                    + getRawLongitude() + "], Prec=" + getRawPrecision() + ", Radius=" + getRawRadius() + ", LeftB="
                    + getRawLeftBound() + ", RightB=" + getRawRightBound() + " )";
            break;
        case 3:
            subareaMessage = "( Polyline, SF=" + getRawScaleFactor() + " P1 A&D = [" + getRawP1Angle() + ","
                    + getRawP1Dist() + "], P2 A&D = [" + getRawP2Angle() + "," + getRawP2Dist() + "],P3 A&D = ["
                    + getRawP3Angle() + "," + getRawP3Dist() + "],P4 A&D = [" + getRawP4Angle() + "," + getRawP4Dist()
                    + "])";
            break;
        case 4:
            subareaMessage = "( Polygon, SF=" + getRawScaleFactor() + " P1 A&D = [" + getRawP1Angle() + ","
                    + getRawP1Dist() + "], P2 A&D = [" + getRawP2Angle() + "," + getRawP2Dist() + "],P3 A&D = ["
                    + getRawP3Angle() + "," + getRawP3Dist() + "],P4 A&D = [" + getRawP4Angle() + "," + getRawP4Dist()
                    + "])";
            break;
        case 5:
            subareaMessage = "(Text=" + getText() + getText1() + getText2() + ")";
            break;
        default:
            subareaMessage = "(Message Unsupported)";

            break;
        }

        return subareaMessage;
    }

}
