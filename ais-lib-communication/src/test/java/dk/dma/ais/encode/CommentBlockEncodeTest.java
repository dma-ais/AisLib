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
package dk.dma.ais.encode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.CommentBlock;

public class CommentBlockEncodeTest {

    @Test
    public void lineEncodeTest() {
        int pairs = 100;
        int maxLen = 80;
        Random rand = new Random();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < pairs; i++) {
            String key = Integer.toString(i);
            char c = (char) (65 + rand.nextInt(23));
            String value = StringUtils.leftPad("", rand.nextInt(70) + 1, c);
            map.put(key, value);
        }

        CommentBlock cb = new CommentBlock();
        for (Entry<String, String> pair : map.entrySet()) {
            cb.addString(pair.getKey(), pair.getValue());
        }

        String str = cb.encode(maxLen) + "\r\n";
        str += "!AIVDM,1,1,,A,H39LOOQUQPD0000000000000000,4*6D";

        AisPacket packet = AisPacket.from(str);
        cb = packet.getVdm().getCommentBlock();

        for (Entry<String, String> pair : map.entrySet()) {
            String value = cb.getString(pair.getKey());
            Assert.assertEquals(value, pair.getValue());
        }
    }

}
