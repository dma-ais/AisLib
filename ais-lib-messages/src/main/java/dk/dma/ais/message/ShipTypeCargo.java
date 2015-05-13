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

public class ShipTypeCargo implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ShipType {
        UNDEFINED, WIG, PILOT, SAR, TUG, PORT_TENDER, ANTI_POLLUTION, LAW_ENFORCEMENT, MEDICAL, FISHING, TOWING, TOWING_LONG_WIDE, DREDGING, DIVING, MILITARY, SAILING, PLEASURE, HSC, PASSENGER, CARGO, TANKER, SHIPS_ACCORDING_TO_RR, UNKNOWN
    }

    public enum CargoType {
        UNDEFINED, A, B, C, D
    }

    private final ShipType shipType;
    private final CargoType cargoType;

    /** The original coded value as per ITU 1371.5 table 53 */
    private final int code;

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

    public ShipType getShipType() {
        return this.shipType;
    }

    public CargoType getShipCargo() {
        return this.cargoType;
    }

    public int getCode() {
        return code;
    }

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
