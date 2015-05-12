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
 * 
 * Aids-to-navigation report (AtoN) as defined by ITU-R M.1371-4
 * 
 */
public class AisMessage21 extends AisMessage implements IPositionMessage, IDimensionMessage, INameMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    int atonType; // 5 bits : Type of AtoN
    String name; // 120 bits : Name of AtoN in ASCII
    int posAcc; // 1 bit : AisPosition Accuracy
    AisPosition pos; // : Lat/Long 1/100000 minute
    int dimBow; // 9 bits : GPS Ant. Distance from Bow
    int dimStern; // 9 bits : GPS Ant. Distance from Stern
    int dimPort; // 6 bits : GPS Ant. Distance from Port
    int dimStarboard; // 6 bits : GPS Ant. Distance from Starboard
    int posType; // 4 bits : Type of AisPosition Fixing Device
    int utcSec; // 6 bits : UTC Seconds
    int offPosition; // 1 bit : Off AisPosition Flag
    int regional; // 8 bits : Regional Bits
    int raim; // 1 bit : RAIM Flag
    int virtual; // 1 bit : Virtual/Pseudo AtoN Flag
    int assigned; // 1 bit : Assigned Mode Flag
    int spare1; // 1 bit : Spare
    String nameExt; // 0-84 bits : Extended name in ASCII
    int spare2; // 0-6 bits : Spare

    public AisMessage21() {
        super(21);
    }

    public AisMessage21(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

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
        throw new UnsupportedOperationException();
    }

    public int getAtonType() {
        return atonType;
    }

    public void setAtonType(int atonType) {
        this.atonType = atonType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosAcc() {
        return posAcc;
    }

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

    public void setPos(AisPosition pos) {
        this.pos = pos;
    }

    public int getDimBow() {
        return dimBow;
    }

    public void setDimBow(int dimBow) {
        this.dimBow = dimBow;
    }

    public int getDimStern() {
        return dimStern;
    }

    public void setDimStern(int dimStern) {
        this.dimStern = dimStern;
    }

    public int getDimPort() {
        return dimPort;
    }

    public void setDimPort(int dimPort) {
        this.dimPort = dimPort;
    }

    public int getDimStarboard() {
        return dimStarboard;
    }

    public void setDimStarboard(int dimStarboard) {
        this.dimStarboard = dimStarboard;
    }

    public int getPosType() {
        return posType;
    }

    public void setPosType(int posType) {
        this.posType = posType;
    }

    public int getUtcSec() {
        return utcSec;
    }

    public void setUtcSec(int utcSec) {
        this.utcSec = utcSec;
    }

    public int getOffPosition() {
        return offPosition;
    }

    public void setOffPosition(int offPosition) {
        this.offPosition = offPosition;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getRaim() {
        return raim;
    }

    public void setRaim(int raim) {
        this.raim = raim;
    }

    public int getVirtual() {
        return virtual;
    }

    public void setVirtual(int virtual) {
        this.virtual = virtual;
    }

    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public int getSpare1() {
        return spare1;
    }

    public void setSpare1(int spare1) {
        this.spare1 = spare1;
    }

    public String getNameExt() {
        return nameExt;
    }

    public void setNameExt(String nameExt) {
        this.nameExt = nameExt;
    }

    public int getSpare2() {
        return spare2;
    }

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
