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
import dk.dma.enav.model.geometry.Position;

/**
 * AIS message 21
 * <p>
 * Aids-to-navigation report (AtoN) as defined by ITU-R M.1371-4
 */
public class AisMessage21 extends AisMessage implements IPositionMessage, IDimensionMessage, INameMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The Aton type.
     */
    int atonType; // 5 bits : Type of AtoN
    /**
     * The Name.
     */
    String name; // 120 bits : Name of AtoN in ASCII
    /**
     * The Pos acc.
     */
    int posAcc; // 1 bit : AisPosition Accuracy
    /**
     * The Pos.
     */
    AisPosition pos; // : Lat/Long 1/100000 minute
    /**
     * The Dim bow.
     */
    int dimBow; // 9 bits : GPS Ant. Distance from Bow
    /**
     * The Dim stern.
     */
    int dimStern; // 9 bits : GPS Ant. Distance from Stern
    /**
     * The Dim port.
     */
    int dimPort; // 6 bits : GPS Ant. Distance from Port
    /**
     * The Dim starboard.
     */
    int dimStarboard; // 6 bits : GPS Ant. Distance from Starboard
    /**
     * The Pos type.
     */
    int posType; // 4 bits : Type of AisPosition Fixing Device
    /**
     * The Utc sec.
     */
    int utcSec; // 6 bits : UTC Seconds
    /**
     * The Off position.
     */
    int offPosition; // 1 bit : Off AisPosition Flag
    /**
     * The Regional.
     */
    int regional; // 8 bits : Regional Bits
    /**
     * The Raim.
     */
    int raim; // 1 bit : RAIM Flag
    /**
     * The Virtual.
     */
    int virtual; // 1 bit : Virtual/Pseudo AtoN Flag
    /**
     * The Assigned.
     */
    int assigned; // 1 bit : Assigned Mode Flag
    /**
     * The Spare 1.
     */
    int spare1; // 1 bit : Spare
    /**
     * The Name ext.
     */
    String nameExt; // 0-84 bits : Extended name in ASCII
    /**
     * The Spare 2.
     */
    int spare2; // 0-6 bits : Spare

    /**
     * Instantiates a new Ais message 21.
     */
    public AisMessage21() {
        super(21);
    }

    /**
     * Instantiates a new Ais message 21.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage21(Vdm vdm) throws AisMessageException, SixbitException {
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
        if (binArray.getLength() < 272 || binArray.getLength() > 360) {
            throw new AisMessageException("Message 21 wrong length " + binArray.getLength());
        }

        super.parse(binArray);

        this.atonType = (int) binArray.getVal(5);
        this.name = binArray.getString(20);
        this.posAcc = (int) binArray.getVal(1);

        this.pos = new AisPosition();
        this.pos.setRawLongitude(binArray.getVal(28));
        this.pos.setRawLatitude(binArray.getVal(27));

        this.dimBow = (int) binArray.getVal(9);
        this.dimStern = (int) binArray.getVal(9);
        this.dimPort = (int) binArray.getVal(6);
        this.dimStarboard = (int) binArray.getVal(6);
        this.posType = (int) binArray.getVal(4);
        this.utcSec = (int) binArray.getVal(6);
        this.offPosition = (int) binArray.getVal(1);
        this.regional = (int) binArray.getVal(8);
        this.raim = (int) binArray.getVal(1);
        this.virtual = (int) binArray.getVal(1);
        this.assigned = (int) binArray.getVal(1);
        this.spare1 = (int) binArray.getVal(1);

        if (binArray.getLength() > 272) {
            this.nameExt = binArray.getString((binArray.getLength() - 272) / 6);
        }

    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(atonType, 5);
        encoder.addString(name, 20);
        encoder.addVal(posAcc, 1);
        encoder.addVal(pos.getRawLongitude(), 28);
        encoder.addVal(pos.getRawLatitude(), 27);
        encoder.addVal(dimBow, 9);
        encoder.addVal(dimStern, 9);
        encoder.addVal(dimPort, 6);
        encoder.addVal(dimStarboard, 6);
        encoder.addVal(posType, 4);
        encoder.addVal(utcSec, 6);
        encoder.addVal(offPosition, 1);
        encoder.addVal(regional, 8);
        encoder.addVal(raim, 1);
        encoder.addVal(virtual, 1);
        encoder.addVal(assigned, 1);
        encoder.addVal(spare1, 1);

        if (nameExt != null)
            encoder.addString(nameExt, (vdm.getBinArray().getLength() - 272) / 6);
        return encoder;
    }

    /**
     * Gets aton type.
     *
     * @return the aton type
     */
    public int getAtonType() {
        return atonType;
    }

    /**
     * Sets aton type.
     *
     * @param atonType the aton type
     */
    public void setAtonType(int atonType) {
        this.atonType = atonType;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
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

    @Override
    public Position getValidPosition() {
        AisPosition pos = this.pos;
        return pos == null ? null : pos.getGeoLocation();
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

    public int getDimBow() {
        return dimBow;
    }

    /**
     * Sets dim bow.
     *
     * @param dimBow the dim bow
     */
    public void setDimBow(int dimBow) {
        this.dimBow = dimBow;
    }

    public int getDimStern() {
        return dimStern;
    }

    /**
     * Sets dim stern.
     *
     * @param dimStern the dim stern
     */
    public void setDimStern(int dimStern) {
        this.dimStern = dimStern;
    }

    public int getDimPort() {
        return dimPort;
    }

    /**
     * Sets dim port.
     *
     * @param dimPort the dim port
     */
    public void setDimPort(int dimPort) {
        this.dimPort = dimPort;
    }

    public int getDimStarboard() {
        return dimStarboard;
    }

    /**
     * Sets dim starboard.
     *
     * @param dimStarboard the dim starboard
     */
    public void setDimStarboard(int dimStarboard) {
        this.dimStarboard = dimStarboard;
    }

    /**
     * Gets pos type.
     *
     * @return the pos type
     */
    public int getPosType() {
        return posType;
    }

    /**
     * Sets pos type.
     *
     * @param posType the pos type
     */
    public void setPosType(int posType) {
        this.posType = posType;
    }

    /**
     * Gets utc sec.
     *
     * @return the utc sec
     */
    public int getUtcSec() {
        return utcSec;
    }

    /**
     * Sets utc sec.
     *
     * @param utcSec the utc sec
     */
    public void setUtcSec(int utcSec) {
        this.utcSec = utcSec;
    }

    /**
     * Gets off position.
     *
     * @return the off position
     */
    public int getOffPosition() {
        return offPosition;
    }

    /**
     * Sets off position.
     *
     * @param offPosition the off position
     */
    public void setOffPosition(int offPosition) {
        this.offPosition = offPosition;
    }

    /**
     * Gets regional.
     *
     * @return the regional
     */
    public int getRegional() {
        return regional;
    }

    /**
     * Sets regional.
     *
     * @param regional the regional
     */
    public void setRegional(int regional) {
        this.regional = regional;
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
     * Gets virtual.
     *
     * @return the virtual
     */
    public int getVirtual() {
        return virtual;
    }

    /**
     * Sets virtual.
     *
     * @param virtual the virtual
     */
    public void setVirtual(int virtual) {
        this.virtual = virtual;
    }

    /**
     * Gets assigned.
     *
     * @return the assigned
     */
    public int getAssigned() {
        return assigned;
    }

    /**
     * Sets assigned.
     *
     * @param assigned the assigned
     */
    public void setAssigned(int assigned) {
        this.assigned = assigned;
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
     * Gets name ext.
     *
     * @return the name ext
     */
    public String getNameExt() {
        return nameExt;
    }

    /**
     * Sets name ext.
     *
     * @param nameExt the name ext
     */
    public void setNameExt(String nameExt) {
        this.nameExt = nameExt;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", assigned=");
        builder.append(assigned);
        builder.append(", atonType=");
        builder.append(atonType);
        builder.append(", dimBow=");
        builder.append(dimBow);
        builder.append(", dimPort=");
        builder.append(dimPort);
        builder.append(", dimStarboard=");
        builder.append(dimStarboard);
        builder.append(", dimStern=");
        builder.append(dimStern);
        builder.append(", name=");
        builder.append(name);
        builder.append(", nameExt=");
        builder.append(nameExt);
        builder.append(", offPosition=");
        builder.append(offPosition);
        builder.append(", pos=");
        builder.append(pos);
        builder.append(", posAcc=");
        builder.append(posAcc);
        builder.append(", posType=");
        builder.append(posType);
        builder.append(", raim=");
        builder.append(raim);
        builder.append(", regional=");
        builder.append(regional);
        builder.append(", spare1=");
        builder.append(spare1);
        builder.append(", spare2=");
        builder.append(spare2);
        builder.append(", utcSec=");
        builder.append(utcSec);
        builder.append(", virtual=");
        builder.append(virtual);
        builder.append("]");
        return builder.toString();
    }

}
