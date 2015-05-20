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
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.reader.SendRequest;
import dk.dma.ais.sentence.Abm;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class EncodeSendTest {

    @Test
    public void encodeSrmAbmTest() throws SendException, SixbitException {
        int destination = 992199007;
        String message = "START TEST TEST TEST END";

        AisMessage12 msg12 = new AisMessage12();
        msg12.setDestination(destination);
        msg12.setMessage(message);

        SendRequest sendRequest = new SendRequest(msg12, 1, destination);

        String[] sentences = sendRequest.createSentences();
        System.out.println("MSG12 SendRequest sentences:\n" + StringUtils.join(sentences, "\r\n"));

        Abm abm = new Abm();
        abm.setTextData(msg12);
        abm.setSequence(0);
        abm.setTotal(1);
        abm.setNum(1);
        String encoded = abm.getEncoded();
        System.out.println("MSG12 ABM encoded: " + encoded);
    }

}
