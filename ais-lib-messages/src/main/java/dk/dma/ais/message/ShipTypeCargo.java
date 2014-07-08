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

    private ShipType shipType;
    private CargoType cargoType;

    public ShipTypeCargo(int intShipType) {
        shipType = ShipType.UNDEFINED;
        cargoType = CargoType.UNDEFINED;
        int firstDigit = intShipType / 10;
        int secondDigit = intShipType % 10;

        if (firstDigit == 3) {
            switch (secondDigit) {
            case 0:
                shipType = ShipType.FISHING;
                break;
            case 1:
                shipType = ShipType.TOWING;
                break;
            case 2:
                shipType = ShipType.TOWING_LONG_WIDE;
                break;
            case 3:
                shipType = ShipType.DREDGING;
                break;
            case 4:
                shipType = ShipType.DIVING;
                break;
            case 5:
                shipType = ShipType.MILITARY;
                break;
            case 6:
                shipType = ShipType.SAILING;
                break;
            case 7:
                shipType = ShipType.PLEASURE;
                break;
            case 8:
                shipType = ShipType.UNKNOWN;
                break; // reserved for future use
            case 9:
                shipType = ShipType.UNKNOWN;
                break; // reserved for future use
            }
        }

        if (firstDigit == 5) {
            switch (secondDigit) {
            case 0:
                shipType = ShipType.PILOT;
                break;
            case 1:
                shipType = ShipType.SAR;
                break;
            case 2:
                shipType = ShipType.TUG;
                break;
            case 3:
                shipType = ShipType.PORT_TENDER;
                break;
            case 4:
                shipType = ShipType.ANTI_POLLUTION;
                break;
            case 5:
                shipType = ShipType.LAW_ENFORCEMENT;
                break;
            case 6:
                shipType = ShipType.UNKNOWN;
                break; // Spare  for assignments to local vessels
            case 7:
                shipType = ShipType.UNKNOWN;
                break; // Spare  for assignments to local vessels
            case 8:
                shipType = ShipType.MEDICAL;
                break;
            case 9:
                shipType = ShipType.SHIPS_ACCORDING_TO_RR;
                break;
            }
        }
        if (firstDigit == 2 || firstDigit == 4 || firstDigit > 5) {
            switch (firstDigit) {
            case 2:
                shipType = ShipType.WIG;
                break;
            case 4:
                shipType = ShipType.HSC;
                break;
            case 6:
                shipType = ShipType.PASSENGER;
                break;
            case 7:
                shipType = ShipType.CARGO;
                break;
            case 8:
                shipType = ShipType.TANKER;
                break;
            case 9:
                shipType = ShipType.UNKNOWN;
                break;
            }

            switch (secondDigit) {
            case 1:
                cargoType = CargoType.A;
                break;
            case 2:
                cargoType = CargoType.B;
                break;
            case 3:
                cargoType = CargoType.C;
                break;
            case 4:
                cargoType = CargoType.D;
                break;
            }
        }

    }

    public ShipType getShipType() {
        return this.shipType;
    }

    public CargoType getShipCargo() {
        return this.cargoType;
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
