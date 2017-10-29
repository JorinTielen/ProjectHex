package com.fantasticfive.game;

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
    public void testGenerateHash() throws Exception {
    }

    @Test
    public void testGetBuildingPreset() throws Exception {
    }

    @Test
    public void testGetUnitPreset() throws Exception {
    }

    @Test
    public void testCreateUnit() throws Exception {
    }

    @Test
    public void testCreateBuilding() throws Exception {
    }

    @Test
    public void testSellBuilding() throws Exception {
    }

    @Test
    public void testClaimLand() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testGetPlayers() throws Exception {
    }

    @Test
    public void testHexEmpty() throws Exception {
    }

    @Test
    public void testGetUnitOnHex() throws Exception {
    }

    @Test
    public void testGetSelectedUnit() throws Exception {
    }

    @Test
    public void testAttackBuilding() throws Exception {
    }

    @Test
    public void testGetBuildingAtLocation() throws Exception {
    }

    @Test
    public void testGetCurrentPlayer() throws Exception {
    }
}