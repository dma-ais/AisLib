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

import dk.dma.ais.sentence.Vdm;

/**
 * Abstract base class for static AIS messages 5 and 24
 */
public abstract class AisStaticCommon extends AisMessage implements IDimensionMessage, INameMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Call sign: 7 = 6 bit ASCII characters, @@@@@@@ = not available = default
     */
    protected String callsign; // 7x6 (42) bits

    /**
     * Ship name: Maximum 20 characters 6 bit ASCII, as defined in Table 44
     *
     * {@literal @@@@@@@@@@@@@@@@@@@@ } = not available = default. For SAR aircraft, it should be set to "SAR AIRCRAFT NNNNNNN"
     * where NNNNNNN equals the aircraft registration number
     */
    protected String name; // 20x6 (120) bits

    /**
     * Type of ship and cargo type: 0 = not available or no ship = default 1-99 = as defined in � 3.3.2 100-199 =
     * reserved, for regional use 200-255 = reserved, for future use Not applicable to SAR aircraft
     */
    protected int shipType; // 8 bits

    /**
     * GPS Ant. Distance from bow (A): Reference point for reported position. Also indicates the dimension of ship (m)
     * (see Fig. 42 and § 3.3.3)
     * <p>
     * NOTE: When GPS position is not available, but the ships dimensions is available, then this field should be 0
     */
    protected int dimBow; // 9 bits

    /**
     * GPS Ant. Distance from stern (B) Reference point for reported position. Also indicates the dimension of ship (m)
     * (see Fig. 42 and § 3.3.3)
     * <p>
     * NOTE: When GPS position is not available, but the ships dimensions is available, then this field should be
     * representing the length of the ship
     */
    protected int dimStern; // 9 bits

    /**
     * GPS Ant. Distance from port (C) Reference point for reported position. Also indicates the dimension of ship (m)
     * (see Fig. 42 and § 3.3.3)
     * <p>
     * NOTE: When GPS position is not available, but the ships dimensions is available, then this field should be 0
     */
    protected int dimPort; // 6 bits

    /**
     * GPS Ant. Distance from starboard (D): Reference point for reported position. Also indicates the dimension of ship
     * (m) (see Fig. 42 and § 3.3.3)
     * <p>
     * NOTE: When GPS position is not available, but the ships dimensions is available, then this field should be
     * representing the with of the ship
     */
    protected int dimStarboard; // 6 bits

    /**
     * Instantiates a new Ais static common.
     *
     * @param msgId the msg id
     */
    public AisStaticCommon(int msgId) {
        super(msgId);
    }

    /**
     * Instantiates a new Ais static common.
     *
     * @param vdm the vdm
     */
    public AisStaticCommon(Vdm vdm) {
        super(vdm);
    }

    /**
     * Gets callsign.
     *
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * Sets callsign.
     *
     * @param callsign the callsign
     */
    public void setCallsign(String callsign) {
        this.callsign = callsign;
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

    /**
     * Gets ship type.
     *
     * @return the ship type
     */
    public int getShipType() {
        return shipType;
    }

    /**
     * Sets ship type.
     *
     * @param shipType the ship type
     */
    public void setShipType(int shipType) {
        this.shipType = shipType;
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

}
