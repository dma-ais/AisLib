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
package dk.dma.ais.transform;

import junit.framework.Assert;

import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.packet.AisPacketTagging.SourceType;
import dk.dma.ais.reader.AisPacketReader;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;
import dk.dma.enav.model.Country;

public class AisTaggingTest {

    @Test
    public void testPrepend() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacketReader.from(msg);

        AisPacketTagging tagging = new AisPacketTagging();
        tagging.setSourceId("AISD");
        tagging.setSourceBs(999999999);
        tagging.setSourceCountry(Country.getByCode("SWE"));
        AisPacketTaggingTransformer tranformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, tagging);
        AisPacket newPacket = tranformer.transform(packet);
        AisPacketTagging newTagging = AisPacketTagging.parse(newPacket);

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), "AISD");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
    }

    @Test
    public void testReplace() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacketReader.from(msg);
        AisPacketTagging tagging = new AisPacketTagging();
        tagging.setSourceId("AISD");
        AisPacketTaggingTransformer tranformer = new AisPacketTaggingTransformer(Policy.REPLACE, tagging);
        AisPacket newPacket = tranformer.transform(packet);
        AisPacketTagging newTagging = AisPacketTagging.parse(newPacket);

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), "AISD");
        Assert.assertEquals(newTagging.getSourceBs(), null);
        Assert.assertEquals(newTagging.getSourceCountry(), null);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
    }
    
    @Test
    public void testMergeOverride() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\1G2:0125,c:1354719387,somekey:somevalue*07\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacketReader.from(msg);

        AisPacketTagging tagging = new AisPacketTagging();
        tagging.setSourceId("bar");
        tagging.setSourceBs(999999999);
        
        AisPacketTaggingTransformer tranformer = new AisPacketTaggingTransformer(Policy.MERGE_OVERRIDE, tagging);
        AisPacket newPacket = tranformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        AisPacketTagging newTagging = AisPacketTagging.parse(newPacket);
        CommentBlock cb = newPacket.getVdm().getCommentBlock();
        
        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "bar");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 999999999);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        Assert.assertEquals(cb.getString("somekey"), "somevalue");
    }
    
    @Test
    public void testMergePreserve() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\1G2:0125,c:1354719387,somekey:somevalue*07\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacketReader.from(msg);

        AisPacketTagging tagging = new AisPacketTagging();
        tagging.setSourceId("bar");
        tagging.setSourceBs(999999999);
        tagging.setSourceType(SourceType.SATELLITE);
        tagging.setSourceCountry(Country.getByCode("SWE"));
        
        AisPacketTaggingTransformer tranformer = new AisPacketTaggingTransformer(Policy.MERGE_PRESERVE, tagging);
        AisPacket newPacket = tranformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        AisPacketTagging newTagging = AisPacketTagging.parse(newPacket);
        CommentBlock cb = newPacket.getVdm().getCommentBlock();
        
        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "foo");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "SWE");
        Assert.assertEquals(cb.getString("somekey"), "somevalue");
    }
    
    

}
