package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.Color;
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
    }

    public void addPlayer(String username) {
        boolean available = true;
        ArrayList<Color> usedColors = new ArrayList<Color>();

        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                System.out.println("This username is already in this game!");
                available = false;
            }
            if (!usedColors.contains(p.getColor())) {
                usedColors.add(p.getColor());
            }
        }

        Color color;
        do {
            color = Color.getRandomColor();
        } while (usedColors.contains(color));

        if (available) {
            Player p = new Player(username, color);
            players.add(p);
            Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p); //Random Point???
            Building b2 = buildingFactory.createBuilding(BuildingType.BARRACKS, new Point(2, 1), p);
            p.purchaseBuilding(b);
            p.purchaseBuilding(b2);
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
        if (hexEmpty(location)) {
            Unit unit = unitFactory.getUnitPreset(unitType);
            if (player.getGold() - unit.getPurchaseCost() > 0) {
                player.purchaseUnit(unitFactory.createUnit(unitType, location, player));
            } else {
                throw new NotImplementedException();
            }
        }
    }

    public void createBuilding(Player player, BuildingType buildingType, Point location) {
        if (map.isHexBuildable(location, player)) {
            player.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, player));
            map.createBuilding(buildingType, location);
        }
    }

    public void sellBuilding(Player player, Point location){
        Building building = player.getBuildingAtLocation(location);
        if (building != null && !(building instanceof TownCentre)){
            int cost = 0;
            if (building instanceof Barracks){
                cost = ((Barracks)buildingFactory.getBuildingPreset(BuildingType.BARRACKS)).getPurchaseCost();
            }
            else if (building instanceof Fortification){
                cost = ((Fortification)buildingFactory.getBuildingPreset(BuildingType.FORTIFICATION)).getPurchaseCost();
            }
            else if (building instanceof Resource){
                cost = ((Resource)buildingFactory.getBuildingPreset(BuildingType.RESOURCE)).getPurchaseCost();
            }
            player.sellBuilding(building, cost);
            Hexagon hex = map.getHexAtLocation(location);
            hex.removeObject();
            hex.removeObjectType();
        }


    }

    public void claimLand(Unit unit) {
        throw new NotImplementedException();
    }

    public void update() {
        throw new NotImplementedException();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getTestPlayer() {
        for (Player player : getPlayers()) {
            if (player.getUsername().equals("maxim")) { //This player is created in ProjectHex.java
                return player;
            }
        }
        return null;
    }

    public void MoveUnit(Unit unit, Point location) {
        if (hexEmpty(location)) {
            unit.move(location);
        }
    }

    public Unit getTestUnit() {
        if (getTestPlayer().getUnits().size() != 0) {
            return getTestPlayer().getUnits().get(0);
        }
        return null;
    }

    public boolean hexEmpty(Point location) {
        boolean empty = true;
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (unit.getLocation() == location) {
                    empty = false;
                }
            }
        }
        return empty;
    }

    public void attackBuilding(Player player, Point locationUnit, Point locationBuilding){
        Unit unit = getUnitAtLocation(locationUnit);
        Building building = getBuildingAtLocation(locationBuilding);
        if (unit != null && building != null){
            unit.attack(building);
        }
    }

    public Unit getUnitAtLocation(Point location){
        for (Player player : players){
            Unit unit = player.getUnitAtLocation(location);
            if (unit != null){
                return unit;
            }
        }
        return null;
    }

    public Building getBuildingAtLocation(Point location){
        for (Player player : players){
            Building building = player.getBuildingAtLocation(location);
            if (building != null){
                return building;
            }

        }
        return null;
    }
}
