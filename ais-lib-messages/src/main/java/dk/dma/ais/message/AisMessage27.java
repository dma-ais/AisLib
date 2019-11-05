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
 * AIS message 27
 * <p>
 * Long-range broadcast position report implemented according to ITU-R M.1371-4
 */
public class AisMessage27 extends AisMessage implements IPositionMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * As defined for Message 1.
     */
    private int posAcc; // 1 bit

    /**
     * As defined for Message 1.
     */
    private int raim; // 1 bit : RAIM flag

    /**
     * As defined for Message 1.
     */
    private int navStatus; // 4 bits

    /**
     * Longitude and latitude in 1/10 min.
     */
    private AisPosition pos;

    /**
     * Speed over ground (0-62 knots) 63 = not available = default.
     */
    private int sog; // 6 bits

    /**
     * Course over ground (0-359 degrees); 511 = not available = default.
     */
    private int cog; // 9 bits

    /**
     * Status of current GNSS position. 0 = Position is the current GNSS position; 1 = Reported position is not the current GNSS
     * position = default.
     */
    private int gnssPosStatus; // 1 bit

    /**
     * Spare set to zero to preserve byte boundaries.
     */
    private int spare; // 1 bit

    /**
     * Instantiates a new Ais message 27.
     */
    public AisMessage27() {
        super(27);
    }

    /**
     * Instantiates a new Ais message 27.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage27(Vdm vdm) throws AisMessageException, SixbitException {
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
        BinArray binArray = vdm.getBinArray();
        if (binArray.getLength() != 96) {
            throw new AisMessageException("Message 27 wrong length " + binArray.getLength());
        }
        super.parse(binArray);
        this.posAcc = (int) binArray.getVal(1);
        this.raim = (int) binArray.getVal(1);
        this.navStatus = (int) binArray.getVal(4);
        this.pos = new AisPosition();
        this.pos.setRawLongitude(binArray.getVal(18));
        this.pos.setRawLatitude(binArray.getVal(17));
        this.pos.set1817();
        this.sog = (int) binArray.getVal(6);
        this.cog = (int) binArray.getVal(9);
        this.gnssPosStatus = (int) binArray.getVal(1);
        this.spare = (int) binArray.getVal(1);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(posAcc, 1);
        encoder.addVal(raim, 1);
        encoder.addVal(navStatus, 4);
        encoder.addVal(this.pos.getRawLongitude(), 18);
        encoder.addVal(this.pos.getRawLatitude(), 17);
        encoder.addVal(sog, 6);
        encoder.addVal(cog, 9);
        encoder.addVal(gnssPosStatus, 1);
        encoder.addVal(spare, 1);
        return encoder;
    }

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPosAcc() {
        return posAcc;
    }

    /**
     * Sets pos acc.
     *
     * @param posAcc the pos acc
     */
    public void setPosAcc(int posAcc) {
        this.posAcc = posAcc;
    }

    /**
     * Gets raim.
     *
     * @return the raim
     */
    public int getRaim() {
        return raim;
    }

    /**
     * Sets raim.
     *
     * @param raim the raim
     */
    public void setRaim(int raim) {
        this.raim = raim;
    }

    /**
     * Gets nav status.
     *
     * @return the nav status
     */
    public int getNavStatus() {
        return navStatus;
    }

    /**
     * Sets nav status.
     *
     * @param navStatus the nav status
     */
    public void setNavStatus(int navStatus) {
        this.navStatus = navStatus;
    }

    public AisPosition getPos() {
        return pos;
    }

    /**
     * Sets pos.
     *
     * @param pos the pos
     */
    public void setPos(AisPosition pos) {
        this.pos = pos;
    }

    /**
     * Gets sog.
     *
     * @return the sog
     */
    public int getSog() {
        return sog;
    }

    /**
     * Sets sog.
     *
     * @param sog the sog
     */
    public void setSog(int sog) {
        this.sog = sog;
    }

    /**
     * Gets cog.
     *
     * @return the cog
     */
    public int getCog() {
        return cog;
    }

    /**
     * Sets cog.
     *
     * @param cog the cog
     */
    public void setCog(int cog) {
        this.cog = cog;
    }

    /**
     * Gets gnss pos status.
     *
     * @return the gnss pos status
     */
    public int getGnssPosStatus() {
        return gnssPosStatus;
    }

    /**
     * Sets gnss pos status.
     *
     * @param gnssPosStatus the gnss pos status
     */
    public void setGnssPosStatus(int gnssPosStatus) {
        this.gnssPosStatus = gnssPosStatus;
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
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(", ");
        builder.append("posAcc=").append(posAcc);
        builder.append(", raim=").append(raim);
        builder.append(", navStatus=").append(navStatus);
        builder.append(", pos=").append(pos);
        builder.append(", sog=").append(sog);
        builder.append(", cog=").append(cog);
        builder.append(", gnssPosStatus=").append(gnssPosStatus);
        builder.append(", spare=").append(spare);
        builder.append('}');
        return builder.toString();
    }
}
