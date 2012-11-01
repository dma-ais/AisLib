package dk.dma.ais.decode;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.enav.model.voyage.NavigationalStatus;

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
        NavigationalStatus underWayUsingengine = NavigationalStatus.fromAIS(under_way_using_engine);
        NavigationalStatus moored2 = NavigationalStatus.fromAIS(moored);
        NavigationalStatus engagedInFishing = NavigationalStatus.fromAIS(engaged_in_fishing);
        NavigationalStatus aisSart = NavigationalStatus.fromAIS(ais_sart);

        System.out.println(underWayUsingengine);
        Assert.assertEquals(underWayUsingengine.toString(), "Under way using engine");

        System.out.println(moored2);
        Assert.assertEquals(moored2.toString(), "Moored");

        System.out.println(engagedInFishing);
        Assert.assertEquals(engagedInFishing.toString(), "Engaged in fishing");

        System.out.println(aisSart);
        Assert.assertEquals(aisSart.toString(), "AIS-SART");

    }

}
