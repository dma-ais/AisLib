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
package dk.dma.enav.ais.message;

import dk.dma.enav.ais.binary.BinArray;
import dk.dma.enav.ais.binary.SixbitEncoder;
import dk.dma.enav.ais.binary.SixbitException;
import dk.dma.enav.ais.message.binary.AisApplicationMessage;
import dk.dma.enav.ais.sentence.Vdm;

/**
 * Abstract base class for binary AIS messages 6 and 8
 */
public abstract class AisBinaryMessage extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    protected int spare = 0;
    protected int dac; // 10 bits: Designated area code (DAC)
    protected int fi; // 6 bits: Function identifier
    protected BinArray data;
    protected AisApplicationMessage appMessage = null;

    /**
     * Construct empty binary message with msgId
     * 
     * @param msgId
     */
    public AisBinaryMessage(int msgId) {
        super(msgId);
    }

    /**
     * Construct binary message from VDM sentence
     * 
     * @param vdm
     */
    public AisBinaryMessage(Vdm vdm) {
        super(vdm);
    }

    /**
     * Get the application specific message, if it is implemented
     * 
     * @return application specific message
     * @throws BitExhaustionException
     */
    public AisApplicationMessage getApplicationMessage() throws SixbitException {
        if (appMessage != null) {
            return appMessage;
        }
        appMessage = AisApplicationMessage.getInstance(this);
        return appMessage;
    }

    protected SixbitEncoder encode() {
        SixbitEncoder encoder = super.encode();
        return encoder;
    }

    /**
     * Get binary data for this message
     * 
     * @return
     */
    public SixbitEncoder getBinaryData() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(dac, 10);
        encoder.addVal(fi, 6);
        encoder.append(appMessage.getEncoded());
        return encoder;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public int getDac() {
        return dac;
    }

    public void setDac(int dac) {
        this.dac = dac;
    }

    public int getFi() {
        return fi;
    }

    public void setFi(int fi) {
        this.fi = fi;
    }

    /**
     * Get the raw binary data of this message
     * 
     * @return
     */
    public BinArray getData() {
        return data;
    }

    /**
     * Set the raw binary data of this message
     * 
     * @param data
     */
    public void setData(BinArray data) {
        this.data = data;
    }

    /**
     * Get the application specific message contained in the sentence
     * 
     * @return
     */
    public AisApplicationMessage getAppMessage() {
        return appMessage;
    }

    /**
     * Set application specific message to be contained in this message
     * 
     * @param appMessage
     */
    public void setAppMessage(AisApplicationMessage appMessage) {
        this.dac = appMessage.getDac();
        this.fi = appMessage.getFi();
        this.appMessage = appMessage;
    }

    /**
     * Set values from ABM/BBM binary part
     * 
     * @param binArray
     * @throws SixbitException
     */
    public void setBinary(BinArray binArray) throws SixbitException {
        this.dac = (int) binArray.getVal(10);
        this.fi = (int) binArray.getVal(6);
        this.data = binArray;
        appMessage = AisApplicationMessage.getInstance(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", dac=");
        builder.append(dac);
        builder.append(", fi=");
        builder.append(fi);
        builder.append(", data=");
        builder.append(data);
        return builder.toString();
    }

}
