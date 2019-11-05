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
import dk.dma.ais.binary.SixbitException;

/**
 * Addressed area notice ASM
 */
public class AddressedAreaNotice extends AreaNotice {

    /**
     * Instantiates a new Addressed area notice.
     */
    public AddressedAreaNotice() {
        super(23);
    }

    /**
     * Instantiates a new Addressed area notice.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public AddressedAreaNotice(BinArray binArray) throws SixbitException {
        super(23, binArray);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AddressedAreaNotice [");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
