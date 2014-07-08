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
package dk.dma.ais.packet;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.transform.AisPacketTaggingTransformer;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;
import dk.dma.enav.model.Country;

public class AisPacketTagsTest {

    @Test
    public void testPrepend() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacket.readFromString(msg);

        AisPacketTags tagging = new AisPacketTags();
        tagging.setSourceId("AISD");
        tagging.setSourceBs(999999999);
        tagging.setSourceCountry(Country.getByCode("SWE"));
        AisPacketTaggingTransformer transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, tagging);
        AisPacket newPacket = transformer.transform(packet);
        AisPacketTags newTagging = newPacket.getTags();

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), "AISD");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());

        packet = AisPacket.readFromString("\\key:oldval*51\\\r\n" + msg);
        tagging = new AisPacketTags();
        transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, tagging);
        transformer.getExtraTags().put("somekey", "someval");
        transformer.getExtraTags().put("key", "newval");
        newPacket = transformer.transform(packet);
        newTagging = newPacket.getTags();
        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), null);
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("somekey"), "someval");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("key"), "oldval");
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());

        msg = "\\s:AAUSAT3,c:1364272372,sub:2,bid:0,seq:231288,type:1,rssi:-72,freq:162000000*5E\\!AIVDM,1,1,,C,18153ogP?w1dD@@`JiRN4?wp0000,0*48";
        packet = AisPacket.readFromString(msg);
        tagging = new AisPacketTags();
        tagging.setSourceId("AIS-SAT");
        transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, tagging);
        newPacket = transformer.transform(packet);
        newTagging = newPacket.getTags();
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("si"), "AIS-SAT");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("s"), "AAUSAT3");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("sub"), "2");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("bid"), "0");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("seq"), "231288");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("type"), "1");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("rssi"), "-72");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("freq"), "162000000");
    }

    @Test
    public void testReplace() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacket.readFromString(msg);
        AisPacketTags tagging = new AisPacketTags();
        tagging.setSourceId("AISD");
        AisPacketTaggingTransformer transformer = new AisPacketTaggingTransformer(Policy.REPLACE, tagging);
        AisPacket newPacket = transformer.transform(packet);
        AisPacketTags newTagging = newPacket.getTags();

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), "AISD");
        Assert.assertEquals(newTagging.getSourceBs(), null);
        Assert.assertEquals(newTagging.getSourceCountry(), null);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());

        packet = AisPacket.readFromString("\\key:oldval*51\\\r\n" + msg);
        tagging = new AisPacketTags();
        transformer = new AisPacketTaggingTransformer(Policy.REPLACE, tagging);
        transformer.getExtraTags().put("somekey", "someval");
        transformer.getExtraTags().put("key", "newval");
        newPacket = transformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        newTagging = newPacket.getTags();
        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1363174860000L);
        Assert.assertEquals(newTagging.getSourceId(), null);
        Assert.assertEquals(newTagging.getSourceBs(), null);
        Assert.assertEquals(newTagging.getSourceCountry(), null);
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("somekey"), "someval");
        Assert.assertEquals(newPacket.getVdm().getCommentBlock().getString("key"), "newval");

    }

    @Test
    public void testMergeOverride() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\1G2:0125,c:1354719387,somekey:somevalue*07\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacket.readFromString(msg);

        AisPacketTags tagging = new AisPacketTags();
        tagging.setSourceId("bar");
        tagging.setSourceBs(999999999);

        AisPacketTaggingTransformer transformer = new AisPacketTaggingTransformer(Policy.MERGE_OVERRIDE, tagging);
        AisPacket newPacket = transformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        AisPacketTags newTagging = newPacket.getTags();
        CommentBlock cb = newPacket.getVdm().getCommentBlock();

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "bar");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 999999999);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        Assert.assertEquals(cb.getString("somekey"), "somevalue");

        packet = AisPacket.readFromString("\\key:oldval*51\\\r\n" + msg);
        tagging = new AisPacketTags();
        transformer = new AisPacketTaggingTransformer(Policy.MERGE_OVERRIDE, tagging);
        transformer.getExtraTags().put("somekey", "someval");
        transformer.getExtraTags().put("key", "newval");
        newPacket = transformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        newTagging = newPacket.getTags();
        cb = newPacket.getVdm().getCommentBlock();
        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "foo");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        Assert.assertEquals(cb.getString("somekey"), "someval");
        Assert.assertEquals(cb.getString("key"), "newval");
    }

    @Test
    public void testMergePreserve() throws SentenceException {
        String msg;
        msg = "\\si:foo,sb:2190047*78\\\r\n";
        msg += "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\1G2:0125,c:1354719387,somekey:somevalue*07\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n";
        msg += "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45";
        AisPacket packet = AisPacket.readFromString(msg);

        AisPacketTags tagging = new AisPacketTags();
        tagging.setSourceId("bar");
        tagging.setSourceBs(999999999);
        tagging.setSourceType(SourceType.SATELLITE);
        tagging.setSourceCountry(Country.getByCode("SWE"));

        AisPacketTaggingTransformer transformer = new AisPacketTaggingTransformer(Policy.MERGE_PRESERVE, tagging);
        AisPacket newPacket = transformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        AisPacketTags newTagging = newPacket.getTags();
        CommentBlock cb = newPacket.getVdm().getCommentBlock();

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "foo");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "SWE");
        Assert.assertEquals(cb.getString("somekey"), "somevalue");

        packet = AisPacket.readFromString("\\key:oldval*51\\\r\n" + msg);
        tagging = new AisPacketTags();
        transformer = new AisPacketTaggingTransformer(Policy.MERGE_PRESERVE, tagging);
        transformer.getExtraTags().put("somekey", "someval");
        transformer.getExtraTags().put("key", "newval");
        newPacket = transformer.transform(packet);
        System.out.println("NEW packet:\n" + newPacket.getStringMessage());
        newTagging = newPacket.getTags();
        cb = newPacket.getVdm().getCommentBlock();

        Assert.assertEquals(newTagging.getTimestamp().getTime(), 1354719387000L);
        Assert.assertEquals(newTagging.getSourceId(), "foo");
        Assert.assertEquals(newTagging.getSourceBs().intValue(), 2190047);
        Assert.assertEquals(newTagging.getSourceCountry().getThreeLetter(), "DNK");
        Assert.assertEquals(cb.getString("somekey"), "somevalue");
        Assert.assertEquals(cb.getString("key"), "oldval");

    }

}
