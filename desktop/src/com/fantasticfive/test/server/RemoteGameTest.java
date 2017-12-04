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
        Unit unit = game.getUnitPreset(UnitType.SCOUT);
        unit.setLocation(new Point(1,2));
        unit.setOwner(game.getCurrentPlayer());

        Assert.assertFalse("Dit moet nog gedaan worden",true);
    }

    @Test
    public void testBuyBuilding() throws RemoteException {
        game.buyBuilding(BuildingType.BARRACKS, new Point(2,1));

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
        Unit unit = game.getUnitPreset(UnitType.SWORDSMAN);
        game.addPlayer("Player 2");
        unit.setOwner(game.getPlayers().get(1));

        game.attackBuilding(unit, new Point(1,0));
    }

    @Test
    public void testGetBuildingAtLocation() {
        Assert.assertTrue("Not yet implemented",false);
    }

    @Test
    public void testHexEmpty() {
        Assert.assertTrue("Not yet implemented",false);
    }

    @Test
    public void testHexEmptyResource() {
        Assert.assertTrue("Not yet implemented",false);
    }

    @Test
    public void testGetUnitOnHex() {
        Assert.assertTrue("Not yet implemented",false);
    }

    @Test
    public void testGetSelectedUnit() {
        Assert.assertTrue("Not yet implemented",false);
    }
}
