package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class Game {
    private List<Player> players;
    private Map map;
    private UnitFactory unitFactory;
    private BuildingFactory buildingFactory;

    private String hash;

    public Game() {

    }

    public void addPlayer(Player player) {
        this.players.add(player);
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

    public void CreateUnit(Player player, UnitType unitType) {
        throw new NotImplementedException();
    }

    public void CreateBuilding(Player player, BuildingType buildingType) {
        throw new NotImplementedException();
    }
}
