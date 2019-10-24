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


/**
 * Interface for all messages carrying a the dimension of the sending target.
 */
public interface IDimensionMessage {

    /**
     * Gets dim bow.
     *
     * @return the dim bow
     */
    int getDimBow();

    /**
     * Gets dim stern.
     *
     * @return the dim stern
     */
    int getDimStern();

    /**
     * Gets dim port.
     *
     * @return the dim port
     */
    int getDimPort();

    /**
     * Gets dim starboard.
     *
     * @return the dim starboard
     */
    int getDimStarboard();

}
