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
 * AIS message 17
 * <p>
 * GNSS broadcast binary message
 */
public class AisMessage17 extends AisMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private int spare1; // 2 bits: Spare. Should be set to zero. Reserved for future use
    private int lon; // 18 bits: Surveyed longitude
    private int lat; // 17 bits Surveyed latitude
    private int spare2; // 5 bits: Not used. Should be set to zero. Reserved for future use
    private int messageType; // 6 bits: Recommendation ITU-R M.823
    private int stationId; // 10 bits: Recommendation ITU-R M.823 station identifier
    private int zCount; // 13 bits: Time value in 0.6 s (0-3 599.4)
    private int seqNum; // 3 bits: Message sequence number (cyclic 0-7)
    private int dataWordCount; // 5 bits: Number of DGNSS data words following the two word header, up to amaximum of 29
    private int health; // 3 bits: Reference station health (specified in Recommendation ITU-R M.823)
    private int[] dataWords; // 29 bit each: DGNSS message data words excluding parity

    /**
     * Instantiates a new Ais message 17.
     */
    public AisMessage17() {
        super(7);
    }

    /**
     * Instantiates a new Ais message 17.
     *
     * @param num the num
     */
    public AisMessage17(int num) {
        super(num);
    }

    /**
     * Instantiates a new Ais message 17.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage17(Vdm vdm) throws AisMessageException, SixbitException {
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
        BinArray sixbit = vdm.getBinArray();
        if (sixbit.getLength() < 80 || sixbit.getLength() > 816) {
            throw new AisMessageException("Message " + msgId + " wrong length: " + sixbit.getLength());
        }
        super.parse(sixbit);
        this.spare1 = (int) sixbit.getVal(2);
        this.lon = (int) sixbit.getVal(18);
        this.lat = (int) sixbit.getVal(17);
        this.spare2 = (int) sixbit.getVal(5);
        this.messageType = (int) sixbit.getVal(6);
        this.stationId = (int) sixbit.getVal(10);
        this.zCount = (int) sixbit.getVal(13);
        this.seqNum = (int) sixbit.getVal(3);
        this.dataWordCount = (int) sixbit.getVal(5);
        this.health = (int) sixbit.getVal(3);

        // How many datawords are actually in the remaining part
        // How to handle if length - 120 does not match the
        // indicated data word count

        this.dataWords = new int[this.dataWordCount];
        for (int i = 0; i < this.dataWordCount && sixbit.hasMoreBits(); i++) {
            this.dataWords[i] = (int) sixbit.getVal(24);
        }
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(spare1, 2);
        encoder.addVal(lon, 18);
        encoder.addVal(lat, 17);
        encoder.addVal(spare2, 5);
        encoder.addVal(messageType, 6);
        encoder.addVal(stationId, 10);
        encoder.addVal(zCount, 13);
        encoder.addVal(seqNum, 3);
        encoder.addVal(dataWordCount, 5);
        encoder.addVal(health, 3);
        for (int dataWord : dataWords)
            encoder.addVal(dataWord, 24);
        return encoder;
    }

    /**
     * Gets spare 1.
     *
     * @return the spare 1
     */
    public int getSpare1() {
        return spare1;
    }

    /**
     * Sets spare 1.
     *
     * @param spare1 the spare 1
     */
    public void setSpare1(int spare1) {
        this.spare1 = spare1;
    }

    /**
     * Gets lon.
     *
     * @return the lon
     */
    public int getLon() {
        return lon;
    }

    /**
     * Sets lon.
     *
     * @param lon the lon
     */
    public void setLon(int lon) {
        this.lon = lon;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public int getLat() {
        return lat;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(int lat) {
        this.lat = lat;
    }

    /**
     * Gets spare 2.
     *
     * @return the spare 2
     */
    public int getSpare2() {
        return spare2;
    }

    /**
     * Sets spare 2.
     *
     * @param spare2 the spare 2
     */
    public void setSpare2(int spare2) {
        this.spare2 = spare2;
    }

    /**
     * Gets message type.
     *
     * @return the message type
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Sets message type.
     *
     * @param messageType the message type
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * Gets station id.
     *
     * @return the station id
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * Sets station id.
     *
     * @param stationId the station id
     */
    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getzCount() {
        return zCount;
    }

    /**
     * Sets count.
     *
     * @param zCount the z count
     */
    public void setzCount(int zCount) {
        this.zCount = zCount;
    }

    /**
     * Gets seq num.
     *
     * @return the seq num
     */
    public int getSeqNum() {
        return seqNum;
    }

    /**
     * Sets seq num.
     *
     * @param seqNum the seq num
     */
    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    /**
     * Gets data word count.
     *
     * @return the data word count
     */
    public int getDataWordCount() {
        return dataWordCount;
    }

    /**
     * Sets data word count.
     *
     * @param dataWordCount the data word count
     */
    public void setDataWordCount(int dataWordCount) {
        this.dataWordCount = dataWordCount;
    }

    /**
     * Gets health.
     *
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets health.
     *
     * @param health the health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Get data words int [ ].
     *
     * @return the int [ ]
     */
    public int[] getDataWords() {
        return dataWords;
    }

    /**
     * Sets data words.
     *
     * @param dataWords the data words
     */
    public void setDataWords(int[] dataWords) {
        this.dataWords = dataWords;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", spare1=");
        builder.append(spare1);
        builder.append(", lon=");
        builder.append(lon);
        builder.append(", lat=");
        builder.append(lat);
        builder.append(", spare2=");
        builder.append(spare2);
        builder.append(", messageType=");
        builder.append(messageType);
        builder.append(", stationId=");
        builder.append(stationId);
        builder.append(", zCount=");
        builder.append(zCount);
        builder.append(", seqNum=");
        builder.append(seqNum);
        builder.append(", dataWordCount=");
        builder.append(dataWordCount);
        builder.append(", health=");
        builder.append(health);
        return builder.toString();
    }

}
