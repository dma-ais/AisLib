/* Copyright (c) 2011 Danish Maritime Safety Administration
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.enav.ais.message;

import java.io.Serializable;

import dk.dma.enav.model.ship.ShipType;
import dk.dma.enav.model.voyage.cargo.CargoType;

public class ShipTypeCargo implements Serializable {
    private static final long serialVersionUID = 1L;

    private ShipType shipType;
    private final CargoType cargoType;

    public ShipTypeCargo(int intShipType) {
        shipType = ShipType.UNDEFINED;
        int firstDigit = intShipType / 10;
        int secondDigit = intShipType % 10;

        cargoType = (firstDigit == 2 || firstDigit == 4 || firstDigit > 5) ? CargoType.fromAIS(secondDigit)
                : CargoType.UNDEFINED;

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

    @Override
    public String toString() {
        // only capitalize the first letter - need to remove underscore - maybe
        ShipTypeCargo shipTypeCargo = this;

        return shipTypeCargo.prettyType() + " cargo of " + getShipCargo();

    }

}
