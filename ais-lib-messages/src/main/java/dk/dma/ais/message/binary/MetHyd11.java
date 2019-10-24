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
package dk.dma.ais.message.binary;

import dk.dma.ais.binary.BinArray;
import dk.dma.ais.binary.SixbitEncoder;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisPosition;

/**
 * The type Met hyd 11.
 */
public class MetHyd11 extends AisApplicationMessage {

    private AisPosition pos;
    private int utcDay; // 5 bits : UTC Day
    private int utcHour; // 5 bits : UTC Hour
    private int utcMinute; // 6 bits : UTC Minute
    private int wind; // 7 bits
    private int gust; // 7 bits
    private int windDirection; // 9 bits
    private int gustDirection; // 9 bits
    private int airTemp; // 11 bits
    private int humidity; // 7 bits
    private int dewPoint; // 10 bits
    private int airPressure; // 9 bits
    private int airPressureTend; // 2 bits
    private int horzVisibility; // 8 bits
    private int waterLevel; // 9 bits
    private int waterLevelTrend; // 2 bits
    private int surfaceCurrent; // 8 bits
    private int surfaceCurrentDir; // 9 bits
    private int secondCurrent; // 8 bits
    private int secondCurrentDir; // 9 bits
    private int secondCurrentLevel; // 5 bits
    private int thirdCurrent; // 8 bits
    private int thirdCurrentDir; // 9 bits
    private int thirdCurrentLevel; // 5 bits
    private int waveHeight; // 8 bits
    private int wavePeriod; // 6 bits
    private int waveDirection; // 9 bits
    private int swellHeight; // 8 bits
    private int swellPeriod; // 6 bits
    private int swellDirection; // 9 bits
    private int seaState; // 4 bits
    private int waterTemp; // 10 bits
    private int precipitation; // 3 bits
    private int salinity; // 9 bits
    private int ice; // 2 bits
    private int spare; // 6 bits;

    /**
     * Instantiates a new Met hyd 11.
     *
     * @param binArray the bin array
     * @throws SixbitException the sixbit exception
     */
    public MetHyd11(BinArray binArray) throws SixbitException {
        super(1, 11, binArray);
    }

    @Override
    public SixbitEncoder getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void parse(BinArray binArray) throws SixbitException {
        pos = new AisPosition();
        pos.set2524();
        this.pos.setRawLatitude(binArray.getVal(24));
        this.pos.setRawLongitude(binArray.getVal(25));
        this.utcDay = (int) binArray.getVal(5);
        this.utcHour = (int) binArray.getVal(5);
        this.utcMinute = (int) binArray.getVal(6);
        this.wind = (int) binArray.getVal(7);
        this.gust = (int) binArray.getVal(7);
        this.windDirection = (int) binArray.getVal(9);
        this.gustDirection = (int) binArray.getVal(9);
        this.airTemp = (int) binArray.getVal(11);
        this.humidity = (int) binArray.getVal(7);
        this.dewPoint = (int) binArray.getVal(10);
        this.airPressure = (int) binArray.getVal(9);
        this.airPressureTend = (int) binArray.getVal(2);
        this.horzVisibility = (int) binArray.getVal(8);
        this.waterLevel = (int) binArray.getVal(9);
        this.waterLevelTrend = (int) binArray.getVal(2);
        this.surfaceCurrent = (int) binArray.getVal(8);
        this.surfaceCurrentDir = (int) binArray.getVal(9);
        this.secondCurrent = (int) binArray.getVal(8);
        this.secondCurrentDir = (int) binArray.getVal(9);
        this.secondCurrentLevel = (int) binArray.getVal(5);
        this.thirdCurrent = (int) binArray.getVal(8);
        this.thirdCurrentDir = (int) binArray.getVal(9);
        this.thirdCurrentLevel = (int) binArray.getVal(5);
        this.waveHeight = (int) binArray.getVal(8);
        this.wavePeriod = (int) binArray.getVal(6);
        this.waveDirection = (int) binArray.getVal(9);
        this.swellHeight = (int) binArray.getVal(8);
        this.swellPeriod = (int) binArray.getVal(6);
        this.swellDirection = (int) binArray.getVal(9);
        this.seaState = (int) binArray.getVal(4);
        this.waterTemp = (int) binArray.getVal(10);
        this.precipitation = (int) binArray.getVal(3);
        this.salinity = (int) binArray.getVal(9);
        this.ice = (int) binArray.getVal(2);
        this.spare = (int) binArray.getVal(6);
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
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
     * Gets utc day.
     *
     * @return the utc day
     */
    public int getUtcDay() {
        return utcDay;
    }

    /**
     * Sets utc day.
     *
     * @param utcDay the utc day
     */
    public void setUtcDay(int utcDay) {
        this.utcDay = utcDay;
    }

    /**
     * Gets utc hour.
     *
     * @return the utc hour
     */
    public int getUtcHour() {
        return utcHour;
    }

    /**
     * Sets utc hour.
     *
     * @param utcHour the utc hour
     */
    public void setUtcHour(int utcHour) {
        this.utcHour = utcHour;
    }

    /**
     * Gets utc minute.
     *
     * @return the utc minute
     */
    public int getUtcMinute() {
        return utcMinute;
    }

    /**
     * Sets utc minute.
     *
     * @param utcMinute the utc minute
     */
    public void setUtcMinute(int utcMinute) {
        this.utcMinute = utcMinute;
    }

    /**
     * Gets wind.
     *
     * @return the wind
     */
    public int getWind() {
        return wind;
    }

    /**
     * Sets wind.
     *
     * @param wind the wind
     */
    public void setWind(int wind) {
        this.wind = wind;
    }

    /**
     * Gets gust.
     *
     * @return the gust
     */
    public int getGust() {
        return gust;
    }

    /**
     * Sets gust.
     *
     * @param gust the gust
     */
    public void setGust(int gust) {
        this.gust = gust;
    }

    /**
     * Gets wind direction.
     *
     * @return the wind direction
     */
    public int getWindDirection() {
        return windDirection;
    }

    /**
     * Sets wind direction.
     *
     * @param windDirection the wind direction
     */
    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * Gets gust direction.
     *
     * @return the gust direction
     */
    public int getGustDirection() {
        return gustDirection;
    }

    /**
     * Sets gust direction.
     *
     * @param gustDirection the gust direction
     */
    public void setGustDirection(int gustDirection) {
        this.gustDirection = gustDirection;
    }

    /**
     * Gets air temp.
     *
     * @return the air temp
     */
    public int getAirTemp() {
        return airTemp;
    }

    /**
     * Sets air temp.
     *
     * @param airTemp the air temp
     */
    public void setAirTemp(int airTemp) {
        this.airTemp = airTemp;
    }

    /**
     * Gets humidity.
     *
     * @return the humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Sets humidity.
     *
     * @param humidity the humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     * Gets dew point.
     *
     * @return the dew point
     */
    public int getDewPoint() {
        return dewPoint;
    }

    /**
     * Sets dew point.
     *
     * @param dewPoint the dew point
     */
    public void setDewPoint(int dewPoint) {
        this.dewPoint = dewPoint;
    }

    /**
     * Gets air pressure.
     *
     * @return the air pressure
     */
    public int getAirPressure() {
        return airPressure;
    }

    /**
     * Sets air pressure.
     *
     * @param airPressure the air pressure
     */
    public void setAirPressure(int airPressure) {
        this.airPressure = airPressure;
    }

    /**
     * Gets air pressure tend.
     *
     * @return the air pressure tend
     */
    public int getAirPressureTend() {
        return airPressureTend;
    }

    /**
     * Sets air pressure tend.
     *
     * @param airPressureTend the air pressure tend
     */
    public void setAirPressureTend(int airPressureTend) {
        this.airPressureTend = airPressureTend;
    }

    /**
     * Gets horz visibility.
     *
     * @return the horz visibility
     */
    public int getHorzVisibility() {
        return horzVisibility;
    }

    /**
     * Sets horz visibility.
     *
     * @param horzVisibility the horz visibility
     */
    public void setHorzVisibility(int horzVisibility) {
        this.horzVisibility = horzVisibility;
    }

    /**
     * Gets water level.
     *
     * @return the water level
     */
    public int getWaterLevel() {
        return waterLevel;
    }

    /**
     * Sets water level.
     *
     * @param waterLevel the water level
     */
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    /**
     * Gets water level trend.
     *
     * @return the water level trend
     */
    public int getWaterLevelTrend() {
        return waterLevelTrend;
    }

    /**
     * Sets water level trend.
     *
     * @param waterLevelTrend the water level trend
     */
    public void setWaterLevelTrend(int waterLevelTrend) {
        this.waterLevelTrend = waterLevelTrend;
    }

    /**
     * Gets surface current.
     *
     * @return the surface current
     */
    public int getSurfaceCurrent() {
        return surfaceCurrent;
    }

    /**
     * Sets surface current.
     *
     * @param surfaceCurrent the surface current
     */
    public void setSurfaceCurrent(int surfaceCurrent) {
        this.surfaceCurrent = surfaceCurrent;
    }

    /**
     * Gets surface current dir.
     *
     * @return the surface current dir
     */
    public int getSurfaceCurrentDir() {
        return surfaceCurrentDir;
    }

    /**
     * Sets surface current dir.
     *
     * @param surfaceCurrentDir the surface current dir
     */
    public void setSurfaceCurrentDir(int surfaceCurrentDir) {
        this.surfaceCurrentDir = surfaceCurrentDir;
    }

    /**
     * Gets second current.
     *
     * @return the second current
     */
    public int getSecondCurrent() {
        return secondCurrent;
    }

    /**
     * Sets second current.
     *
     * @param secondCurrent the second current
     */
    public void setSecondCurrent(int secondCurrent) {
        this.secondCurrent = secondCurrent;
    }

    /**
     * Gets second current dir.
     *
     * @return the second current dir
     */
    public int getSecondCurrentDir() {
        return secondCurrentDir;
    }

    /**
     * Sets second current dir.
     *
     * @param secondCurrentDir the second current dir
     */
    public void setSecondCurrentDir(int secondCurrentDir) {
        this.secondCurrentDir = secondCurrentDir;
    }

    /**
     * Gets second current level.
     *
     * @return the second current level
     */
    public int getSecondCurrentLevel() {
        return secondCurrentLevel;
    }

    /**
     * Sets second current level.
     *
     * @param secondCurrentLevel the second current level
     */
    public void setSecondCurrentLevel(int secondCurrentLevel) {
        this.secondCurrentLevel = secondCurrentLevel;
    }

    /**
     * Gets third current.
     *
     * @return the third current
     */
    public int getThirdCurrent() {
        return thirdCurrent;
    }

    /**
     * Sets third current.
     *
     * @param thirdCurrent the third current
     */
    public void setThirdCurrent(int thirdCurrent) {
        this.thirdCurrent = thirdCurrent;
    }

    /**
     * Gets third current dir.
     *
     * @return the third current dir
     */
    public int getThirdCurrentDir() {
        return thirdCurrentDir;
    }

    /**
     * Sets third current dir.
     *
     * @param thirdCurrentDir the third current dir
     */
    public void setThirdCurrentDir(int thirdCurrentDir) {
        this.thirdCurrentDir = thirdCurrentDir;
    }

    /**
     * Gets third current level.
     *
     * @return the third current level
     */
    public int getThirdCurrentLevel() {
        return thirdCurrentLevel;
    }

    /**
     * Sets third current level.
     *
     * @param thirdCurrentLevel the third current level
     */
    public void setThirdCurrentLevel(int thirdCurrentLevel) {
        this.thirdCurrentLevel = thirdCurrentLevel;
    }

    /**
     * Gets wave height.
     *
     * @return the wave height
     */
    public int getWaveHeight() {
        return waveHeight;
    }

    /**
     * Sets wave height.
     *
     * @param waveHeight the wave height
     */
    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    /**
     * Gets wave period.
     *
     * @return the wave period
     */
    public int getWavePeriod() {
        return wavePeriod;
    }

    /**
     * Sets wave period.
     *
     * @param wavePeriod the wave period
     */
    public void setWavePeriod(int wavePeriod) {
        this.wavePeriod = wavePeriod;
    }

    /**
     * Gets wave direction.
     *
     * @return the wave direction
     */
    public int getWaveDirection() {
        return waveDirection;
    }

    /**
     * Sets wave direction.
     *
     * @param waveDirection the wave direction
     */
    public void setWaveDirection(int waveDirection) {
        this.waveDirection = waveDirection;
    }

    /**
     * Gets swell height.
     *
     * @return the swell height
     */
    public int getSwellHeight() {
        return swellHeight;
    }

    /**
     * Sets swell height.
     *
     * @param swellHeight the swell height
     */
    public void setSwellHeight(int swellHeight) {
        this.swellHeight = swellHeight;
    }

    /**
     * Gets swell period.
     *
     * @return the swell period
     */
    public int getSwellPeriod() {
        return swellPeriod;
    }

    /**
     * Sets swell period.
     *
     * @param swellPeriod the swell period
     */
    public void setSwellPeriod(int swellPeriod) {
        this.swellPeriod = swellPeriod;
    }

    /**
     * Gets swell direction.
     *
     * @return the swell direction
     */
    public int getSwellDirection() {
        return swellDirection;
    }

    /**
     * Sets swell direction.
     *
     * @param swellDirection the swell direction
     */
    public void setSwellDirection(int swellDirection) {
        this.swellDirection = swellDirection;
    }

    /**
     * Gets sea state.
     *
     * @return the sea state
     */
    public int getSeaState() {
        return seaState;
    }

    /**
     * Sets sea state.
     *
     * @param seaState the sea state
     */
    public void setSeaState(int seaState) {
        this.seaState = seaState;
    }

    /**
     * Gets water temp.
     *
     * @return the water temp
     */
    public int getWaterTemp() {
        return waterTemp;
    }

    /**
     * Sets water temp.
     *
     * @param waterTemp the water temp
     */
    public void setWaterTemp(int waterTemp) {
        this.waterTemp = waterTemp;
    }

    /**
     * Gets precipitation.
     *
     * @return the precipitation
     */
    public int getPrecipitation() {
        return precipitation;
    }

    /**
     * Sets precipitation.
     *
     * @param precipitation the precipitation
     */
    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * Gets salinity.
     *
     * @return the salinity
     */
    public int getSalinity() {
        return salinity;
    }

    /**
     * Sets salinity.
     *
     * @param salinity the salinity
     */
    public void setSalinity(int salinity) {
        this.salinity = salinity;
    }

    /**
     * Gets ice.
     *
     * @return the ice
     */
    public int getIce() {
        return ice;
    }

    /**
     * Sets ice.
     *
     * @param ice the ice
     */
    public void setIce(int ice) {
        this.ice = ice;
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
        builder.append("[MetHyd11: airPressure=");
        builder.append(airPressure);
        builder.append(", airPressureTend=");
        builder.append(airPressureTend);
        builder.append(", airTemp=");
        builder.append(airTemp);
        builder.append(", dewPoint=");
        builder.append(dewPoint);
        builder.append(", gust=");
        builder.append(gust);
        builder.append(", gustDirection=");
        builder.append(gustDirection);
        builder.append(", horzVisibility=");
        builder.append(horzVisibility);
        builder.append(", humidity=");
        builder.append(humidity);
        builder.append(", ice=");
        builder.append(ice);
        builder.append(", pos=");
        builder.append(pos);
        builder.append(", precipitation=");
        builder.append(precipitation);
        builder.append(", salinity=");
        builder.append(salinity);
        builder.append(", seaState=");
        builder.append(seaState);
        builder.append(", secondCurrent=");
        builder.append(secondCurrent);
        builder.append(", secondCurrentDir=");
        builder.append(secondCurrentDir);
        builder.append(", secondCurrentLevel=");
        builder.append(secondCurrentLevel);
        builder.append(", spare=");
        builder.append(spare);
        builder.append(", surfaceCurrent=");
        builder.append(surfaceCurrent);
        builder.append(", surfaceCurrentDir=");
        builder.append(surfaceCurrentDir);
        builder.append(", swellDirection=");
        builder.append(swellDirection);
        builder.append(", swellHeight=");
        builder.append(swellHeight);
        builder.append(", swellPeriod=");
        builder.append(swellPeriod);
        builder.append(", thirdCurrent=");
        builder.append(thirdCurrent);
        builder.append(", thirdCurrentDir=");
        builder.append(thirdCurrentDir);
        builder.append(", thirdCurrentLevel=");
        builder.append(thirdCurrentLevel);
        builder.append(", utcDay=");
        builder.append(utcDay);
        builder.append(", utcHour=");
        builder.append(utcHour);
        builder.append(", utcMinute=");
        builder.append(utcMinute);
        builder.append(", waterLevel=");
        builder.append(waterLevel);
        builder.append(", waterLevelTrend=");
        builder.append(waterLevelTrend);
        builder.append(", waterTemp=");
        builder.append(waterTemp);
        builder.append(", waveDirection=");
        builder.append(waveDirection);
        builder.append(", waveHeight=");
        builder.append(waveHeight);
        builder.append(", wavePeriod=");
        builder.append(wavePeriod);
        builder.append(", wind=");
        builder.append(wind);
        builder.append(", windDirection=");
        builder.append(windDirection);
        builder.append("]");
        return builder.toString();
    }

}
