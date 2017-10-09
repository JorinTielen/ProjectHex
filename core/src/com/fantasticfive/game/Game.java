package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.Color;
import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private Map map;
    private UnitFactory unitFactory = new UnitFactory();
    private BuildingFactory buildingFactory = new BuildingFactory();

    private String hash;
    private int id;

    public Game() {

    }

    public void addPlayer(String username) {
        boolean available = true;
        ArrayList<Color> usedColors = new ArrayList<Color>();
        for (Player p : players) {
            if(p.getUsername() == username){
                System.out.println("This username is already in this game!");
                available = false;
            }
            if()
        }

        if(available){
            players.add(new Player(username, Color.RED));
        }
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
        throw new NotImplementedException();
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
}
