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

    protected long mmsi;
    protected Date received;
    protected Date sourceTimestamp;
    protected Date created;

    public AisReport() {
        this.created = new Date();
    }

    public void update(AisMessage aisMessage) {
        this.mmsi = aisMessage.getUserId();
        this.received = new Date();
        this.sourceTimestamp = aisMessage.getVdm().getTimestamp();
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public Date getSourceTimestamp() {
        return sourceTimestamp;
    }

    public void setSourceTimestamp(Date sourceTimestamp) {
        this.sourceTimestamp = sourceTimestamp;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getMmsi() {
        return mmsi;
    }

    public void setMmsi(long mmsi) {
        this.mmsi = mmsi;
    }

}
