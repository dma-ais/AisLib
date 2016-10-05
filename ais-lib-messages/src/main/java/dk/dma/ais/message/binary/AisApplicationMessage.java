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
 * Abstract base class for application specific messages
 */
public abstract class AisApplicationMessage {

    protected int dac; // 10 bits: Designated area code (DAC)
    protected int fi; // 6 bits: Function identifier

    public AisApplicationMessage(int dac, int fi) {
        this.dac = dac;
        this.fi = fi;
    }

    /**
     * Constructor that also parses six bit string
     * 
     * @param binArray
     * @throws BitExhaustionException
     * @throws SixbitException
     */
    public AisApplicationMessage(int dac, int fi, BinArray binArray) throws SixbitException {
        this.dac = dac;
        this.fi = fi;
        parse(binArray);
    }

    /**
     * Method to parse given six bit string
     * 
     * @param binArray
     * @throws BitExhaustionException
     */
    public abstract void parse(BinArray binArray) throws SixbitException;

    /**
     * Method that returns six bit encoding of message
     * 
     * @return
     */
    public abstract SixbitEncoder getEncoded();

    /**
     * Method to get application specific message from an {@link AisBinaryMessage}. When implementing new application
     * specific messages they should be added here.
     * 
     * @param binary
     *            message
     * @return application specific message or null if not implemented
     * @throws SixbitException
     * @throws BitExhaustionException
     */
    public static AisApplicationMessage getInstance(AisBinaryMessage binaryMessage) throws SixbitException {
        if (binaryMessage.getFi() == 3) {
            // Capability interrogation
            return new Capability(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 5) {
            return new AsmAcknowledge(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 11) {
            return new MetHyd11(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 22) {
            return new BroadcastAreaNotice(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 23) {
            return new AddressedAreaNotice(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 27) {
            return new BroadcastRouteInformation(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 28) {
            return new AddressedRouteInformation(binaryMessage.getData());
        }
		if (binaryMessage.getDac() == 1 && binaryMessage.getFi() == 31) {
            return new MetHyd31(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == 0 && binaryMessage.getFi() == 32) {
            return new RouteSuggestionReply(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == BroadcastIntendedRoute.DAC && binaryMessage.getFi() == BroadcastIntendedRoute.FI) {
            return new BroadcastIntendedRoute(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == RouteSuggestion.DAC && binaryMessage.getFi() == RouteSuggestion.FI) {
            return new RouteSuggestion(binaryMessage.getData());
        }
	if (binaryMessage.getDac() == 200 && binaryMessage.getFi() == 10) {
            return new InlandVoyage(binaryMessage.getData());
        }	

        return new UnknownAsm(binaryMessage);
    }

    public int getDac() {
        return dac;
    }

    public int getFi() {
        return fi;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("dac=");
        builder.append(dac);
        builder.append(", fi=");
        builder.append(fi);
        return builder.toString();
    }

}
