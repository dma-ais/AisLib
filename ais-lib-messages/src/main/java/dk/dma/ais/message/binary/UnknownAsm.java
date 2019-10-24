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
import dk.dma.ais.message.AisBinaryMessage;

/**
 * Dummy message that represents an unknown ASM
 */
public class UnknownAsm extends AisApplicationMessage {

    private BinArray binArray;

    /**
     * Instantiates a new Unknown asm.
     *
     * @param dac the dac
     * @param fi  the fi
     */
    public UnknownAsm(int dac, int fi) {
        super(dac, fi);
    }

    /**
     * Instantiates a new Unknown asm.
     *
     * @param binaryMessage the binary message
     */
    public UnknownAsm(AisBinaryMessage binaryMessage) {
        this(binaryMessage.getDac(), binaryMessage.getFi());
        this.binArray = binaryMessage.getData();        
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        this.binArray = binArray;
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.append(binArray);
        return encoder;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        if (binArray != null) {
            builder.append(", binary length = ");
            builder.append(binArray.size());
        }
        builder.append("]");
        return builder.toString();
    }

}
