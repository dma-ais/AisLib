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
