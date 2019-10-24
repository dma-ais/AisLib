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

import dk.dma.ais.message.ShipTypeCargo.ShipType;

import java.util.HashMap;

import static java.util.Objects.requireNonNull;

/**
 * The enum Ship type color.
 *
 * @author Kasper Nielsen
 */
public enum ShipTypeColor {
    /**
     * Blue ship type color.
     */
    BLUE(ShipType.PASSENGER),
    /**
     * Grey ship type color.
     */
    GREY(ShipType.UNDEFINED, ShipType.UNKNOWN),
    /**
     * Green ship type color.
     */
    GREEN(ShipType.CARGO),
    /**
     * Orange ship type color.
     */
    ORANGE(ShipType.FISHING),
    /**
     * Purple ship type color.
     */
    PURPLE(ShipType.SAILING, ShipType.PLEASURE),
    /**
     * Red ship type color.
     */
    RED(ShipType.TANKER),
    /**
     * Turquoise ship type color.
     */
    TURQUOISE,
    /**
     * Yellow ship type color.
     */
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

    /**
     * Get ship types ship type [ ].
     *
     * @return the ship type [ ]
     */
    public ShipType[] getShipTypes() {
        return shipTypes;
    }

    /**
     * Gets color.
     *
     * @param type the type
     * @return the color
     */
    public static ShipTypeColor getColor(ShipType type) {
        ShipTypeColor c = REVERSE_LOOKUP.get(requireNonNull(type));
        return c == null ? ShipTypeColor.TURQUOISE : c;
    }
}
