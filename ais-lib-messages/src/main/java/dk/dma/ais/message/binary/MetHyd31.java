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
import dk.dma.ais.message.binary.AisApplicationMessage;

public class MetHyd31 extends AisApplicationMessage {

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
	private int waterLevel; // 12 bits
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
	private int spare; // 6 bits
	private int posAcc; // 1 bit

	public MetHyd31(BinArray binArray) throws SixbitException {
		super(1, 31, binArray);
	}

	@Override
	public SixbitEncoder getEncoded() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void parse(BinArray binArray) throws SixbitException {
		pos = new AisPosition();
		pos.set2524();
		this.pos.setRawLongitude(binArray.getVal(25));
		this.pos.setRawLatitude(binArray.getVal(24));
		this.posAcc = (int) binArray.getVal(1);
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
		this.waterLevel = (int) binArray.getVal(12);
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

	public AisPosition getPos() {
		return pos;
	}

	public void setPos(AisPosition pos) {
		this.pos = pos;
	}

	public int getUtcDay() {
		return utcDay;
	}

	public void setUtcDay(int utcDay) {
		this.utcDay = utcDay;
	}

	public int getUtcHour() {
		return utcHour;
	}

	public void setUtcHour(int utcHour) {
		this.utcHour = utcHour;
	}

	public int getUtcMinute() {
		return utcMinute;
	}

	public void setUtcMinute(int utcMinute) {
		this.utcMinute = utcMinute;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public int getGust() {
		return gust;
	}

	public void setGust(int gust) {
		this.gust = gust;
	}

	public int getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	public int getGustDirection() {
		return gustDirection;
	}

	public void setGustDirection(int gustDirection) {
		this.gustDirection = gustDirection;
	}

	public int getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(int airTemp) {
		this.airTemp = airTemp;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	public int getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(int airPressure) {
		this.airPressure = airPressure;
	}

	public int getAirPressureTend() {
		return airPressureTend;
	}

	public void setAirPressureTend(int airPressureTend) {
		this.airPressureTend = airPressureTend;
	}

	public int getHorzVisibility() {
		return horzVisibility;
	}

	public void setHorzVisibility(int horzVisibility) {
		this.horzVisibility = horzVisibility;
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	public int getWaterLevelTrend() {
		return waterLevelTrend;
	}

	public void setWaterLevelTrend(int waterLevelTrend) {
		this.waterLevelTrend = waterLevelTrend;
	}

	public int getSurfaceCurrent() {
		return surfaceCurrent;
	}

	public void setSurfaceCurrent(int surfaceCurrent) {
		this.surfaceCurrent = surfaceCurrent;
	}

	public int getSurfaceCurrentDir() {
		return surfaceCurrentDir;
	}

	public void setSurfaceCurrentDir(int surfaceCurrentDir) {
		this.surfaceCurrentDir = surfaceCurrentDir;
	}

	public int getSecondCurrent() {
		return secondCurrent;
	}

	public void setSecondCurrent(int secondCurrent) {
		this.secondCurrent = secondCurrent;
	}

	public int getSecondCurrentDir() {
		return secondCurrentDir;
	}

	public void setSecondCurrentDir(int secondCurrentDir) {
		this.secondCurrentDir = secondCurrentDir;
	}

	public int getSecondCurrentLevel() {
		return secondCurrentLevel;
	}

	public void setSecondCurrentLevel(int secondCurrentLevel) {
		this.secondCurrentLevel = secondCurrentLevel;
	}

	public int getThirdCurrent() {
		return thirdCurrent;
	}

	public void setThirdCurrent(int thirdCurrent) {
		this.thirdCurrent = thirdCurrent;
	}

	public int getThirdCurrentDir() {
		return thirdCurrentDir;
	}

	public void setThirdCurrentDir(int thirdCurrentDir) {
		this.thirdCurrentDir = thirdCurrentDir;
	}

	public int getThirdCurrentLevel() {
		return thirdCurrentLevel;
	}

	public void setThirdCurrentLevel(int thirdCurrentLevel) {
		this.thirdCurrentLevel = thirdCurrentLevel;
	}

	public int getWaveHeight() {
		return waveHeight;
	}

	public void setWaveHeight(int waveHeight) {
		this.waveHeight = waveHeight;
	}

	public int getWavePeriod() {
		return wavePeriod;
	}

	public void setWavePeriod(int wavePeriod) {
		this.wavePeriod = wavePeriod;
	}

	public int getWaveDirection() {
		return waveDirection;
	}

	public void setWaveDirection(int waveDirection) {
		this.waveDirection = waveDirection;
	}

	public int getSwellHeight() {
		return swellHeight;
	}

	public void setSwellHeight(int swellHeight) {
		this.swellHeight = swellHeight;
	}

	public int getSwellPeriod() {
		return swellPeriod;
	}

	public void setSwellPeriod(int swellPeriod) {
		this.swellPeriod = swellPeriod;
	}

	public int getSwellDirection() {
		return swellDirection;
	}

	public void setSwellDirection(int swellDirection) {
		this.swellDirection = swellDirection;
	}

	public int getSeaState() {
		return seaState;
	}

	public void setSeaState(int seaState) {
		this.seaState = seaState;
	}

	public int getWaterTemp() {
		return waterTemp;
	}

	public void setWaterTemp(int waterTemp) {
		this.waterTemp = waterTemp;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public int getSalinity() {
		return salinity;
	}

	public void setSalinity(int salinity) {
		this.salinity = salinity;
	}

	public int getIce() {
		return ice;
	}

	public void setIce(int ice) {
		this.ice = ice;
	}

	public int getSpare() {
		return spare;
	}

	public void setSpare(int spare) {
		this.spare = spare;
	}

	public int getPosAcc() {
		return posAcc;
	}

	public void setPosAcc(int posAcc) {
		this.posAcc = posAcc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[MetHyd31: airPressure=");
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
