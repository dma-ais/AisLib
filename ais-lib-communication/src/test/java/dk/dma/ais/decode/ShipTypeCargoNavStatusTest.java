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
package dk.dma.ais.decode;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;

public class ShipTypeCargoNavStatusTest {

    /**
     * Example of using the custom ShipTypeCargo class
     * 
     */
    @Test
    public void parseShips() {

        System.out.println("ShipTypeCargo testing:");

        int pilot = 50;
        int military = 35;
        int medical = 58;
        int wig = 20;
        int cargoD = 74;
        int tankerA = 81;

        // Prepare Ships
        ShipTypeCargo shipPilot = new ShipTypeCargo(pilot);
        ShipTypeCargo shipMilitary = new ShipTypeCargo(military);
        ShipTypeCargo shipMedical = new ShipTypeCargo(medical);
        ShipTypeCargo shipWig = new ShipTypeCargo(wig);
        ShipTypeCargo shipCargoD = new ShipTypeCargo(cargoD);
        ShipTypeCargo shipTankerA = new ShipTypeCargo(tankerA);

        System.out.println(shipPilot);
        Assert.assertEquals(shipPilot.prettyType(), "Pilot");

        System.out.println(shipMilitary);
        Assert.assertEquals(shipMilitary.prettyType(), "Military");

        System.out.println(shipMedical);
        Assert.assertEquals(shipMedical.prettyType(), "Medical");

        System.out.println(shipWig);
        Assert.assertEquals(shipWig.prettyType(), "WIG");

        System.out.println(shipCargoD);
        Assert.assertEquals(shipCargoD.prettyType(), "Cargo");

        System.out.println(shipTankerA);
        Assert.assertEquals(shipTankerA.prettyType(), "Tanker");

    }

    /**
     * Example of using the custom NavigationalStatus class
     * 
     */
    @Test
    public void parseNavStatus() {
        System.out.println("NavigationalStatus testing:");
        int under_way_using_engine = 0;
        int moored = 5;
        int engaged_in_fishing = 7;
        int ais_sart = 14;

        // Prepare Ships
        NavigationalStatus underWayUsingengine = NavigationalStatus.get(under_way_using_engine);
        NavigationalStatus moored2 = NavigationalStatus.get(moored);
        NavigationalStatus engagedInFishing = NavigationalStatus.get(engaged_in_fishing);
        NavigationalStatus aisSart = NavigationalStatus.get(ais_sart);

        System.out.println(underWayUsingengine);
        Assert.assertEquals(underWayUsingengine.prettyStatus(), "Under way using engine");

        System.out.println(moored2);
        Assert.assertEquals(moored2.prettyStatus(), "Moored");

        System.out.println(engagedInFishing);
        Assert.assertEquals(engagedInFishing.prettyStatus(), "Engaged in fishing");

        System.out.println(aisSart);
        Assert.assertEquals(aisSart.prettyStatus(), "AIS-SART");

    }

}
