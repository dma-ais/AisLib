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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.Vdm;

/**
 * AIS message 2
 * <p>
 * Assigned scheduled position report
 * <p>
 * This class handles the content of an AIS class A transponders general position report as defined by ITU-R M.1371-4.
 */
public class AisMessage2 extends AisMessage1 {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Ais message 2.
     */
    public AisMessage2() {
        super(2);
    }

    /**
     * Instantiates a new Ais message 2.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage2(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}
