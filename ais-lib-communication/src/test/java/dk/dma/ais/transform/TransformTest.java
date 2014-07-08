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
package dk.dma.ais.transform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;

public class TransformTest {

    @Test
    public void cropVdmTest() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacket.readFromString(msg);

        CropVdmTransformer tranformer = new CropVdmTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 2);
        Assert.assertTrue(lines.get(0).startsWith("!BSVDM"));
        Assert.assertTrue(lines.get(1).startsWith("!BSVDM"));
        System.out.println("Cropped packet: " + newPacket.getStringMessage());
    }

    @Test
    public void sourceTypeSatTest() throws SentenceException {

        Set<String> satGhRegions = new HashSet<>();
        satGhRegions.add("802");
        satGhRegions.add("804");
        Set<String> satSources = new HashSet<>();
        satSources.add("ORBCOMM999");
        SourceTypeSatTransformer transformer = new SourceTypeSatTransformer(satGhRegions, satSources);

        String msg;
        msg = "$PGHP,1,2013,3,21,10,13,55,0,338,808,,1,05*2D\r\n";
        msg += "\\g:1-2-9065,s:rORBCOMM000,c:1363860835*52\\!AIVDM,2,1,2,B,53P7rUP2=rKtli@SF20EHE:1<lThF22222222219JIIQ:4s:0LB0C@UDQp88,0*20\r\n";
        msg += "\\g:2-2-9065*57\\!AIVDM,2,2,2,B,88888888880,2*25";
        Assert.assertFalse(isSat(transformer, msg));

        msg = "$PGHP,1,2013,3,20,21,0,39,0,338,808,,1,4B*65\r\n";
        msg += "\\s:rORBCOMM999u,c:1363813239*54\\!AIVDM,1,1,,,19NSAwkOh1PHO:`34h4BLWs>00Rb,0*4B";
        Assert.assertTrue(isSat(transformer, msg));

        msg = "$PGHP,1,2013,3,21,10,35,54,607,211,802,,1,53*2A\r\n";
        msg += "!ABVDM,1,1,8,A,13P;mNP000Q1MQFNnutbWb=f05B<,0*53";
        Assert.assertTrue(isSat(transformer, msg));
    }

    private boolean isSat(IAisPacketTransformer trans, String msg) throws SentenceException {
        AisPacket packet = AisPacket.readFromString(msg);
        packet = trans.transform(packet);
        AisPacketTags tagging = packet.getTags();
        System.out.println("Sat transformed\n" + packet.getStringMessage());
        System.out.println("cb: " + packet.getVdm().getCommentBlock());
        System.out.println("tagging: " + tagging);
        return tagging.getSourceType() != null && tagging.getSourceType() == SourceType.SATELLITE;
    }

    @Test
    public void vdmVdoTest() throws SentenceException, AisMessageException, SixbitException {
        VdmVdoTransformer transformer = new VdmVdoTransformer(220431000);
        String msg;
        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacket.readFromString(msg);

        packet = transformer.transform(packet);
        System.out.println("VDO packet:\n" + packet.getStringMessage());

        Vdm vdm = packet.getVdm();
        Assert.assertNotNull(vdm);
        AisMessage message = packet.getAisMessage();
        Assert.assertNotNull(message);
        Assert.assertTrue(message.getVdm().isOwnMessage());
    }

}
