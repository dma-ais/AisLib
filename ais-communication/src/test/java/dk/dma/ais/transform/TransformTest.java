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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTagging;
import dk.dma.ais.packet.AisPacketTagging.SourceType;
import dk.dma.ais.reader.AisPacketReader;
import dk.dma.ais.sentence.SentenceException;

public class TransformTest {

    @Test
    public void cropVdmTest() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n";
        msg += "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n";
        msg += "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A";
        AisPacket packet = AisPacketReader.from(msg);

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
        SourceTypeSatTransformer transformer = new SourceTypeSatTransformer();
        transformer.getSatGhRegions().add("802");
        transformer.getSatGhRegions().add("804");
        transformer.getSatSources().add("ORBCOMM999");
        
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
        AisPacket packet = AisPacketReader.from(msg);
        packet = trans.transform(packet);        
        AisPacketTagging tagging = AisPacketTagging.parse(packet);
        System.out.println("Sat transformed\n" + packet.getStringMessage());
        System.out.println("cb: " + packet.getVdm().getCommentBlock());
        System.out.println("tagging: " + tagging);
        return tagging.getSourceType() != null && tagging.getSourceType() == SourceType.SATELLITE;        
    }

}
