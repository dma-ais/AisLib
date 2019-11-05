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

    /**
     * Instantiates a new Inland voyage.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
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

    /**
     * Gets length of ship.
     *
     * @return the length of ship
     */
    public int getLengthOfShip() {
        return lengthOfShip;
    }

    /**
     * Sets length of ship.
     *
     * @param lengthOfShip the length of ship
     */
    public void setLengthOfShip(int lengthOfShip) {
        this.lengthOfShip = lengthOfShip;
    }

    /**
     * Gets beam of ship.
     *
     * @return the beam of ship
     */
    public int getBeamOfShip() {
        return beamOfShip;
    }

    /**
     * Sets beam of ship.
     *
     * @param beamOfShip the beam of ship
     */
    public void setBeamOfShip(int beamOfShip) {
        this.beamOfShip = beamOfShip;
    }

    /**
     * Gets combination type.
     *
     * @return the combination type
     */
    public int getCombinationType() {
        return combinationType;
    }

    /**
     * Sets combination type.
     *
     * @param combinationType the combination type
     */
    public void setCombinationType(int combinationType) {
        this.combinationType = combinationType;
    }

    /**
     * Gets hazardous cargo.
     *
     * @return the hazardous cargo
     */
    public int getHazardousCargo() {
        return hazardousCargo;
    }

    /**
     * Sets hazardous cargo.
     *
     * @param hazardousCargo the hazardous cargo
     */
    public void setHazardousCargo(int hazardousCargo) {
        this.hazardousCargo = hazardousCargo;
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
     * Gets loaded or unloaded.
     *
     * @return the loaded or unloaded
     */
    public int getLoadedOrUnloaded() {
        return loadedOrUnloaded;
    }

    /**
     * Sets loaded or unloaded.
     *
     * @param loadedOrUnloaded the loaded or unloaded
     */
    public void setLoadedOrUnloaded(int loadedOrUnloaded) {
        this.loadedOrUnloaded = loadedOrUnloaded;
    }

    /**
     * Gets quality of speed data.
     *
     * @return the quality of speed data
     */
    public int getQualityOfSpeedData() {
        return qualityOfSpeedData;
    }

    /**
     * Sets quality of speed data.
     *
     * @param qualityOfSpeedData the quality of speed data
     */
    public void setQualityOfSpeedData(int qualityOfSpeedData) {
        this.qualityOfSpeedData = qualityOfSpeedData;
    }

    /**
     * Gets quality of course data.
     *
     * @return the quality of course data
     */
    public int getQualityOfCourseData() {
        return qualityOfCourseData;
    }

    /**
     * Sets quality of course data.
     *
     * @param qualityOfCourseData the quality of course data
     */
    public void setQualityOfCourseData(int qualityOfCourseData) {
        this.qualityOfCourseData = qualityOfCourseData;
    }

    /**
     * Gets quality of heading data.
     *
     * @return the quality of heading data
     */
    public int getQualityOfHeadingData() {
        return qualityOfHeadingData;
    }

    /**
     * Sets quality of heading data.
     *
     * @param qualityOfHeadingData the quality of heading data
     */
    public void setQualityOfHeadingData(int qualityOfHeadingData) {
        this.qualityOfHeadingData = qualityOfHeadingData;
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
     * Gets vessel id.
     *
     * @return the vessel id
     */
    public String getVesselId() {
        return vesselId;
    }

    /**
     * Sets vessel id.
     *
     * @param vesselId the vessel id
     */
    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }
}
