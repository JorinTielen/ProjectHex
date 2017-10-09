package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<Player>();
    private Map map;
    private UnitFactory unitFactory = new UnitFactory();
    private BuildingFactory buildingFactory = new BuildingFactory();

    private String hash;
    private int id;

    public Game() {
        //Just for testing
        players.add(new Player("maxim", null));
        players.add(new Player("enemy", null));
        players.get(0).addGold(99999);
    }

    public void addPlayer(String username) {
        throw new NotImplementedException();
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void startGame() {
        throw new NotImplementedException();
    }

    public void generateHash() {
        throw new NotImplementedException();
    }

    public void createUnit(Player player, UnitType unitType, Point location) {
        Unit unit = unitFactory.getUnitPreset(unitType);
        if(player.getGold() - unit.getPurchaseCost() > 0) {
            player.purchaseUnit(unitFactory.createUnit(unitType, location, player));
        } else {
            throw new NotImplementedException();
        }
    }

    public void createBuilding(Player player, BuildingType buildingType, Point location) {
        throw new NotImplementedException();
    }

    public void claimLand(Unit unit){
        throw new NotImplementedException();
    }

    public void update() {
        throw new NotImplementedException();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getTestPlayer() {
        for(Player player : getPlayers()) {
            if(player.getUsername() == "maxim") {
                return player;
            }
        }
        return null;
    }
}
