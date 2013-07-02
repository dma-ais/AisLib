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
package dk.dma.ais.packet;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.sentence.SentenceException;

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

    private static AisPacket read(String... lines) {
        try {
            return AisPacket.readFromString(StringUtils.join(lines));
        } catch (SentenceException e) {
            throw new Error(e);
        }
    }
}
