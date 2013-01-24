/* Copyright (c) 2011 Danish Maritime Authority
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
package dk.dma.ais.message;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.Vdm;

/**
 * AIS message 17
 * 
 * GNSS broadcast binary message
 * 
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

    public AisMessage17() {
        super(7);
    }

    public AisMessage17(int num) {
        super(num);
    }

    public AisMessage17(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

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

        // TODO

        return encoder;
    }

    public int getSpare1() {
        return spare1;
    }

    public void setSpare1(int spare1) {
        this.spare1 = spare1;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getSpare2() {
        return spare2;
    }

    public void setSpare2(int spare2) {
        this.spare2 = spare2;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getzCount() {
        return zCount;
    }

    public void setzCount(int zCount) {
        this.zCount = zCount;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public int getDataWordCount() {
        return dataWordCount;
    }

    public void setDataWordCount(int dataWordCount) {
        this.dataWordCount = dataWordCount;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int[] getDataWords() {
        return dataWords;
    }

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
