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

import dk.dma.enav.model.geometry.Position;
import net.jcip.annotations.NotThreadSafe;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A tracking report is a unique, separate report concerning a specific vessel's current
 * position, speed, and course.
 * <p>
 * This piece of information is stored in the tracker to keep track of each vessel's movements
 * and whereabouts.
 */
@NotThreadSafe
public abstract class TrackingReport implements Cloneable {

    private Map<String, Object> properties = new HashMap<>(2);

    /**
     * Gets property.
     *
     * @param propertyName the property name
     * @return the property
     */
    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * Sets property.
     *
     * @param propertyName  the property name
     * @param propertyValue the property value
     */
    public void setProperty(String propertyName, Object propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    /**
     * Remove property.
     *
     * @param propertyName the property name
     */
    public void removeProperty(String propertyName) {
        properties.remove(propertyName);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        TrackingReport clone = (TrackingReport) super.clone();
        clone.properties = new HashMap<>(2);
        properties.forEach((k,v) -> properties.put(k,v));
        return clone;
    }

    // ---

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public abstract long getTimestamp();

    /**
     * Gets timestamp typed.
     *
     * @return the timestamp typed
     */
    public abstract LocalDateTime getTimestampTyped();

    /**
     * Gets position.
     *
     * @return the position
     */
    public abstract Position getPosition();

    /**
     * Gets course over ground.
     *
     * @return the course over ground
     */
    public abstract float getCourseOverGround();

    /**
     * Gets speed over ground.
     *
     * @return the speed over ground
     */
    public abstract float getSpeedOverGround();

    /**
     * Gets true heading.
     *
     * @return the true heading
     */
    public abstract float getTrueHeading();
}
