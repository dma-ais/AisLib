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
package dk.dma.ais.message;

import dk.dma.enav.model.geometry.Position;

/**
 * AIS position class
 * 
 * Convert raw unsigned AIS position to signed 1/10000 degree position and provide helper methods for other formats
 * 
 */
public class AisPosition {

    private long rawLongitude;
    private long rawLatitude;

    private int bitCorrection = 0;
    private double resolution = 10000.0;

    public AisPosition() {}

    /**
     * Constructor given raw latitude and raw longitude as received in AIS message
     * 
     * @param rawLatitude
     * @param rawLongitude
     */
    public AisPosition(long rawLatitude, long rawLongitude) {
        this.rawLatitude = rawLatitude;
        this.rawLongitude = rawLongitude;
    }

    /**
     * Constructor given a GeoLocation
     * 
     * @param location
     */
    public AisPosition(Position location) {
        setLatitude(Math.round(location.getLatitude() * resolution * 60.0));
        setLongitude(Math.round(location.getLongitude() * resolution * 60.0));
    }

    /**
     * Set the resolution to be 25 and 24 bits for longitude and latitude respectively
     */
    public void set2524() {
        resolution = 1000.0;
        bitCorrection = 3;
    }

    /**
     * Set the raw latitude as received from AIS
     * 
     * @param rawLatitude
     */
    public void setRawLatitude(long rawLatitude) {
        this.rawLatitude = rawLatitude;
    }

    /**
     * Set the raw longitude as received from AIS
     * 
     * @param rawLatitude
     */
    public void setRawLongitude(long rawLongitude) {
        this.rawLongitude = rawLongitude;
    }

    public long getRawLatitude() {
        return rawLatitude;
    }

    public long getRawLongitude() {
        return rawLongitude;
    }

    /**
     * Set signed longitude
     */
    public void setLongitude(long longitude) {
        if (longitude < 0) {
            rawLongitude = longitude + (0x10000000 >> bitCorrection);
        } else {
            rawLongitude = longitude;
        }
    }

    /**
     * Set signed latitude
     */
    public void setLatitude(long latitude) {
        if (latitude < 0) {
            rawLatitude = latitude + (0x8000000 >> bitCorrection);
        } else {
            rawLatitude = latitude;
        }
    }

    /**
     * Get signed longitude
     * 
     * @return
     */
    public long getLongitude() {
        long longitude;
        if (rawLongitude >= 0x8000000 >> bitCorrection) {
            longitude = (0x10000000 >> bitCorrection) - rawLongitude;
            longitude *= -1;
        } else {
            longitude = rawLongitude;
        }
        return longitude;
    }

    /**
     * Get signed latitude
     * 
     * @return
     */
    public long getLatitude() {
        long latitude;
        if (rawLatitude >= 0x4000000 >> bitCorrection) {
            latitude = (0x8000000 >> bitCorrection) - rawLatitude;
            latitude *= -1;
        } else {
            latitude = rawLatitude;
        }
        return latitude;
    }

    /**
     * Get position as {@link Position} object
     * 
     * @return
     */
    public Position getGeoLocation() {
        return Position.create(getLatitude() / resolution / 60.0, getLongitude() / resolution / 60.0);
    }

    public Position tryGetGeoLocation() {
        double lat = getLatitude() / resolution / 60.0;
        double lon = getLongitude() / resolution / 60.0;
        if (Position.isValid(lat, lon)) {
            return Position.create(lat, lon);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bitCorrection;
        result = prime * result + (int) (rawLatitude ^ rawLatitude >>> 32);
        result = prime * result + (int) (rawLongitude ^ rawLongitude >>> 32);
        long temp;
        temp = Double.doubleToLongBits(resolution);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        AisPosition pos2 = (AisPosition) obj;
        return pos2.rawLatitude == this.rawLatitude && pos2.rawLongitude == this.rawLongitude;
    }

    @Override
    public String toString() {
        return "(" + getRawLatitude() + "," + getRawLongitude() + ") = (" + getLatitude() / resolution / 60.0 + ","
                + getLongitude() / resolution / 60.0 + ")";
    }

}
