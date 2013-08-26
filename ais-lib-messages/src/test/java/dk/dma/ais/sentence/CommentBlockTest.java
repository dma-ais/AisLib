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
package dk.dma.ais.sentence;

import org.junit.Assert;
import org.junit.Test;

public class CommentBlockTest {

    @Test
    public void parseTest() throws CommentBlockException {
        CommentBlock cb;
        
        cb = new CommentBlock();        
        cb.addLine("\\*00\\");
        Assert.assertEquals(cb.getSize(), 0);
        
        cb = new CommentBlock();        
        cb.addLine("\\c:1354719388*56\\");
        Assert.assertEquals(cb.getTimestamp().longValue(), 1354719388L); 
        
        cb = new CommentBlock();        
        cb.addLine("\\s:AAUSAT3,c:1364272372,sub:2,bid:0,seq:231288,type:1,rssi:-72,freq:162000000*5E\\!AIVDM,1,1,,C,18153ogP?w1dD@@`JiRN4?wp0000,0*48");
        Assert.assertEquals(cb.getTimestamp().longValue(), 1364272372L);
        Assert.assertEquals(cb.getString("s"), "AAUSAT3");
        Assert.assertEquals(cb.getInt("sub").intValue(), 2);
        Assert.assertEquals(cb.getInt("bid").intValue(), 0);
        Assert.assertEquals(cb.getInt("rssi").intValue(), -72);
        Assert.assertEquals(cb.getInt("type").intValue(), 1);
        Assert.assertEquals(cb.getInt("seq").intValue(), 231288);
        Assert.assertEquals(cb.getInt("freq").intValue(), 162000000);
        
    }
   

}
