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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage12;
import dk.dma.ais.reader.SendException;
import dk.dma.ais.reader.SendRequest;
import dk.dma.ais.sentence.Abm;

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
