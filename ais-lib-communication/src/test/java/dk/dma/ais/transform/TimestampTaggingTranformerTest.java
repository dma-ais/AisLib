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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.SentenceException;

public class TimestampTaggingTranformerTest {

    @Test
    public void gateHouseExistTest() throws SentenceException {
        String msg;
        msg = "$PGHP,1,2013,3,18,9,19,25,875,219,,2190048,1,55*19\r\n";
        msg += "!AIVDM,1,1,,B,139e17?P00PbH4TKr0<N4?v@R<08,0*28\r\n";

        AisPacket packet = AisPacket.readFromString(msg);

        TimestampTaggingTransformer tranformer = new TimestampTaggingTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 2);
        Assert.assertEquals(newPacket.getBestTimestamp(), packet.getBestTimestamp());
    }

    @Test
    public void timestampAddedTest() throws SentenceException {
        String msg;
        msg = "!BSVDO,1,1,,A,4025bp1ulq9CGPoQUBP5cJ10259<,0*7E\r\n";

        AisPacket packet = AisPacket.readFromString(msg);

        TimestampTaggingTransformer tranformer = new TimestampTaggingTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        System.out.println("Timestamp added: \n"+newPacket.getStringMessage());
        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 2);
        Assert.assertNotEquals(newPacket.getBestTimestamp(), packet.getBestTimestamp());
    }
    
    @Test
    public void timestampCommentBlockExists() throws SentenceException {
        String msg;
        msg = "\\c:1410516594*57\\\r\n";
        msg += "!BSVDO,1,1,,A,4025bp1ulq9CGPoQUBP5cJ10259<,0*7E\r\n";
        
        AisPacket packet = AisPacket.readFromString(msg);

        TimestampTaggingTransformer tranformer = new TimestampTaggingTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        System.out.println("Timestamp already exists: \n"+newPacket.getStringMessage());
        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 2);
        Assert.assertEquals(newPacket.getBestTimestamp(), packet.getBestTimestamp());
    }
    
    @Test
    public void timestampPGHPAndCommentBlockTest() throws SentenceException {
        String msg;
        msg = "\\si:AISSAT*3D\\r\n";
        msg +="$PGHP,1,2014,9,12,10,0,0,0,338,808,,1,5D*56\r\n";
        msg +="\\s:rORBCOMM000u,c:1410516000*5E\\!AIVDM,1,1,,B,344dGd5000R:KGpRB3g`Vjkl2000,0*5D\r\n";
        
        AisPacket packet = AisPacket.readFromString(msg);

        TimestampTaggingTransformer tranformer = new TimestampTaggingTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        System.out.println("Timestamp already exists in PGHP: \n"+newPacket.getStringMessage());
        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 3);
        Assert.assertEquals(newPacket.getBestTimestamp(), packet.getBestTimestamp());
        Assert.assertEquals(newPacket.getTags().getSourceId(),packet.getTags().getSourceId());
    }
    
    @Test
    public void commentBlockSourcePreservedAndTimestampAddedTest() throws SentenceException {
        String msg;
        msg = "\\si:AISSAT*3D\\r\n";
        msg +="!AIVDM,1,1,,B,344dGd5000R:KGpRB3g`Vjkl2000,0*5D\r\n";
        
        AisPacket packet = AisPacket.readFromString(msg);

        TimestampTaggingTransformer tranformer = new TimestampTaggingTransformer();
        AisPacket newPacket = tranformer.transform(packet);

        System.out.println("Commentblock sourceid preserved but timestamp added: \n"+newPacket.getStringMessage());
        List<String> lines = newPacket.getStringMessageLines();
        Assert.assertEquals(lines.size(), 2);
        Assert.assertNotEquals(newPacket.getBestTimestamp(), packet.getBestTimestamp());
        Assert.assertEquals(newPacket.getTags().getSourceId(),packet.getTags().getSourceId());
    }


        
    


}
