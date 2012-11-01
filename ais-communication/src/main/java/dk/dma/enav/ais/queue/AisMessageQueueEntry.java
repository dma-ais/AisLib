/* Copyright (c) 2012 Danish Maritime Authority
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
package dk.dma.enav.ais.queue;

import java.io.Serializable;

import dk.dma.enav.ais.message.AisMessage;

/**
 * A message queue entry. Contains AisMessage and timestamp for insertion.
 */
public class AisMessageQueueEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private long timestamp;
    private AisMessage aisMessage;

    public AisMessageQueueEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    public AisMessageQueueEntry(AisMessage aisMessage) {
        this();
        this.aisMessage = aisMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public AisMessage getAisMessage() {
        return aisMessage;
    }

    public void setAisMessage(AisMessage aisMessage) {
        this.aisMessage = aisMessage;
    }

}
