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
package dk.dma.ais.configuration.bus;

import dk.dma.ais.bus.AisBusSocket;

/**
 * The type Ais bus socket configuration.
 */
public abstract class AisBusSocketConfiguration extends AisBusComponentConfiguration {

    private String name;
    private String description;

    /**
     * Instantiates a new Ais bus socket configuration.
     */
    public AisBusSocketConfiguration() {

    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Configure.
     *
     * @param socket the socket
     */
    protected void configure(AisBusSocket socket) {
        socket.setName(name);
        socket.setDescription(description);
        super.configure(socket);
    }

}
