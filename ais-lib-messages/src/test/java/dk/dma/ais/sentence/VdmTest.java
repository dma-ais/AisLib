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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessageException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VdmTest {

    @Test
    public void msgidTest() {
        Vdm vdm = new Vdm();
        int result = -1;
        try {
            result = vdm.parse("!AIVDM,1,1,,B,19NS7Sp02wo?HETKA2K6mUM20<L=,0*27\r\n");
            Assert.assertEquals("Adding message 1 failed", 0, result);
            Assert.assertEquals("Message ID wrong", 1, vdm.getMsgId());
            result = vdm.parse("JunkInFront!AIVDM,1,1,,B,19NS7Sp02wo?HETKA2K6mUM20<L=,0*27\r\n");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            return;
        }
    }

    @Test
    public void multipleTest() {
        Vdm vdm = new Vdm();
        int result = -1;
        try {
            result = vdm.parse("!AIVDM,1,1,,B,19NS7Sp02wo?HETKA2K6mUM20<L=,0*27\r\n");
            Assert.assertEquals("Adding message 1 failed", 0, result);
            vdm = new Vdm();
            result = vdm.parse("!AIVDM,2,1,6,B,55ArUT02:nkG<I8GB20nuJ0p5HTu>0hT9860TV16000006420BDi@E53,0*33\r\n");
            Assert.assertEquals("Adding message 5 part 1 failed", 1, result);
            result = vdm.parse("!AIVDM,2,2,6,B,1KUDhH888888880,2*6A");
            Assert.assertEquals("Adding message 5 part 1 failed", 0, result);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void parseTest() throws SentenceException, SixbitException, AisMessageException {
        String sentence = "!AIVDM,1,1,,,<>j?1GhlFfBPD5CDP6B?=P6BF,0*42";
        Vdm vdm = new Vdm();
        int result = vdm.parse(sentence);
        Assert.assertEquals("Adding message 12 failed", 0, result);
        Assert.assertEquals("Message ID wrong", 12, vdm.getMsgId());
        AisMessage12 msg = (AisMessage12) AisMessage.getInstance(vdm);
        System.out.println(msg);
        Assert.assertEquals("Wrong destination", 219593000, msg.getDestination());
        Assert.assertEquals("Wrong user id", 992199007, msg.getUserId());

    }

    @Test
    public void canParseTwice() throws SentenceException {
        String sentence = "!BSVDM,1,1,,B,33@nl?@01EPk<FDPw<2qW7`B07kh,0*5E";
        Vdm vdm = new Vdm();

        int parseResult = vdm.parse(sentence);

        assertThat(parseResult, is(0));

        int secondParseResult = vdm.parse(sentence);

        assertThat(secondParseResult, is(0));
    }
}
