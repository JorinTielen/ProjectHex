package com.fantasticfive.shared;


import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import org.junit.*;
import static org.junit.Assert.*;

public class PlayerTest extends SettingsTest {
    private Player player;


    @Before
    public void before() {
        player = new Player("Test", Color.RED,1);
    }

    @After
    public void after() {
        player = null;
    }

    @Test
    public void testAddGold() {
        int expected = player.getGold() + 5;
        player.addGold(5);
        int result = player.getGold();
        assertEquals(expected,result);
    }

    @Test
    public void testRemoveGold() {
        int expected = player.getGold() - 55;
        player.removeGold(55);
        int result = player.getGold();
        assertEquals(expected,result);
    }

    @Test
    public void testPurchaseBuilding() {
        player.addGold(999);


        //TownCentre
        Building building = new TownCentre(150, null);
        player.purchaseBuilding(building);
        assertTrue("Purchased townCentre should be in Player's building list",
                player.getBuildings().contains(building));

        //Barracks
        building = new Barracks(150, 30, null);
        int expectedGold = player.getGold() - ((Barracks)building).getPurchaseCost();
        player.purchaseBuilding(building);
        int actualGold = player.getGold();
        assertTrue("Purchased barracks should be in Player's building list",
                player.getBuildings().contains(building));
        assertEquals("Barracks: Player's gold should have been reduced",
                expectedGold, actualGold);

        //Resource
        building = new Resource(150, 30, 3, null);
        expectedGold = player.getGold() - ((Resource)building).getPurchaseCost();
        player.purchaseBuilding(building);
        actualGold = player.getGold();
        assertTrue("Purchased resource should be in Player's building list",
                player.getBuildings().contains(building));
        assertEquals("Resource: Player's gold should have been reduced",
                expectedGold, actualGold);

        //Fortification
        building = new Fortification(150, 30, null);
        expectedGold = player.getGold() - ((Fortification)building).getPurchaseCost();
        player.purchaseBuilding(building);
        actualGold = player.getGold();
        assertTrue("Purchased fortification should be in Player's building list",
                player.getBuildings().contains(building));
        assertEquals("Fortification: Player's gold should have been reduced",
                expectedGold, actualGold);

        //Try purchasing without enough money
        player.removeGold(999);
        building = new Barracks(150, 30, null);
        player.purchaseBuilding(building);
        assertFalse("Purchased barracks should not be in the player's building list",
                player.getBuildings().contains(building));
    }

    @Test
    public void testSellBuilding() {
        Building building = new Barracks(150,30, null);
        player.addGold(999);
        player.purchaseBuilding(building);
        player.sellBuilding(building, ((Barracks)building).getPurchaseCost());

        assertFalse(player.getBuildings().contains(building));
    }

    @Test
    public void testGetBuildingAtLocation() {
        Building building = new TownCentre(150, null);
        building.setLocation(new Point(1,1));
        player.addGold(999);
        player.purchaseBuilding(building);
        Building buildingAtLocation = player.getBuildingAtLocation(new Point(1,1));
        assertNotNull("Player should have returned the created barracks",
                buildingAtLocation);

        building = new TownCentre(150,null);
        building.setLocation(new Point(1,2));
        buildingAtLocation = player.getBuildingAtLocation(new Point(1,2));
        assertNull("Player should have returned null", buildingAtLocation);
    }

    @Test
    public void testPurchaseUnit() {
        player.addGold(999);
        Unit unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        int expectedGold = player.getGold() - unit.getPurchaseCost();
        player.purchaseUnit(unit);
        int actualGold = player.getGold();

        assertTrue("Unit should be in player's unit list",
                player.getUnits().contains(unit));
        assertEquals("Player should have unit cost redacted from gold",
                expectedGold, actualGold);

        //Test without gold
        player.removeGold(2000);
        unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.purchaseUnit(unit);
        assertFalse("Unit should not be in player's unit list",
                player.getUnits().contains(unit));
    }

    @Test
    public void testSellUnit() {
        Unit unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.addGold(999);
        player.purchaseUnit(unit);
        int expectedGold = player.getGold() + (int)(unit.getPurchaseCost() * 0.66);
        player.sellUnit(unit);
        int actualGold = player.getGold();

        assertFalse("Purchased unit should no longer be in player's unit list",
                player.getUnits().contains(unit));
        assertEquals("Player should have gotten gold from sale",
                expectedGold, actualGold);

        //Test with unit that has not been purchased by player
        unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        expectedGold = player.getGold();
        player.sellUnit(unit);
        actualGold = player.getGold();

        assertEquals("Player should not have gotten gold from sale",
                expectedGold, actualGold);
    }

    @Test
    public void testEndTurn() {
        player.addGold(999);
        Unit unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.purchaseUnit(unit);
        unit = new Unit(UnitType.ARCHER, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.purchaseUnit(unit);
        unit.toggleSelected();

        int expectedGold = player.getGold() - 2;
        player.endTurn();

        Boolean selectedUnit = false;
        for(Unit u: player.getUnits()) {
            if(u.getSelected()) {
                selectedUnit = true;
            }
        }

        assertFalse("All units should have been deselected", selectedUnit);

        int actualGoldPerTurn = player.getGold();
        assertEquals("Should have redacted gold per turn", expectedGold, actualGoldPerTurn);
    }

    @Test
    public void testGetGoldPerTurn() {
        player.addGold(999);
        Unit unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.purchaseUnit(unit);
        unit = new Unit(UnitType.SWORDSMAN, 100, 20, 15, 1, 2, 40, 1, false, 100);
        player.purchaseUnit(unit);

        int expectedGoldPerTurn = -2;
        int actualGoldPerTurn = player.getGoldPerTurn();
        assertEquals("Should return 2", expectedGoldPerTurn, actualGoldPerTurn);
    }
}