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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.Date;

import net.jcip.annotations.NotThreadSafe;
import dk.dma.ais.proprietary.DmaSourceTag;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.Country;

/**
 * Tags for an AisPacket. Encoded as comment blocks.
 */
@NotThreadSafe
public class AisPacketTagging {

    public enum SourceType {
        TERRESTRIAL, SATELLITE;
        public static SourceType fromString(String st) {
            if (st == null)
                return null;
            if (st.equalsIgnoreCase("LIVE")) {
                return TERRESTRIAL;
            } else if (st.equalsIgnoreCase("SAT")) {
                return SATELLITE;
            }
            return null;
        }
    }

    /**
     * Timestamp (comment block key: 'c', value: seconds since 1970)
     */
    private Date timestamp = null;
    /**
     * Source identifier (comment block key: 'si')
     */
    private String sourceId = null;
    /**
     * Source base station MMSI (comment block key: 'sb')
     */
    private Integer sourceBs = null;
    /**
     * Source country in ISO 3166 three letter code (comment block key: 'sc')
     */
    private Country sourceCountry = null;
    /**
     * Source type (comment block key: 'st', value: SAT | LIVE)
     */
    private SourceType sourceType = null;

    public AisPacketTagging() {

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getSourceBs() {
        return sourceBs;
    }

    public void setSourceBs(Integer sourceBs) {
        this.sourceBs = sourceBs;
    }

    public Country getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(Country sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    /**
     * Parse tags from AisPacket. Uses comment block with first priority and fall back to proprietary tags.
     * 
     * @param packet
     * @return tagging instance
     */
    public static AisPacketTagging parse(AisPacket packet) {
        requireNonNull(packet);
        AisPacketTagging tags = new AisPacketTagging();

        // Get timestamp
        tags.setTimestamp(packet.getTimestamp());
        // Get comment block
        Vdm vdm = packet.getVdm();
        CommentBlock cb = (vdm != null) ? vdm.getCommentBlock() : null;
        // Get from comment block
        if (cb != null) {
            tags.setSourceId(cb.getString("si"));
            tags.setSourceBs(cb.getInt("sb"));
            String cc = cb.getString("sc");
            if (cc != null) {
                tags.setSourceCountry(Country.getByCode(cc));
            }
            tags.setSourceType(SourceType.fromString(cb.getString("st")));
        }

        // Go through proprietary tags to set missing fields
        if (vdm == null || vdm.getTags() == null) {
            return tags;
        }
        for (IProprietaryTag tag : vdm.getTags()) {
            if (tags.getSourceId() == null && (tag instanceof DmaSourceTag)) {
                tags.setSourceId(((DmaSourceTag) tag).getSourceName());
            }
            if (tag instanceof IProprietarySourceTag) {
                IProprietarySourceTag sourceTag = (IProprietarySourceTag) tag;
                if (tags.getSourceBs() == null) {
                    tags.setSourceBs(sourceTag.getBaseMmsi());
                }
                if (tags.getSourceCountry() == null) {
                    tags.setSourceCountry(sourceTag.getCountry());
                }
            }
        }

        return tags;
    }

}
