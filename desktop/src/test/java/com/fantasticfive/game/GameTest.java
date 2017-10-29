package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

/**
 * ProjectHex Created by Sven de Vries on 29-10-2017
 */
public class GameTest extends SettingsTest {
    private Game game;

    @Before
    public void initGame() {
        game = new Game();
    }

    @Test
    public void testAddPlayer() throws Exception {
        game.addPlayer("testPlayer");
        List<Player> players = game.getPlayers();
        assertEquals("testPlayer", players.get(0).getUsername());
    }

    @Test
    public void testRemovePlayer() throws Exception {
        game.addPlayer("testPlayer");
        Player p = game.getPlayers().get(0);
        game.removePlayer(p);
        assertFalse(game.getPlayers().contains(p));
    }

    @Test
    public void testStartGame() throws Exception {
        game.addPlayer("testPlayer");
        game.addPlayer("testPlayer2");
        Player p = game.getPlayers().get(0);
        game.startGame();
        assertEquals(p, game.getCurrentPlayer());
    }

    @Test
    public void testEndTurnLastPlayer() throws Exception {
        game.addPlayer("testPlayer");
        game.addPlayer("testPlayer2");
        Player p = game.getPlayers().get(1);
        game.startGame();
        game.endTurn();
        assertEquals(p, game.getCurrentPlayer());
    }

    @Test
    public void testEndTurnFirstPlayerAgain() throws Exception {
        game.addPlayer("testPlayer");
        game.addPlayer("testPlayer2");
        Player p = game.getPlayers().get(0);
        game.startGame();
        game.endTurn();
        game.endTurn();
        assertEquals(p, game.getCurrentPlayer());
    }

    @Test
    public void testLeaveGame() throws Exception {
        game.addPlayer("testPlayer");
        game.addPlayer("testPlayer2");
        Player p = game.getPlayers().get(1);
        game.startGame();
        game.leaveGame();
        assertEquals(p, game.getCurrentPlayer());
    }

    @Test
    public void testCreateUnit() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createUnit(UnitType.SWORDSMAN, new Point(1,1));
        Player p = game.getCurrentPlayer();
        assertEquals(1, p.getUnits().size());
    }

    @Test
    public void testCreateBuilding() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createBuilding(BuildingType.BARRACKS, new Point(1,1));
        Player p = game.getCurrentPlayer();
        assertEquals(2, p.getBuildings().size());
    }

    @Test
    public void testSellBuilding() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createBuilding(BuildingType.BARRACKS, new Point(1,1));
        game.sellBuilding(new Point(1,1));
        Player p = game.getCurrentPlayer();
        assertEquals(1, p.getBuildings().size());
    }

    @Test
    public void testGetPlayers() throws Exception {
        game.addPlayer("testPlayer");
        assertEquals(1,game.getPlayers().size());
    }

    @Test
    public void testHexEmptyTrue() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        assertTrue(game.hexEmpty(new Point(1,1)));
    }

    @Test
    public void testHexEmptyFalse() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createUnit(UnitType.SWORDSMAN, new Point(1,1));
        assertFalse(game.hexEmpty(new Point(1,1)));
    }

    @Test
    public void testGetUnitOnHex() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createUnit(UnitType.SWORDSMAN, new Point(1,1));
        Hexagon hex = new Hexagon(GroundType.GRASS, new Point(1,1), 62);
        Unit unit = game.getUnitOnHex(hex);
        assertEquals(UnitType.SWORDSMAN, unit.getUnitType());
    }

    @Test
    public void testGetSelectedUnit() throws Exception {
        game.addPlayer("testPlayer");
        game.startGame();
        game.setMap(new Map(20,20));
        game.createUnit(UnitType.SWORDSMAN, new Point(1,1));
        Player p = game.getCurrentPlayer();
        Unit unit = p.getUnits().get(0);
        unit.toggleSelected();
        assertEquals(unit, game.getSelectedUnit());
    }

    @Test
    public void testAttackBuilding() throws Exception {
        game.addPlayer("testPlayer");
        game.setMap(new Map(20,20));
        game.startGame();
        game.createUnit(UnitType.ARCHER, new Point(0,0));
        game.createBuilding(BuildingType.BARRACKS, new Point(1,1));
        Player p = game.getCurrentPlayer();
        Unit unit = p.getUnits().get(0);
        game.attackBuilding(unit, new Point(1,1));
        Building building = p.getBuildings().get(1);
        assertEquals(100, building.health);
    }

    @Test
    public void testGetBuildingAtLocation() throws Exception {
        game.addPlayer("testPlayer");
        game.setMap(new Map(20,20));
        game.startGame();
        game.createBuilding(BuildingType.BARRACKS, new Point(1,1));
        Player p = game.getCurrentPlayer();
        Building building = p.getBuildings().get(1);
        assertEquals(building, game.getBuildingAtLocation(new Point(1,1)));
    }
}