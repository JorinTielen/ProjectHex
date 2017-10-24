package com.fantasticfive.game;

import com.fantasticfive.game.enums.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<Player>();
    private Player currentPlayer;
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
            p.addGold(100);
            if (username.equals("enemy")) {
                Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(10,13),p);
                p.purchaseBuilding(b);
            } else {
                Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p); //Random Point???
                p.purchaseBuilding(b);
            }
        }
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        if(players.size() == 1){
            System.out.println(currentPlayer.getUsername() + " has won the game!");
        }
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void startGame() {
        currentPlayer = players.get(0);
    }

    public void endTurn() {
        if (players.size() != 0) {
            currentPlayer.endTurn();
            int i = players.indexOf(currentPlayer);

            if (i != players.size() - 1) {
                currentPlayer = players.get(i + 1);
            } else {
                currentPlayer = players.get(0);
            }
        }
    }

    public void leaveGame() {
        players.remove(currentPlayer);
        endTurn();

        if(players.size() == 1){
            System.out.println(currentPlayer.getUsername() + " has won the game!");
        }
    }

    public void generateHash() {
        throw new NotImplementedException();
    }

    public Building getBuildingPreset(BuildingType buildingType){
        return buildingFactory.getBuildingPreset(buildingType);
    }

    public Unit getUnitPreset(UnitType unitType){
        return unitFactory.getUnitPreset(unitType);
    }

    public void createUnit(UnitType unitType, Point location) {
        if (hexEmpty(location)) {
            Unit unit = unitFactory.getUnitPreset(unitType);
            if (currentPlayer.getGold() - unit.getPurchaseCost() > 0) {
                currentPlayer.purchaseUnit(unitFactory.createUnit(unitType, location, currentPlayer));
            } else {
                System.out.println("Not enough gold");
            }
        } else {
            System.out.println("Hex not empty");
        }
    }

    public void createBuilding(BuildingType buildingType, Point location) {
//        if (map.isHexBuildable(location, currentPlayer)) { //TODO uncomment when hex owner is implemented
            if (hexEmpty(location)) {
                currentPlayer.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, currentPlayer));
            }
//        }
    }

    public void sellBuilding(Point location){
        Building building = currentPlayer.getBuildingAtLocation(location);
        if (building != null && !(building instanceof TownCentre)){
            int cost = 0;
            if (building instanceof Barracks) {
                cost = ((Barracks) buildingFactory.getBuildingPreset(BuildingType.BARRACKS)).getPurchaseCost();
            } else if (building instanceof Fortification) {
                cost = ((Fortification) buildingFactory.getBuildingPreset(BuildingType.FORTIFICATION)).getPurchaseCost();
            } else if (building instanceof Resource) {
                cost = ((Resource) buildingFactory.getBuildingPreset(BuildingType.RESOURCE)).getPurchaseCost();
            }
            currentPlayer.sellBuilding(building, cost);
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

    public boolean hexEmpty(Point location) {
        //Check if unit is on hex
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (unit.getLocation().equals(location)) {
                    return false;
                }
            }
        }

        //Check if hex isn't water, mountain or possessed by a building
        Hexagon hex = map.getHexAtLocation(location);
        if (hex.getObjectType() == ObjectType.MOUNTAIN
                || hex.getGroundType() == GroundType.WATER
                || getBuildingAtLocation(location) != null){
            return false;
        }
        else{
            return true;
        }
    }

    public Unit getUnitOnHex(Hexagon hex) {
        Unit unit = null;
        for (Player p : getPlayers()) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public Unit getSelectedUnit() {
        Unit unit = null;
        for (Player p : getPlayers()) {
            for (Unit u : p.getUnits()) {
                if (u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public void attackBuilding(Unit selectedUnit, Point locationBuilding) {
        Building building = getBuildingAtLocation(locationBuilding);
        if (selectedUnit != null && building != null) {
            if(selectedUnit.attack(building)){
                Player enemy = building.getOwner();
                enemy.removeBuilding(building);
                if(building instanceof TownCentre){
                    removePlayer(enemy);
                }
            }
        }
    }

    public Building getBuildingAtLocation(Point location) {
        for (Player player : players) {
            Building building = player.getBuildingAtLocation(location);
            if (building != null) {
                return building;
            }

        }
        return null;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
}
