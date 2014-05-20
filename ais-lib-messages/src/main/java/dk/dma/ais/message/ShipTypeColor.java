/* Copyright (c) 2011 Danish Maritime Authority
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
package dk.dma.ais.message;

import dk.dma.ais.message.ShipTypeCargo.ShipType;

import java.util.HashMap;

import static java.util.Objects.requireNonNull;

/**
 * @author Kasper Nielsen
 */
public enum ShipTypeColor {
    BLUE(ShipType.PASSENGER),
    GREY(ShipType.UNDEFINED, ShipType.UNKNOWN),
    GREEN(ShipType.CARGO),
    ORANGE(ShipType.FISHING),
    PURPLE(ShipType.SAILING, ShipType.PLEASURE),
    RED(ShipType.TANKER),
    TURQUOISE,
    YELLOW(ShipType.HSC, ShipType.WIG);

    private static final HashMap<ShipType, ShipTypeColor> REVERSE_LOOKUP = new HashMap<>();
    private final ShipType[] shipTypes;

    static {
        for (ShipTypeColor shipTypeColor : ShipTypeColor.values()) {
            for (ShipType shipType : shipTypeColor.shipTypes) {
                REVERSE_LOOKUP.put(shipType, shipTypeColor);
            }
        }
    }

    private ShipTypeColor(ShipType... shipTypes) {
        this.shipTypes = shipTypes;
    }

    public ShipType[] getShipTypes() {
        return shipTypes;
    }

    public static ShipTypeColor getColor(ShipType type) {
        ShipTypeColor c = REVERSE_LOOKUP.get(requireNonNull(type));
        return c == null ? ShipTypeColor.TURQUOISE : c;
    }
}
