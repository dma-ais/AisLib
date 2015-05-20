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
package dk.dma.ais.encode;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.CommentBlock;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
