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
package dk.dma.ais.proprietary;

import java.util.Date;

import dk.dma.enav.model.Country;

/**
 * Interface for proprietary source tags
 */
public interface IProprietarySourceTag extends IProprietaryTag {

    /**
     * Time of message receival at source
     *
     * @return timestamp
     */
    Date getTimestamp();

    /**
     * Country origin of message
     *
     * @return country
     */
    Country getCountry();

    /**
     * Unique region identifier
     *
     * @return region
     */
    String getRegion();

    /**
     * Base station MMSI
     *
     * @return base mmsi
     */
    Integer getBaseMmsi();

}
