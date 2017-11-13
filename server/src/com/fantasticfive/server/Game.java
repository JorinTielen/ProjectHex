package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The com.fantasticfive.server.Game class is the main class of the game, and takes care of the core functionalities of the game.
 */
public class Game extends UnicastRemoteObject implements IGame {
    private List<IPlayer> players = new ArrayList<IPlayer>();
    private IPlayer currentPlayer;
    private IMap map;
    private UnitFactory unitFactory = new UnitFactory();
    private BuildingFactory buildingFactory = new BuildingFactory();
    private String hash;
    private int id;

    public Game() throws RemoteException{
    }

    /**
     * Adds a player to the game. Checks if the username is already in use and gives the player a random colour and his towncentre
     * @param username The user's username
     */
    public void addPlayer(String username) {
        boolean available = true;
        ArrayList<Color> usedColors = new ArrayList<Color>();

        for (IPlayer p : players) {
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
            IPlayer p = new Player(username, color);
            players.add(p);
            p.addGold(100);
            if (username.equals("enemy")) {
                IBuilding b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(10,13),p);
                p.purchaseBuilding(b);
            } else {
                IBuilding b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p); //Random Point???
                p.purchaseBuilding(b);
            }
        }
    }

    public void removePlayer(IPlayer player) {
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
        //If players list is not empty
        if (players.size() != 0) {
            currentPlayer.endTurn();
            int i = players.indexOf(currentPlayer);

            //If current player is not the last in the list
            if (i != players.size() - 1) {
                currentPlayer = players.get(i + 1);
            }
            //If current player is the last in the list
            else {
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

    public IBuilding getBuildingPreset(BuildingType buildingType){
        return buildingFactory.getBuildingPreset(buildingType);
    }

    public Unit getUnitPreset(com.fantasticfive.shared.enums.UnitType unitType){
        return unitFactory.getUnitPreset(unitType);
    }

    /**
     * Purchase a unit for the current player
     * @param unitType The type of unit
     * @param location The location to place the unit
     */
    public void createUnit(com.fantasticfive.shared.enums.UnitType unitType, Point location) {
        //When hex is empty
        if (hexEmpty(location)) {
            Unit unit = unitFactory.getUnitPreset(unitType);
            //When player has enough gold to buy unit
            if (currentPlayer.getGold() - unit.getPurchaseCost() > 0) {
                currentPlayer.purchaseUnit(unitFactory.createUnit(unitType, location, currentPlayer));
            } else {
                System.out.println("Not enough gold");
            }
        }
        //When hex is not empty
        else {
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
        IBuilding building = currentPlayer.getBuildingAtLocation(location);
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

    public void claimLand(IUnit unit) {
        throw new NotImplementedException();
    }

    public void update() {
        throw new NotImplementedException();
    }

    public List<IPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Checks if a specified Hex has a unit, building or mountain on it. Also checks if the hex is water.
     * @param location The location of the hex to check.
     * @return True if the hex is empty, false if it's not empty.
     */
    public boolean hexEmpty(Point location) {
        //Check if unit is on hex
        for (IPlayer player : players) {
            for (IUnit unit : player.getUnits()) {
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

    public IUnit getUnitOnHex(Hexagon hex) {
        IUnit unit = null;
        for (IPlayer p : getPlayers()) {
            for (IUnit u : p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public IUnit getSelectedUnit() {
        IUnit unit = null;
        for (IPlayer p : getPlayers()) {
            for (IUnit u : p.getUnits()) {
                if (u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public void attackBuilding(Unit selectedUnit, Point locationBuilding) {
        IBuilding building = getBuildingAtLocation(locationBuilding);
        if (selectedUnit != null && building != null) {
            if(selectedUnit.attack(building)){
                IPlayer enemy = building.getOwner();
                enemy.removeBuilding(building);
                if(building instanceof TownCentre){
                    removePlayer(enemy);
                }
            }
        }
    }

    public IBuilding getBuildingAtLocation(Point location) {
        for (IPlayer player : players) {
            IBuilding building = player.getBuildingAtLocation(location);
            if (building != null) {
                return building;
            }

        }
        return null;
    }

    public IPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }
}
