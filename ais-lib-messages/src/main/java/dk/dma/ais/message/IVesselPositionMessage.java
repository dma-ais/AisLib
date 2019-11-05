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
package dk.dma.ais.message;

import dk.dma.enav.model.geometry.Position;

/**
 * Interface to capture the communalities between message 1,2,3 and 18
 */
public interface IVesselPositionMessage extends IPositionMessage {

    /**
     * Speed over ground
     *
     * @return sog
     */
    int getSog();

    /**
     * Course over ground
     *
     * @return the cog
     */
    int getCog();

    /**
     * True heading
     *
     * @return true heading
     */
    int getTrueHeading();

    /**
     * Returns a valid position if this message has a valid position, otherwise null.
     *
     * @return a valid position if this message has a valid position, otherwise null
     */
    Position getValidPosition();

    /**
     * UTC sec
     *
     * @return utc sec
     */
    int getUtcSec();

    /**
     * Determine if position is valid
     *
     * @return boolean
     */
    boolean isPositionValid();

    /**
     * Course over ground valid
     *
     * @return boolean
     */
    boolean isCogValid();

    /**
     * Speed over ground valid
     *
     * @return boolean
     */
    boolean isSogValid();

    /**
     * Heading valid
     *
     * @return boolean
     */
    boolean isHeadingValid();

    /**
     * Get raim
     *
     * @return raim
     */
    int getRaim();

}
