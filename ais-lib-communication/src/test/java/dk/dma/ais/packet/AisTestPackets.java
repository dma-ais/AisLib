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

import dk.dma.ais.sentence.SentenceException;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * AisPacket is immutable hence methods instead of
 * 
 * @author Kasper Nielsen
 */
public class AisTestPackets {

    public static AisPacket p1() {
        return read(
                "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n",
                "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n",
                "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A");
    }

    public static AisPacket p2() {
        return read(
                "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n",
                "\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n",
                "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45");
    }

    public static AisPacket p3() {
        return read(
                "$PGHP,1,2010,6,11,11,46,11,929,244,0,,1,72*21\r\n",
                "\\si:AISD*3F\\\r\n",
                "\\1G2:0125,c:1354719387*0D\\!AIVDM,2,1,4,A,539LiHP2;42`@pE<000<tq@V1<TpL4000000001?1SV@@73R0J0TQCAD,0*1E\r\n",
                "\\2G2:0125*7B\\!AIVDM,2,2,4,A,R0EQCP000000000,2*45");
    }

    public static AisPacket p4() {
        return read(
                "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n",
                "\\g:1-2-0136,c:1363174860*24\\!BSVDM,2,1,4,B,53B>2V000000uHH4000@T4p4000000000000000S30C6340006h00000,0*4C\r\n",
                "\\g:2-2-0136*59\\!BSVDM,2,2,4,B,000000000000000,2*3A");
    }

    public static AisPacket p5() {
        return read(
                "$PGHP,1,2013,3,13,10,39,18,375,219,,2190047,1,4A*57\r\n",
                "\\g:1-2-0136,c:1363174860*24\\!BSVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*4A\r\n");
    }

    private static AisPacket read(String... lines) {
        try {
            return AisPacket.readFromString(StringUtils.join(lines));
        } catch (SentenceException e) {
            throw new Error(e);
        }
    }
}
