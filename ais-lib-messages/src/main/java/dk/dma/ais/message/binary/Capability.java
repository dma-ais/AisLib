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
package dk.dma.ais.message.binary;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;

/**
 * Capability ASM
 */
public class Capability extends AisApplicationMessage {

    private int reqDac; // 10 bits
    private int spare; // 70 bits

    /**
     * Instantiates a new Capability.
     */
    public Capability() {
        super(1, 3);
    }

    /**
     * Instantiates a new Capability.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public Capability(BinArray binArray) throws SixbitException {
        super(1, 3, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(reqDac, 10);
        encoder.addVal(spare, 70);
        return encoder;
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.reqDac = (int) binArray.getVal(10);
    }

    /**
     * Gets req dac.
     *
     * @return the req dac
     */
    public int getReqDac() {
        return reqDac;
    }

    /**
     * Sets req dac.
     *
     * @param reqDac the req dac
     */
    public void setReqDac(int reqDac) {
        this.reqDac = reqDac;
    }

    /**
     * Gets spare.
     *
     * @return the spare
     */
    public int getSpare() {
        return spare;
    }

    /**
     * Sets spare.
     *
     * @param spare the spare
     */
    public void setSpare(int spare) {
        this.spare = spare;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Capability: reqDac=");
        builder.append(reqDac);
        builder.append("]");
        return builder.toString();
    }

}
