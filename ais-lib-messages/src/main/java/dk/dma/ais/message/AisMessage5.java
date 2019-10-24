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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.Vdm;

/**
 * AIS message 4
 * <p>
 * Ship static and voyage related data as defined by ITU-R M.1371-4
 */
public class AisMessage5 extends AisStaticCommon {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * AIS version indicator: 0 = station compliant with Recommendation ITU-R M.1371-1 1 = station compliant with
     * Recommendation ITU-R M.1371-3 2-3 = station compliant with future editions
     */
    int version; // 2 bits

    /**
     * IMO number: 1-999999999; 0 = not available = default � Not applicable to SAR aircraft
     */
    long imo; // 30 bits

    /**
     * Type of electronic position fixing device: 0 = undefined (default) 1 = GPS 2 = GLONASS 3 = combined GPS/GLONASS 4
     * = Loran-C 5 = Chayka 6 = integrated navigation system 7 = surveyed 8 = Galileo, 9-14 = not used 15 = internal
     * GNSS
     */
    int posType; // 4 bits

    /**
     * ETA: Estimated time of arrival; MMDDHHMM UTC Bits 19-16: month; 1-12; 0 = not available = default Bits 15-11:
     * day; 1-31; 0 = not available = default Bits 10-6: hour; 0-23; 24 = not available = default Bits 5-0: minute;
     * 0-59; 60 = not available = default For SAR aircraft, the use of this field may be decided by the responsible
     * administration
     */
    long eta; // 20 bits

    /**
     * Maximum present static draught: In 1/10 m, 255 = draught 25.5 m or greater, 0 = not available = default in
     * accordance with IMO Resolution A.851 Not applicable to SAR aircraft, should be set to 0
     */
    int draught; // 8 bits

    /**
     * Ship Destination: Maximum 20 characters using 6-bit ASCII;
     *
     * {@code @@@@@@@@@@@@@@@@@@@@} = not available For SAR aircraft, the use of this field may be decided by the responsible                      administration
     */
    String dest; // 6x20 (120) bits

    /**
     * DTE: Data terminal equipment (DTE) ready 0 = available 1 = not available = default see � 3.3.1
     */
    int dte; // 1 bit : DTE flag

    /**
     * Spare. Not used. Should be set to zero. Reserved for future use
     */
    int spare; // 1 bit : spare

    /**
     * Instantiates a new Ais message 5.
     */
    public AisMessage5() {
        super(5);
    }

    /**
     * Instantiates a new Ais message 5.
     *
     * @param vdm the vdm
     * @throws AisMessageException the ais message exception
     * @throws SixbitException     the sixbit exception
     */
    public AisMessage5(Vdm vdm) throws AisMessageException, SixbitException {
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
        if (binArray.getLength() < 424) {
            throw new AisMessageException("Message 5 wrong length " + binArray.getLength());
        }

        super.parse(binArray);

        this.version = (int) binArray.getVal(2);
        this.imo = binArray.getVal(30);
        this.callsign = binArray.getString(7);
        this.name = binArray.getString(20);
        this.shipType = (int) binArray.getVal(8);
        this.dimBow = (int) binArray.getVal(9);
        this.dimStern = (int) binArray.getVal(9);
        this.dimPort = (int) binArray.getVal(6);
        this.dimStarboard = (int) binArray.getVal(6);
        this.posType = (int) binArray.getVal(4);
        this.eta = binArray.getVal(20);
        this.draught = (int) binArray.getVal(8);
        this.dest = binArray.getString(20);
        this.dte = (int) binArray.getVal(1);
        this.spare = (int) binArray.getVal(1);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(version, 2);
        encoder.addVal(imo, 30);
        encoder.addString(callsign, 7);
        encoder.addString(name, 20);
        encoder.addVal(shipType, 8);
        encoder.addVal(dimBow, 9);
        encoder.addVal(dimStern, 9);
        encoder.addVal(dimPort, 6);
        encoder.addVal(dimStarboard, 6);
        encoder.addVal(posType, 4);
        encoder.addVal(eta, 20);
        encoder.addVal(draught, 8);
        encoder.addString(dest, 20);
        encoder.addVal(dte, 1);
        encoder.addVal(spare, 1);
        return encoder;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Gets imo.
     *
     * @return the imo
     */
    public long getImo() {
        return imo;
    }

    /**
     * Sets imo.
     *
     * @param imo the imo
     */
    public void setImo(long imo) {
        this.imo = imo;
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
     * Gets eta.
     *
     * @return the eta
     */
    public long getEta() {
        return eta;
    }

    /**
     * Get ETA as date object
     *
     * @return date eta date
     */
    public Date getEtaDate() {
        int min = (int) (eta & 0x3F);
        int hour = (int) (eta & 0x7C0) >> 6;
        int day = (int) (eta & 0xF800) >> 11;
        int mon = (int) (eta & 0xF0000) >> 16;
        if (min == 60 || hour == 24 || day == 0 || mon == 0) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
        
		cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, mon - 1);
        
        return cal.getTime();
    }

    /**
     * Sets eta.
     *
     * @param eta the eta
     */
    public void setEta(long eta) {
        this.eta = eta;
    }

    /**
     * Gets draught.
     *
     * @return the draught
     */
    public int getDraught() {
        return draught;
    }

    /**
     * Sets draught.
     *
     * @param draught the draught
     */
    public void setDraught(int draught) {
        this.draught = draught;
    }

    /**
     * Gets dest.
     *
     * @return the dest
     */
    public String getDest() {
        return dest;
    }

    /**
     * Sets dest.
     *
     * @param dest the dest
     */
    public void setDest(String dest) {
        this.dest = dest;
    }

    /**
     * Gets dte.
     *
     * @return the dte
     */
    public int getDte() {
        return dte;
    }

    /**
     * Sets dte.
     *
     * @param dte the dte
     */
    public void setDte(int dte) {
        this.dte = dte;
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
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", callsign=");
        builder.append(callsign);
        builder.append(", dest=");
        builder.append(dest);
        builder.append(", dimBow=");
        builder.append(dimBow);
        builder.append(", dimPort=");
        builder.append(dimPort);
        builder.append(", dimStarboard=");
        builder.append(dimStarboard);
        builder.append(", dimStern=");
        builder.append(dimStern);
        builder.append(", draught=");
        builder.append(draught);
        builder.append(", dte=");
        builder.append(dte);
        builder.append(", eta=");
        builder.append(eta);
        builder.append(", imo=");
        builder.append(imo);
        builder.append(", name=");
        builder.append(name);
        builder.append(", posType=");
        builder.append(posType);
        builder.append(", shipType=");
        builder.append(shipType);
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", version=");
        builder.append(version);
        builder.append("]");
        return builder.toString();
    }

}
