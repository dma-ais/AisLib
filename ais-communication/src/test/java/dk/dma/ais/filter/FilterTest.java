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
package dk.dma.ais.filter;

import junit.framework.Assert;

import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.sentence.SentenceException;

public class FilterTest {

    @Test
    public void taggingFilterTest() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacket.readFromString(msg);
        AisPacketTags filterTagging = new AisPacketTags();
        filterTagging.setSourceType(SourceType.TERRESTRIAL);
        TaggingFilter filter = new TaggingFilter(filterTagging);
        Assert.assertFalse(filter.rejectedByFilter(packet));
    }

}
