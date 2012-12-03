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

    public SubArea() {}

    @Override
    public boolean equals(Object obj) {
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

    public int getRawAreaShape() {
        return rawsSAreaShape;
    }

    public int getRawEDim() {
        return rawSEDim;
    }

    public int getRawLatitude() {
        return rawSLatitude;
    }

    public int getRawLeftBound() {
        return rawSLeftBound;
    }

    public int getRawLongitude() {
        return rawSLongitude;
    }

    public int getRawNDim() {
        return rawSNDim;
    }

    public int getRawOrient() {
        return rawSOrient;
    }

    public int getRawP1Angle() {
        return rawSP1Angle;
    }

    public int getRawP1Dist() {
        return rawSP1Dist;
    }

    public int getRawP2Angle() {
        return rawSP2Angle;
    }

    public int getRawP2Dist() {
        return rawSP2Dist;
    }

    public int getRawP3Angle() {
        return rawSP3Angle;
    }

    public int getRawP3Dist() {
        return rawSP3Dist;
    }

    public int getRawP4Angle() {
        return rawSP4Angle;
    }

    public int getRawP4Dist() {
        return rawSP4Dist;
    }

    public int getRawPrecision() {
        return rawSPrecision;
    }

    public int getRawRadius() {
        return rawSRadius;
    }

    public int getRawRightBound() {
        return rawSRightBound;
    }

    public int getRawScaleFactor() {
        return rawSScaleFactor;
    }

    public int getSpare() {
        return rawSSpare;
    }

    public int getText() {
        return sText;
    }

    public int getText1() {
        return sText2;
    }

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

    public void setRawAreaShape(int rawsSAreaShape) {
        this.rawsSAreaShape = rawsSAreaShape;
    }

    public void setRawEDim(int rawSEDim) {
        this.rawSEDim = rawSEDim;
    }

    public void setRawLatitude(int rawSLatitude) {
        this.rawSLatitude = rawSLatitude;
    }

    public void setRawLeftBound(int rawSLeftBound) {
        this.rawSLeftBound = rawSLeftBound;
    }

    public void setRawLongitude(int rawSLongitude) {
        this.rawSLongitude = rawSLongitude;
    }

    public void setRawNDim(int rawSNDim) {
        this.rawSNDim = rawSNDim;
    }

    public void setRawOrient(int rawSOrient) {
        this.rawSOrient = rawSOrient;
    }

    public void setRawP1Angle(int rawSP1Angle) {
        this.rawSP1Angle = rawSP1Angle;
    }

    public void setRawP1Dist(int rawSP1Dist) {
        this.rawSP1Dist = rawSP1Dist;
    }

    public void setRawP2Angle(int rawSP2Angle) {
        this.rawSP2Angle = rawSP2Angle;
    }

    public void setRawP2Dist(int rawSP2Dist) {
        this.rawSP2Dist = rawSP2Dist;
    }

    public void setRawP3Angle(int rawSP3Angle) {
        this.rawSP3Angle = rawSP3Angle;
    }

    public void setRawP3Dist(int rawSP3Dist) {
        this.rawSP3Dist = rawSP3Dist;
    }

    public void setRawP4Angle(int rawSP4Angle) {
        this.rawSP4Angle = rawSP4Angle;
    }

    public void setRawP4Dist(int rawSP4Dist) {
        this.rawSP4Dist = rawSP4Dist;
    }

    public void setRawPrecision(int rawSPrecision) {
        this.rawSPrecision = rawSPrecision;
    }

    public void setRawRadius(int rawSRadius) {
        this.rawSRadius = rawSRadius;
    }

    public void setRawRightBound(int rawSRightBound) {
        this.rawSRightBound = rawSRightBound;
    }

    public void setRawScaleFactor(int rawSScaleFactor) {
        this.rawSScaleFactor = rawSScaleFactor;
    }

    public void setSpare(int rawSSpare) {
        this.rawSSpare = rawSSpare;
    }

    public void setText(int sText) {
        this.sText = sText;
    }

    public void setText1(int sText1) {
        this.sText1 = sText1;
    }

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
