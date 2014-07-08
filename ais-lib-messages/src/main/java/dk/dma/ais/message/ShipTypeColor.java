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
