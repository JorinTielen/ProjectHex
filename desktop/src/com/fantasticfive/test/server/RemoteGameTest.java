package com.fantasticfive.test.server;

import com.fantasticfive.server.RemoteGame;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.test.settings.SettingsTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteGameTest extends SettingsTest {

    private RemoteGame game;

    @Before
    public void before() throws RemoteException {
        game = new RemoteGame(1099);
        game.addPlayer("Test");
        game.startGame();
    }

    @After
    public void after() {
        game = null;
    }


    @Test
    public void testStartGame() throws RemoteException {
        Player p = game.getCurrentPlayer();
        Assert.assertEquals("Player 'test' should be the current player","Test", p.getUsername());
    }

    @Test
    public void testEndTurn() throws RemoteException {
        game.addPlayer("Speler 2");
        game.endTurn(game.getCurrentPlayer().getId());
        Assert.assertEquals("Player 'Speler 2' should be the current player","Speler 2", game.getCurrentPlayer().getUsername());
    }

    @Test
    public void testLeaveGame() throws RemoteException {
        game.addPlayer("Speler 2");
        Player player = game.getCurrentPlayer();
        game.leaveGame(game.getCurrentPlayer().getId());
        boolean result = game.getPlayers().contains(player);
        Assert.assertFalse("Player 'Test' should no longer be in the player list", result);
    }

    @Test
    public void testUpdateFromLocal() throws RemoteException {
        List<Player> players = game.getPlayers();
        players.get(0).addGold(6);
        game.updateFromLocal(players);

        int expectedGold = 106;
        int actualGold = game.getCurrentPlayer().getGold();
        Assert.assertEquals("Gold should have been 106", expectedGold, actualGold);
    }

    @Test
    public void testAddPlayer() throws RemoteException {
        game.addPlayer("Speler 2");
        game.addPlayer("Speler 3");

        int expectedAmountOfPlayers = 3;
        int actualAmountOfPlayers = game.getPlayers().size();
        Assert.assertEquals("Player 'Speler 3' was not added to the game",
                expectedAmountOfPlayers, actualAmountOfPlayers);
    }

    @Test
    public void testBuyUnit() throws RemoteException {
        game.buyUnit(UnitType.SWORDSMAN, new Point(2,2), game.getCurrentPlayer().getId());

        int expectedAmountOfUnits = 1;
        int actualAmountOfUnits = game.getCurrentPlayer().getUnits().size();
        Assert.assertEquals("Unit should have been added to player's list of units",
                expectedAmountOfUnits, actualAmountOfUnits);
    }

    @Test
    public void testClaimLand() throws RemoteException {
        //Get the random location of the player's towncentre
        Point location = game.getPlayers().get(0).getBuildings().get(0).getLocation();

        //Place the unit 1 tile away from the town centre
        game.buyUnit(UnitType.SCOUT, new Point(location.getX(),location.getY() + 1), game.getCurrentPlayer().getId());
        Unit unit = game.getUnitAtLocation(new Point(location.getX(), location.getY() +1));
        unit.move(new Point(unit.getLocation().getX(), unit.getLocation().getY() + 1), 1);

        game.claimLand(unit);

        Hexagon hex = game.getMap().getHexAtLocation(new Point(location.getX(), location.getY() + 2));
        boolean claimed = hex.hasOwner();

        Assert.assertTrue("Hex should have been claimed",claimed);

        unit.resetMoves();

        unit.setLocation(new Point(location.getX() + 3, location.getY() + 3));
        game.claimLand(unit);
        hex = game.getMap().getHexAtLocation(new Point(location.getX() + 3, location.getY() + 3));

        claimed = hex.hasOwner();

        Assert.assertFalse("Hex should not have been claimed", claimed);
    }

    @Test
    public void testBuyBuilding() throws RemoteException {
        //Get the random location of the player's towncentre
        Point location = game.getPlayers().get(0).getBuildings().get(0).getLocation();

        game.buyBuilding(BuildingType.BARRACKS, new Point(location.getX(), location.getY() +1));

        int expectedAmountOfBuildings = 2;
        int actualAmountOfBuildings = game.getCurrentPlayer().getBuildings().size();
        Assert.assertEquals("Player should have 2 buildings",
                expectedAmountOfBuildings, actualAmountOfBuildings);
    }

    @Test
    public void testSellBuilding() throws RemoteException {
        game.buyBuilding(BuildingType.BARRACKS, new Point(2,1));
        game.sellBuilding(new Point(2,1));

        int expectedAmountOfBuildings = 1;
        int actualAmountOfBuildings = game.getCurrentPlayer().getBuildings().size();
        Assert.assertEquals("Building should have been removed from player's list",
                expectedAmountOfBuildings, actualAmountOfBuildings);
    }

    @Test
    public void testAttackBuilding() throws RemoteException {
        //Get the random location of the first player's towncentre
        Point p1Location = game.getPlayers().get(0).getBuildings().get(0).getLocation();

        game.addPlayer("Player 2");

        //Get the random location of the second player's towncentre
        Point p2Location = game.getPlayers().get(1).getBuildings().get(0).getLocation();

        game.buyUnit(UnitType.SWORDSMAN, new Point(p1Location.getX(), p1Location.getY() + 1), game.getCurrentPlayer().getId());
        Unit unit = game.getUnitAtLocation(new Point(p1Location.getX(), p1Location.getY() + 1));

        game.endTurn(game.getCurrentPlayer().getId());
        game.buyBuilding(BuildingType.BARRACKS, new Point(p2Location.getX(), p2Location.getY() + 1));

        Building b = game.getBuildingAtLocation(new Point(p2Location.getX(), p2Location.getY() + 1));
        int buildingHealth = b.getHealth();

        game.endTurn(game.getCurrentPlayer().getId());

        unit.setLocation(new Point(b.getLocation().getX(), b.getLocation().getY() + 1));

        game.attackBuilding(unit, b);

        Assert.assertTrue(b.getHealth() < buildingHealth);
    }

    @Test
    public void testGetBuildingAtLocation() {
        Point location = game.getPlayers().get(0).getBuildings().get(0).getLocation();
        Building b = game.getBuildingAtLocation(location);

        Assert.assertNotNull("Should have returned a building",b);
    }

    @Test
    public void testHexEmpty() {
        boolean result = game.hexEmpty(new Point(2,1));
        Assert.assertTrue("Hex should have been empty",result);
    }

    @Test
    public void testGetUnitOnHex() {
        game.buyUnit(UnitType.SWORDSMAN, new Point(2,1), game.getCurrentPlayer().getId());

        Unit u = game.getUnitAtLocation(new Point(2,1));

        Assert.assertNotNull("Should have returned a unit",u);
    }

    @Test
    public void testGetSelectedUnit() {
        game.buyUnit(UnitType.SWORDSMAN, new Point(2,1), game.getCurrentPlayer().getId());

        Unit u = game.getPlayers().get(0).getUnits().get(0);
        u.toggleSelected();

        boolean result = u.getSelected();

        Assert.assertTrue("Should have returned a unit",result);
    }
}
