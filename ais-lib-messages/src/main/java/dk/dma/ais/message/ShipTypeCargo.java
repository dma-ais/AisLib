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

import java.io.Serializable;

/**
 * The type Ship type cargo.
 */
public class ShipTypeCargo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The enum Ship type.
     */
    public enum ShipType {
        /**
         * Undefined ship type.
         */
        UNDEFINED,
        /**
         * Wig ship type.
         */
        WIG,
        /**
         * Pilot ship type.
         */
        PILOT,
        /**
         * Sar ship type.
         */
        SAR,
        /**
         * Tug ship type.
         */
        TUG,
        /**
         * Port tender ship type.
         */
        PORT_TENDER,
        /**
         * Anti pollution ship type.
         */
        ANTI_POLLUTION,
        /**
         * Law enforcement ship type.
         */
        LAW_ENFORCEMENT,
        /**
         * Medical ship type.
         */
        MEDICAL,
        /**
         * Fishing ship type.
         */
        FISHING,
        /**
         * Towing ship type.
         */
        TOWING,
        /**
         * Towing long wide ship type.
         */
        TOWING_LONG_WIDE,
        /**
         * Dredging ship type.
         */
        DREDGING,
        /**
         * Diving ship type.
         */
        DIVING,
        /**
         * Military ship type.
         */
        MILITARY,
        /**
         * Sailing ship type.
         */
        SAILING,
        /**
         * Pleasure ship type.
         */
        PLEASURE,
        /**
         * Hsc ship type.
         */
        HSC,
        /**
         * Passenger ship type.
         */
        PASSENGER,
        /**
         * Cargo ship type.
         */
        CARGO,
        /**
         * Tanker ship type.
         */
        TANKER,
        /**
         * Ships according to rr ship type.
         */
        SHIPS_ACCORDING_TO_RR,
        /**
         * Unknown ship type.
         */
        UNKNOWN
    }

    /**
     * The enum Cargo type.
     */
    public enum CargoType {
        /**
         * Undefined cargo type.
         */
        UNDEFINED,
        /**
         * A cargo type.
         */
        A,
        /**
         * B cargo type.
         */
        B,
        /**
         * C cargo type.
         */
        C,
        /**
         * D cargo type.
         */
        D
    }

    private final ShipType shipType;
    private final CargoType cargoType;

    /** The original coded value as per ITU 1371.5 table 53 */
    private final int code;

    /**
     * Instantiates a new Ship type cargo.
     *
     * @param intShipType the int ship type
     */
    public ShipTypeCargo(int intShipType) {
        this.code = intShipType;

        ShipType shipTypeTmp = ShipType.UNDEFINED;
        CargoType cargoTypeTmp = CargoType.UNDEFINED;

        int firstDigit = intShipType / 10;
        int secondDigit = intShipType % 10;

        if (firstDigit == 3) {
            switch (secondDigit) {
            case 0:
                shipTypeTmp = ShipType.FISHING;
                break;
            case 1:
                shipTypeTmp = ShipType.TOWING;
                break;
            case 2:
                shipTypeTmp = ShipType.TOWING_LONG_WIDE;
                break;
            case 3:
                shipTypeTmp = ShipType.DREDGING;
                break;
            case 4:
                shipTypeTmp = ShipType.DIVING;
                break;
            case 5:
                shipTypeTmp = ShipType.MILITARY;
                break;
            case 6:
                shipTypeTmp = ShipType.SAILING;
                break;
            case 7:
                shipTypeTmp = ShipType.PLEASURE;
                break;
            case 8:
                shipTypeTmp = ShipType.UNKNOWN;
                break; // reserved for future use
            case 9:
                shipTypeTmp = ShipType.UNKNOWN;
                break; // reserved for future use
            }
        }

        if (firstDigit == 5) {
            switch (secondDigit) {
            case 0:
                shipTypeTmp = ShipType.PILOT;
                break;
            case 1:
                shipTypeTmp = ShipType.SAR;
                break;
            case 2:
                shipTypeTmp = ShipType.TUG;
                break;
            case 3:
                shipTypeTmp = ShipType.PORT_TENDER;
                break;
            case 4:
                shipTypeTmp = ShipType.ANTI_POLLUTION;
                break;
            case 5:
                shipTypeTmp = ShipType.LAW_ENFORCEMENT;
                break;
            case 6:
                shipTypeTmp = ShipType.UNKNOWN;
                break; // Spare  for assignments to local vessels
            case 7:
                shipTypeTmp = ShipType.UNKNOWN;
                break; // Spare  for assignments to local vessels
            case 8:
                shipTypeTmp = ShipType.MEDICAL;
                break;
            case 9:
                shipTypeTmp = ShipType.SHIPS_ACCORDING_TO_RR;
                break;
            }
        }
        if (firstDigit == 2 || firstDigit == 4 || firstDigit > 5) {
            switch (firstDigit) {
            case 2:
                shipTypeTmp = ShipType.WIG;
                break;
            case 4:
                shipTypeTmp = ShipType.HSC;
                break;
            case 6:
                shipTypeTmp = ShipType.PASSENGER;
                break;
            case 7:
                shipTypeTmp = ShipType.CARGO;
                break;
            case 8:
                shipTypeTmp = ShipType.TANKER;
                break;
            case 9:
                shipTypeTmp = ShipType.UNKNOWN;
                break;
            }

            switch (secondDigit) {
            case 1:
                cargoTypeTmp = CargoType.A;
                break;
            case 2:
                cargoTypeTmp = CargoType.B;
                break;
            case 3:
                cargoTypeTmp = CargoType.C;
                break;
            case 4:
                cargoTypeTmp = CargoType.D;
                break;
            }
        }

        this.shipType = shipTypeTmp;
        this.cargoType= cargoTypeTmp;
    }

    /**
     * Gets ship type.
     *
     * @return the ship type
     */
    public ShipType getShipType() {
        return this.shipType;
    }

    /**
     * Gets ship cargo.
     *
     * @return the ship cargo
     */
    public CargoType getShipCargo() {
        return this.cargoType;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Pretty type string.
     *
     * @return the string
     */
    public String prettyType() {
        ShipTypeCargo shipTypeCargo = this;
        String result;

        if (shipTypeCargo.getShipType().equals(ShipType.TOWING_LONG_WIDE)) {
            result = "Towing Long/Wide";
        } else if (shipTypeCargo.getShipType().equals(ShipType.WIG)) {
            result = "WIG";
        } else if (shipTypeCargo.getShipType().equals(ShipType.HSC)) {
            result = "HSC";
        } else {
            String shipType = shipTypeCargo.getShipType().toString().replace("_", " ");
            result = shipType.substring(0, 1) + shipType.substring(1).toLowerCase();
        }

        return result;
    }

    /**
     * Pretty cargo string.
     *
     * @return the string
     */
    public String prettyCargo() {
        CargoType shipTypeCargo = this.getShipCargo();
        String result;
        result = "Undefined";

        switch (shipTypeCargo) {
        case A:
            result = "Category A";
            break;
        case B:
            result = "Category B";
            break;
        case C:
            result = "Category C";
            break;
        case D:
            result = "Category D";
            break;
        default:
            break;
        }

        return result;
    }

    @Override
    public String toString() {
        // only capitalize the first letter - need to remove underscore - maybe
        ShipTypeCargo shipTypeCargo = this;

        return shipTypeCargo.prettyType() + " cargo of " + shipTypeCargo.prettyCargo();
    }

}
