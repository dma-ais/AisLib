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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.message.AisMessage4;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.sentence.Abm;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.geometry.Position;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EncodeTest {

    // private static final Logger LOG = Logger.getLogger(EncodeTest.class);

    /**
     * Method to do adhoc test of decode and encode
     */
    @Test
    public void rawEncodeDecodeTest() {
        Random rand = new Random(new Date().getTime());

        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            // Make random length list of bits length
            int fieldsCount = rand.nextInt(32);
            List<Integer> bitLengths = new ArrayList<>();
            List<Integer> values = new ArrayList<>(fieldsCount);
            for (int j = 0; j < fieldsCount; j++) {
                bitLengths.add(rand.nextInt(30) + 1);
            }
            // Make values
            for (int j = 0; j < fieldsCount; j++) {
                int maxVal = (int) Math.pow(2, bitLengths.get(j));
                values.add(rand.nextInt(maxVal));
            }

            // TODO
        }

    }

    /**
     * Decode VDM, re-encode, and compare messages
     * 
     */
    @Test
    public void vdmDecodeEncode() throws IOException, SentenceException, SixbitException, AisMessageException {

        URL url = ClassLoader.getSystemResource("decode_encode_messages.txt");
        Assert.assertNotNull(url);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Assert.assertNotNull(in);
            String line;
            Vdm vdm = new Vdm();
            while ((line = in.readLine()) != null) {
                int res = vdm.parse(new SentenceLine(line));
                if (res != 0) {
                    continue;
                }
                AisMessage msg = AisMessage.getInstance(vdm);
                Assert.assertNotNull(msg);
                vdm = new Vdm();

                // TODO Do encoding of new Vdm with decoded message and compare
                // messages

                // TODO Fill decode_encode_messages.txt with supported messages

            }
        }
    }

    @Test
    public void aisDecodeEncode() throws IOException, SentenceException, SixbitException, AisMessageException {

        URL url = ClassLoader.getSystemResource("decode_encode_messages.txt");
        Assert.assertNotNull(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        Assert.assertNotNull(in);
        String line;
        Vdm vdm = new Vdm();
        while ((line = in.readLine()) != null) {
            try {
                int res = vdm.parse(line);
                if (res != 0) {
                    continue;
                }

                AisMessage msg = AisMessage.getInstance(vdm);
                Assert.assertNotNull(msg);

                if (!(msg instanceof AisPositionMessage) || !(msg instanceof AisMessage4)
                        || !(msg instanceof AisStaticCommon)) {
                    vdm = new Vdm();
                    continue;
                }

                String expected = vdm.getSixbitString();
                String encoded = msg.getEncoded().encode();
                Assert.assertTrue(expected.equals(encoded));

                vdm = new Vdm();

            } catch (SixbitException e) {
                System.err.println("Failed line: " + line);
                throw e;
            }
        }
    }

    @Test
    public void posEncode() throws SixbitException {
        // Make and AisMessage1
        AisMessage1 msg1 = new AisMessage1();
        msg1.setRepeat(0);
        msg1.setUserId(265410000);
        msg1.setNavStatus(0);
        msg1.setRot(0);
        msg1.setSog(201);
        msg1.setPosAcc(1);
        AisPosition pos = new AisPosition(Position.create(55.0, 11.0));
        msg1.setPos(pos);
        msg1.setCog(732);
        msg1.setTrueHeading(76);
        msg1.setUtcSec(42);
        msg1.setSpecialManIndicator(0);
        msg1.setSpare(0);
        msg1.setRaim(0);
        msg1.setSyncState(0);
        msg1.setSlotTimeout(0);
        msg1.setSubMessage(2230);

        // Make VDM sentences
        String[] sentences = Vdm.createSentences(msg1, 1);
        System.out.println("POS VDM:\n" + StringUtils.join(sentences, "\n"));

    }

    @Test
    public void aisEncode() throws SentenceException, SixbitException, AisMessageException {
        // !BSVDM,1,1,,A,13u7El0039PkDmpPr?o2o2ID00Rn,0*12
        // msgId=1, repeat=0, userId=265410000, cog=732, navStatus=0,
        // pos=(34508764,6727356), posAcc=1, raim=0, regional=0, rot=0, sog=201,
        // spare=0, syncState=0,
        // trueHeading=76, utcSec=42, slotTimeout=0, subMessage=2230]
        AisMessage1 msg1 = new AisMessage1();
        msg1.setRepeat(0);
        msg1.setUserId(265410000);
        msg1.setNavStatus(0);
        msg1.setRot(0);
        msg1.setSog(201);
        msg1.setPosAcc(1);
        AisPosition pos = new AisPosition(34508764, 6727356);
        msg1.setPos(pos);
        msg1.setCog(732);
        msg1.setTrueHeading(76);
        msg1.setUtcSec(42);
        msg1.setSpecialManIndicator(0);
        msg1.setSpare(0);
        msg1.setRaim(0);
        msg1.setSyncState(0);
        msg1.setSlotTimeout(0);
        msg1.setSubMessage(2230);
        String encoded = msg1.getEncoded().encode();
        Assert.assertTrue(encoded.equals("13u7El0039PkDmpPr?o2o2ID00Rn"));

        // !AIVDM,2,1,4,A,<02:oP20ABCh@<51C5P31<<P:?2?EB7PDB16693P6?BPI?EBP3?=@E<C,0*78
        // !AIVDM,2,2,4,A,1BIPB5@?BDP?>P385>>1<PikPD81>;0,2*3C
        // [msgId=12, repeat=0, userId=2275200, destination=538003772,
        // message=PLEASE CALL JOBOURG TRAFFIC FOR YOUR COMPULSARY REPORT ON
        // CHENNAL 13 THANK, retransmit=0, seqNum=0, spare=0]
        Vdm vdm = new Vdm();
        Assert.assertEquals(1,
                vdm.parse("!AIVDM,2,1,4,A,<02:oP20ABCh@<51C5P31<<P:?2?EB7PDB16693P6?BPI?EBP3?=@E<C,0*78"));
        Assert.assertEquals(0, vdm.parse("!AIVDM,2,2,4,A,1BIPB5@?BDP?>P385>>1<PikPD81>;0,2*3C"));
        AisMessage12 msg12 = (AisMessage12) AisMessage.getInstance(vdm);

        String expected = "<02:oP20ABCh@<51C5P31<<P:?2?EB7PDB16693P6?BPI?EBP3?=@E<C1BIPB5@?BDP?>P385>>1<PikPD81>;0";
        encoded = msg12.getEncoded().encode();
        System.out.println("expected: " + expected);
        System.out.println("encoded : " + encoded);
        // Assert.assertTrue(expected.equals(encoded));

        // !AIVDM,2,1,1,B,53prCH028H5I=0O;W7I<n118>2373F222222220Q000005v:04Bkk3hTVQAp,0*06
        // !AIVDM,2,2,1,B,88888888880,2*26
        // [msgId=5, repeat=0, userId=261002080, callsign=SPG2916,
        // dest=KOLOBRZEG , dimBow=0, dimPort=0, dimStarboard=0, dimStern=0,
        // draught=17, dte=0, eta=516736, imo=8937558, name=SM PRC 105 ,
        // posType=1, shipType=33, spare=0, version=0]
        vdm = new Vdm();
        Assert.assertEquals(1,
                vdm.parse("!AIVDM,2,1,1,B,53prCH028H5I=0O;W7I<n118>2373F222222220Q000005v:04Bkk3hTVQAp,0*06"));
        Assert.assertEquals(0, vdm.parse("!AIVDM,2,2,1,B,88888888880,2*26"));
        AisMessage5 msg5 = (AisMessage5) AisMessage.getInstance(vdm);
        expected = "53prCH028H5I=0O;W7I<n118>2373F222222220Q000005v:04Bkk3hTVQAp88888888880";
        msg5.setDest("KOLOBRZEG");
        msg5.setName("SM PRC  105");
        encoded = msg5.getEncoded().encode();
        System.out.println("expected: " + expected);
        System.out.println("encoded : " + encoded);
        Assert.assertTrue(expected.equals(encoded));
    }

    /**
     * Test encoding ABM messages
     * 
     * @throws SentenceChecksumFailedException
     * @throws SentenceException
     * @throws SentenceStartNotFoundException
     * @throws AisMessageException
     * @throws BitExhaustionException
     */
    @Test
    public void abmEncode() throws SentenceException, SixbitException, AisMessageException {

        int destination = 992199007;
        AisMessage12 msg12 = new AisMessage12();
        msg12.setMessage("START TEST FROM DAMSA END");

        Abm abm = new Abm();
        abm.setTalker("AI");
        abm.setTotal(1);
        abm.setNum(1);
        abm.setDestination(destination);
        abm.setTextData(msg12);
        abm.setSequence(0);
        String encoded = abm.getEncoded();
        System.out.println("ABM msg12 for " + destination + ": " + encoded);

        abm = new Abm();
        abm.setTalker("AI");
        abm.setTotal(1);
        abm.setNum(1);
        abm.setDestination(219012679);
        abm.setTextData(msg12);
        abm.setSequence(0);
        encoded = abm.getEncoded();
        System.out.println("ABM msg12 for FRV TEST: " + encoded);

        abm = new Abm();
        abm.setTalker("AI");
        abm.setTotal(1);
        abm.setNum(1);
        abm.setDestination(219015063);
        abm.setTextData(msg12);
        abm.setSequence(0);
        encoded = abm.getEncoded();
        System.out.println("ABM msg12 for DAMSA1: " + encoded);

    }

    /**
     * Test encoding VDM/VDO messages
     * 
     * @throws BitExhaustionException
     * @throws IllegalArgumentException
     */
    @Test
    public void vdmEncode() throws SixbitException {
        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(219012679);
        msg12.setUserId(219015063);
        msg12.setMessage("TEST FROM FRV");
        msg12.setSeqNum(1);
        System.out.println(msg12);

        Vdm vdm = new Vdm();
        vdm.setTalker("AI");
        vdm.setTotal(1);
        vdm.setNum(1);
        vdm.setMessageData(msg12);
        vdm.setChannel('A');

        String encoded = vdm.getEncoded();
        String expected = "!AIVDM,1,1,,A,<3@oWUll=oTLD5CDP6B?=P6BF,0*7C";
        System.out.println("MSG 12 encoded : " + encoded);
        System.out.println("expected: " + expected);
        Assert.assertTrue(encoded.equals(expected));
    }

}
