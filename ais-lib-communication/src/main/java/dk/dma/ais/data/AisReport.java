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
