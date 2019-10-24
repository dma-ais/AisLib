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
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.sentence.Vdm;

/**
 * Abstract base class for binary AIS messages 6 and 8
 */
public abstract class AisBinaryMessage extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The Spare.
     */
    protected int spare;
    /**
     * The Dac.
     */
    protected int dac; // 10 bits: Designated area code (DAC)
    /**
     * The Fi.
     */
    protected int fi; // 6 bits: Function identifier
    /**
     * The Data.
     */
    protected BinArray data;
    /**
     * The App message.
     */
    protected AisApplicationMessage appMessage;

    /**
     * Construct empty binary message with msgId
     *
     * @param msgId the msg id
     */
    public AisBinaryMessage(int msgId) {
        super(msgId);
    }

    /**
     * Construct binary message from VDM sentence
     *
     * @param vdm the vdm
     */
    public AisBinaryMessage(Vdm vdm) {
        super(vdm);
    }

    /**
     * Get the application specific message, if it is implemented
     *
     * @return application specific message
     * @throws SixbitException the sixbit exception
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
     * @return binary data
     */
    public SixbitEncoder getBinaryData() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(dac, 10);
        encoder.addVal(fi, 6);
        encoder.append(appMessage.getEncoded());
        return encoder;
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

    /**
     * Gets dac.
     *
     * @return the dac
     */
    public int getDac() {
        return dac;
    }

    /**
     * Sets dac.
     *
     * @param dac the dac
     */
    public void setDac(int dac) {
        this.dac = dac;
    }

    /**
     * Gets fi.
     *
     * @return the fi
     */
    public int getFi() {
        return fi;
    }

    /**
     * Sets fi.
     *
     * @param fi the fi
     */
    public void setFi(int fi) {
        this.fi = fi;
    }

    /**
     * Get the raw binary data of this message
     *
     * @return data
     */
    public BinArray getData() {
        return data;
    }

    /**
     * Set the raw binary data of this message
     *
     * @param data the data
     */
    public void setData(BinArray data) {
        this.data = data;
    }

    /**
     * Get the application specific message contained in the sentence
     *
     * @return app message
     */
    public AisApplicationMessage getAppMessage() {
        return appMessage;
    }

    /**
     * Set application specific message to be contained in this message
     *
     * @param appMessage the app message
     */
    public void setAppMessage(AisApplicationMessage appMessage) {
        this.dac = appMessage.getDac();
        this.fi = appMessage.getFi();
        this.appMessage = appMessage;
    }

    /**
     * Set values from ABM/BBM binary part
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
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
