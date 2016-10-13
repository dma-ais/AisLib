/*
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

/**
 *
 * Subtype (DAC=200, FI=10) of AIS type 8 binary application message.
 * With title: Inland ship static and voyage related data
 * 
 * @author oteken
 */
public class InlandVoyage extends AisApplicationMessage{

    /** Unique European vessel identification number. */
    private String vesselId;  // 48 bits
    
    /** Length of ship 1-8000 in 0.1m. 0 = default. */
    private int lengthOfShip;  // 13 bits
    
    /** Beam of ship 1-1000 in 0.1m. 0 = default. */
    private int beamOfShip; // 10 bits
    
    /** Numeric ERI Classification. */
    private int combinationType; // 14 bits
    
    /** Number of blue cones 0-3. 4 = B-Flag, 5 = unknown. */
    private int hazardousCargo; // 3 bits
    
    /** Draught of ship 1-2000 in 0.01m. 0 = unknown. */
    private int draught; // 11 bits
    
    /** 
     * Status of load on ship. 
     * 1 = loaded, 2 = unloaded, 0 = not available/default. 3 should not be used. 
     */
    private int loadedOrUnloaded; // 2 bits
    
    /** 1 = high, 0 = low/GNSS/ default. */
    private int qualityOfSpeedData; // 1 bit
    
    /** 1 = high, 0 = low/GNSS/ default. */
    private int qualityOfCourseData; // 1 bit
    
    /** 1 = high, 0 = low/GNSS/ default. */
    private int qualityOfHeadingData; // 1 bit
    
    private int spare; // 8 bit

    public InlandVoyage(BinArray binArray) throws SixbitException {
        super(200, 10, binArray);
    }
    
    
    @Override
    public void parse(BinArray binArray) throws SixbitException {
        StringBuilder vesselIdBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int value = (int) binArray.getVal(6);
            vesselIdBuilder.append((char) value);
        }
        this.vesselId = vesselIdBuilder.toString();
        // remove all non numeric characters
        this.vesselId = this.vesselId.replaceAll("[^\\d.]", "");
        
        this.lengthOfShip = (int) binArray.getVal(13);
        this.beamOfShip = (int) binArray.getVal(10);
        this.combinationType = (int) binArray.getVal(14);
        this.hazardousCargo = (int) binArray.getVal(3);
        this.draught = (int) binArray.getVal(11);
        this.loadedOrUnloaded = (int) binArray.getVal(2);
        this.qualityOfSpeedData = (int) binArray.getVal(1);
        this.qualityOfCourseData = (int) binArray.getVal(1);
        this.qualityOfHeadingData = (int) binArray.getVal(1);
        this.spare = (int) binArray.getVal(8);
    }

    @Override
    public SixbitEncoder getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[InlandVoyage: lengthOfShip=");
        builder.append(lengthOfShip);
        builder.append(", beamOfShip=");
        builder.append(beamOfShip);
        builder.append(", combinationType=");
        builder.append(combinationType);
        builder.append(", hazardousCargo=");
        builder.append(hazardousCargo);
        builder.append(", draught=");
        builder.append(draught);
        builder.append(", loadedOrUnloaded=");
        builder.append(loadedOrUnloaded);
        builder.append(", qualityOfSpeedData=");
        builder.append(qualityOfSpeedData);
        builder.append(", qualityOfCourseData=");
        builder.append(qualityOfCourseData);
        builder.append(", qualityOfHeadingData=");
        builder.append(qualityOfHeadingData);
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", vesselId=");
        builder.append(vesselId);
        builder.append("]");
        return builder.toString();
    }

    public int getLengthOfShip() {
        return lengthOfShip;
    }

    public void setLengthOfShip(int lengthOfShip) {
        this.lengthOfShip = lengthOfShip;
    }

    public int getBeamOfShip() {
        return beamOfShip;
    }

    public void setBeamOfShip(int beamOfShip) {
        this.beamOfShip = beamOfShip;
    }

    public int getCombinationType() {
        return combinationType;
    }

    public void setCombinationType(int combinationType) {
        this.combinationType = combinationType;
    }

    public int getHazardousCargo() {
        return hazardousCargo;
    }

    public void setHazardousCargo(int hazardousCargo) {
        this.hazardousCargo = hazardousCargo;
    }

    public int getDraught() {
        return draught;
    }

    public void setDraught(int draught) {
        this.draught = draught;
    }

    public int getLoadedOrUnloaded() {
        return loadedOrUnloaded;
    }

    public void setLoadedOrUnloaded(int loadedOrUnloaded) {
        this.loadedOrUnloaded = loadedOrUnloaded;
    }

    public int getQualityOfSpeedData() {
        return qualityOfSpeedData;
    }

    public void setQualityOfSpeedData(int qualityOfSpeedData) {
        this.qualityOfSpeedData = qualityOfSpeedData;
    }

    public int getQualityOfCourseData() {
        return qualityOfCourseData;
    }

    public void setQualityOfCourseData(int qualityOfCourseData) {
        this.qualityOfCourseData = qualityOfCourseData;
    }

    public int getQualityOfHeadingData() {
        return qualityOfHeadingData;
    }

    public void setQualityOfHeadingData(int qualityOfHeadingData) {
        this.qualityOfHeadingData = qualityOfHeadingData;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public String getVesselId() {
        return vesselId;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }
}
