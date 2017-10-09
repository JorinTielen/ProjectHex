package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.Color;
import com.fantasticfive.game.enums.GroundType;
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
        players = new ArrayList<Player>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(String username) {
        boolean available = true;
        ArrayList<Color> usedColors = new ArrayList<Color>();

        for (Player p : players) {
            if(p.getUsername() == username){
                System.out.println("This username is already in this game!");
                available = false;
            }
            if(!usedColors.contains(p.getColor())){
                usedColors.add(p.getColor());
            }
        }

        Color color;
        do {
            color = Color.getRandomColor();
        } while (usedColors.contains(color));

        if(available) {
            Player p = new Player(username, color);
            players.add(p);
            Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(0,0)); //Random Point???
            p.purchaseBuilding(b);
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
