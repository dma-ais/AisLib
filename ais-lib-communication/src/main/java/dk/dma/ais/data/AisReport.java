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
package dk.dma.ais.data;

import java.io.Serializable;
import java.util.Date;

import dk.dma.ais.message.AisMessage;

/**
 * Abstract class representing any AIS report
 */
public abstract class AisReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The Mmsi.
     */
    protected long mmsi;
    /**
     * The Received.
     */
    protected Date received;
    /**
     * The Source timestamp.
     */
    protected Date sourceTimestamp;
    /**
     * The Created.
     */
    protected Date created;

    /**
     * Instantiates a new Ais report.
     */
    public AisReport() {
        this.created = new Date();
    }

    /**
     * Update.
     *
     * @param aisMessage the ais message
     */
    public void update(AisMessage aisMessage) {
        this.mmsi = aisMessage.getUserId();
        this.received = new Date();
        this.sourceTimestamp = aisMessage.getVdm().getTimestamp();
    }

    /**
     * Gets received.
     *
     * @return the received
     */
    public Date getReceived() {
        return received;
    }

    /**
     * Sets received.
     *
     * @param received the received
     */
    public void setReceived(Date received) {
        this.received = received;
    }

    /**
     * Gets source timestamp.
     *
     * @return the source timestamp
     */
    public Date getSourceTimestamp() {
        return sourceTimestamp;
    }

    /**
     * Sets source timestamp.
     *
     * @param sourceTimestamp the source timestamp
     */
    public void setSourceTimestamp(Date sourceTimestamp) {
        this.sourceTimestamp = sourceTimestamp;
    }

    /**
     * Gets created.
     *
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets created.
     *
     * @param created the created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets mmsi.
     *
     * @return the mmsi
     */
    public long getMmsi() {
        return mmsi;
    }

    /**
     * Sets mmsi.
     *
     * @param mmsi the mmsi
     */
    public void setMmsi(long mmsi) {
        this.mmsi = mmsi;
    }

}
