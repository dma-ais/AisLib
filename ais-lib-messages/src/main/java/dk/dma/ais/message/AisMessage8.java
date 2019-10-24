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
package dk.dma.ais.message;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.Vdm;

/**
 * AIS message 8
 * <p>
 * Binary broadcast message as defined by ITU-R M.1371-4
 */
public class AisMessage8 extends AisBinaryMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Ais message 8.
     */
    public AisMessage8() {
        super(8);
    }

    /**
     * Instantiates a new Ais message 8.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage8(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    /**
     * Parse.
     *
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        if (binArray.getLength() < 56 || binArray.getLength() > 1008) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + binArray.getLength());
        }
        super.parse(binArray);
        this.spare = (int) binArray.getVal(2);
        this.dac = (int) binArray.getVal(10);
        this.fi = (int) binArray.getVal(6);
        this.data = binArray;
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(spare, 2);
        encoder.addVal(dac, 10);
        encoder.addVal(fi, 6);
        encoder.append(appMessage.getEncoded());
        return encoder;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
