package com.fantasticfive.server;

import com.fantasticfive.projecthex.tables.BuildingSellTable;
import com.fantasticfive.server.BuildingFactory;
import com.fantasticfive.server.UnitFactory;
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
    private List<Player> players = new ArrayList<Player>();
    private Player currentPlayer;
    private IMap map;
    private UnitFactory unitFactory = new UnitFactory();
    private BuildingFactory buildingFactory = new BuildingFactory();
    private String hash;
    private int id;

    public Game() throws RemoteException {
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        if (players.size() == 1) {
            System.out.println(currentPlayer.getUsername() + " has won the game!");
        }
    }

    public void setMap() {
        map = new Map(20, 15);
    }

    @Override
    public void addPlayer(String username) throws RemoteException {
        //
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

    }

    public void generateHash() {
        throw new NotImplementedException();
    }

    public Building getBuildingPreset(BuildingType buildingType) {
        return buildingFactory.getBuildingPreset(buildingType);
    }

    public Unit getUnitPreset(UnitType unitType) {
        return unitFactory.getUnitPreset(unitType);
    }

    /**
     * Purchase a unit for the current player
     *
     * @param unitType The type of unit
     * @param location The location to place the unit
     */
    public void createUnit(UnitType unitType, Point location) {
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

    public void sellBuilding(Point location) {
        Building building = currentPlayer.getBuildingAtLocation(location);
        if (building != null && !(building instanceof TownCentre)) {
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

    /**
     * Checks if a specified Hex has a unit, building or mountain on it. Also checks if the hex is water.
     *
     * @param location The location of the hex to check.
     * @return True if the hex is empty, false if it's not empty.
     */
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
                || getBuildingAtLocation(location) != null) {
            return false;
        } else {
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
            if (selectedUnit.attack(building)) {
                Player enemy = building.getOwner();
                enemy.removeBuilding(building);
                if (building instanceof TownCentre) {
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
