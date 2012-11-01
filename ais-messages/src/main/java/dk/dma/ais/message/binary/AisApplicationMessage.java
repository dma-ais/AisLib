/* Copyright (c) 2011 Danish Maritime Safety Administration
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
        if (binaryMessage.getDac() == 0 && binaryMessage.getFi() == 32) {
            return new RouteSuggestionReply(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == BroadcastIntendedRoute.DAC && binaryMessage.getFi() == BroadcastIntendedRoute.FI) {
            return new BroadcastIntendedRoute(binaryMessage.getData());
        }
        if (binaryMessage.getDac() == RouteSuggestion.DAC && binaryMessage.getFi() == RouteSuggestion.FI) {
            return new RouteSuggestion(binaryMessage.getData());
        }

        return new UnknownAsm(binaryMessage.getDac(), binaryMessage.getFi());
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
