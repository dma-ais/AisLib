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

public class SentenceLineTest {

    @Test
    public void sentenceLineTest() {
        SentenceLine sl;
        
        sl = new SentenceLine("\r\n");
        System.out.println("sl: " + sl.toString());
        
        sl = new SentenceLine("NON SENTENCE LINE");
        System.out.println("sl: " + sl.toString());        
        
        sl = new SentenceLine("$");
        System.out.println("sl: " + sl.toString());
        
        sl = new SentenceLine("$PDYP");
        System.out.println("sl: " + sl.toString());
                
        sl = new SentenceLine("$PGHP,1,2013,3,18,9,19,9,499,219,,2190048,1,28*23\r\n");
        System.out.println("sl: " + sl.toString());
        Assert.assertEquals(sl.getFields().size(), 15);
        Assert.assertEquals(sl.getPrefix(), "");
        Assert.assertTrue(sl.isChecksumMatch());
        
        sl = new SentenceLine("!AIVDM,2,1,6,B,55ArUT02:nkG<I8GB20nuJ0p5HTu>0hT9860TV16000006420BDi@E53,0*33\r\n");
        System.out.println("sl: " + sl.toString());
        Assert.assertTrue(sl.isChecksumMatch());
        Assert.assertEquals(sl.getPrefix(), "");
        Assert.assertEquals(sl.getFields().size(), 8);
        
        sl = new SentenceLine("\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n");
        System.out.println("sl: " + sl.toString());
        Assert.assertTrue(sl.isChecksumMatch());
        Assert.assertTrue(sl.getPrefix().length() > 0);
        Assert.assertEquals(sl.getFields().size(), 8);
        
        sl = new SentenceLine("!AIVDM,2,1,6,B,55ArUT02:nkG<I8GB20nuJ0p5HTu>0hT9860TV16000006420BDi@E53,0*\r\n");
        System.out.println("sl: " + sl.toString());
        Assert.assertFalse(sl.isChecksumMatch());
        Assert.assertEquals(sl.getPrefix(), "");
        Assert.assertEquals(sl.getFields().size(), 7);
    }

   
}
