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

import java.io.Serializable;

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketSource implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** We cache the hash. */
    private transient int hash;

    /** Source base station MMSI (comment block key: 'sb'), negative if no base station */
    private final int sourceBaseStation;

    /** Source country in ISO 3166 three letter code (comment block key: 'sc') */
    private final Country sourceCountry;

    /** Source identifier (comment block key: 'si') */
    private final String sourceId;

    /** Source region. */
    private final String sourceRegion;

    /** Source type (comment block key: 'st', value: SAT | LIVE) */
    private final SourceType sourceType;

    AisPacketSource(String sourceId, int sourceBaseStation, Country sourceCountry, SourceType sourceType,
            String sourceRegion) {
        this.sourceBaseStation = sourceBaseStation;
        this.sourceCountry = sourceCountry;
        this.sourceId = sourceId == null ? null : sourceId.intern();
        this.sourceType = sourceType;
        this.sourceRegion = sourceRegion == null ? null : sourceRegion.intern();
        this.hash = calcHash();
    }

    /** {@inheritDoc} */
    int calcHash() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sourceBaseStation;
        result = prime * result + (sourceCountry == null ? 0 : sourceCountry.hashCode());
        result = prime * result + (sourceId == null ? 0 : sourceId.hashCode());
        result = prime * result + (sourceRegion == null ? 0 : sourceRegion.hashCode());
        result = prime * result + (sourceType == null ? 0 : sourceType.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AisPacketSource other = (AisPacketSource) obj;
        if (sourceBaseStation != other.sourceBaseStation) {
            return false;
        }
        if (sourceCountry == null) {
            if (other.sourceCountry != null) {
                return false;
            }
        } else if (!sourceCountry.equals(other.sourceCountry)) {
            return false;
        }
        if (sourceId == null) {
            if (other.sourceId != null) {
                return false;
            }
        } else if (!sourceId.equals(other.sourceId)) {
            return false;
        }
        if (sourceRegion == null) {
            if (other.sourceRegion != null) {
                return false;
            }
        } else if (!sourceRegion.equals(other.sourceRegion)) {
            return false;
        }
        if (sourceType != other.sourceType) {
            return false;
        }
        return true;
    }

    /** Returns the base station source. */
    public int getSourceBaseStation() {
        return sourceBaseStation;
    }

    /**
     * @return the sourceCountry
     */
    public Country getSourceCountry() {
        return sourceCountry;
    }

    /**
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @return the sourceRegion
     */
    public String getSourceRegion() {
        return sourceRegion;
    }

    /**
     * @return the sourceType
     */
    public SourceType getSourceType() {
        return sourceType;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int h = hash;
        return h == 0 ? hash = calcHash() : h;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SourceBundle [sourceBaseStation=" + sourceBaseStation + ", sourceCountry="
                + (sourceCountry == null ? "null" : sourceCountry.getThreeLetter()) + ", sourceId=" + sourceId
                + ", sourceRegion=" + sourceRegion + ", sourceType=" + sourceType + "]";
    }

    public static AisPacketSource create(AisPacket packet) {
        AisPacketTags tags = packet.getTags();
        Integer sourceBaseStation = tags.getSourceBs();
        Country sourceCountry = tags.getSourceCountry();
        String sourceId = tags.getSourceId();
        SourceType sourceType = tags.getSourceType();

        IProprietarySourceTag sourceTag = packet.getVdm().getSourceTag();
        String region = sourceTag == null ? null : sourceTag.getRegion();

        return new AisPacketSource(sourceId, sourceBaseStation == null ? Integer.MIN_VALUE : sourceBaseStation,
                sourceCountry, sourceType, region);
    }

    public static Predicate<AisPacketSource> createPredicate(String expression) {
        return AisPacketSourceFilters.parseSourceFilter(expression);
    }
}
